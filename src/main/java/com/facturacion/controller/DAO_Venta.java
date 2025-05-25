package com.facturacion.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.almacen.model.Pieza;
import com.almacen.model.Proveedor;
import com.almacen.model.Tipo_Pieza;
import com.facturacion.model.Cliente;
import com.facturacion.model.Venta;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Venta extends DAO implements DAO_Interface<Venta, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Venta(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Venta obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Venta obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Venta obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Venta search(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Venta> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Venta> respuesta = new ArrayList<Venta>();

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar todas las ventas
            statement = connect.prepareStatement("SELECT ven.id_venta, ven.fk_cliente, ven.precio_venta, ven.fecha_venta FROM venta ven;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while (resultado.next()) {
                Venta venta = new Venta(
                    resultado.getInt(1),
                    searchCliente(resultado.getInt(2)),
                    resultado.getFloat(3),
                    resultado.getDate(4).toLocalDate(),
                    searchListPiezas(resultado.getInt(1))
                );
                respuesta.add(venta);
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

    // METODOS OTROS DAOs

    private Cliente searchCliente(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Cliente respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos del cliente cli.id_cliente = id
            statement = connect.prepareStatement("SELECT cli.id_cliente, cli.dni, cli.nombre, cli.apellidos, cli.email, direccion FROM cliente cli WHERE cli.id_cliente = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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

    private List<Pieza> searchListPiezas(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Pieza> respuesta = new ArrayList<Pieza>();

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar pieza pi.id_pieza = id, tambien su tipo y proveedor
            statement = connect.prepareStatement("SELECT pi.id_pieza, pi.nombre, pi.descripcion, pi.precio, has.cantidad, pr.id_proveedor, pr.cif, pr.nombre, pr.email, pr.direccion, tp.id_tipo_pieza, tp.nombre, tp.descripcion FROM venta_has_pieza has JOIN pieza pi ON has.pieza = pi.id_pieza JOIN tipo_pieza tp ON pi.fk_tipo_pieza = tp.id_tipo_pieza JOIN proveedor pr ON pi.fk_proveedor = pr.id_proveedor WHERE has.venta = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while (resultado.next()) {
                Tipo_Pieza tipo = new Tipo_Pieza(
                    resultado.getInt(11),
                    resultado.getString(12),
                    resultado.getString(13)
                );
                Proveedor proveedor = new Proveedor(
                    resultado.getInt(6),
                    resultado.getString(7),
                    resultado.getString(8),
                    resultado.getString(9),
                    resultado.getString(10)
                );
                respuesta.add( new Pieza(
                    resultado.getInt(1),
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getFloat(4),
                    resultado.getInt(5),
                    tipo,
                    proveedor
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