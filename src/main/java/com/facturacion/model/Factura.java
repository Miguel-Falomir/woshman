package com.facturacion.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.utilities.Cargo;

public class Factura {

    // ATRIBUTOS

    private Integer id;
    private Integer iva;
    private Float precioBruto;
    private Float precioTotal;
    private LocalDate pago;
    private List<Cargo> listCargos;

    // CONSTRUCTORES

    public Factura(){}

    public Factura(Integer id, Averia averia){
        this.id = id;
        this.iva = 10;
        this.precioBruto = averia.getPrecio();
        this.precioTotal = 0.0f;
        this.pago = null;
        this.listCargos = new ArrayList<Cargo>( Arrays.asList(
            averia
        ));
    }

    public Factura(Integer id, Venta venta){
        this.id = id;
        this.iva = 21;
        this.precioBruto = venta.getPrecio();
        this.precioTotal = 0.0f;
        this.pago = null;
        this.listCargos = new ArrayList<Cargo>( Arrays.asList(
            venta
        ));
    }

    public Factura(Integer id, Integer iva, Float precioBruto, Float precioTotal, LocalDate pago, List<Cargo> listCargos){
        this.id = id;
        this.iva = iva;
        this.precioBruto = precioBruto;
        this.precioTotal = precioTotal;
        this.pago = pago;
        Cargo first = listCargos.getFirst();
        if (first instanceof Averia) {
            this.listCargos = new ArrayList<>( Arrays.asList(
                (Averia) first
            ));
        } else if (first instanceof Venta) {
            this.listCargos = new ArrayList<>( Arrays.asList(
                (Venta) first
            ));
        } else {
            this.listCargos = null;
        }
    }

    // GETTERS Y SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public Float getPrecioBruto() {
        return precioBruto;
    }

    public void setPrecioBruto(Float precioBruto) {
        this.precioBruto = precioBruto;
    }

    public Float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Float precioTotal) {
        this.precioTotal = precioTotal;
    }

    public LocalDate getPago() {
        return pago;
    }

    public void setPago(LocalDate pago) {
        this.pago = pago;
    }

    public List<Cargo> getListCargos() {
        return listCargos;
    }

    public void setListCargos(List<Cargo> listaCargos) {
        this.listCargos = listaCargos;
    }

    // METODO AGREGAR CARGO

    public void insertCargo(Cargo cargo) {
        // recopilar clases 'cargo' y 'listaCargos.getFirst()' para comparar
        Class<? extends Cargo> listaClass = listCargos.getFirst().getClass();
        Class<? extends Cargo> cargoClass = cargo.getClass();
        // comprobar que las clases coinciden
        if (cargoClass.equals(listaClass)) {
            if (cargo instanceof Averia) { // guardar 'cargo' como averia
                Averia averia = (Averia) cargo;
                listCargos.add(averia);
                precioBruto += averia.getPrecio();
            } else if (cargo instanceof Venta) { // guardar 'cargo' como venta
                Venta venta = (Venta) cargo;
                listCargos.add(venta);
                //precioBruto += venta.getPrecio();
            }
        // si no coinciden, mostrar mensaje error
        } else {
            if (listaClass.equals(Averia.class)) {
                System.out.println("ERROR: 'listaCargos' SOLO ADMITE AVERÍAS");
            } else if (listaClass.equals(Venta.class)) {
                System.out.println("ERROR: 'listaCargos' SOLO ADMITE VENTAS");
            }
        }
    }

    // METODO QUITAR CARGO

    public void removeCargo(Cargo cargo){
        // comprobar tipo cargo
        if (cargo instanceof Averia){
            // quitar averia de 'listaCargos'
            Averia averia = (Averia) cargo;
            listCargos.removeIf(i -> {
                Averia a = (Averia) i;
                return a.getId() == averia.getId();
            });
            // restar precio
            precioBruto -= averia.getPrecio();
        } else if (cargo instanceof Venta) {
            /*
            // quitar venta de 'listaCargos'
            Venta venta = (Venta) cargo;
            listaCargos.removeIf(i -> {
                Venta v = (Venta) cargo;
                return v.getId() == venta.getId();
            });
            // restar precio
            precioBruto -= venta.getPrecio();
            */
        }

    }

    // METODO PAGAR

    public void pay(){
        this.pago = LocalDate.now();
        this.precioTotal = precioBruto + (precioBruto * (iva / 100));
    }

    // TOSTRING, HASHCODE Y EQUALS

    @Override
    public String toString() {
        return "Factura [id=" + id + ", iva=" + iva + ", precioBruto=" + precioBruto + ", precioTotal=" + precioTotal
                + ", pago=" + pago + ", listaCargos=" + listCargos + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((iva == null) ? 0 : iva.hashCode());
        result = prime * result + ((precioBruto == null) ? 0 : precioBruto.hashCode());
        result = prime * result + ((precioTotal == null) ? 0 : precioTotal.hashCode());
        result = prime * result + ((pago == null) ? 0 : pago.hashCode());
        result = prime * result + ((listCargos == null) ? 0 : listCargos.hashCode());
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
        Factura other = (Factura) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (iva == null) {
            if (other.iva != null)
                return false;
        } else if (!iva.equals(other.iva))
            return false;
        if (precioBruto == null) {
            if (other.precioBruto != null)
                return false;
        } else if (!precioBruto.equals(other.precioBruto))
            return false;
        if (precioTotal == null) {
            if (other.precioTotal != null)
                return false;
        } else if (!precioTotal.equals(other.precioTotal))
            return false;
        if (pago == null) {
            if (other.pago != null)
                return false;
        } else if (!pago.equals(other.pago))
            return false;
        if (listCargos == null) {
            if (other.listCargos != null)
                return false;
        } else if (!listCargos.equals(other.listCargos))
            return false;
        return true;
    }
}
