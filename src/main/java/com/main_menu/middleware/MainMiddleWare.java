package com.main_menu.middleware;

import com.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class MainMiddleWare {

    // OBJETOS ALMACENAR DATOS ENTRADA

    App app;

    // DAOs



    // CONSTRUCTOR

    public MainMiddleWare(App app) {
        this.app = app;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_ChangeUser;

    @FXML
    private Button Buton_Module1;

    @FXML
    private Button Buton_Module2;

    // EVENTOS

    @FXML
    void Buton_ChangeUser_OnMouseClicked(MouseEvent event) {
        
    }

    @FXML
    void Buton_Module1_OnMouseClicked(MouseEvent event) {

    }

    @FXML
    void Buton_Module2_OnMouseClicked(MouseEvent event) {

    }
}
