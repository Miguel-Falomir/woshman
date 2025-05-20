package com.facturacion.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.almacen.model.Pieza;
import com.almacen.model.Proveedor;
import com.almacen.model.Tipo_Pieza;
import com.empleados.model.Empleado;
import com.empleados.model.Permiso;
import com.empleados.model.Rol;
import com.facturacion.model.Averia;
import com.facturacion.model.Cliente;
import com.facturacion.model.Estado_Averia;
import com.facturacion.model.Tipo_Averia;
import com.utilities.DAO;
import com.utilities.DAO_Interface;
import com.vehiculos.model.Categoria;
import com.vehiculos.model.Marca;
import com.vehiculos.model.Modelo;
import com.vehiculos.model.Vehiculo;

public class DAO_Averia extends DAO implements DAO_Interface<Averia, Integer> {

    // CONEXION

    private Connection connect;

    // CONSTRUCTOR

    public DAO_Averia(Connection connect){
        this.connect = connect;
    }

    // METODOS CRUD

    @Override
    public boolean insert(Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Averia obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Averia search(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Averia respuesta = null;

        // variables auxiliares
        Empleado auxEmpleado = null;
        Vehiculo auxVehiculo = null;
        Cliente auxCliente = null;
        Estado_Averia auxEstado = null;
        Tipo_Averia auxTipo = null;
        List<Pieza> auxListaPiezas = new ArrayList<Pieza>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos de todas las averias
            statement = connect.prepareStatement("SELECT ave.id_averia, ave.precio_averia, ave.descripcion, ave.fecha_entrada, ave.fecha_salida, ave.solucion, ave.observaciones, ave.fk_empleado, ave.fk_vehiculo, ave.fk_cliente, ave.fk_estado_averia, ave.fk_tipo_averia FROM averia ave WHERE ave.id_averia = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar claves ajenas (y clave primaria)
            int idAveria = resultado.getInt(1);
            int fkEmpleado = resultado.getInt(8);
            int fkVehiculo = resultado.getInt(9);
            int fkCliente = resultado.getInt(10);
            int fkEstado = resultado.getInt(11);
            int fkTipo = resultado.getInt(12);

            // recopilar empleado
            auxEmpleado = searchEmpleado(fkEmpleado);

            // recopilar vehiculo
            auxVehiculo = searchVehiculo(fkVehiculo);

            // recopilar cliente
            auxCliente = searchCliente(fkCliente);

            // recopilar estado
            auxEstado = searchEstado(fkEstado);

            // recopilar tipo
            auxTipo = searchTipo(fkTipo);

            // recopilar lista piezas
            // (como la averia es null, se le pasa la clave primaria guardada al principio)
            auxListaPiezas = searchPiezasByAveria(idAveria);

            // guardar datos averia en 'respuesta'
            respuesta = new Averia(
                resultado.getInt(1),
                resultado.getFloat(2),
                resultado.getString(3),
                resultado.getDate(4),
                resultado.getDate(5),
                resultado.getString(6),
                resultado.getString(7),
                auxEmpleado,
                auxVehiculo,
                auxCliente,
                auxEstado,
                auxTipo,
                auxListaPiezas
            );

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
        
        // devolver respuesta
        return respuesta;
    }

    @Override
    public List<Averia> searchAll() {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Averia> respuesta = new ArrayList<Averia>();

        // variables auxiliares
        Averia auxAveria = null;
        Empleado auxEmpleado = null;
        Vehiculo auxVehiculo = null;
        Cliente auxCliente = null;
        Estado_Averia auxEstado = null;
        Tipo_Averia auxTipo = null;
        List<Pieza> auxListaPiezas = new ArrayList<Pieza>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos de todas las averias
            statement = connect.prepareStatement("SELECT ave.id_averia, ave.precio_averia, ave.descripcion, ave.fecha_entrada, ave.fecha_salida, ave.solucion, ave.observaciones, ave.fk_empleado, ave.fk_vehiculo, ave.fk_cliente, ave.fk_estado_averia, ave.fk_tipo_averia FROM averia ave;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultado a lista 'respuesta'
            while (resultado.next()){
                // guardar claves ajenas (y clave primaria)
                int idAveria = resultado.getInt(1);
                int fkEmpleado = resultado.getInt(8);
                int fkVehiculo = resultado.getInt(9);
                int fkCliente = resultado.getInt(10);
                int fkEstado = resultado.getInt(11);
                int fkTipo = resultado.getInt(12);

                // recopilar empleado
                auxEmpleado = searchEmpleado(fkEmpleado);

                // recopilar vehiculo
                auxVehiculo = searchVehiculo(fkVehiculo);

                // recopilar cliente
                auxCliente = searchCliente(fkCliente);

                // recopilar estado
                auxEstado = searchEstado(fkEstado);

                // recopilar tipo
                auxTipo = searchTipo(fkTipo);

                // recopilar lista piezas
                // (como la averia es null, se le pasa la clave primaria guardada al principio)
                auxListaPiezas = searchPiezasByAveria(idAveria);

                // guardar datos averia en 'auxAveria'
                auxAveria = new Averia(
                    resultado.getInt(1),
                    resultado.getFloat(2),
                    resultado.getString(3),
                    resultado.getDate(4),
                    resultado.getDate(5),
                    resultado.getString(6),
                    resultado.getString(7),
                    auxEmpleado,
                    auxVehiculo,
                    auxCliente,
                    auxEstado,
                    auxTipo,
                    auxListaPiezas
                );

                // agregar 'auxAveria' a lista 'respuesta'
                respuesta.add(auxAveria);
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
        
        // devolver respuesta
        return respuesta;
    }

    public List<Averia> searchByUser(Integer empleado){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Averia> respuesta = new ArrayList<Averia>();

        // variables auxiliares
        Averia auxAveria = null;
        Empleado auxEmpleado = null;
        Vehiculo auxVehiculo = null;
        Cliente auxCliente = null;
        Estado_Averia auxEstado = null;
        Tipo_Averia auxTipo = null;
        List<Pieza> auxListaPiezas = new ArrayList<Pieza>();

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos de todas las averias
            statement = connect.prepareStatement("SELECT ave.id_averia, ave.precio_averia, ave.descripcion, ave.fecha_entrada, ave.fecha_salida, ave.solucion, ave.observaciones, ave.fk_empleado, ave.fk_vehiculo, ave.fk_cliente, ave.fk_estado_averia, ave.fk_tipo_averia FROM averia ave WHERE ave.fk_empleado = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, empleado);
            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultado a lista 'respuesta'
            while (resultado.next()){
                // guardar claves ajenas (y clave primaria)
                int idAveria = resultado.getInt(1);
                int fkEmpleado = resultado.getInt(8);
                int fkVehiculo = resultado.getInt(9);
                int fkCliente = resultado.getInt(10);
                int fkEstado = resultado.getInt(11);
                int fkTipo = resultado.getInt(12);

                // recopilar empleado
                auxEmpleado = searchEmpleado(fkEmpleado);

                // recopilar vehiculo
                auxVehiculo = searchVehiculo(fkVehiculo);

                // recopilar cliente
                auxCliente = searchCliente(fkCliente);

                // recopilar estado
                auxEstado = searchEstado(fkEstado);

                // recopilar tipo
                auxTipo = searchTipo(fkTipo);

                // recopilar lista piezas
                // (como la averia es null, se le pasa la clave primaria guardada al principio)
                auxListaPiezas = searchPiezasByAveria(idAveria);

                // guardar datos averia en 'auxAveria'
                auxAveria = new Averia(
                    resultado.getInt(1),
                    resultado.getFloat(2),
                    resultado.getString(3),
                    resultado.getDate(4),
                    resultado.getDate(5),
                    resultado.getString(6),
                    resultado.getString(7),
                    auxEmpleado,
                    auxVehiculo,
                    auxCliente,
                    auxEstado,
                    auxTipo,
                    auxListaPiezas
                );

                // agregar 'auxAveria' a lista 'respuesta'
                respuesta.add(auxAveria);
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
        
        // devolver respuesta
        return respuesta;
    }

    // METODOS CRUD DE OTROS DAOs
    // (se copian en DAO_Averia para no tener que importar nada de fuera del modulo)

    private Empleado searchEmpleado(Integer id) {
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

    private Vehiculo searchVehiculo(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Vehiculo respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos de vehiculo, modelo, categoria y marca relativos a vehiculo veh.id_vehiculo = id
            statement = connect.prepareStatement("SELECT veh.id_vehiculo, veh.matricula, mo.id_modelo, mo.nombre, cat.id_categoria, cat.nombre, cat.descripcion, ma.id_marca, ma.nombre FROM vehiculo veh JOIN modelo mo ON mo.id_modelo = veh.fk_modelo JOIN categoria cat ON mo.fk_categoria = cat.id_categoria JOIN marca ma ON ma.id_marca = mo.fk_marca WHERE veh.id_vehiculo = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado en objeto 'respuesta'
            Marca marca = new Marca(
                resultado.getInt(8),
                resultado.getString(9)
            );
            Categoria categoria = new Categoria(
                resultado.getInt(5),
                resultado.getString(6),
                (resultado.getString(7) == null) ? "": resultado.getString(7) // Operador Ternario -> (condicion) ? [valor_verdadero] : [valor_falso]
            );
            Modelo modelo = new Modelo(
                resultado.getInt(3),
                resultado.getString(4),
                marca,
                categoria
            );
            respuesta = new Vehiculo(
                resultado.getInt(1),
                resultado.getString(2),
                modelo
            );

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
        
        // devolver respuesta
        return respuesta;
    }

    private Cliente searchCliente(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Cliente respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar todos los datos del cliente cli.id_cliente = id
            statement = connect.prepareStatement("SELECT cli.id_cliente, cli.dni, cli.nombre, cli.apellidos, cli.email, direccion FROM cliente cli WHERE cli.id_cliente = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // forzar que 'resultado' apunte a primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar resultado en objeto 'respuesta'
            respuesta = new Cliente(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3),
                resultado.getString(4),
                resultado.getString(5),
                resultado.getString(6)
            );

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
        
        // devolver respuesta
        return respuesta;
    }

    private Estado_Averia searchEstado(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Estado_Averia respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos estado averia
            statement = connect.prepareStatement("SELECT est.id_estado_averia, est.nombre, est.descripcion FROM estado_averia est WHERE est.id_estado_averia = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar datos en objeto 'respuesta'
            respuesta = new Estado_Averia(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3)
            );

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

        // devolver respuesta
        return respuesta;
    }

    private Tipo_Averia searchTipo(Integer id) {
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Tipo_Averia respuesta = null;

        // (intentar) ejecutar busqueda
        try {
            // consulta 1: buscar datos estado averia
            statement = connect.prepareStatement("SELECT tav.id_tipo_averia, tav.nombre, tav.descripcion FROM tipo_averia tav WHERE tav.id_tipo_averia = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);

            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte a de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }
            resultado.next();

            // guardar datos en objeto 'respuesta'
            respuesta = new Tipo_Averia(
                resultado.getInt(1),
                resultado.getString(2),
                resultado.getString(3)
            );

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

        // devolver respuesta
        return respuesta;
    }

    private List<Pieza> searchPiezasByAveria(Integer averia){
        // variables internas
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<Pieza> respuesta = new ArrayList<Pieza>();

        // (intentar) ejecutar busqueda
        try{
            // consulta 1: buscar todas las piezas, tambien el tipo y proveedor de cada una, relacionadas con 'averia.id'
            statement = connect.prepareStatement("SELECT pi.id_pieza, pi.nombre, pi.descripcion, pi.precio, pi.cantidad, pr.id_proveedor, pr.cif, pr.nombre, pr.email, pr.direccion, tp.id_tipo_pieza, tp.nombre, tp.descripcion FROM pieza pi JOIN tipo_pieza tp ON pi.fk_tipo_pieza = tp.id_tipo_pieza JOIN proveedor pr ON pi.fk_proveedor = pr.id_proveedor JOIN pieza_has_averia has ON pi.id_pieza = has.pieza JOIN averia ave ON has.averia = ave.id_averia WHERE ave.id_averia = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, averia);
            
            // ejecutar consulta
            resultado = statement.executeQuery();

            // asegurar que 'resultado' apunte antes de la primera fila
            if (!resultado.isBeforeFirst()){
                resultado.beforeFirst();
            }

            // agregar todos los resultados a lista 'respuesta'
            while (resultado.next()) {
                Tipo_Pieza tipo = new Tipo_Pieza(
                    resultado.getInt(11),
                    resultado.getString(12),
                    resultado.getString(13)
                );
                Proveedor proveedor = new Proveedor(
                    resultado.getInt(6),
                    resultado.getString(7),
                    resultado.getString(8),
                    resultado.getString(9),
                    resultado.getString(10)
                );
                respuesta.add( new Pieza(
                    resultado.getInt(1),
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getFloat(4),
                    resultado.getInt(5),
                    tipo,
                    proveedor
                ));
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
        
        // devolver respuesta
        return respuesta;
    }
}
