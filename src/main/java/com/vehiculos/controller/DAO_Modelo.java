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

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca de una tajada
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM modelo mo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca ORDER BY mo.id_modelo ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultado a lista 'respuesta'
            while (resultado.next()){
                Marca marca = new Marca(
                    resultado.getInt(6),
                    resultado.getString(7)
                );
                Categoria categoria = new Categoria(
                    resultado.getInt(3),
                    resultado.getString(4),
                    (resultado.getString(5) == null) ? "": resultado.getString(7) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                );
                Modelo modelo = new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
                );
                respuesta.add(modelo);
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

    public List<Modelo> searchByMarca(Integer id){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Modelo> respuesta = new ArrayList<Modelo>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca de una tajada
            statement = connect.prepareStatement("SELECT mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM modelo mo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca WHERE ma.id_marca = ? ORDER BY mo.id_modelo ASC;");
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultado a lista 'respuesta'
            while (resultado.next()){
                Marca marca = new Marca(
                    resultado.getInt(6),
                    resultado.getString(7)
                );
                Categoria categoria = new Categoria(
                    resultado.getInt(3),
                    resultado.getString(4),
                    (resultado.getString(5) == null) ? "": resultado.getString(7) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
                );
                Modelo modelo = new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
                );
                respuesta.add(modelo);
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
