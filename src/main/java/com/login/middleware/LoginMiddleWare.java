package com.login.middleware;

//import java.io.IOException;
//import java.sql.Connection;

import com.App;
import com.login.controller.DAO_Usuario;
import com.login.model.Usuario;
import com.main_menu.middleware.MainMiddleWare;
import com.utilities.MiddleWare;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginMiddleWare{

    // OBJETO APP
    // Entre otras cosas, contiene la conexion a BD

    App app;

    // OBJETOS ALMACENAR DATOS ENTRADA

    Usuario auxUser = new Usuario();
    String auxUsername = "";
    String auxPassword = "";

    // DAOs

    public DAO_Usuario dao = null;

    // CONSTRUCTOR

    public LoginMiddleWare(App app){
        this.app = app;
        this.dao = new DAO_Usuario(this.app.getConnection());
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
        } else if (auxUser.getContrasenya().equals(auxPassword)){
            System.out.println("Contraseña correcta. Bienvenid@ de nuevo, " + auxUsername);
            app.changeStage("main_menu", "main", "LOGIN", 720, 540, true, MainMiddleWare.class);
        } else {
            System.out.println("Contraseña incorrecta. Repita la contraseña o cambie de usuario");
        }
    }

}