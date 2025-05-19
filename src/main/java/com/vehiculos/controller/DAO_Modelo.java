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

public class DAO_Modelo extends DAO implements DAO_Interface<Modelo, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Modelo(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Modelo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int cantidadFilas;
        boolean exito = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: contar cantidad filas
            statement = connect.prepareStatement("SELECT count(*) FROM modelo;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado
            cantidadFilas = resultado.getInt(1);

            // consulta 2: buscat todos los modelos con m.nombre == obj.nombre
            statement = connect.prepareStatement("SELECT count(*) FROM modelo m WHERE m.nombre = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, obj.getNombre());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // comprobar si nombre se repite
            boolean nombreUnique = (resultado.getInt(1) == 0);
            if (nombreUnique) {
                // consulta 3: agregar modelo
                statement = connect.prepareStatement("INSERT INTO modelo(id_modelo, fk_marca, fk_categoria, nombre) VALUES(?, ?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setInt(2, obj.getMarca().getId());
                statement.setInt(3, obj.getCategoria().getId());
                statement.setString(4, obj.getNombre());
                System.out.println("INSERTAR NUEVO MODELO: " + statement.executeUpdate());
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

        // devolver 'exito', para indicar si se ha completado la insercion
        return exito;
    }

    @Override
    public boolean update(Modelo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean exito = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: comprobar que el nuevo nombre no se repite
            statement = connect.prepareStatement("SELECT count(*) FROM modelo m WHERE m.nombre = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, obj.getNombre());

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
                // consulta 2: actualizar modelo
                statement = connect.prepareStatement("UPDATE modelo m SET m.fk_marca = ?, m.fk_categoria = ?, m.nombre = ? WHERE m.id_modelo = ?;");
                statement.setInt(1, obj.getMarca().getId());
                statement.setInt(2, obj.getCategoria().getId());
                statement.setString(3, obj.getNombre());
                statement.setInt(4, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("INSERTAR MODELO: " + update);
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
    public boolean delete(Modelo obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean exito = false;

        // (intentar) ejecutar eliminacion
        try {
            // consulta 1: comprobar cantidad vehiculos con v.fk_modelo = m.id_modelo
            statement = connect.prepareStatement("SELECT count(*) FROM modelo m JOIN vehiculo v ON m.id_modelo = v.fk_modelo WHERE m.id_modelo = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroVehiculos = (resultado.getInt(1) == 0);

            if (zeroVehiculos){
                // consulta 2: borrar modelo
                statement = connect.prepareStatement("DELETE FROM modelo WHERE id_modelo = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                update = statement.executeUpdate();
                System.out.println("ELIMINAR MODELO: " + update);

                // consulta 3: actualizar manualmente los IDs de los modelos posteriores
                statement = connect.prepareStatement("UPDATE modelo SET id_modelo = id_modelo - 1 WHERE id_modelo > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
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

    @Override
    public Modelo search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Modelo respuesta = null;

        try {
            // consulta 1: buscar todos los datos de modelo, categoria y marca donde mo.id_modelo = id
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.nombre, ma.id_marca, ma.nombre, cat.id_categoria, cat.nombre, cat.descripcion FROM modelo mo JOIN marca ma ON mo.fk_marca = ma.id_marca JOIN categoria cat ON mo.fk_categoria = cat.id_categoria WHERE mo.id_modelo = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // agregar resultado a 'respuesta'
            Categoria categoria = new Categoria(
                resultado.getInt(5),
                resultado.getString(6),
                (resultado.getString(7) == null) ? "" : resultado.getString(7)
            );
            Marca marca = new Marca(
                resultado.getInt(3),
                resultado.getString(4)
            );
            respuesta = new Modelo(
                resultado.getInt(1),
                resultado.getString(2),
                marca,
                categoria
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
    public List<Modelo> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Modelo> respuesta = new ArrayList<Modelo>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca de una tajada
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM modelo mo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca ORDER BY mo.id_modelo ASC;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultado a lista 'respuesta'
            while (resultado.next()){
                Marca marca = new Marca(
                    resultado.getInt(6),
                    resultado.getString(7)
                );
                Categoria categoria = new Categoria(
                    resultado.getInt(3),
                    resultado.getString(4),
                    (resultado.getString(5) == null) ? "": resultado.getString(7) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                );
                Modelo modelo = new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
                );
                respuesta.add(modelo);
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

    public List<Modelo> searchByMarca(Integer id){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Modelo> respuesta = new ArrayList<Modelo>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca de una tajada
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM modelo mo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca WHERE ma.id_marca = ? ORDER BY mo.id_modelo ASC;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultado a lista 'respuesta'
            while (resultado.next()){
                Marca marca = new Marca(
                    resultado.getInt(6),
                    resultado.getString(7)
                );
                Categoria categoria = new Categoria(
                    resultado.getInt(3),
                    resultado.getString(4),
                    (resultado.getString(5) == null) ? "": resultado.getString(7) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                );
                Modelo modelo = new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
                );
                respuesta.add(modelo);
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
