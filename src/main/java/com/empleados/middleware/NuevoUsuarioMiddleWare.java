package com.empleados.middleware;

import com.App;
import com.empleados.controller.DAO_Empleado;
import com.empleados.model.Empleado;
import com.menu.middleware.MainMiddleWare;
import com.utilities.MiddleWare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NuevoUsuarioMiddleWare extends MiddleWare {

    // OBJETO APP
    // Entre otras cosas, contiene la conexion a BD

    App app;

    // OBJETOS ALMACENAR DATOS ENTRADA

    String auxNombre = "";
    String auxApellidos = "";
    String auxUsername = "";
    String auxPassword = "";
    String auxConfirm = "";

    // DAOs

    public DAO_Empleado dao = null;

    // CONSTRUCTOR

    public NuevoUsuarioMiddleWare(App app){
        this.app = app;
        this.dao = new DAO_Empleado(this.app.getConnection());
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Empleado_Registrar;

    @FXML
    private TextField Input_Apellidos;

    @FXML
    private TextField Input_Nombre;

    @FXML
    private PasswordField Input_Password_Confirmar;

    @FXML
    private PasswordField Input_Password_Insertar;

    @FXML
    private TextField Input_Username;

    // EVENTOS

    @FXML
    void OnAction_Buton_Empleado_Cancelar(ActionEvent event) {
        app.changeStage("empleados", "form_login", "Login", 400, 300, false, LoginMiddleWare.class);
    }

    @FXML
    void OnAction_Buton_Empleado_Registrar(ActionEvent event) {
        Func_User_Insert();
    }

    // METODOS

    private void Func_User_Insert(){
        // variables internas
        Empleado emp = null;
        Alert alert = new Alert(AlertType.ERROR);

        // recopilar datos de los inputs
        auxNombre = Input_Nombre.getText();
        auxApellidos = Input_Apellidos.getText();
        auxUsername = Input_Username.getText();
        auxConfirm = Input_Password_Confirmar.getText();
        auxPassword = Input_Password_Insertar.getText();

        // comprobar que la contrasenya coincide
        if (auxPassword.equals(auxConfirm)){
            // generar empleado nuevo
            emp = new Empleado(
                auxNombre,
                auxApellidos,
                auxUsername,
                auxPassword
            );

            // (intentar) insertar empleado nuevo
            // el metodo dao.insert(emp) devuelve false si emp.username o emp.password se repiten en BD
            boolean completed = dao.insert(emp);
            if (completed){
                // configurar alerta tipo CONFIRMATION
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("Bienvenid@ al equipo, " + auxUsername + ".");
                alert.setContentText("Por razones de seguridad, se le ha asignado el rol 'mecanico', el que menos permisos tiene.");
    
                // lanzar alerta
                alert.showAndWait();
    
                // navegar a menu principal con nuevo usuario
                app.setUser(emp);
                app.changeStage("menu", "menu_principal", "WOSHMAN", 960, 540, true, MainMiddleWare.class);
            } else {
                // configurar alerta tipo ERROR
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("Error base datos");
                alert.setContentText("No se puede agregar el nuevo usuario porque username y/o password se repiten en la base de datos.");

                // lanzar alerta
                alert.show();
            }

        } else {
            // configurar alerta tipo ERROR
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("Error contraseña");
            alert.setContentText("Ambos campos deben ser identicos para poder validar la contraseña.");

            // lanzar alerta
            alert.show();
        }
    }

}
