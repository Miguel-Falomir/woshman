package com.empleados.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empleados.model.Permiso;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Permiso extends DAO implements DAO_Interface<Permiso, Integer>{

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Permiso(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Permiso obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Permiso obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Permiso obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Permiso search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Permiso respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los permisos ////////////////
            statement = connect.prepareStatement("SELECT p.id_permiso, p.nombre, p.descripcion FROM permiso p WHERE p.id_permiso = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' a de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // agregar todas las respuestas a lista 'auxMarcas'
            respuesta = new Permiso(
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
    public List<Permiso> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Permiso> respuesta = new ArrayList<Permiso>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los permisos ////////////////
            statement = connect.prepareStatement("SELECT p.id_permiso, p.nombre, p.descripcion FROM permiso p ORDER BY p.id_permiso ASC;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxMarcas'
            while(resultado.next()){
                respuesta.add( new Permiso(
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

    public List<Permiso> searchByRol(Integer id_rol){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Permiso> respuesta = new ArrayList<Permiso>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar permisos del rol indicado /////////
            statement = connect.prepareStatement("SELECT p.id_permiso, p.nombre, p.descripcion FROM permiso p JOIN rol_has_permiso h ON p.id_permiso = h.permiso WHERE h.rol = ? ORDER BY p.id_permiso ASC;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id_rol);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while(resultado.next()){
                respuesta.add( new Permiso(
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
