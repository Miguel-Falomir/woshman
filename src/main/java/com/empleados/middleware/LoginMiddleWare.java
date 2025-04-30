package com.empleados.middleware;

//import java.io.IOException;
//import java.sql.Connection;

import com.App;
import com.empleados.controller.DAO_Empleado;
import com.empleados.model.Empleado;
import com.menu.middleware.MainMiddleWare;

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
    private Button Buton_SignUp;

    @FXML
    private TextField Input_Password;

    @FXML
    private TextField Input_Username;

    // EVENTOS

    @FXML
    void OnMouseClicked_Buton_Enter(MouseEvent event) {
        Func_User_Login();
    }

    @FXML
    void OnMouseClicked_Buton_SignUp(MouseEvent event) {
        app.changeStage("empleados", "nuevo_usuario_form", "REGISTRO", 480, 360, false, NuevoUsuarioMiddleWare.class);
    }

    // METODOS

    private void Func_User_Login(){
        // variable auxiliar
        String text = "";
        // recopilar datos de los inputs
        auxUsername = Input_Username.getText();
        auxPassword = Input_Password.getText();

        // buscar usuario a partir de nombre
        auxUser = dao.searchByNombre(auxUsername);

        // mostrar si 'auxUsername' y 'auxPassword' coinciden
        if(auxUser == null){
            text = String.format("Usuario %s no existe o no se encuentra", auxUsername.isBlank()? "[empty]" : auxUsername);
            System.out.println(text);
        } else if (auxUser.getPassword().equals(auxPassword)){
            text = String.format("Contraseña correcta. Bienvenid@ de nuevo, %s", auxUser.getNombre() + " " + auxUser.getApellidos());
            System.out.println(text);
            app.setUser(auxUser);
            app.changeStage("menu", "main", "WOSHMAN", 960, 540, true, MainMiddleWare.class);
        } else {
            System.out.println("Contraseña incorrecta. Repita la contraseña o cambie de usuario");
        }
    }

}