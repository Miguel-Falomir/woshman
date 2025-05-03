package com.vehiculos.middleware;

import java.util.Optional;

import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Vehiculo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

public class VehiculoCellWare {

    // OBJETOS PUNTERO (ESTOS NO PRETENDEN SER INSTANCIAS NUEVAS, SINO REFERENCIAS A OTROS OBJETOS YA EXISTENTES)

    DAO_Vehiculo dao;
    Vehiculo vehiculo;
    ListaVehiculosMenuWare menuWare;

    // CONSTRUCTORES

    public VehiculoCellWare(){}

    public VehiculoCellWare(ListaVehiculosMenuWare menuWare){
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
        Func_Borrar_Vehiculo();
    }

    @FXML
    void OnAction_Buton_Editar(ActionEvent event) {
        System.out.println("EDITAR");
    }

    // METODO BORRAR VEHICULO

    private void Func_Borrar_Vehiculo(){
        // inicializar ventana alert
        Alert a = new Alert(AlertType.NONE);

        // (intentar) ejecutar eliminacion
        if(dao.delete(vehiculo)){
            a.setAlertType(AlertType.INFORMATION);
            a.setHeaderText("OPERACIÓN COMPLETADA");
            a.setContentText("El vehiculo " + vehiculo.getMatricula() + " ha sido eliminado de la base de datos.");
        } else {
            a.setAlertType(AlertType.ERROR);
            a.setHeaderText("ERROR SQL");
            a.setContentText("No se puede borrar " + vehiculo.getMatricula() + " porque tiene asignadas 1 o más averías");
        }

        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        a.showAndWait();

        // reiniciar la lista para que se muestre la ausencia del vehiculo eliminado
        menuWare.rebootObserVehiculos();
    }
}
