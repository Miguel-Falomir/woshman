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
import com.vehiculos.model.Vehiculo;

public class DAO_Vehiculo extends DAO implements DAO_Interface<Vehiculo, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Vehiculo(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public void insert(Vehiculo obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Vehiculo obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Vehiculo obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Vehiculo search(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Vehiculo> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Vehiculo> respuesta = new ArrayList<Vehiculo>();
        List<Marca> auxMarcas = new ArrayList<Marca>();
        List<Categoria> auxCategorias = new ArrayList<Categoria>();
        List<Modelo> auxModelos = new ArrayList<Modelo>();

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

            // agregar todas las respuestas a lista 'auxModelos'
            while(resultado.next()){
                Marca marca = auxMarcas.get( resultado.getInt(3) );
                Categoria categoria = auxCategorias.get( resultado.getInt(4) );
                auxModelos.add( new Modelo(
                    resultado.getInt(1),
                    resultado.getString(2),
                    marca,
                    categoria
                ));
            }

            // consulta 4: Buscar todos los vehiculos ///////////////
            statement = connect.prepareStatement("SELECT v.id_vehiculo, v.matricula, v.fk_modelo FROM vehiculo v ORDER BY v.id_vehiculo ASC;");

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'listVehiculos'
            while(resultado.next()){
                Modelo modelo = auxModelos.get( resultado.getInt(3) );
                respuesta.add( new Vehiculo(
                    resultado.getInt(1), 
                    resultado.getString(2),
                    modelo
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

    public List<Vehiculo> searchByMarca(){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

    public List<Vehiculo> searchByModelo(){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

    public List<Vehiculo> searchByMatricula(String matricula){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

}
