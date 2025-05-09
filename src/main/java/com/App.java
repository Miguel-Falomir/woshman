package com;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.empleados.controller.DAO_Permiso;

import com.empleados.middleware.LoginMiddleWare;
import com.empleados.middleware.NuevoUsuarioMiddleWare;
import com.empleados.model.Empleado;
import com.empleados.model.Permiso;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DB_Connector;
import com.utilities.MiddleWare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class App extends Application {
    
    // VARIABLES ESTATICAS

    private static Scene mainScene;
    private static Stage mainStage;
    private static DB_Connector connector;
    private static Empleado user;

    // GETTERS Y SETTERS

    public Connection getConnection(){
        /* No intentar definir private static Connection conn = connector.getConnection()
         * El constructor de los MiddleWare invocaria este metodo antes de que se pudiese
         * inicializar 'conn', y recibiria la conexion como 'null'
         */
        return connector.getConnection();
    }

    public Empleado getUser(){
        return App.user;
    }

    public void setUser(Empleado user){
        App.user = user;
    }

    // METODO COMPROBAR SI USUARIO TIENE X PERMISO

    public static boolean checkPermiso(Integer id_perm){
        DAO_Permiso dao = new DAO_Permiso(connector.getConnection());
        Permiso perm = dao.search(id_perm);
        List<Permiso> listaPermisos = user.getRol().getListaPermisos();
        return listaPermisos.contains(perm);
    }

    // METODO CAMBIAR DE VENTANA

    public void changeStage(String newModule, String newStage, String title, int width, int heigth, Boolean resize, Class<? extends MiddleWare> midWareClass){
        
        // preparar archivo .fxml
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource(newModule + "/gui/" + newStage + ".fxml")
        );

        // para imprimir ventana nueva:
        try {
            // cerrar ventana actual
            mainStage.close();

            // elegir controlador 'MidWare' para interfaz
            // no se implementa switch-case porque Class<?> parece no admitirlo
            if (midWareClass.equals(LoginMiddleWare.class)){
                loader.setControllerFactory(lambda -> {
                    return new LoginMiddleWare(this);
                }); // metodo lambda => valor -> {[codigo para devolver el valor]}
            } else if (midWareClass.equals(MainMiddleWare.class)){
                loader.setControllerFactory(lambda -> {
                    return new MainMiddleWare(this);
                });
            } else if (midWareClass.equals(NuevoUsuarioMiddleWare.class)){
                loader.setControllerFactory(lambda -> {
                    return new NuevoUsuarioMiddleWare(this);
                });
            }

            // cargar escena nueva en objeto 'Parent'
            Parent root = loader.load();

            // los objetos con anotacion @FXML se inicializan al llegar a loader.load()
            // por eso es aqui donde se les empieza a aplicar modificaciones especificas para cada ventana
            
            // para 'MainMiddleWare', activar botones modulos segun rol del usuario
            if (midWareClass.equals(MainMiddleWare.class)){
                // variables internas
                List<String> mitemsList = new ArrayList<String>();
                MenuItem mitem = null;

                // guardar nombres de elementos correspondientes al rol actual
                boolean notFacturacion =  true;
                boolean notAlmacen = true;
                boolean notVehiculos = true;
                boolean notEmpleados = true;
                // Facturacion
                if (checkPermiso(0)) {mitemsList.add("Mitem_Lista_Averias"); notFacturacion = false;}
                if (checkPermiso(4)) {mitemsList.add("Mitem_Estado_Averia"); notFacturacion = false;}
                if (checkPermiso(8)) {mitemsList.add("Mitem_Tipo_Averia"); notFacturacion = false;}
                if (checkPermiso(12)) {mitemsList.add("Mitem_Lista_Ventas"); notFacturacion = false;}
                if (checkPermiso(13)) {mitemsList.add("Mitem_Realizar_Venta"); notFacturacion = false;}
                if (checkPermiso(16)) {mitemsList.add("Mitem_Lista_Facturas"); notFacturacion = false;}
                if (checkPermiso(75)) {mitemsList.add("Mitem_Lista_Clientes"); notFacturacion = false;}
                // Almacen
                if (checkPermiso(31)) {mitemsList.add("Mitem_Lista_Piezas"); notAlmacen = false;}
                if (checkPermiso(36)) {mitemsList.add("Mitem_Tipo_Pieza"); notAlmacen = false;}
                if (checkPermiso(40)) {mitemsList.add("Mitem_Lista_Proveedores"); notAlmacen = false;}
                if (checkPermiso(22)) {mitemsList.add("Mitem_Lista_Encargos"); notAlmacen = false;}
                if (checkPermiso(26)) {mitemsList.add("Mitem_Lista_Albaranes"); notAlmacen = false;}
                // Vehiculos
                if (checkPermiso(44)) {mitemsList.add("Mitem_Lista_Vehiculos"); notVehiculos = false;}
                if (checkPermiso(48)) {mitemsList.add("Mitem_Lista_Modelos"); notVehiculos = false;}
                if (checkPermiso(52)) {mitemsList.add("Mitem_Lista_Marcas"); notVehiculos = false;}
                if (checkPermiso(56)) {mitemsList.add("Mitem_Lista_Categorias"); notVehiculos = false;}
                // Empleados
                if (checkPermiso(60)) {mitemsList.add("Mitem_Lista_Empleados"); notEmpleados = false;}
                if (checkPermiso(66)) {mitemsList.add("Mitem_Lista_Roles"); notEmpleados = false;}
                if (checkPermiso(71)) {mitemsList.add("Mitem_Lista_Permisos"); notEmpleados = false;}

                // si no tiene acceso a ninguna funcionalidad, dehabilitar todo el modulo
                // Facturacion
                if (notFacturacion){
                    Menu Facturacion = (Menu) loader.getNamespace().get("Menu_Facturacion");
                    Facturacion.setDisable(true);
                }
                // Almacen
                if (notAlmacen){
                    Menu Almacen = (Menu) loader.getNamespace().get("Menu_Almacen");
                    Almacen.setDisable(true);
                }
                // Vehiculos
                if (notVehiculos){
                    Menu Vehiculos = (Menu) loader.getNamespace().get("Menu_Vehiculos");
                    Vehiculos.setDisable(true);
                }
                // Empleados
                if (notEmpleados){
                    Menu Empleados = (Menu) loader.getNamespace().get("Menu_Empleados");
                    Empleados.setDisable(true);
                }

                // activar elementos guardados
                for (String itemName : mitemsList) {
                    mitem = (MenuItem) loader.getNamespace().get(itemName);
                    mitem.setDisable(false);
                    mitem.setVisible(true);
                }
            }

            // definir dimensiones ventana
            mainScene.setRoot(root);
            mainStage.setScene(mainScene);
            mainStage.setTitle(title);
            mainStage.setWidth(width);
            mainStage.setHeight(heigth);
            mainStage.setResizable(resize);

            if (resize){
                mainStage.setMinWidth(width);
                mainStage.setMinHeight(heigth);
            }

            // mostrar ventana
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // INICIALIZAR RECURSOS

    @Override
    public void start(Stage primaryStage) throws Exception {
        // declarar ventana login
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("empleados/gui/form_login.fxml")
        );

        // declarar un constructor personalizado para crear el controlador del login.
        // este constructor permite referenciar la conexion a la base de datos,
        // para que el controlador pueda inicializar sus propios DAOs
        loader.setControllerFactory(lambda -> {
            return new LoginMiddleWare(this);
        });

        // cargar escena principal en objeto 'Parent'
        Parent root = loader.load();

        // inicializar 'Parent'
        mainScene = new Scene(root);
        mainStage = primaryStage;
        mainStage.setTitle("login");
        mainStage.setWidth(400);
        mainStage.setHeight(300);
        mainStage.setResizable(false);
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    // EJECUTAR HILO PRINCIPAL

    public static void main(String[] args) {
        // inicializar 'connector'
        connector = new DB_Connector(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/taller_db",
                "root",
                "mysql");

        // abrir conexion a la base de datos
        // a partir de aqui, la magia surge en el metodo 'start()'
        connector.StartConnection();
        System.out.println("Se abre la conexión");

        // ejecutar interfaz grafica
        // este bucle se repite indefinidamente hasta que el usuario pulse 'exit'
        launch();

        // cerrar conexion
        connector.CloseConnection();
        System.out.println("Se cierra la conexión porque el usuario ha decidido dejar de usar la aplicación.");
    }
}
