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
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: buscar todos los vehiculos con v.matricula == obj.matricula
            statement = connect.prepareStatement("SELECT count(*) FROM vehiculo v WHERE v.matricula = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, obj.getMatricula());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // comprobar que matricula no se repita
            boolean matriculaUnique = (resultado.getInt(1) == 0);
            if (matriculaUnique) {
                // consulta 2: contar cantidad filas
                statement = connect.prepareStatement("SELECT count(*) FROM vehiculo;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                // ejecutar consulta
                resultado = statement.executeQuery();

                // forzar que 'resultado' apunte a primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();

                // guardar resultado
                int cantidadFilas = resultado.getInt(1);

                // consulta 3: agregar vehiculo nuevo a BD
                statement = connect.prepareStatement("INSERT INTO vehiculo(id_vehiculo, fk_modelo, matricula) VALUES(?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setInt(2, obj.getModelo().getId());
                statement.setString(3, obj.getMatricula());

                // ejecutar insercion
                insert = statement.executeUpdate();
                System.out.println("INSERTAR NUEVO VEHICULO: " + insert);
                success = true;
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

        // devolver 'success', para indicar si se ha completado la insercion
        return success;
    }

    @Override
    public boolean update(Vehiculo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: comprobar que la nueva matricula no se repite
            statement = connect.prepareStatement("SELECT count(*) FROM vehiculo v WHERE v.matricula = ? AND v.id_vehiculo <> ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, obj.getMatricula());
            statement.setInt(2, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

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
                System.out.println("ACTUALIZAR VEHICULO: " + update);
                success = true;
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

        // devolver 'success', para indicar si se ha completado la actualizacion
        return success;
    }

    @Override
    public boolean delete(Vehiculo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int delete;
        boolean success = false;

        // (intentar) ejecutar eliminacion
        try {
            // consulta 1: comprobar que obj.id no aparece en ninguna averia
            // si se intenta borrar un vehiculo que aparece en X averias, antes de borrar las averias
            // la base de datos lanza un error al programa
            statement = connect.prepareStatement("SELECT count(*) FROM averia a JOIN vehiculo v ON a.fk_vehiculo = v.id_vehiculo WHERE v.id_vehiculo = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
                delete = statement.executeUpdate();
                System.out.println("ELIMINAR VEHICULO: " + delete);

                // consulta 3: actualizar manualmente los IDs de los vehiculos posteriores
                // ya se que deberia haber hecho la columna autoincremental,
                // pero a estas alturas no tengo tiempo de rectificar la base de datos
                statement = connect.prepareStatement("UPDATE vehiculo SET id_vehiculo = id_vehiculo - 1 WHERE id_vehiculo > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                // de nuevo, kashate boludo
                delete = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + delete);
                success = true;
            } else {
                System.out.println("ERROR, EL VEHÍCULO TIENE ASIGNADAS 1 O MÁS AVERÍAS.");
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

        // devolver 'success', para indicar si se ha completado el borrado
        return success;
    }

    @Override
    public Vehiculo search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Vehiculo respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca relativos a vehiculo veh.id_vehiculo = id
            statement = connect.prepareStatement("SELECT veh.id_vehiculo, veh.matricula, mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM vehiculo veh JOIN modelo mo ON mo.id_modelo = veh.fk_modelo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca WHERE veh.id_vehiculo = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado en objeto 'respuesta'
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
            statement = connect.prepareStatement("SELECT veh.id_vehiculo, veh.matricula, mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM vehiculo veh JOIN modelo mo ON mo.id_modelo = veh.fk_modelo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca ORDER BY veh.id_vehiculo ASC;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

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
