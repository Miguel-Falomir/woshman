package com.login.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.login.model.Usuario;
import com.utilities.DAO;

public class DAO_Usuario implements DAO<Usuario, Long> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Usuario(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public void insert(Usuario obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Usuario obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Usuario obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Usuario search(Long id) {
        // variables internas
        PreparedStatement stat = null;
        ResultSet resultado = null;
        Usuario respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta SQL
            stat = connect.prepareStatement("SELECT * FROM usuario u WHERE u.idusuario = ?;");
            stat.setLong(1, id);
            // acceder a BD
            resultado = stat.executeQuery();
            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();
            // guardar primera fila en 'resultado'
            respuesta = new Usuario(
                resultado.getLong(1),
                resultado.getString(2),
                resultado.getString(3)
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
    public List<Usuario> searchAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

    public Usuario searchByNombre(String nombre){
        // variables internas
        PreparedStatement stat = null;
        ResultSet resultado = null;
        Usuario respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta SQL
            stat = connect.prepareStatement("SELECT * FROM usuario u WHERE u.nombre = ?;");
            stat.setString(1, nombre);
            // acceder a BD
            resultado = stat.executeQuery();
            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();
            // guardar primera fila en 'resultado'
            respuesta = new Usuario(
                resultado.getLong(1),
                resultado.getString(2),
                resultado.getString(3)
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
