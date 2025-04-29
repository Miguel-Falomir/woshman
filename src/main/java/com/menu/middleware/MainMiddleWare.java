package com.menu.middleware;

import java.io.IOException;
import java.util.List;

import com.App;
import com.login.middleware.LoginMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.middleware.ListaVehiculosMenuWare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class MainMiddleWare {

    // OBJETO APP
    // Entre otras cosas, contiene la conexion a BD

    App app;

    // OBJETOS ALMACENAR DATOS ENTRADA

    SubMenuWare subMenuWare;

    // DAOs

    DAO dao;

    // CONSTRUCTOR

    public MainMiddleWare(App app) {
        this.app = app;
    }

    // GETTERS Y SETTERS

    public DAO getDao(){
        return dao;
    }

    public void setDao(DAO dao){
        this.dao = dao;
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
        //app.changeScene(Central_Box, "almacen", "grid_1", "prueba");
    }

    @FXML
    void OnAction_Mitem_Lista_Proveedores(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Roles(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Vehiculos(ActionEvent event) {
        changeScene("vehiculos", "listview_arbol", "Lista Vehiculos", ListaVehiculosMenuWare.class);
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
        app.setUser(null);
        app.changeStage("login", "login_form", "login", 400, 300, false, LoginMiddleWare.class);
    }

    // METODO CAMBIAR ESCENA

    public void changeScene(String newModule, String newStage, String title, Class<?> menuWareClass){
        // englobar todo el proceso en un try-catch
        // de esta forma, si surge un fallo al generar la nueva pantalla,
        // aborta tambien el proceso de limpiar la pantalla vieja
        try {
            // limpiar contenedor
            Central_Box.getChildren().clear();
    
            // preparar archivo .fxml
            FXMLLoader loader = new FXMLLoader(
                App.class.getResource(newModule + "/gui/" + newStage + ".fxml")
            );

            // cargar archivo .fxml
            Parent root = loader.load();

            // obtener SubMenuWare del archivo .fxml
            subMenuWare = loader.getController();

            // asignar MainMiddleWare al SubMenuWare actual
            if (menuWareClass.equals(ListaVehiculosMenuWare.class)){
                subMenuWare.setMainController(this);
            }

            // asignar MiddleWare del menu principal al MenuWare

            //
            if (newModule.equals("vehiculos")){
                dao = new DAO_Vehiculo(app.getConnection());
            }

            // agregar archivo al panel central
            Central_Box.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
