package com.facturacion.middleware;

import com.facturacion.controller.DAO_Cliente;
import com.facturacion.model.Cliente;
import com.utilities.FormWare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InsertarClienteFormWare extends FormWare {

    // OBJETOS PUNTERO

    ListaClientesSubMenuWare menuWare;
    DAO_Cliente dao;

    // OBJETOS ALMACENAR DATOS INTERNOS

    Cliente cliente;

    // CONSTRUCTORES

    public InsertarClienteFormWare(){}

    public InsertarClienteFormWare(ListaClientesSubMenuWare menuWare){
        this.cliente = new Cliente();
        this.menuWare = menuWare;
        this.dao = this.menuWare.getDaoCliente();
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Aceptar;

    @FXML
    private Button Buton_Cancelar;

    @FXML
    private TextField Input_Apellidos;

    @FXML
    private TextField Input_DNI;

    @FXML
    private TextField Input_Direccion;

    @FXML
    private TextField Input_Email;

    @FXML
    private TextField Input_Nombre;

    // EVENTOS

    @FXML
    void OnAction_Buton_Aceptar(ActionEvent event) {
        Func_Insert_Cliente();
    }

    @FXML
    void OnAction_Buton_Cancelar(ActionEvent event) {
        Func_Close();
    }

    // METODO INICIALIZAR

    public void initialize(){
        // asignar manejadores de eventos a elementos UI
        // al escribir en 'Input_Nombre', contenido se asigna a 'cliente.nombre'
        Input_Nombre.textProperty().addListener((observable, oldValue, newValue) -> {
            cliente.setNombre(newValue);
        });
        // al escribir en 'Input_Apellidos', contenido se asigna a 'cliente.apellidos'
        Input_Apellidos.textProperty().addListener((observable, oldValue, newValue) -> {
            cliente.setApellidos(newValue);
        });
        // al escribir en 'Input_DNI', contenido se asigna a 'cliente.dni'
        Input_DNI.textProperty().addListener((observable, oldValue, newValue) -> {
            cliente.setDni(newValue);
        });
        // al escribir en 'Input_Email', contenido se asigna a 'cliente.email'
        Input_Email.textProperty().addListener((observable, oldValue, newValue) -> {
            cliente.setEmail(newValue);
        });
        // al escribir en 'Input_Direccion', contenido se asigna a 'cliente.direccion'
        Input_Direccion.textProperty().addListener((observable, oldValue, newValue) -> {
            cliente.setDireccion(newValue);
        });

    }

    // METODO CERRAR FORMULARIO

    private void Func_Close(){
        Stage thisStage = (Stage) Buton_Cancelar.getScene().getWindow();
        thisStage.close();
    }

    // METODO INSERTAR VEHICULO

    private void Func_Insert_Cliente(){
        // inicializar ventana alert
        Alert a = new Alert(AlertType.NONE);
        String nombre = cliente.getNombre() + " " + cliente.getApellidos();

        // (intentar) ejecutar actualizacion
        if(dao.insert(cliente)){
            a.setAlertType(AlertType.INFORMATION);
            a.setHeaderText("OPERACIÓN COMPLETADA");
            a.setContentText("El cliente " + nombre + " se ha guardado en la base de datos.");
        } else {
            a.setAlertType(AlertType.ERROR);
            a.setHeaderText("ERROR SQL");
            a.setContentText("Datos duplicados en cliente " + nombre + ". Recuerde que tanto dni como email no pueden repetirse.");
        }

        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        a.showAndWait();

        // reiniciar la lista para que se muestre la ausencia del vehiculo eliminado
        menuWare.Func_Reboot_ObserClientes();

        // cerrar ventana
        Func_Close();
    }

}
