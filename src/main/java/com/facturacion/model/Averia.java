package com.facturacion.model;

import java.time.LocalDate;
import java.util.List;

import com.almacen.model.Pieza;
import com.empleados.model.Empleado;
import com.utilities.Cargo;
import com.vehiculos.model.Vehiculo;

public class Averia extends Cargo {

    // ATRIBUTOS

    private Integer id;
    private Empleado empleado;
    private Vehiculo vehiculo;
    private Cliente cliente;
    private Estado_Averia estado;
    private Tipo_Averia tipo;
    private Float precio = 0.0f;
    private LocalDate entrada;
    private LocalDate salida;
    private String descripcion;
    private String solucion;
    private String observaciones;
    private List<Pieza> listaPiezas;

    // CONSTRUCTORES

    public Averia(){}

    public Averia (Integer id, String descripcion, Empleado empleado, Vehiculo vehiculo, Cliente cliente, Estado_Averia estado, Tipo_Averia tipo, LocalDate entrada){
        this.id = id;
        this.precio = 0.0f;
        this.descripcion = descripcion;
        this.entrada = entrada;
        this.salida = null;
        this.solucion = "";
        this.observaciones = "";
        this.empleado = empleado;
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.estado = estado;
        this.tipo = tipo;
        this.listaPiezas = null;
    }

    public Averia (Integer id, Float precio, String descripcion, LocalDate entrada, LocalDate salida, String solucion, String observaciones, Empleado empleado, Vehiculo vehiculo, Cliente cliente, Estado_Averia estado, Tipo_Averia tipo, List<Pieza> listaPiezas){
        this.id = id;
        this.precio = precio;
        this.descripcion = descripcion;
        this.entrada = entrada;
        this.salida = salida;
        this.solucion = solucion;
        this.observaciones = observaciones;
        this.empleado = empleado;
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.estado = estado;
        this.tipo = tipo;
        this.listaPiezas = listaPiezas;
    }

    // GETTERS Y SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Estado_Averia getEstado() {
        return estado;
    }

    public void setEstado(Estado_Averia estado) {
        this.estado = estado;
    }

    public Tipo_Averia getTipo() {
        return tipo;
    }

    public void setTipo(Tipo_Averia tipo) {
        this.tipo = tipo;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public LocalDate getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDate entrada) {
        this.entrada = entrada;
    }

    public LocalDate getSalida() {
        return salida;
    }

    public void setSalida(LocalDate salida) {
        this.salida = salida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Pieza> getListaPiezas(){
        return listaPiezas;
    }

    public void setListaPiezas(List<Pieza> listaPiezas){
        this.listaPiezas = listaPiezas;
    }

    // TOSTRING, HASHCODE Y EQUALS

    @Override
    public String toString() {
        return "Averia [id=" + id + ", empleado=" + empleado + ", vehiculo=" + vehiculo + ", cliente=" + cliente
                + ", estado=" + estado + ", tipo=" + tipo + ", precio=" + precio + ", entrada=" + entrada + ", salida="
                + salida + ", descripcion=" + descripcion + ", solucion=" + solucion + ", observaciones="
                + observaciones + ", listaPiezas=" + listaPiezas + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((empleado == null) ? 0 : empleado.hashCode());
        result = prime * result + ((vehiculo == null) ? 0 : vehiculo.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        result = prime * result + ((precio == null) ? 0 : precio.hashCode());
        result = prime * result + ((entrada == null) ? 0 : entrada.hashCode());
        result = prime * result + ((salida == null) ? 0 : salida.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((solucion == null) ? 0 : solucion.hashCode());
        result = prime * result + ((observaciones == null) ? 0 : observaciones.hashCode());
        result = prime * result + ((listaPiezas == null) ? 0 : listaPiezas.hashCode());
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
        Averia other = (Averia) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (empleado == null) {
            if (other.empleado != null)
                return false;
        } else if (!empleado.equals(other.empleado))
            return false;
        if (vehiculo == null) {
            if (other.vehiculo != null)
                return false;
        } else if (!vehiculo.equals(other.vehiculo))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        if (estado == null) {
            if (other.estado != null)
                return false;
        } else if (!estado.equals(other.estado))
            return false;
        if (tipo == null) {
            if (other.tipo != null)
                return false;
        } else if (!tipo.equals(other.tipo))
            return false;
        if (precio == null) {
            if (other.precio != null)
                return false;
        } else if (!precio.equals(other.precio))
            return false;
        if (entrada == null) {
            if (other.entrada != null)
                return false;
        } else if (!entrada.equals(other.entrada))
            return false;
        if (salida == null) {
            if (other.salida != null)
                return false;
        } else if (!salida.equals(other.salida))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (solucion == null) {
            if (other.solucion != null)
                return false;
        } else if (!solucion.equals(other.solucion))
            return false;
        if (observaciones == null) {
            if (other.observaciones != null)
                return false;
        } else if (!observaciones.equals(other.observaciones))
            return false;
        if (listaPiezas == null) {
            if (other.listaPiezas != null)
                return false;
        } else if (!listaPiezas.equals(other.listaPiezas))
            return false;
        return true;
    }
}
