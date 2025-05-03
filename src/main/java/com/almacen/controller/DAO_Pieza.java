package com.almacen.controller;

import java.util.List;

import com.almacen.model.Pieza;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Pieza extends DAO implements DAO_Interface<Pieza, Integer> {

    @Override
    public boolean insert(Pieza obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Pieza obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Pieza obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Pieza search(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Pieza> searchAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

}
