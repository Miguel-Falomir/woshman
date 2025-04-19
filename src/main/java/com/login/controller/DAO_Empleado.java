package com.login.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.login.model.Empleado;
import com.utilities.DAO;

public class DAO_Empleado implements DAO<Empleado, Long> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Empleado(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public void insert(Empleado obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Empleado obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Empleado obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Empleado search(Long id) {
        // variables internas
        PreparedStatement stat = null;
        ResultSet resultado = null;
        Empleado respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta SQL
            stat = connect.prepareStatement("SELECT * FROM empleado e WHERE e.id_empleado = ?;");
            stat.setLong(1, id);
            // acceder a BD
            resultado = stat.executeQuery();
            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();
            // guardar primera fila en 'resultado'
            respuesta = new Empleado(
                resultado.getLong(1),
                resultado.getInt(2),
                resultado.getString(3),
                resultado.getString(4),
                resultado.getString(5),
                resultado.getString(6)
            );
        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        // pase lo que pase, cerrar 'stat'
        } finally {
            if (stat != null){
                try {
                    stat.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver resultado
        return respuesta;
    }

    @Override
    public List<Empleado> searchAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

    public Empleado searchByNombre(String nombre){
        // variables internas
        PreparedStatement stat = null;
        ResultSet resultado = null;
        Empleado respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta SQL
            stat = connect.prepareStatement("SELECT * FROM empleado e WHERE e.username = ?;");
            stat.setString(1, nombre);
            // acceder a BD
            resultado = stat.executeQuery();
            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();
            // guardar primera fila en 'resultado'
            respuesta = new Empleado(
                resultado.getLong(1),
                resultado.getInt(2),
                resultado.getString(3),
                resultado.getString(4),
                resultado.getString(5),
                resultado.getString(6)
            );
        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        // pase lo que pase, cerrar 'stat'
        } finally {
            if (stat != null){
                try {
                    stat.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver resultado
        return respuesta;
    }

}
