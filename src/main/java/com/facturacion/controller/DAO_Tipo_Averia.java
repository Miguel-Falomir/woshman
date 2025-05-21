package com.facturacion.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facturacion.model.Tipo_Averia;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Tipo_Averia extends DAO implements DAO_Interface<Tipo_Averia, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Tipo_Averia(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Tipo_Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Tipo_Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Tipo_Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Tipo_Averia search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Tipo_Averia respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos estado averia
            statement = connect.prepareStatement("SELECT tav.id_tipo_averia, est.nombre, est.descripcion FROM tipo_averia tav WHERE tav.id_tipo_averia = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar datos en objeto 'respuesta'
            respuesta = new Tipo_Averia(
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
    public List<Tipo_Averia> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Tipo_Averia> respuesta = new ArrayList<Tipo_Averia>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos estado averia
            statement = connect.prepareStatement("SELECT tav.id_tipo_averia, tav.nombre, tav.descripcion FROM tipo_averia tav;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while (resultado.next()){
                respuesta.add( new Tipo_Averia(
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
