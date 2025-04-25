package com.almacen.middleware;

import com.utilities.MenuWare;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AlmacenMenuWare extends MenuWare {

    // CONSTRUCTOR

    public AlmacenMenuWare(){
        
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Click;

    // EVENTOS

    @FXML
    void OnAction_Buton_Click(){
        System.out.println("AUCH: Don't press me that hard >:´(");
    }

}
