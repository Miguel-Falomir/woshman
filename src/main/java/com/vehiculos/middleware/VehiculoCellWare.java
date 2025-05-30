package com.vehiculos.middleware;

import com.utilities.CellWare;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Vehiculo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

public class VehiculoCellWare extends CellWare {

    // OBJETOS PUNTERO (ESTOS NO PRETENDEN SER INSTANCIAS NUEVAS, SINO REFERENCIAS A OTROS OBJETOS YA EXISTENTES)

    DAO_Vehiculo dao;
    Vehiculo vehiculo;
    ListaVehiculosSubMenuWare menuWare;

    // CONSTRUCTORES

    public VehiculoCellWare(){}

    public VehiculoCellWare(ListaVehiculosSubMenuWare menuWare){
        this.menuWare = menuWare;
        this.dao = this.menuWare.getDaoVehiculo();
    }

    // GETTERS Y SETTERS

    public Vehiculo getVehiculo(){
        return this.vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo){
        this.vehiculo = vehiculo;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Borrar;

    @FXML
    private Button Buton_Editar;

    @FXML
    private Label Label_Marca_Modelo;

    @FXML
    private Label Label_Matricula;

    // EVENTOS

    @FXML
    void OnAction_Buton_Borrar(ActionEvent event) {
        Func_Delete_Vehiculo();
    }

    @FXML
    void OnAction_Buton_Editar(ActionEvent event) {
        Func_Update_Vehiculo();
    }

    // METODO BORRAR VEHICULO

    private void Func_Delete_Vehiculo(){
        // guardar matricula vehiculo
        String matricula = vehiculo.getMatricula();

        // inicializar ventana alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("¿ESTÁ SEGURO?");
        alert.setContentText("Esta acción borrará el vehículo " + matricula);

        // pregunta de seguridad
        alert.showAndWait();
        boolean confirm = alert.getResult().equals(ButtonType.OK);

        // (intentar) ejecutar eliminacion
        if (confirm) {
            // comprobar operacion
            boolean completed = dao.delete(vehiculo);
            if (completed){
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("El vehiculo " + matricula + " ha sido eliminado de la base de datos.");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText("No se puede eliminar " + matricula + " porque tiene asignadas 1 o más averías");
            }
            // reiniciar lista vehiculos
            menuWare.Func_Reboot_ObserVehiculos();
        } else {
            // mostrar mensaje cancelacion
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("OPERACIÓN CANCELADA");
            alert.setContentText("");
        }
        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        alert.showAndWait();
    }

    // METODO EDITAR VEHICULO

    private void Func_Update_Vehiculo(){
        // crear un formulario, pasandole vehiculo
        this.menuWare.getMainController().openFormulary("vehiculos", "form_editar_vehiculo", "editar vehiculo", 480, 360, EditarVehiculoFormWare.class, menuWare, vehiculo);
    }
}
