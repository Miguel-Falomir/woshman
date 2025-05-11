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

public class DAO_Categoria extends DAO implements DAO_Interface<Categoria, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Categoria(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Categoria obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: buscar todas las categorias con cat.nombre == obj.nombre
            statement = connect.prepareStatement("SELECT count(*) FROM categoria cat WHERE cat.nombre = ?;");
            statement.setString(1, obj.getNombre());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // comprobar que nombre no se repita
            boolean nombreUnique = (resultado.getInt(1) == 0);
            if (nombreUnique) {
                // consulta 2: contar cantidad filas
                statement = connect.prepareStatement("SELECT count(*) FROM categoria;");

                // ejecutar consulta
                resultado = statement.executeQuery();

                // forzar que 'resultado' apunte a primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();

                // guardar resultado
                int cantidadFilas = resultado.getInt(1);

                // consulta 3: agregar marca a base de datos
                statement = connect.prepareStatement("INSERT INTO categoria(id_categoria, nombre, descripcion) VALUES (?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setString(2, obj.getNombre());
                statement.setString(3, (obj.getDescripcion() == null) ? "" : obj.getDescripcion());

                // ejecutar insercion
                insert = statement.executeUpdate();
                System.out.println("INSERTAR NUEVA CATEGORÍA: " + insert);
                success = true;

            } else {
                System.out.println("ERROR: NOMBRE REPETIDO");
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
    public boolean update(Categoria obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar actualizacion
        try {
            // comprobar que el nuevo nombre no se repite
            statement = connect.prepareStatement("SELECT count(*) FROM categoria cat WHERE cat.nombre = ? AND cat.id_marca <> ?;");
            statement.setString(1, obj.getNombre());
            statement.setInt(2, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean nombreUnique = (resultado.getInt(1) == 0);
            if (nombreUnique) {
                // consulta 2: actualizar categoria
                statement = connect.prepareStatement("UPDATE categoria cat SET cat.nombre = ?, cat.descripcion = ? WHERE cat.id_categoria = ?;");
                statement.setString(1, obj.getNombre());
                statement.setString(2, obj.getDescripcion());
                statement.setInt(3, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("ACTUALIZAR CATEGORÍA: " + update);
                success = true;
                
            } else {
                System.out.println("ERROR: NOMBRE REPETIDO");
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
    public boolean delete(Categoria obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar eliminacion
        try {
            // consulta 1: comprobar que obj.id no aparece en ningun modelo
            statement = connect.prepareStatement("SELECT count(*) FROM modelo mo JOIN categoria cat ON cat.id_categoria = mo.fk_categoria WHERE cat.id_categoria = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroModelos = (resultado.getInt(1) == 0);
            if (zeroModelos) {
                // consulta 2: borrar categoria
                statement = connect.prepareStatement("DELETE FROM categoria cat WHERE cat.id_categoria = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                update = statement.executeUpdate();
                System.out.println("ELIMINAR VEHICULO: " + update);

                // consulta 3: actualizar manualmente ID posteriores
                statement = connect.prepareStatement("UPDATE categoria cat SET cat.id_categoria = cat.id_categoria - 1 WHERE cat.id_categoria > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + update);
                success = true;
                
            } else {
                System.out.println("ERROR: NOMBRE REPETIDO");
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
    public Categoria search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Categoria respuesta = null;

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: Buscar categoria con cat.id_categoria = id
            statement = connect.prepareStatement("SELECT cat.id_categoria, cat.nombre, cat.descripcion FROM categoria cat WHERE cat.id_categoria = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado en respuesta
            respuesta = new Categoria(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3)
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
    public List<Categoria> searchAll() {
        // variable internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Categoria> respuesta = new ArrayList<Categoria>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las categorias
            statement = connect.prepareStatement("SELECT cat.id_categoria, cat.nombre, cat.descripcion FROM categoria cat;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while(resultado.next()){
                respuesta.add( new Categoria(
                    resultado.getInt(1),
                    resultado.getString(2),
                    resultado.getString(3)
                ));
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
