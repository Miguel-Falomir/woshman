package com.vehiculos.controller;

import java.sql.Connection;
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
    public void insert(Marca obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Marca obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Marca obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Marca search(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Marca> searchAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

}
