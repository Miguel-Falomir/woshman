package com.almacen.model;

public class Pieza {

    // ATRIBUTOS

    Integer id;
    String nombre;
    String descripcion;
    Float precio;
    Integer cantidad;
    Tipo_Pieza tipo;
    Proveedor proveedor;

    // CONSTRUCTORES

    public Pieza(){}

    public Pieza(Integer id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Pieza(Integer id, String nombre, String descripcion, Float precio, Integer cantidad){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Pieza(Integer id, String nombre, String descripcion, Float precio, Integer cantidad, Tipo_Pieza tipo, Proveedor proveedor){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.proveedor = proveedor;
    }

    // GETTERS Y SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Tipo_Pieza getTipo() {
        return tipo;
    }

    public void setTipo(Tipo_Pieza tipo) {
        this.tipo = tipo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    // TOSTRING, HASHCODE Y EQUALS

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((precio == null) ? 0 : precio.hashCode());
        result = prime * result + ((cantidad == null) ? 0 : cantidad.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        result = prime * result + ((proveedor == null) ? 0 : proveedor.hashCode());
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
        Pieza other = (Pieza) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (precio == null) {
            if (other.precio != null)
                return false;
        } else if (!precio.equals(other.precio))
            return false;
        if (cantidad == null) {
            if (other.cantidad != null)
                return false;
        } else if (!cantidad.equals(other.cantidad))
            return false;
        if (tipo == null) {
            if (other.tipo != null)
                return false;
        } else if (!tipo.equals(other.tipo))
            return false;
        if (proveedor == null) {
            if (other.proveedor != null)
                return false;
        } else if (!proveedor.equals(other.proveedor))
            return false;
        return true;
    }

    

}
