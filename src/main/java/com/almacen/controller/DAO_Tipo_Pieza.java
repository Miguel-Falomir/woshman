package com.almacen.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.almacen.model.Tipo_Pieza;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Tipo_Pieza extends DAO implements DAO_Interface<Tipo_Pieza, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Tipo_Pieza(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD
    @Override
    public boolean insert(Tipo_Pieza obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try{
            // consulta 1: contar tipos pieza con tipi.nombre == obj.nombre
            statement = connect.prepareStatement("SELECT count(*) FROM tipo_pieza tipi WHERE tipi.nombre = ?;");
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
                statement = connect.prepareStatement("SELECT count(*) FROM tipo_pieza;");

                // ejecutar consulta
                resultado = statement.executeQuery();

                // forzar que 'resultado' apunte a primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();

                // guardar resultado
                int cantidadFilas = resultado.getInt(1);

                // consulta 3: agregar nuevo tipo pieza
                statement = connect.prepareStatement("INSERT INTO tipo_pieza(id_tipo_pieza, nombre, descripcion) VALUES (?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setString(2, obj.getNombre());
                statement.setString(3, obj.getDescripcion());

                // ejecutar consulta
                insert = statement.executeUpdate();
                System.out.println("INSERTAR NUEVO TIPO PIEZA: " + insert);
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
    public boolean update(Tipo_Pieza obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: contar tipos pieza con tipi.nombre == obj.nombre
            statement = connect.prepareStatement("SELECT count(*) FROM tipo_pieza tipi WHERE tipi.nombre = ?;");
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
                // consulta 2: actualizar tipo pieza
                statement = connect.prepareStatement("UPDATE tipo_pieza tipi SET tipi.nombre = ?, tipi.descripcion = ? WHERE tipi.id_tipo_pieza = ?;");
                statement.setString(1, obj.getNombre());
                statement.setString(2, obj.getDescripcion());
                statement.setInt(3, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("ACTUALIZAR TIPO PIEZA: " + update);
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
    public boolean delete(Tipo_Pieza obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int delete;
        boolean success = false;

        // (intentar) ejecutar borrado
        try{
            // consulta 1: comprobar que el tipo no se encuentra en ninguna pieza
            statement = connect.prepareStatement("SELECT count(*) FROM pieza pi WHERE pi.fk_tipo_pieza = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroPiezas = (resultado.getInt(1) == 0);
            if (zeroPiezas) {
                // consulta 2: borrar proveedor
                statement = connect.prepareStatement("DELETE FROM tipo_pieza tipi WHERE tipi.id_tipo_pieza = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                delete = statement.executeUpdate();
                System.out.println("ELIMINAR TIPO PIEZA: " + delete);

                // consulta 3: actualizar manualmente IDs posteriores
                statement = connect.prepareStatement("UPDATE tipo_pieza tipi SET tipi.id_tipo_pieza = tipi.id_tipo_pieza - 1 WHERE tipi.id_tipo_pieza > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                delete = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + delete);
                success = true;

            } else {
                System.out.println("ERROR: TIPO PIEZA REFERENCIADO EN 1 O MÁS PIEZAS");
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
    public Tipo_Pieza search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Tipo_Pieza respuesta = null;

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar tipo pieza por ID
            statement = connect.prepareStatement("SELECT tipi.id_tipo_pieza, tipi.nombre, tipi.descripcion FROM tipo_pieza tipi WHERE tipi.id_tipo_pieza = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar 'resultado' en objeto 'respuesta'
            respuesta = new Tipo_Pieza(
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
    public List<Tipo_Pieza> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Tipo_Pieza> respuesta = new ArrayList<Tipo_Pieza>();

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar tipo pieza por ID
            statement = connect.prepareStatement("SELECT tipi.id_tipo_pieza, tipi.nombre, tipi.descripcion FROM tipo_pieza tipi;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while (resultado.next()) {
                respuesta.add( new Tipo_Pieza(
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
