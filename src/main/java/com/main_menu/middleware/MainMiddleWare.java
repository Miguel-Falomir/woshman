package com.main_menu.middleware;

import com.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class MainMiddleWare {

    // OBJETO APP
    // Entre otras cosas, contiene la conexion a BD

    App app;

    // OBJETOS ALMACENAR DATOS ENTRADA

    //

    // DAOs

    //

    // METODOS ESTATICOS

    private void enableMenuItems(){
        Integer rol = app.getUser().getId();
        
        // comparar rol, para activar MenuItems
        switch (rol){
            case 1: // mecanico
                Buton_Lista_Averias.setDisable(false);
                Buton_Realizar_Venta.setDisable(false);
                Buton_Lista_Piezas.setDisable(false);
                Buton_Lista_Vehiculos.setDisable(false);
                break;
            case 2: // encargado
                Buton_Lista_Averias.setDisable(false);
                Buton_Lista_Ventas.setDisable(false);
                Buton_Lista_Facturas.setDisable(false);
                Buton_Lista_Clientes.setDisable(false);
                Buton_Realizar_Venta.setDisable(false);
                Buton_Lista_Piezas.setDisable(false);
                Buton_Lista_Proveedores.setDisable(false);
                Buton_Lista_Encargos.setDisable(false);
                Buton_Lista_Albaranes.setDisable(false);
                Buton_Lista_Vehiculos.setDisable(false);
                Buton_Lista_Modelos.setDisable(false);
                Buton_Lista_Marcas.setDisable(false);
                Buton_Lista_Empleados.setDisable(false);
                break;
            default: // administrador
                
                break;
        }
    }

    // CONSTRUCTOR

    //public MainMiddleWare(){}

    public MainMiddleWare(App app, Boolean bool) {
        this.app = app;
    }

    // ELEMENTOS UI

    @FXML
    private MenuItem Buton_Estado_Averia;

    @FXML
    private MenuItem Buton_Lista_Albaranes;

    @FXML
    private MenuItem Buton_Lista_Averias;

    @FXML
    private MenuItem Buton_Lista_Categorias;

    @FXML
    private MenuItem Buton_Lista_Clientes;

    @FXML
    private MenuItem Buton_Lista_Empleados;

    @FXML
    private MenuItem Buton_Lista_Encargos;

    @FXML
    private MenuItem Buton_Lista_Facturas;

    @FXML
    private MenuItem Buton_Lista_Marcas;

    @FXML
    private MenuItem Buton_Lista_Modelos;

    @FXML
    private MenuItem Buton_Lista_Permisos;

    @FXML
    private MenuItem Buton_Lista_Piezas;

    @FXML
    private MenuItem Buton_Lista_Proveedores;

    @FXML
    private MenuItem Buton_Lista_Roles;

    @FXML
    private MenuItem Buton_Lista_Vehiculos;

    @FXML
    private MenuItem Buton_Lista_Ventas;

    @FXML
    private MenuItem Buton_Realizar_Venta;

    @FXML
    private MenuItem Buton_Tipo_Averia;

    @FXML
    private MenuItem Buton_Tipo_Pieza;

    @FXML
    private HBox Central_Box;

    // EVENTOS

    @FXML
    void OnAction_Buton_Estado_Averia(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Albaranes(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Averias(ActionEvent event) {
        
    }

    @FXML
    void OnAction_Buton_Lista_Categorias(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Clientes(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Empleados(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Encargos(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Facturas(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Marcas(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Modelos(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Permisos(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Piezas(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Proveedores(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Roles(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Vehiculos(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Lista_Ventas(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Realizar_Venta(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Tipo_Averia(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Tipo_Pieza(ActionEvent event) {

    }

}
