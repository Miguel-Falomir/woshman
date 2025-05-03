package com.vehiculos.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.utilities.DAO;
import com.utilities.DAO_Interface;
import com.vehiculos.model.Categoria;
import com.vehiculos.model.Marca;
import com.vehiculos.model.Modelo;
import com.vehiculos.model.Vehiculo;

public class DAO_Vehiculo extends DAO implements DAO_Interface<Vehiculo, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Vehiculo(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Vehiculo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int cantidadFilas;
        boolean exito = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: contar cantidad filas
            statement = connect.prepareStatement("SELECT count(*) FROM vehiculo;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunta a la primera fila
            resultado.first();

            // guardar resultado
            cantidadFilas = resultado.getInt(1);

            // consulta 2: buscar todos los vehiculos con v.matricula == obj.matricula
            statement = connect.prepareStatement("SELECT count(*) FROM vehiculo v WHERE v.matricula = ?;");
            statement.setString(1, obj.getMatricula());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunta a la primera fila
            resultado.first();

            // agregar vehiculo nuevo a BD
            boolean matriculaUnica = (resultado.getInt(1) == 0);
            if (matriculaUnica) {
                statement = connect.prepareStatement("INSERT INTO vehiculo(id_vehiculo, fk_modelo, matricula) VALUES(?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setInt(2, obj.getModelo().getId());
                statement.setString(3, obj.getMatricula());
                System.out.println("INSERTAR NUEVO VEHICULO: " + statement.executeUpdate());
                exito = true;
            } else {
                System.out.println("ERROR: MATRÍCULA REPETIDA");
            }

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        
        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver 'exito', para indicar si se ha completado la insercion
        return exito;
    }

    @Override
    public boolean update(Vehiculo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean exito = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: comprobar que la nueva matricula no se repite
            statement = connect.prepareStatement("SELECT count(*) FROM vehiculo v WHERE v.matricula = ?;");
            statement.setString(1, obj.getMatricula());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a la primera fila
            resultado.first();

            // materializar 'resultado' en booleano
            boolean matriculaUnique = (resultado.getInt(1) == 0);

            if (matriculaUnique){
                // consulta 2: actualizar vehiculo
                statement = connect.prepareStatement("UPDATE vehiculo v SET v.fk_modelo = ?, v.matricula = ? WHERE v.id_vehiculo = ?;");
                statement.setInt(1, obj.getModelo().getId());
                statement.setString(2, obj.getMatricula());
                statement.setInt(3, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("INSERTAR VEHICULO: " + update);
                exito = true;
            }

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        
        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver 'exito', para indicar si se ha completado la actualizacion
        return exito;
    }

    @Override
    public boolean delete(Vehiculo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean exito = false;

        // (intentar) ejecutar eliminacion
        try {
            // consulta 1: comprobar que obj.id no aparece en ninguna averia
            // si se intenta borrar un vehiculo que aparece en X averias, antes de borrar las averias
            // la base de datos lanza un error al programa
            statement = connect.prepareStatement("SELECT count(*) FROM averia a JOIN vehiculo v ON a.fk_vehiculo = v.id_vehiculo WHERE v.id_vehiculo = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroAverias = (resultado.getInt(1) == 0);

            if (zeroAverias) {
                // consulta 2: borrar vehiculo
                statement = connect.prepareStatement("DELETE FROM vehiculo WHERE id_vehiculo = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                update = statement.executeUpdate();
                System.out.println("ELIMINAR VEHICULO: " + update);

                // consulta 3: actualizar manualmente los IDs de los vehiculos posteriores
                // ya se que deberia haber hecho la columna autoincremental,
                // pero a estas alturas no tengo tiempo de rectificar la base de datos
                statement = connect.prepareStatement("UPDATE vehiculo SET id_vehiculo = id_vehiculo - 1 WHERE id_vehiculo > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                // de nuevo, kashate boludo
                update = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + update);
                exito = true;
            }

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        
        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver 'exito', para indicar si se ha completado la actualizacion
        return exito;
    }

    public boolean deleteCascade(Vehiculo obj){
        // variables internas
        PreparedStatement statement = null;
        /* ResultSet resultado = null; */
        int update;
        boolean exito = false;

        // (intentar) ejecutar eliminacion
        try {
            // consulta 1: borrar las averias 
            statement = connect.prepareStatement("DELETE FROM averia a WHERE a.fk_vehiculo = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar actualizacion
            update = statement.executeUpdate();
            System.out.println("ELIMINAR AVERIAS: " + update);

            // consulta 2: borrar vehiculo
            statement = connect.prepareStatement("DELETE FROM vehiculo WHERE id = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar eliminacion
            update = statement.executeUpdate();
            System.out.println("ELIMINAR VEHICULO: " + update);

            // consulta 3: actualizar manualmente los IDs de los vehiculos posteriores
            statement = connect.prepareStatement("UPDATE vehiculo SET id_vehiculo = id_vehiculo - 1 WHERE id_vehiculo > ?;");
            statement.setInt(1, obj.getId());

            // ejecutar actualizacion
            update = statement.executeUpdate();
            System.out.println("REORGANIZAR IDs MANUALMENTE: " + update);
            exito = true;

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        
        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver 'exito', para indicar si se ha completado la actualizacion
        return exito;
    }

    @Override
    public Vehiculo search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Vehiculo respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca mientras veh-id_vehiculo = id
            statement = connect.prepareStatement("SELECT veh.id_vehiculo, veh.matricula, mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM vehiculo veh JOIN modelo mo ON mo.id_modelo = veh.fk_modelo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca WHERE veh.id_vehiculo = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a de la primera fila
            resultado.first();

            // agregar resultado a 'respuesta'
            Marca marca = new Marca(
                resultado.getInt(8),
                resultado.getString(9)
            );
            Categoria categoria = new Categoria(
                resultado.getInt(5),
                resultado.getString(6),
                (resultado.getString(7) == null) ? "": resultado.getString(7) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
            );
            Modelo modelo = new Modelo(
                resultado.getInt(3),
                resultado.getString(4),
                marca,
                categoria
            );
            respuesta = new Vehiculo(
                resultado.getInt(1),
                resultado.getString(2),
                modelo
            );

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        
        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        
        // devolver respuesta
        return respuesta;
    }

    @Override
    public List<Vehiculo> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Vehiculo> respuesta = new ArrayList<Vehiculo>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca de una tajada
            statement = connect.prepareStatement("SELECT veh.id_vehiculo, veh.matricula, mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM vehiculo veh JOIN modelo mo ON mo.id_modelo = veh.fk_modelo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca ORDER BY veh.id_vehiculo ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultado a lista 'respuesta'
            while (resultado.next()){
                Marca marca = new Marca(
                    resultado.getInt(8),
                    resultado.getString(9)
                );
                Categoria categoria = new Categoria(
                    resultado.getInt(5),
                    resultado.getString(6),
                    (resultado.getString(7) == null) ? "": resultado.getString(7) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                );
                Modelo modelo = new Modelo(
                    resultado.getInt(3),
                    resultado.getString(4),
                    marca,
                    categoria
                );
                Vehiculo vehiculo = new Vehiculo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    modelo
                );
                respuesta.add(vehiculo);
            }

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        
        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        
        // devolver respuesta
        return respuesta;
    }

}
