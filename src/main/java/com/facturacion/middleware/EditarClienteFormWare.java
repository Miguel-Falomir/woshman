package com.facturacion.middleware;

import com.facturacion.controller.DAO_Cliente;
import com.facturacion.model.Cliente;
import com.utilities.FormWare;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarClienteFormWare extends FormWare {

    // OBJETOS PUNTERO

    ListaClientesSubMenuWare menuWare;
    DAO_Cliente daoCliente;

    // OBJETOS ALMACENAR DATOS INTERNOS

    Cliente cliente;

    // CONSTRUCTORES

    public EditarClienteFormWare(){}

    public EditarClienteFormWare(Cliente cliente, ListaClientesSubMenuWare menuWare){
        this.cliente = cliente;
        this.menuWare = menuWare;
        this.daoCliente = this.menuWare.getDaoCliente();
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

    // METODO INICIALIZAR

    public void initialize(){
        // rellenar campos de entrada con atributos cliente
        Input_Nombre.setText(cliente.getNombre());
        Input_Apellidos.setText(cliente.getApellidos());
        Input_DNI.setText(cliente.getDni());
        Input_Email.setText(cliente.getEmail());
        Input_Direccion.setText(cliente.getDireccion());

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
        // al pulsar 'Buton_Aceptar', se inserta pieza en base de datos
        Buton_Aceptar.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Func_Update_Cliente();
            }
        });
        // al pulsar 'Buton_Cancelar', se cierra el formulario
        Buton_Cancelar.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // mostrar mensaje cancelacion
                Alert alert = new Alert(AlertType.WARNING);
                alert.setHeaderText("OPERACIÓN CANCELADA");
                alert.setContentText("");
                alert.showAndWait();
                // cerrar ventana
                Func_Close();
            }
        });
    }

    // METODO CERRAR FORMULARIO

    private void Func_Close(){
        Stage thisStage = (Stage) Buton_Cancelar.getScene().getWindow();
        thisStage.close();
    }

    // METODO ACTUALIZAR CLIENTE

    private void Func_Update_Cliente(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.NONE);
        String nombre = cliente.getNombre() + " " + cliente.getApellidos();

        // comprobar que se han rellenado todos los campos obligatorios
        boolean nombreMissing = (cliente.getNombre() == "" || cliente.getNombre() == null || cliente.getNombre().length() <= 0);
        boolean dniMissing = (cliente.getDni() == "" || cliente.getDni() == null || cliente.getDni().length() <= 0);
        boolean emailMissing = (cliente.getEmail() == "" || cliente.getEmail() == null || cliente.getEmail().length() <= 0);
        boolean direccionMissing = (cliente.getDireccion() == "" || cliente.getDireccion() == null || cliente.getDireccion().length() <= 0);
        if (nombreMissing) { // falta 'Nombre'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Nombre' es obligatorio.");
        } else if (dniMissing) { // falta 'DNI'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'DNI' es obligatorio.");
        } else if (emailMissing) { // falta 'Email'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Email' es obligatorio.");
        } else if (direccionMissing) { // falta 'Direccion'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Direccion' es obligatorio.");
        } else { // con todos los campos rellenados, (intentar) ejecutar actualizacion
            boolean completed = daoCliente.update(cliente);
            if (completed) {
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("El cliente " + nombre + " se ha guardado en la base de datos.");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText("Datos duplicados en cliente " + nombre + ". Recuerde que tanto dni como email no pueden repetirse.");
            }
        }

        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        alert.showAndWait();

        // si se ha completado la operacion, reiniciar lista y cerrar ventana
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            menuWare.Func_Reboot_ObserClientes();
            Func_Close();
        }

    }

}
