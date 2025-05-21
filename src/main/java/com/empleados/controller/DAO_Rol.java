package com.empleados.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empleados.model.Permiso;
import com.empleados.model.Rol;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Rol extends DAO implements DAO_Interface<Rol, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Rol(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Rol obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Rol obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Rol obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Rol search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Rol respuesta = null;
        List<Permiso> auxPermisos = new ArrayList<Permiso>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar rol
            statement = connect.prepareStatement("SELECT rol.id_rol, rol.nombre, rol.descripcion FROM rol WHERE rol.id_rol = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();
            
            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar primera fila en variable auxiliar
            respuesta = new Rol(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3)
            );

            // consulta 3: Buscar permisos del rol //////////////////
            statement = connect.prepareStatement("SELECT p.* FROM permiso p JOIN rol_has_permiso h ON p.id_permiso = h.permiso JOIN rol r ON h.rol = r.id_rol WHERE r.id_rol = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setLong(1, respuesta.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxPermisos'
            while(resultado.next()){
                auxPermisos.add( new Permiso(
                    resultado.getInt(1),
                    resultado.getString(2)
                ));
            }

            // asignar permisos al rol
            respuesta.setListaPermisos(auxPermisos);

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

        // devolver resultado
        return respuesta;
    }

    @Override
    public List<Rol> searchAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

    public Rol searchByUser(Integer id){
        return null;
    }

}
