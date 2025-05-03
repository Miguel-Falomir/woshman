package com.vehiculos.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.utilities.DAO;
import com.utilities.DAO_Interface;
import com.vehiculos.model.Marca;

public class DAO_Marca extends DAO implements DAO_Interface<Marca, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Marca(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Marca obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Marca obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Marca obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Marca search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Marca respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas //////////////////
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma WHERE ma.id_marca = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // agregar respuesta a lista 'auxMarcas'
            respuesta = new Marca(
                resultado.getInt(1),
                resultado.getString(2)
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
    public List<Marca> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Marca> respuesta = new ArrayList<Marca>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas //////////////////
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma ORDER BY ma.id_marca ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxMarcas'
            while(resultado.next()){
                respuesta.add( new Marca(
                    resultado.getInt(1),
                    resultado.getString(2)
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

    public List<Marca> searchAllAlphabetically(){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Marca> respuesta = new ArrayList<Marca>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas //////////////////
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma ORDER BY ma.nombre ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxMarcas'
            while(resultado.next()){
                respuesta.add( new Marca(
                    resultado.getInt(1),
                    resultado.getString(2)
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
