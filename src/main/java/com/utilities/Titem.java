package com.utilities;

public class Titem {

    // LISTA TIPOS

    public static enum Tipo {
        MARCA,
        MODELO
    }

    // ATRIBUTOS

    private Integer id;
    private String nombre;
    private Tipo tipo;

    // CONSTRUCTOR

    public Titem(String nombre){
        this.nombre = nombre;
    }

    public Titem(Integer id, String nombre, Tipo tipo){
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
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

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    // METODO 'toString'

    @Override
    public String toString() {
        return this.nombre;
    }

}
