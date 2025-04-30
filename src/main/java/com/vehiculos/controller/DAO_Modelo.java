package com.vehiculos.controller;

import java.sql.Connection;
import java.util.List;

import com.utilities.DAO;
import com.utilities.DAO_Interface;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

}
