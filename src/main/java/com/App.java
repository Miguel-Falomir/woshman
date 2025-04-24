package com;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.Namespace;

import com.login.middleware.LoginMiddleWare;
import com.login.model.Empleado;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DB_Connector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
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

    // METODO CAMBIAR DE VENTANA

    public void changeStage(String newModule, String newStage, String title, int width, int heigth, Boolean resize, Class<?> MidWare){
        // preparar archivo .fxml
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource(newModule + "/gui/" + newStage + ".fxml")
        );

        // para imprimir ventana nueva:
        try {
            // cerrar ventana actual
            mainStage.close();

            // elegir controlador para interfaz
            if (MidWare.equals(LoginMiddleWare.class)){
                loader.setControllerFactory(lambda -> {
                    return new LoginMiddleWare(this);
                });
            } else if (MidWare.equals(MainMiddleWare.class)){
                loader.setControllerFactory(lambda -> {
                    return new MainMiddleWare(this);
                });
            }

            // cargar escena nueva en objeto 'Parent'
            Parent root = loader.load();

            // los objetos con anotacion @FXML se inicializan al llegar a loader.load()
            // por eso es aqui donde se les empieza a aplicar modificaciones especificas para cada ventana
            
            // para 'MainMiddleWare', activar botones modulos segun rol del usuario
            if (MidWare.equals(MainMiddleWare.class)){
                // variables internas
                Integer rol = this.getUser().getId();
                List<String> mitemsList = new ArrayList<String>();
                MenuItem mitem = null;

                // guardar nombres de elementos correspondientes al rol actual
                switch (rol) {
                    case 1: // mecanico
                        mitemsList.add("Mitem_Lista_Averias");
                        mitemsList.add("Mitem_Realizar_Venta");
                        mitemsList.add("Mitem_Lista_Piezas");
                        mitemsList.add("Mitem_Lista_Vehiculos");
                        // como no tiene acceso a ninguna funcionalidad,
                        // se le deshabilita todo el modulo "Empleados"
                        Menu Empleados = (Menu) loader.getNamespace().get("Menu_Empleados");
                        Empleados.setDisable(true);
                        break;
                    case 2: // encargado
                        mitemsList.add("Mitem_Lista_Averias");
                        mitemsList.add("Mitem_Lista_Ventas");
                        mitemsList.add("Mitem_Lista_Facturas");
                        mitemsList.add("Mitem_Lista_Clientes");
                        mitemsList.add("Mitem_Realizar_Venta");
                        mitemsList.add("Mitem_Lista_Piezas");
                        mitemsList.add("Mitem_Lista_Proveedores");
                        mitemsList.add("Mitem_Lista_Encargos");
                        mitemsList.add("Mitem_Lista_Albaranes");
                        mitemsList.add("Mitem_Lista_Vehiculos");
                        mitemsList.add("Mitem_Lista_Modelos");
                        mitemsList.add("Mitem_Lista_Marcas");
                        mitemsList.add("Mitem_Lista_Empleados");
                        break;
                    default: // admimistrador
                        mitemsList.add("Mitem_Lista_Averias");
                        mitemsList.add("Mitem_Tipo_Averia");
                        mitemsList.add("Mitem_Estado_Averia");
                        mitemsList.add("Mitem_Lista_Ventas");
                        mitemsList.add("Mitem_Lista_Facturas");
                        mitemsList.add("Mitem_Realizar_Venta");
                        mitemsList.add("Mitem_Lista_Clientes");
                        mitemsList.add("Mitem_Lista_Piezas");
                        mitemsList.add("Mitem_Tipo_Pieza");
                        mitemsList.add("Mitem_Lista_Proveedores");
                        mitemsList.add("Mitem_Lista_Encargos");
                        mitemsList.add("Mitem_Lista_Albaranes");
                        mitemsList.add("Mitem_Lista_Vehiculos");
                        mitemsList.add("Mitem_Lista_Modelos");
                        mitemsList.add("Mitem_Lista_Marcas");
                        mitemsList.add("Mitem_Lista_Categorias");
                        mitemsList.add("Mitem_Lista_Empleados");
                        mitemsList.add("Mitem_Lista_Roles");
                        mitemsList.add("Mitem_Lista_Permisos");
                        break;
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

    // METODO CAMBIAR ESCENA

    public void changeScene(HBox Central_Box, String newModule, String newStage, String title, Class<?> MidWare){
        // englobar todo el proceso en un try-catch
        // de esta forma, si surge un fallo al generar la nueva pantalla,
        // aborta tambien el proceso de limpiar la pantalla vieja
        try {
            // limpiar contenedor
            Central_Box.getChildren().clear();
    
            // preparar archivo .fxml
            FXMLLoader loader = new FXMLLoader(
                App.class.getResource(newModule + "/gui/" + newStage + ".fxml")
            );

            // cargar archivo .fxml
            Parent root = loader.load();

            // agregar archivo al panel central
            Central_Box.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // INICIALIZAR RECURSOS

    @Override
    public void start(Stage primaryStage) throws Exception {
        // declarar ventana login
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("login/gui/login.fxml")
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
