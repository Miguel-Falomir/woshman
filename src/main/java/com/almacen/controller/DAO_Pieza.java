package com.almacen.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.almacen.model.Pieza;
import com.almacen.model.Proveedor;
import com.almacen.model.Tipo_Pieza;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Pieza extends DAO implements DAO_Interface<Pieza, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Pieza(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Pieza obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try{
            // consulta 1: buscar todas las piezas con pi.nombre == obj.nombre
            statement = connect.prepareStatement("SELECT count(*) FROM pieza pi WHERE pi.nombre = ?");
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
                statement = connect.prepareStatement("SELECT count(*) FROM categoria;");

                // ejecutar consulta
                resultado = statement.executeQuery();

                // forzar que 'resultado' apunte a primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();

                // guardar resultado
                int cantidadFilas = resultado.getInt(1);

                // consulta 3: agregar pieza nueva a base de datos
                statement = connect.prepareStatement("INSERT INTO pieza(id_pieza, fk_tipo_pieza, fk_proveedor, nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?, ?, ?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setInt(2, obj.getTipo().getId());
                statement.setInt(3, obj.getProveedor().getId());
                statement.setString(4, obj.getNombre());
                statement.setString(5, obj.getDescripcion());
                statement.setFloat(6, obj.getPrecio());
                statement.setInt(7, obj.getCantidad());

                // ejecutar consulta
                insert = statement.executeUpdate();
                System.out.println("INSERTAR NUEVA PIEZA: " + insert);
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
    public boolean update(Pieza obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: comprobar que el nuevo nombre no se repite
            statement = connect.prepareStatement("SELECT count(*) FROM pieza pi WHERE pi.nombre = ? AND pi.id_pieza <> ?;");
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
                // consulta 2: actualizar pieza
                statement = connect.prepareStatement("UPDATE pieza pi set pi.fk_tipo_pieza = ?, pi.fk_proveedor = ?, pi.nombre = ?, pi.descripcion = ?, pi.precio = ?, pi.cantidad = ? WHERE pi.id_vehiculo = ?;");
                statement.setInt(1, obj.getTipo().getId());
                statement.setInt(2, obj.getProveedor().getId());
                statement.setString(3, obj.getNombre());
                statement.setString(4, obj.getDescripcion());
                statement.setFloat(5, obj.getPrecio());
                statement.setInt(6, obj.getCantidad());
                statement.setInt(7, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("ACTUALIZAR PIEZA: " + update);
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

        // devolver 'success', para indicar si se ha completado la actualizacion
        return success;
    }

    @Override
    public boolean delete(Pieza obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar borrado
        try{
            // consulta 1: comprobar que la pieza no se encuentra en ninguna venta y/o averia
            statement = connect.prepareStatement("SELECT count(*) FROM pieza pi JOIN pieza_has_averia ave ON ave.pieza = pi.id_pieza JOIN venta_has_pieza ven ON ven.pieza = pi.id_pieza WHERE pi.id_pieza = ?;");
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
            if (zeroAverias) {
                // consulta 2: borrar pieza
                statement = connect.prepareStatement("DELETE FROM pieza pi WHERE pi.id_pieza = ?");
                statement.setInt(1, obj.getId());

                // ejecutar eliminacion
                update = statement.executeUpdate();
                System.out.println("ELIMINAR PIEZA: " + update);

                // consulta 3: actualizar manualmente IDs posteriores
                statement = connect.prepareStatement("UPDATE pieza pi SET pi.id_pieza = pi.id_pieza - 1 WHERE pi.id_pieza > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + update);
                success = true;
            } else {
                System.out.println("ERROR: LA PIEZA TIENE ASIGNADAS 1 O MÁS VENTAS Y/O AVERÍAS.");
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
    public Pieza search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Pieza respuesta = null;

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar pieza pi.id_pieza = id, tambien su tipo y proveedor
            statement = connect.prepareStatement("SELECT pi.id_pieza, pi.nombre, pi.descripcion, pi.precio, pi.cantidad, pr.id_proveedor, pr.cif, pr.nombre, pr.email, pr.direccion, tp.id_tipo_pieza, tp.nombre FROM pieza pi JOIN tipo_pieza tp ON pi.fk_tipo_pieza = tp.id_tipo_pieza JOIN proveedor pr ON pi.fk_proveedor = pr.id_proveedor WHERE pi.id_pieza = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado en objeto 'respuesta'
            Tipo_Pieza tipo = new Tipo_Pieza(
                resultado.getInt(11),
                resultado.getString(12)
            );
            Proveedor proveedor = new Proveedor(
                resultado.getInt(6),
                resultado.getString(7),
                resultado.getString(8),
                resultado.getString(9),
                resultado.getString(10)
            );
            respuesta = new Pieza(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3),
                resultado.getFloat(4),
                resultado.getInt(5),
                tipo,
                proveedor
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
    public List<Pieza> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Pieza> respuesta = new ArrayList<Pieza>();

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar todas las piezas, tambien el tipo y proveedor de cada una
            statement = connect.prepareStatement("SELECT pi.id_pieza, pi.nombre, pi.descripcion, pi.precio, pi.cantidad, pr.id_proveedor, pr.cif, pr.nombre, pr.email, pr.direccion, tp.id_tipo_pieza, tp.nombre FROM pieza pi JOIN tipo_pieza tp ON pi.fk_tipo_pieza = tp.id_tipo_pieza JOIN proveedor pr ON pi.fk_proveedor = pr.id_proveedor;");
            
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
                    resultado.getString(12)
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
