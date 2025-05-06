package com.facturacion.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facturacion.model.Cliente;
import com.utilities.DAO;
import com.utilities.DAO_Interface;
import com.vehiculos.model.Categoria;
import com.vehiculos.model.Marca;
import com.vehiculos.model.Modelo;
import com.vehiculos.model.Vehiculo;

public class DAO_Cliente extends DAO implements DAO_Interface<Cliente, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Cliente(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Cliente obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int cantidadFilas;
        boolean exito = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: contar cantidad filas
            statement = connect.prepareStatement("SELECT count(*) FROM cliente;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado
            cantidadFilas = resultado.getInt(1);

            // consulta 2: buscar todos los clientes con c.dni == obj.dni
            statement = connect.prepareStatement("SELECT count(*) FROM cliente c WHERE c.dni = ?;");
            statement.setString(1, obj.getDni());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado
            boolean dniUnique = (resultado.getInt(1) == 0);
            
            // consulta 3: buscar todos los clientes con c.email == obj.email
            statement = connect.prepareStatement("SELECT count(*) FROM cliente c WHERE c.email = ?;");
            statement.setString(1, obj.getEmail());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado
            boolean emailUnique = (resultado.getInt(1) == 0);

            // comprobar que tanto dni como email no se repitan
            if(dniUnique && emailUnique){
                // consulta 4: agregar cliente nuevo a BD
                statement = connect.prepareStatement("INSERT INTO cliente(id_cliente, dni, nombre, apellidos, email, direccion) VALUES (?, ?, ?, ?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setString(2, obj.getDni());
                statement.setString(3, obj.getNombre());
                statement.setString(4, obj.getApellidos());
                statement.setString(5, obj.getEmail());
                statement.setString(6, obj.getDireccion());
                System.out.println("INSERTAR CLIENTE NUEVO: " + statement.executeUpdate());
                exito = true;
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

        // devolver 'exito', para indicar si se ha completado la insercion
        return exito;
    }

    @Override
    public boolean update(Cliente obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        boolean exito = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: buscar todos los clientes con c.dni == obj.dni
            statement = connect.prepareStatement("SELECT count(*) FROM cliente c WHERE c.dni = ? AND c.id_cliente <> ?;");
            statement.setString(1, obj.getDni());
            statement.setInt(2, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado
            boolean dniUnique = (resultado.getInt(1) == 0);
            
            // consulta 2: buscar todos los clientes con c.email == obj.email
            statement = connect.prepareStatement("SELECT count(*) FROM cliente c WHERE c.email = ? AND c.id_cliente <> ?;");
            statement.setString(1, obj.getEmail());
            statement.setInt(2, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado
            boolean emailUnique = (resultado.getInt(1) == 0);

            // comprobar que tanto dni como email no se repitan
            if(dniUnique && emailUnique){
                // consulta 4: agregar cliente nuevo a BD
                statement = connect.prepareStatement("UPDATE cliente c SET c.dni = ?, c.nombre = ?, c.apellidos = ?, c.email = ?, c.direccion = ? WHERE c.di_cliente = ?;");
                statement.setString(1, obj.getDni());
                statement.setString(2, obj.getNombre());
                statement.setString(3, obj.getApellidos());
                statement.setString(4, obj.getEmail());
                statement.setString(5, obj.getDireccion());
                System.out.println("ACTUALIZAR CLIENTE: " + statement.executeUpdate());
                exito = true;
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

        // devolver 'exito', para indicar si se ha completado la actualizacion
        return exito;
    }

    @Override
    public boolean delete(Cliente obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean exito = false;
    
        // (intentar) ejecutar eliminacion
        try {
            // consulta 1: comprobar que id cliente no aparece en ninguna averia
            statement = connect.prepareStatement("SELECT count(*) FROM averia a WHERE a.fk_cliente = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroAverias = (resultado.getInt(1) == 0);

            // consulta 2: comprobar que id cliente no aparece en ninguna venta
            statement = connect.prepareStatement("SELECT count(*) FROM venta v WHERE v.fk_cliente = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroVentas = (resultado.getInt(1) == 0);

            // comprobar que cliente no figura en ninguna venta o averia
            if (zeroAverias && zeroVentas){
                // consuta 3: borrar cliente
                statement = connect.prepareStatement("DELETE FROM cliente WHERE id_cliente = ?");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                update = statement.executeUpdate();
                System.out.println("ELIMINAR CLIENTE: " + update);

                // consulta 4: actualizar IDs posteriores
                statement = connect.prepareStatement("UPDATE cliente SET id_cliente = id_cliente - 1 WHERE id_cliente > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion IDs
                update = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + update);
                exito = true;
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

        // devolver 'exito', para indicar si se ha completado la actualizacion
        return exito;
    }

    @Override
    public Cliente search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Cliente respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos del cliente c.id_cliente = id
            statement = connect.prepareStatement("SELECT c.id_cliente, c.dni, c.nombre, c.apellidos, c.email, direccion FROM cliente c WHERE c.id_cliente = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado en objeto 'respuesta'
            respuesta = new Cliente(
                resultado.getInt(1),
                resultado.getString(2),
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
    public List<Cliente> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Cliente> respuesta = new ArrayList<Cliente>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los clientes
            statement = connect.prepareStatement("SELECT c.id_cliente, c.dni, c.nombre, c.apellidos, c.email, direccion FROM cliente c;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while (resultado.next()){
                Cliente cliente = new Cliente(
                    resultado.getInt(1),
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getString(4),
                    resultado.getString(5),
                    resultado.getString(6)
                );
                respuesta.add(cliente);
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
