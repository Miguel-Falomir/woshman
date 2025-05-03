package com.facturacion.controller;

import java.util.List;

import com.facturacion.model.Factura;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Factura extends DAO implements DAO_Interface<Factura, Integer> {

    @Override
    public boolean insert(Factura obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Factura obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Factura obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Factura search(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Factura> searchAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

}
