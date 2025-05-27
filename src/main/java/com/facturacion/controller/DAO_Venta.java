package com.facturacion.controller;

import java.sql.Connection;
import java.sql.Date;
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
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int cantFacturas;
        int cantVentas;
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try{
            // consulta 1: contar cantidad filas (tabla factura)
            // , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE
            statement = connect.prepareStatement("SELECT count(*) FROM factura;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()) {
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar cantidad facturas
            cantFacturas = resultado.getInt(1);

            // consulta 2: insertar factura de la venta
            statement = connect.prepareStatement("INSERT INTO factura(id_factura, iva, precio_bruto, precio_total, fecha_pago) VALUES (?, ?, ?, ?, ?);");
            statement.setInt(1, cantFacturas);
            statement.setInt(2, 21);
            statement.setFloat(3, obj.getPrecio());
            statement.setFloat(4, (float) (obj.getPrecio() * 1.21));
            statement.setDate(5, Date.valueOf(obj.getFechaVenta()));

            // ejecutar insercion
            insert = statement.executeUpdate();
            System.out.println("CREAR FACTURA NUEVA: " + insert);

            // consulta 3: contar cantidad ventas
            statement = connect.prepareStatement("SELECT count(*) FROM venta;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()) {
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar cantidad filas
            cantVentas = resultado.getInt(1);

            // consulta 4: insertar venta nueva
            statement = connect.prepareStatement("INSERT INTO venta(id_venta, fk_factura, fk_cliente, precio_venta, fecha_venta) VALUES (?, ?, ?, ?, ?);");
            statement.setInt(1, cantVentas);
            statement.setInt(2, cantFacturas);
            statement.setInt(3, obj.getCliente().getId());
            statement.setFloat(4, obj.getPrecio());
            statement.setDate(5, Date.valueOf(obj.getFechaVenta()));

            // ejecutar insercion
            insert = statement.executeUpdate();
            System.out.println("INSERTAR VENTA: " + insert);

            // recorrer 'obj.listaPiezas', y en cada iteracion:
            for (Pieza pieza : obj.getListPiezas()) {
                // consulta 5: asignar pieza nueva a tabla 'venta_has_pieza'
                statement = connect.prepareStatement("INSERT INTO venta_has_pieza(venta, pieza, cantidad) VALUES (?, ?, ?);");
                statement.setInt(1, cantVentas);
                statement.setInt(2, pieza.getId());
                statement.setInt(3, pieza.getCantidad());

                // ejecutar insercion
                insert = statement.executeUpdate();
                System.out.println("INSERTAR PIEZA '" + pieza.getNombre() + "' EN VENTA " + cantVentas + ": " + insert);

                // consulta 3: restar cantidad a la tabla 'pieza'
                statement = connect.prepareStatement("UPDATE pieza pi SET pi.cantidad = pi.cantidad - ? WHERE pi.id_pieza = ?;");
                statement.setInt(1, pieza.getCantidad());
                statement.setInt(2, pieza.getId());

                // ejecutar actualizacion
                insert = statement.executeUpdate();
                System.out.println("ACTUALIZAR CANTIDAD PIEZA '" + pieza.getNombre() + "': " + insert);
            }

            success = true;

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
    public boolean update(Venta obj) {
        //throw new UnsupportedOperationException("Unimplemented method 'update'");
        return false;
    }

    @Override
    public boolean delete(Venta obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int delete;
        boolean zeroFactura;
        boolean success = false;

        // (intentar) ejecutar borrado
        try{
            // consulta 1: comprobar que la venta no pertenezca a ninguna factura
            statement = connect.prepareStatement("SELECT ven.fk_factura FRO venta ven WHERE ven.id_venta = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a la primera fila
            if (!(resultado.isBeforeFirst())) {
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            zeroFactura = (resultado.getInt(1) <= 0);

            if (zeroFactura) {
                // consulta 2: borrar piezas asignadas a la venta
                statement = connect.prepareStatement("DELETE FROM venta_has_pieza has WHERE has.venta = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar borrado
                delete = statement.executeUpdate();
                System.out.println("ELIMINAR REF. PIEZAS: " + delete);

                // consulta 3: borrar venta
                statement = connect.prepareStatement("DELETE FROM venta ven WHERE ven.id_venta = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar borrado
                delete = statement.executeUpdate();
                System.out.println("ELIMINAR VENTA: " + delete);

                // consulta 4: reorganizar IDs manualmente
                statement = connect.prepareStatement("UPDATE venta ven SET ven.id_venta = ven.id_venta - 1 WHERE ven.id_venta > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                delete = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs: " + delete);
                success = true;
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

        // devolver 'success', para indicar si se ha completado el borrado
        return success;
    }

    @Override
    public Venta search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Venta respuesta = null;

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar todas las ventas
            statement = connect.prepareStatement("SELECT ven.id_venta, ven.fk_factura, ven.fk_cliente, ven.precio_venta, ven.fecha_venta FROM venta ven;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // agregar resultados a 'respuesta'
            respuesta = new Venta(
                resultado.getInt(1),
                resultado.getInt(2),
                searchCliente(resultado.getInt(3)),
                resultado.getFloat(4),
                resultado.getDate(5).toLocalDate(),
                searchListPiezas(resultado.getInt(1))
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
    public List<Venta> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Venta> respuesta = new ArrayList<Venta>();

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar todas las ventas
            statement = connect.prepareStatement("SELECT ven.id_venta, ven.fk_factura, ven.fk_cliente, ven.precio_venta, ven.fecha_venta FROM venta ven;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

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
                    resultado.getInt(2),
                    searchCliente(resultado.getInt(3)),
                    resultado.getFloat(4),
                    resultado.getDate(5).toLocalDate(),
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