package com.vehiculos.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.utilities.DAO;
import com.utilities.DAO_Interface;
import com.vehiculos.model.Marca;

public class DAO_Marca extends DAO implements DAO_Interface<Marca, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Marca(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Marca obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int insert;
        boolean success = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: buscar todas las marcas con ma.nombre == obj.nombre
            statement = connect.prepareStatement("SELECT count(*) FROM marca ma WHERE ma.nombre = ?;");
            statement.setString(1, obj.getNombre());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // comprobar que matricula no se repita
            boolean nombreUnique = (resultado.getInt(1) == 0);
            if (nombreUnique) {
                // consulta 2: contar cantidad filas
                statement = connect.prepareStatement("SELECT count(*) FROM marca;");

                // ejecutar consulta
                resultado = statement.executeQuery();

                // forzar que 'resultado' apunte a primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();

                // guardar resultado
                int cantidadFilas = resultado.getInt(1);

                // consulta 3: agregar nueva marca a base de datos
                statement = connect.prepareStatement("INSERT INTO marca(id_marca, nombre) VALUES (?, ?);");
                statement.setInt(1, cantidadFilas);
                statement.setString(2, obj.getNombre());

                // ejecutar insercion
                insert = statement.executeUpdate();
                System.out.println("INSERTAR NUEVA MARCA: " + insert);
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
    public boolean update(Marca obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int update;
        boolean success = false;

        // (intentar) ejecutar actualizacion
        try {
            // consulta 1: comprobar que el nombre de la marca no se repite
            statement = connect.prepareStatement("SELECT count(*) FROM marca ma WHERE ma.nombre = ? AND ma.id_marca <> ?;");
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
                // consulta 2: actualizar marca
                statement = connect.prepareStatement("UPDATE marca ma SET ma.nombre = ? WHERE ma.id_marca = ?;");
                statement.setString(1, obj.getNombre());
                statement.setInt(2, obj.getId());

                // ejecutar actualizacion
                update = statement.executeUpdate();
                System.out.println("ACTUALIZAR MARCA: " + update);
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
    public boolean delete(Marca obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        int delete;
        boolean success = false;

        // (intentar) ejecutar borrado
        try {
            // consulta 1: comprobar que obj.id no aparece en ningun modelo
            statement = connect.prepareStatement("SELECT count(*) FROM modelo mo JOIN marca ma ON mo.fk_marca = ma.id_marca WHERE ma.id_marca = ?;");
            statement.setInt(1, obj.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean zeroModelos = (resultado.getInt(1) == 0);
            if (zeroModelos) {
                // consulta 2: borrar marca
                statement = connect.prepareStatement("DELETE FROM marca ma WHERE ma.id_marca = ?;");
                statement.setInt(1, obj.getId());

                // ejecutar borrado
                delete = statement.executeUpdate();
                System.out.println("ELIMINAR MARCA: " + delete);

                // consulta 3: actualizar manualmente IDs posteriores
                statement = connect.prepareStatement("UPDATE marca ma SET ma.id_marca = ma.id_marca - 1 WHERE ma.id_marca > ?;");
                statement.setInt(1, obj.getId());

                // ejecutar actualizacion
                delete = statement.executeUpdate();
                System.out.println("REORGANIZAR IDs MANUALMENTE: " + delete);
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
    public Marca search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Marca respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar marca con ma.id_marca = id
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma WHERE ma.id_marca = ?;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado en respuesta
            respuesta = new Marca(
                resultado.getInt(1),
                resultado.getString(2)
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
    public List<Marca> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Marca> respuesta = new ArrayList<Marca>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma ORDER BY ma.id_marca ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'auxMarcas'
            while(resultado.next()){
                respuesta.add( new Marca(
                    resultado.getInt(1),
                    resultado.getString(2)
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

    public List<Marca> searchAllAlphabetically(){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Marca> respuesta = new ArrayList<Marca>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas por orden alfabetico
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma ORDER BY ma.nombre ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while(resultado.next()){
                respuesta.add( new Marca(
                    resultado.getInt(1),
                    resultado.getString(2)
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
