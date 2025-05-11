package com.menu.middleware;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import com.App;
import com.almacen.controller.DAO_Pieza;
import com.almacen.controller.DAO_Proveedor;
import com.almacen.controller.DAO_Tipo_Pieza;
import com.almacen.middleware.ListaPiezasSubMenuWare;
import com.empleados.middleware.LoginMiddleWare;
import com.facturacion.controller.DAO_Cliente;
import com.facturacion.middleware.EditarClienteFormWare;
import com.facturacion.middleware.InsertarClienteFormWare;
import com.facturacion.middleware.ListaClientesSubMenuWare;
import com.facturacion.model.Cliente;
import com.utilities.DAO;
import com.utilities.FormWare;
import com.utilities.MiddleWare;
import com.utilities.SubMenuWare;
import com.vehiculos.controller.DAO_Marca;
import com.vehiculos.controller.DAO_Modelo;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.middleware.EditarVehiculoFormWare;
import com.vehiculos.middleware.InsertarVehiculoFormWare;
import com.vehiculos.middleware.ListaVehiculosSubMenuWare;
import com.vehiculos.model.Vehiculo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainMiddleWare extends MiddleWare {

    // OBJETO APP
    // Entre otras cosas, contiene la conexion a BD

    App app;

    // OBJETOS ALMACENAR DATOS ENTRADA

    SubMenuWare menuWare;

    // CONSTRUCTOR

    public MainMiddleWare(App app) {
        this.app = app;
    }

    // GETTERS Y SETTERS

    public SubMenuWare getMenuWare(){
        return this.menuWare;
    }

    public void setMenuWare(SubMenuWare menuWare){
        this.menuWare = menuWare;
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
        changeSubMenu("facturacion", "submenu_lista_clientes", "Lista Clientes", ListaClientesSubMenuWare.class);
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
        changeSubMenu("almacen", "submenu_lista_piezas", "Lista Piezas", ListaPiezasSubMenuWare.class);
    }

    @FXML
    void OnAction_Mitem_Lista_Proveedores(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Roles(ActionEvent event) {

    }

    @FXML
    void OnAction_Mitem_Lista_Vehiculos(ActionEvent event) {
        changeSubMenu("vehiculos", "submenu_lista_vehiculos", "Lista Vehiculos", ListaVehiculosSubMenuWare.class);
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
        app.changeStage("empleados", "form_login", "login", 400, 300, false, LoginMiddleWare.class);
    }

    // METODO CAMBIAR SUB MENU

    public void changeSubMenu(String newModule, String newScene, String title, Class<? extends SubMenuWare> menuWareClass){
        // englobar todo el proceso en un try-catch
        // de esta forma, si surge un fallo al generar la nueva pantalla,
        // aborta tambien el proceso de limpiar la pantalla vieja
        try {
            // limpiar contenedor
            Central_Box.getChildren().clear();
            
            // preparar archivo .fxml
            FXMLLoader loader = new FXMLLoader(
                App.class.getResource(newModule + "/gui/" + newScene + ".fxml")
            );

            // generar controlador correspondiente al submenu
            // asignando DAOs necesarios
            Connection conn = app.getConnection();
            HashMap<String, DAO> daoHashMap = new HashMap<>();
            if (menuWareClass.equals(ListaVehiculosSubMenuWare.class)){ // submenu 'Lista Vehiculos'
                loader.setControllerFactory(lambda -> {
                    daoHashMap.put("vehiculo", new DAO_Vehiculo(conn));
                    daoHashMap.put("modelo", new DAO_Modelo(conn));
                    daoHashMap.put("marca", new DAO_Marca(conn));
                    return new ListaVehiculosSubMenuWare(this, daoHashMap);
                });
            } else if (menuWareClass.equals(ListaClientesSubMenuWare.class)){ // submenu 'Lista Clientes'
                loader.setControllerFactory(lambda -> {
                    daoHashMap.put("cliente", new DAO_Cliente(conn));
                    return new ListaClientesSubMenuWare(this, daoHashMap);
                });
            } else if (menuWareClass.equals(ListaPiezasSubMenuWare.class)){ // submenu 'Lista Piezas'
                loader.setControllerFactory(lambda -> {
                    daoHashMap.put("pieza", new DAO_Pieza(conn));
                    daoHashMap.put("tipo", new DAO_Tipo_Pieza());
                    daoHashMap.put("proveedor", new DAO_Proveedor());
                    return new ListaPiezasSubMenuWare(this, daoHashMap);
                });
            }

            // cargar archivo .fxml
            Parent root = loader.load();

            // obtener SubMenuWare del archivo .fxml
            menuWare = loader.getController();

            // agregar archivo al panel central
            Central_Box.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // METODO ABRIR VENTANA FORMULARIO

    public void openFormulary(String newModule, String newStage, int width, int heigth, Class<? extends FormWare> formWareClass, SubMenuWare menuWare, Object obj){
        // preparar archivo .fxml
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource(newModule + "/gui/" + newStage + ".fxml")
        );

        try {
            // elegir controlador
            if (menuWare instanceof ListaVehiculosSubMenuWare){ // submenu 'Lista Vehiculos'
                ListaVehiculosSubMenuWare submenu = (ListaVehiculosSubMenuWare) menuWare;
                if (formWareClass.equals(EditarVehiculoFormWare.class) && obj instanceof Vehiculo) { // formulario 'Actualizar Vehiculo'
                    loader.setControllerFactory(lambda -> {
                        Vehiculo vehiculo = (Vehiculo) obj;
                        return new EditarVehiculoFormWare(vehiculo, submenu);
                    });
                } else if (formWareClass.equals(InsertarVehiculoFormWare.class)){ // formulario 'Insertar Vehiculo'
                    loader.setControllerFactory(lambda -> {
                        return new InsertarVehiculoFormWare(submenu);
                    });
                }
            } else if (menuWare instanceof ListaClientesSubMenuWare) { // submenu 'Lista Clientes'
                ListaClientesSubMenuWare submenu = (ListaClientesSubMenuWare) menuWare;
                if (formWareClass.equals(EditarClienteFormWare.class) && obj instanceof Cliente){ // formulario 'Actualizar Cliente'
                    loader.setControllerFactory(lambda -> {
                        Cliente cliente = (Cliente) obj;
                        return new EditarClienteFormWare(cliente, submenu);
                    });    
                } else if (formWareClass.equals(InsertarClienteFormWare.class)) { // formulario 'Insertar Cliente'
                    loader.setControllerFactory(lambda -> {
                        return new InsertarClienteFormWare(submenu);
                    });
                }
            }

            // cargar escena y archivo .fxml
            Stage formulary = new Stage();
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // inicializar elementos @FXML
            if (formWareClass.equals(EditarVehiculoFormWare.class)){
                EditarVehiculoFormWare formWare = (EditarVehiculoFormWare) loader.getController();
                formWare.initialize();
            }

            // definir dimensiones ventana
            formulary.setScene(scene);
            formulary.setTitle("formulario");
            formulary.setWidth(width);
            formulary.setHeight(heigth);
            formulary.setResizable(false);

            // mostrar ventana
            formulary.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
