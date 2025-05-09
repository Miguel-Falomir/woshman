package com.empleados.middleware;

//import java.io.IOException;
//import java.sql.Connection;

import com.App;
import com.empleados.controller.DAO_Empleado;
import com.empleados.model.Empleado;
import com.menu.middleware.MainMiddleWare;
import com.utilities.MiddleWare;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LoginMiddleWare extends MiddleWare{

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
    private Button Buton_Entrar;

    @FXML
    private Button Buton_Registrarse;

    @FXML
    private TextField Input_Password;

    @FXML
    private TextField Input_Username;

    // EVENTOS

    @FXML
    void OnMouseClicked_Buton_Entrar(MouseEvent event) {
        Func_User_Login();
    }

    @FXML
    void OnKeyPressed_Buton_Entrar(KeyEvent event) {
        KeyCode ke = event.getCode();
        if (ke.equals(KeyCode.ENTER)) {
            Func_User_Login();
        } else {
            System.out.println("Este método detecta cualquier tecla, pero le he dicho que solo realize la acción si el usuario pulsa ENTER / INTRO");
        }
    }

    @FXML
    void OnMouseClicked_Buton_Registrarse(MouseEvent event) {
        app.changeStage("empleados", "form_nuevo_usuario", "REGISTRO", 480, 360, false, NuevoUsuarioMiddleWare.class);
    }

        @FXML
    void OnKeyPresed_Buton_Registrarse(KeyEvent event) {
        KeyCode ke = event.getCode();
        if (ke.equals(KeyCode.ENTER)) {
            app.changeStage("empleados", "form_nuevo_usuario", "REGISTRO", 480, 360, false, NuevoUsuarioMiddleWare.class);
        } else {
            System.out.println("Este método detecta cualquier tecla, pero le he dicho que solo realize la acción si el usuario pulsa ENTER / INTRO");
        }
    }

    // METODO INICIAR SESION

    private void Func_User_Login(){
        // variables internas
        String text = "";
        Alert alert = new Alert(AlertType.ERROR);

        // recopilar datos de los inputs
        auxUsername = Input_Username.getText();
        auxPassword = Input_Password.getText();

        // buscar usuario a partir de nombre
        auxUser = dao.searchByNombre(auxUsername);

        // mostrar si 'auxUsername' y 'auxPassword' coinciden
        if(auxUser == null){
            text = String.format("Usuario %s no existe o no se encuentra", auxUsername.isBlank()? "[empty]" : auxUsername);
            alert.setHeaderText("ACCESO DENEGADO");
            alert.setContentText(text);
            alert.show();
        } else if (auxUser.getPassword().equals(auxPassword)){
            text = String.format("Contraseña correcta. Bienvenid@ de nuevo, %s", auxUser.getNombre() + " " + auxUser.getApellidos());
            System.out.println(text);
            app.setUser(auxUser);
            app.changeStage("menu", "menu_principal", "WOSHMAN", 960, 540, true, MainMiddleWare.class);
        } else {
            alert.setHeaderText("ACCESO DENEGADO");
            alert.setContentText("Contraseña incorrecta. Repita la contraseña o cambie de usuario");
            alert.show();
        }
    }

}