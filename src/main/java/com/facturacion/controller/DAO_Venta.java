package com.facturacion.controller;

import java.util.List;

import com.facturacion.model.Venta;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Venta extends DAO implements DAO_Interface<Venta, Integer> {

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

}
