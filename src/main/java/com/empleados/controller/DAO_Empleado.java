package com.empleados.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.empleados.model.Empleado;
import com.empleados.model.Permiso;
import com.empleados.model.Rol;
import com.utilities.DAO;
import com.utilities.DAO_Interface;

public class DAO_Empleado extends DAO implements DAO_Interface<Empleado, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Empleado(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Empleado obj) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Rol auxRol = null;
        List<Permiso> auxPermisos = new ArrayList<Permiso>();
        int update = 0;
        int nuevoID = 0;
        boolean success = false;

        // (intentar) ejecutar insercion
        try {
            // consulta 1: comprobar que obj.password y obj.username no se repitan
            statement = connect.prepareStatement("SELECT count(*) FROM empleado e WHERE e.password = ? OR e.username = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, obj.getPassword());
            statement.setString(2, obj.getUsername());

            // ejecutar consulta
            resultado = statement.executeQuery();
            
            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // materializar 'resultado' en booleano
            boolean userUnique = (resultado.getInt(1) == 0);

            if (userUnique){
                // consulta 2: buscar cantidad usuarios
                statement = connect.prepareStatement("SELECT count(*) FROM empleado;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                // ejecutar consulta
                resultado = statement.executeQuery();
                
                // forzar que 'resultado' apunte a la primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();
                
                // asignar resultado al ID del nuevo usuario
                nuevoID = resultado.getInt(1);
                obj.setId(nuevoID);
                
                // consulta 3: buscar rol 1 (mecanico) por defecto //////
                statement = connect.prepareStatement("SELECT * FROM rol r WHERE r.id_rol = 1;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                // ejecutar consulta
                resultado = statement.executeQuery();
                
                // forzar que 'resultado' apunte a la primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                resultado.next();
                
                // guardar primera fila en variable auxiliar
                auxRol = new Rol(
                    resultado.getInt(1),
                    resultado.getString(2),
                    resultado.getString(3)
                );
                
                // consulta 4: Buscar permisos del rol //////////////////
                statement = connect.prepareStatement("SELECT p.* FROM permiso p JOIN rol_has_permiso h ON p.id_permiso = h.permiso JOIN rol r ON h.rol = r.id_rol WHERE r.id_rol = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setLong(1, auxRol.getId());
                
                // ejecutar consulta
                resultado = statement.executeQuery();
                
                // asegurar que 'resultado' apunte antes de la primera fila
                if (!resultado.isBeforeFirst()){
                    resultado.beforeFirst();
                }
                
                // agregar todas las respuestas a lista 'auxPermisos'
                while(resultado.next()){
                    auxPermisos.add( new Permiso(
                        resultado.getInt(1),
                        resultado.getString(2)
                        ));
                    }
                    
                // asignar permisos al rol, y rol al usuario
                auxRol.setListaPermisos(auxPermisos);
                obj.setRol(auxRol);
                
                // consulta 5: insertar usuario nuevo ///////////////////
                statement = connect.prepareStatement("INSERT INTO empleado(id_empleado, fk_rol, nombre, apellidos, username, password) VALUES (?, ?, ?, ?, ?, ?);");
                statement.setLong(1, obj.getId());
                statement.setLong(2, obj.getRol().getId());
                statement.setString(3, obj.getNombre());
                statement.setString(4, obj.getApellidos());
                statement.setString(5, obj.getUsername());
                statement.setString(6, obj.getPassword());
                
                // ejecutar consulta
                update = statement.executeUpdate();
                System.out.println("INSERTAR EMPLEADO: " + update);
                success = true;
            }

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver 'exito', para indicar si se ha completado insercion
        return success;
    }

    @Override
    public boolean update(Empleado obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Empleado obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Empleado search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Empleado respuesta = null;
        Rol auxRol = null;
        List<Permiso> auxPermisos = new ArrayList<Permiso>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar usuario ///////////////////////////
            statement = connect.prepareStatement("SELECT * FROM empleado e WHERE e.id_empleado = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setLong(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar primera fila en 'respuesta'
            respuesta = new Empleado(
                resultado.getInt(1),
                resultado.getString(3),
                resultado.getString(4),
                resultado.getString(5),
                resultado.getString(6)
            );

            // consulta 2: Buscar rol del usuario ///////////////////
            statement = connect.prepareStatement("SELECT * FROM rol r WHERE r.id_rol = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setLong(1, resultado.getInt(2));

            // ejecutar consulta
            resultado = statement.executeQuery();
            
            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar primera fila en variable auxiliar
            auxRol = new Rol(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3)
            );

            // consulta 3: Buscar permisos del rol //////////////////
            statement = connect.prepareStatement("SELECT p.* FROM permiso p JOIN rol_has_permiso h ON p.id_permiso = h.permiso JOIN rol r ON h.rol = r.id_rol WHERE r.id_rol = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setLong(1, auxRol.getId());

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxPermisos'
            while(resultado.next()){
                auxPermisos.add( new Permiso(
                    resultado.getInt(1),
                    resultado.getString(2)
                ));
            }

            // asignar permisos al rol, y rol al usuario
            auxRol.setListaPermisos(auxPermisos);
            respuesta.setRol(auxRol);

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver resultado
        return respuesta;
    }

    @Override
    public List<Empleado> searchAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
    }

    public Empleado searchByNombre(String nombre){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Empleado respuesta = null;
        Rol auxRol = null;
        List<Permiso> auxPermisos = new ArrayList<Permiso>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: Buscar usuario ///////////////////////////
            statement = connect.prepareStatement("SELECT * FROM empleado e WHERE e.username = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, nombre);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar primera fila en 'resultado'
            respuesta = new Empleado(
                resultado.getInt(1),
                resultado.getString(3),
                resultado.getString(4),
                resultado.getString(5),
                resultado.getString(6)
            );

            // consulta 2: Buscar rol ///////////////////////////////
            statement = connect.prepareStatement("SELECT * FROM rol r WHERE r.id_rol = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setLong(1, resultado.getInt(2));
            
            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();
            
            // guardar primera fila en variable auxiliar
            auxRol = new Rol(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3)
            );

            // consulta 3: Buscar permisos del rol //////////////////
            statement = connect.prepareStatement("SELECT p.* FROM permiso p JOIN rol_has_permiso h ON p.id_permiso = h.permiso JOIN rol r ON h.rol = r.id_rol WHERE r.id_rol = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setLong(1, auxRol.getId());
        
            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todas las respuestas a lista 'auxPermisos'
            while(resultado.next()){
                auxPermisos.add( new Permiso(
                    resultado.getInt(1),
                    resultado.getString(2)
                ));
            }

            // asignar permisos al rol, y rol al usuario
            auxRol.setListaPermisos(auxPermisos);
            respuesta.setRol(auxRol);

        // manejar excepciones
        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        
        // pase lo que pase, cerrar 'statement'
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        // devolver resultado
        return respuesta;
    }

}
