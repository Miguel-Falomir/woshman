package com.facturacion.model;

import java.time.LocalDate;
import java.util.List;

import com.almacen.model.Pieza;
import com.utilities.Cargo;

public class Venta extends Cargo {

    // ATRIBUTOS

    private Integer id;
    private Integer idFactura;
    private Cliente cliente;
    private Float precio = 0.0f;
    private LocalDate fechaVenta;
    private List<Pieza> listPiezas;

    // CONSTRUCTORES

    public Venta(){}

    public Venta(Integer id, Integer idFactura, Cliente cliente, LocalDate fechaVenta, List<Pieza> listPiezas){
        this.id = id;
        this.idFactura = idFactura;
        this.cliente = cliente;
        this.fechaVenta = fechaVenta;
        this.listPiezas = listPiezas;
        for (Pieza pieza : listPiezas) {
            Float precio = pieza.getPrecio();
            int cant = pieza.getCantidad();
            precio += precio * cant;
        }
    }

    public Venta(Integer id, Integer idFactura, Cliente cliente, Float precio, LocalDate fechaVenta, List<Pieza> listPiezas){
        this.id = id;
        this.idFactura = idFactura;
        this.cliente = cliente;
        this.precio = precio;
        this.fechaVenta = fechaVenta;
        this.listPiezas = listPiezas;
    }

    // GETTERS Y SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public List<Pieza> getListPiezas() {
        return listPiezas;
    }

    public void setListPiezas(List<Pieza> listPiezas) {
        this.listPiezas = listPiezas;
        precio = 0.0f;
        for (Pieza pieza : listPiezas) {
            Float precio = pieza.getPrecio();
            int cant = pieza.getCantidad();
            precio += precio * cant;
        }
    }

    // TOSTRING, HASHCODE Y EQUALS

    @Override
    public String toString() {
        return "Venta [id=" + id + ", cliente=" + cliente + ", precio=" + precio + ", fechaVenta=" + fechaVenta
                + ", listPiezas=" + listPiezas + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((idFactura == null) ? 0 : idFactura.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        result = prime * result + ((precio == null) ? 0 : precio.hashCode());
        result = prime * result + ((fechaVenta == null) ? 0 : fechaVenta.hashCode());
        result = prime * result + ((listPiezas == null) ? 0 : listPiezas.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Venta other = (Venta) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (idFactura == null) {
            if (other.idFactura != null)
                return false;
        } else if (!idFactura.equals(other.idFactura))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        if (precio == null) {
            if (other.precio != null)
                return false;
        } else if (!precio.equals(other.precio))
            return false;
        if (fechaVenta == null) {
            if (other.fechaVenta != null)
                return false;
        } else if (!fechaVenta.equals(other.fechaVenta))
            return false;
        if (listPiezas == null) {
            if (other.listPiezas != null)
                return false;
        } else if (!listPiezas.equals(other.listPiezas))
            return false;
        return true;
    }
}
