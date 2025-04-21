package com.menu.middleware;

import com.App;
import com.login.middleware.LoginMiddleWare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
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

    // CONSTRUCTOR

    public MainMiddleWare(App app, Boolean bool) {
        this.app = app;
    }

    // ELEMENTOS UI

    @FXML
    private Menu Menu_Empleados;

    @FXML
    private MenuItem Mitem_Estado_Averia;

    @FXML
    private MenuItem Mitem_Lista_Albaranes;

    @FXML
    private MenuItem Mitem_Lista_Averias;

    @FXML
    private MenuItem Mitem_Lista_Categorias;

    @FXML
    private MenuItem Mitem_Lista_Clientes;

    @FXML
    private MenuItem Mitem_Lista_Empleados;

    @FXML
    private MenuItem Mitem_Lista_Encargos;

    @FXML
    private MenuItem Mitem_Lista_Facturas;

    @FXML
    private MenuItem Mitem_Lista_Marcas;

    @FXML
    private MenuItem Mitem_Lista_Modelos;

    @FXML
    private MenuItem Mitem_Lista_Permisos;

    @FXML
    private MenuItem Mitem_Lista_Piezas;

    @FXML
    private MenuItem Mitem_Lista_Proveedores;

    @FXML
    private MenuItem Mitem_Lista_Roles;

    @FXML
    private MenuItem Mitem_Lista_Vehiculos;

    @FXML
    private MenuItem Mitem_Lista_Ventas;

    @FXML
    private MenuItem Mitem_Realizar_Venta;

    @FXML
    private MenuItem Mitem_Tipo_Averia;

    @FXML
    private MenuItem Mitem_Tipo_Pieza;

    @FXML
    private MenuItem Mitem_Ajustes;

    @FXML
    private MenuItem Mitem_Salir;

    @FXML
    private HBox Central_Box;

    // EVENTOS

    @FXML
    void OnAction_Mitem_Estado_Averia(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Albaranes(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Averias(ActionEvent event) {
        
    }

    @FXML
    void OnAction_Mitem_Lista_Categorias(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Clientes(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Empleados(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Encargos(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Facturas(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Marcas(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Modelos(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Permisos(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Piezas(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Proveedores(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Roles(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Vehiculos(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Ventas(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Realizar_Venta(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Tipo_Averia(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Tipo_Pieza(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Ajustes(ActionEvent event){
        
    }

    @FXML
    void OnAction_Mitem_Salir(ActionEvent event){
        Func_Close_Session();
    }

    // METODOS

    private void Func_Close_Session(){
        app.setUser(null);
        app.changeStage("login", "login", "login", 400, 300, false, LoginMiddleWare.class);
    }

    private void Func_Change_Menu(){
        Central_Box.getChildren().clear();
    }

}
