package com;

import java.io.IOException;
import java.sql.Connection;

import com.login.middleware.LoginMiddleWare;
import com.login.model.Empleado;
import com.main_menu.middleware.MainMiddleWare;
import com.utilities.DB_Connector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void setUser(Empleado user){
        App.user = user;
    }

    // METODO CAMBIAR DE VENTANA

    public void changeStage(String newModule, String newStage, String title, int width, int heigth, Boolean resize, Class<?> MidWare){
        // preparar archivo .fxml
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource(newModule + "/gui/" + newStage + ".fxml")
        );

        // comparar variable 'MidWare' con clases paquete 'MiddleWare'
        // al encontrar coincidencia, construir instancia
        if (MidWare.equals(LoginMiddleWare.class)){
            loader.setControllerFactory(lambda -> {
                return new LoginMiddleWare(this);
            });
        } else if (MidWare.equals(MainMiddleWare.class)){
            loader.setControllerFactory(lambda -> {
                return new MainMiddleWare(this);
            });
        }

        // para imprimir ventana nueva:
        try {
            // cerrar ventana actual
            mainStage.close();

            // cargar escena nueva en objeto 'Parent'
            Parent root = loader.load();

            // definir ventana
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
        mainStage.setTitle("STYLESHEET_CASPIAN");
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
