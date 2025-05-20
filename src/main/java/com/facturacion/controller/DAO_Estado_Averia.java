package com.facturacion.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facturacion.model.Estado_Averia;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Estado_Averia extends DAO implements DAO_Interface<Estado_Averia, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Estado_Averia(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Estado_Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Estado_Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Estado_Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Estado_Averia search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Estado_Averia respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos estado averia
            statement = connect.prepareStatement("SELECT est.id_estado_averia, est.nombre, est.descripcion FROM estado_averia est WHERE est.id_estado_averia = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar datos en objeto 'respuesta'
            respuesta = new Estado_Averia(
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
    public List<Estado_Averia> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Estado_Averia> respuesta = new ArrayList<Estado_Averia>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos estado averia
            statement = connect.prepareStatement("SELECT est.id_estado_averia, est.nombre, est.descripcion FROM estado_averia est;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while (resultado.next()){
                respuesta.add( new Estado_Averia(
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
