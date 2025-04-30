package com.empleados.model;

public class Empleado {

    // ATRIBUTOS

    private Integer id;
    private Rol rol;
    private String nombre;
    private String apellidos;
    private String username;
    private String password;
    
    // CONSTRUCTORES

    public Empleado(){}

    public Empleado(String nombre, String contrasenya) {
        this.username = nombre;
        this.password = contrasenya;
    }

    public Empleado(String nombre, String apellidos, String username, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
    }
    
    public Empleado(Integer id, String nombre, String apellidos, String username, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
    }

    public Empleado(Integer id, Rol rol, String nombre, String apellidos, String username, String password) {
        this.id = id;
        this.rol = rol;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
    }
    
    // GETTERS Y SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rol getRol(){
        return rol;
    }

    public void setRol(Rol rol){
        this.rol = rol;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getApellidos(){
        return apellidos;
    }

    public void setApellidos(String apellidos){
        this.apellidos = apellidos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String nombre) {
        this.username = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String contrasenya) {
        this.password = contrasenya;
    }

    // TOSTRING, HASHCODE Y EQUALS

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + username + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
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
        Empleado other = (Empleado) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }

}
