package com.vehiculos.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.utilities.DAO;
import com.utilities.DAO_Interface;
import com.vehiculos.model.Categoria;
import com.vehiculos.model.Marca;
import com.vehiculos.model.Modelo;

public class DAO_Modelo extends DAO implements DAO_Interface<Modelo, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Modelo(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public void insert(Modelo obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Modelo obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Modelo obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Modelo search(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Modelo> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Modelo> respuesta = new ArrayList<Modelo>();
        List<Marca> auxMarcas = new ArrayList<Marca>();
        List<Categoria> auxCategorias = new ArrayList<Categoria>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas //////////////////
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma ORDER BY ma.id_marca ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxMarcas'
            while(resultado.next()){
                auxMarcas.add( new Marca(
                    resultado.getInt(1),
                    resultado.getString(2)
                ));
            }

            // consulta 2: Buscar todas las categorias //////////////
            statement = connect.prepareStatement("SELECT ca.id_categoria, ca.nombre, ca.descripcion FROM categoria ca ORDER BY ca.id_categoria ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxCategorias'
            while(resultado.next()){
                auxCategorias.add( new Categoria(
                    resultado.getInt(1),
                    resultado.getString(2),
                    (resultado.getString(3) == null) ? "": resultado.getString(3) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                ));
            }

            // consulta 3: Buscar todos los modelos /////////////////
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.nombre, mo.fk_marca, mo.fk_categoria FROM modelo mo ORDER BY mo.id_modelo ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'respuesta'
            while(resultado.next()){
                Marca marca = auxMarcas.get( resultado.getInt(3) );
                Categoria categoria = auxCategorias.get( resultado.getInt(4) );
                respuesta.add( new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
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

    public List<Modelo> searchByMarca(Integer id_marca){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Modelo> respuesta = new ArrayList<Modelo>();
        List<Marca> auxMarcas = new ArrayList<Marca>();
        List<Categoria> auxCategorias = new ArrayList<Categoria>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas //////////////////
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma ORDER BY ma.id_marca ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxMarcas'
            while(resultado.next()){
                auxMarcas.add( new Marca(
                    resultado.getInt(1),
                    resultado.getString(2)
                ));
            }

            // consulta 2: Buscar todas las categorias //////////////
            statement = connect.prepareStatement("SELECT ca.id_categoria, ca.nombre, ca.descripcion FROM categoria ca ORDER BY ca.id_categoria ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxCategorias'
            while(resultado.next()){
                auxCategorias.add( new Categoria(
                    resultado.getInt(1),
                    resultado.getString(2),
                    (resultado.getString(3) == null) ? "": resultado.getString(3) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                ));
            }

            // consulta 3: Buscar modelos de la marca ///////////////
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.nombre, mo.fk_marca, mo.fk_categoria FROM modelo mo WHERE mo.fk_marca = ? ORDER BY mo.id_modelo ASC;");
            statement.setInt(1, id_marca);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'respuesta'
            while(resultado.next()){
                Marca marca = auxMarcas.get( resultado.getInt(3) );
                Categoria categoria = auxCategorias.get( resultado.getInt(4) );
                respuesta.add( new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
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

    public List<Modelo> searchByCategoria(Integer id_cat){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Modelo> respuesta = new ArrayList<Modelo>();
        List<Marca> auxMarcas = new ArrayList<Marca>();
        List<Categoria> auxCategorias = new ArrayList<Categoria>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar todas las marcas //////////////////
            statement = connect.prepareStatement("SELECT ma.id_marca, ma.nombre FROM marca ma ORDER BY ma.id_marca ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxMarcas'
            while(resultado.next()){
                auxMarcas.add( new Marca(
                    resultado.getInt(1),
                    resultado.getString(2)
                ));
            }

            // consulta 2: Buscar todas las categorias //////////////
            statement = connect.prepareStatement("SELECT ca.id_categoria, ca.nombre, ca.descripcion FROM categoria ca ORDER BY ca.id_categoria ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxCategorias'
            while(resultado.next()){
                auxCategorias.add( new Categoria(
                    resultado.getInt(1),
                    resultado.getString(2),
                    (resultado.getString(3) == null) ? "": resultado.getString(3) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                ));
            }

            // consulta 3: Buscar modelos de la categoria ///////////
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.fk_marca, mo.fk_categoria, mo.nombre FROM modelo mo WHERE mo.fk_categoria = ? ORDER BY mo.id_modelo ASC;");
            statement.setInt(1, id_cat);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'respuesta'
            while(resultado.next()){
                Marca marca = auxMarcas.get( resultado.getInt(3) );
                Categoria categoria = auxCategorias.get( resultado.getInt(4) );
                respuesta.add( new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
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
