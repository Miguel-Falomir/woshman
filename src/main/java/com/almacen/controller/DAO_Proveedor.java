package com.almacen.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.almacen.model.Proveedor;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Proveedor extends DAO implements DAO_Interface<Proveedor, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Proveedor(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Proveedor obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try{
            // consulta 1: contar proveedores con prov.cif == obj.cif || prov.nombre == obj.nombre || prov.email == obj.email
            statement = connect.prepareStatement("SELECT count(*) FROM proveedor prov WHERE prov.cif = ? OR prov.nombre = ? OR email = ?;");
            statement.setString(1, obj.getCif());
            statement.setString(2, obj.getNombre());
            statement.setString(3, obj.getEmail());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // comprobar que nombre no se repita
            boolean datosUnique = (resultado.getInt(1) == 0);
            if (datosUnique) {
                // consulta 2: contar cantidad filas
                statement = connect.prepareStatement("SELECT count(*) FROM proveedor;");

                // ejecutar consulta
                resultado = statement.executeQuery();

                // forzar que 'resultado' apunte a primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();

                // guardar resultado
                int cantidadFilas = resultado.getInt(1);

                // consulta 3: agregar proveedor nuevo
                statement = connect.prepareStatement("INSERT INTO proveedor(id_proveedor, cif, nombre, email, direccion) VALUES (?, ?, ?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setString(2, obj.getCif());
                statement.setString(3, obj.getNombre());
                statement.setString(4, obj.getEmail());
                statement.setString(5, obj.getDireccion());

                // ejecutar consulta
                insert = statement.executeUpdate();
                System.out.println("INSERTAR NUEVO PROVEEDOR: " + insert);
                success = true;

            } else {
                System.out.println("ERROR: CIF, NOMBRE, Y/O EMAIL REPETIDOS");
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
    public boolean update(Proveedor obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: buscar proveedores con prov.cif == obj.cif || prov.nombre == obj.nombre || prov.email == obj.email
            statement = connect.prepareStatement("SELECT count(*) FROM proveedor prov WHERE (prov.cif = ? OR prov.nombre = ? OR email = ?) AND prov.id_proveedor <> ?;");
            statement.setString(1, obj.getCif());
            statement.setString(2, obj.getNombre());
            statement.setString(3, obj.getEmail());
            statement.setInt(4, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean datosUnique = (resultado.getInt(1) == 0);
            if (datosUnique) {
                // consulta 2: actualizar proveedor
                statement = connect.prepareStatement("UPDATE proveedor prov SET prov.cif = ?, prov.nombre = ?, prov.email = ?, prov.direccion = ? WHERE prov.id_proveedor = ?;");
                statement.setString(1, obj.getCif());
                statement.setString(2, obj.getNombre());
                statement.setString(3, obj.getEmail());
                statement.setString(4, obj.getDireccion());
                statement.setInt(5, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("ACTUALIZAR PROVEEDOR: " + update);
                success = true;

            } else {
                System.out.println("ERROR: CIF, NOMBRE, Y/O EMAIL REPETIDOS");
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
    public boolean delete(Proveedor obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int delete;
        boolean success = false;

        // (intentar) ejecutar borrado
        try{
            // consulta 1: comprobar que el proveedor no se encuentra en ninguna pieza
            statement = connect.prepareStatement("SELECT count(*) FROM pieza pi WHERE pi.fk_proveedor = ?;");
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
                statement = connect.prepareStatement("DELETE FROM proveedor prov WHERE prov.id_proveedor = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                delete = statement.executeUpdate();
                System.out.println("ELIMINAR PIEZA: " + delete);

                // consulta 3: actualizar manualmente IDs posteriores
                statement = connect.prepareStatement("UPDATE proveedor prov SET prov.id_proveedor = prov.id_proveedor - 1 WHERE prov.id_proveedor > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                delete = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + delete);
                success = true;

            } else {
                System.out.println("ERROR: PROVEEDOR REFERENCIADO EN 1 o MÁS PIEZAS");
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
    public Proveedor search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Proveedor respuesta = null;

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar pieza pi.id_pieza = id, tambien su tipo y proveedor
            statement = connect.prepareStatement("SELECT prov.id_proveedor, prov.cif, prov.nombre, prov.email, prov.direccion FROM proveedor prov WHERE prov.id_proveedor = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar 'resultado' en objeto 'respuesta'
            respuesta = new Proveedor(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3),
                resultado.getString(4),
                resultado.getString(5)
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
    public List<Proveedor> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Proveedor> respuesta = new ArrayList<Proveedor>();

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar pieza pi.id_pieza = id, tambien su tipo y proveedor
            statement = connect.prepareStatement("SELECT prov.id_proveedor, prov.cif, prov.nombre, prov.email, prov.direccion FROM proveedor prov;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while(resultado.next()){
                respuesta.add( new Proveedor(
                    resultado.getInt(1),
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getString(4),
                    resultado.getString(5)
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
