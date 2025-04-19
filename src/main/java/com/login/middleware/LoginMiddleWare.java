package com.login.middleware;

//import java.io.IOException;
//import java.sql.Connection;

import com.App;
import com.login.controller.DAO_Empleado;
import com.login.model.Empleado;
import com.main_menu.middleware.MainMiddleWare;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginMiddleWare {

    // OBJETO APP
    // Entre otras cosas, contiene la conexion a BD

    App app;

    // OBJETOS ALMACENAR DATOS ENTRADA

    Empleado auxUser = new Empleado();
    String auxUsername = "";
    String auxPassword = "";

    // DAOs

    public DAO_Empleado dao = null;

    // CONSTRUCTOR

    public LoginMiddleWare(App app){
        this.app = app;
        this.dao = new DAO_Empleado(this.app.getConnection());
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Enter;

    @FXML
    private TextField Input_Password;

    @FXML
    private TextField Input_Username;

    // EVENTOS

    @FXML
    void Buton_Enter_OnMouseClick(MouseEvent event) {
        Func_User_Login();
    }

    // METODOS

    private void Func_User_Login(){
        // recopilar datos de los inputs
        auxUsername = Input_Username.getText();
        auxPassword = Input_Password.getText();

        // buscar usuario a partir de nombre
        auxUser = dao.searchByNombre(auxUsername);

        // mostrar si 'auxUsername' y 'auxPassword' coinciden
        if(auxUser == null){
            System.out.println("Usuario " + auxUsername + " no existe o no se encuentra");
        } else if (auxUser.getPassword().equals(auxPassword)){
            String text = auxUser.getNombre() + " " + auxUser.getApellidos();
            System.out.println("Contraseña correcta. Bienvenid@ de nuevo, " + text);
            app.setUser(auxUser);
            app.changeStage("main_menu", "main", "LOGIN", 720, 540, true, MainMiddleWare.class);
        } else {
            System.out.println("Contraseña incorrecta. Repita la contraseña o cambie de usuario");
        }
    }

}