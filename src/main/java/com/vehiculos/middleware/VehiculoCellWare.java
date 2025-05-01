package com.vehiculos.middleware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VehiculoCellWare {

    @FXML
    private Button Buton_Borrar;

    @FXML
    private Button Buton_Editar;

    @FXML
    private Label Label_Marca_Modelo;

    @FXML
    private Label Label_Matricula;

    @FXML
    void OnAction_Buton_Borrar(ActionEvent event) {
        System.out.println("BORRAR");
    }

    @FXML
    void OnAction_Buton_Editar(ActionEvent event) {
        System.out.println("EDITAR");
    }

}


