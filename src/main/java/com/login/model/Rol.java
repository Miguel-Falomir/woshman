package com.login.model;

import java.util.List;

public class Rol {

    // ATRIBUTOS

    private Integer id;
    private String nombre;
    private String descripcion;
    private List<Permiso> listaPermisos;

    // CONSTRUCTORES

    public Rol(){}

    public Rol(Integer id, String nombre, String descripcion){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    public Rol(Integer id, String nombre, String descripcion, List<Permiso> listaPermisos){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.listaPermisos = listaPermisos;
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

    public List<Permiso> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<Permiso> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    // TOSTRING, HASHCODE Y EQUALS

    @Override
    public String toString() {
        return "Rol [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", listaPermisos="
                + listaPermisos + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((listaPermisos == null) ? 0 : listaPermisos.hashCode());
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
        Rol other = (Rol) obj;
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
        if (listaPermisos == null) {
            if (other.listaPermisos != null)
                return false;
        } else if (!listaPermisos.equals(other.listaPermisos))
            return false;
        return true;
    }

}
