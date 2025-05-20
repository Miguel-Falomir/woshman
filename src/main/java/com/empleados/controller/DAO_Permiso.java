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
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: buscar todos los permisos con per.nombre == obj.nombre
            statement = connect.prepareStatement("SELECT count(*) FROM permiso per WHERE per.nombre = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
                statement = connect.prepareStatement("SELECT count(*) FROM permiso;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                // ejecutar consulta
                resultado = statement.executeQuery();

                // forzar que 'resultado' apunte a primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();

                // guardar resultado
                int cantidadFilas = resultado.getInt(1);

                // consulta 3: agregar permiso nuevo a BD
                statement = connect.prepareStatement("INSERT INTO permiso(id_permiso, nombre, descripcion) VALUES (?,?,?);");
                statement.setInt(1, cantidadFilas);
                statement.setString(2, obj.getNombre());
                statement.setString(3, obj.getDescripcion());

                // ejecutar insercion
                insert = statement.executeUpdate();
                System.out.println("INSERTAR NUEVO PERMISO: " + insert);
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
    public boolean update(Permiso obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: comprobar que obj.nombre no se repita en BD
            statement = connect.prepareStatement("SELECT count(*) FROM permiso per WHERE per.nombre = ? AND per.id_permiso <> ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
                // consulta 2: actualizar permiso
                statement = connect.prepareStatement("UPDATE permiso per SET per.nombre = ?, per.descripcion = ? WHERE per.id_permiso = ?;");
                statement.setString(1, obj.getNombre());
                statement.setString(2, obj.getDescripcion());
                statement.setInt(3, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("ACTUALIZAR PERMISO: " + update);
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
    public boolean delete(Permiso obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int delete;
        boolean success = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: comprobar que obj.id no aparece en ningun rol
            statement = connect.prepareStatement("SELECT count(*) FROM rol_has_permiso has WHERE has.permiso = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroRoles = (resultado.getInt(1) == 0);
            if (zeroRoles) {
                // consulta 2: borrar permiso
                statement = connect.prepareStatement("DELETE FROM permiso per WHERE per.id_permiso = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                delete = statement.executeUpdate();
                System.out.println("ELIMINAR PERMISO: " + delete);

                // consulta 3: reorganizar IDs manualmente
                statement = connect.prepareStatement("UPDATE permiso per SET per.id_permiso = per.id_permiso - 1 WHERE per.id_permiso > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                delete = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + delete);
                success = true;
            } else {
                System.out.println("ERROR: EL PERMISO TIENE ASIGNADOS 1 O MÁS ROLES.");
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
