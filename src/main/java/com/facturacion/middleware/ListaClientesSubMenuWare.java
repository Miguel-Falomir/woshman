package com.facturacion.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.App;
import com.facturacion.controller.DAO_Cliente;
import com.facturacion.model.Cliente;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;
import com.vehiculos.middleware.InsertarVehiculoFormWare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;

public class ListaClientesSubMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Cliente> obserClientes;
    private FilteredList<Cliente> filterClientes;
    private List<Cliente> listaClientes;

    // DAOs

    DAO_Cliente daoCliente;

    // CONSTRUCTORES

    public ListaClientesSubMenuWare(){}

    public ListaClientesSubMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
    }

    // GETTERS Y SETTERS

    public DAO_Cliente getDaoCliente(){
        return daoCliente;
    }

    public void setDaoCliente(DAO_Cliente daoCliente){
        this.daoCliente = daoCliente;
    }

    // ELEMENTOS UI

    @FXML
    TextField Input_Email;

    @FXML
    Button Buton_Agregar;

    @FXML
    TableView<Cliente> TablV_Clientes;

    // EVENTOS

    @FXML
    void OnAction_Buton_Agregar(ActionEvent event){
        Func_Insert_Cliente();
    }

    @FXML
    void OnKeyTyped_Input_Email(KeyEvent event){
        Func_Calculate_Predicate();
    }

    // INICIALIZAR

    public void initialize(){
        // inicializar DAOs
        daoCliente = (DAO_Cliente) daoHashMap.get("cliente");

        // recopilar todos los clientes
        listaClientes = daoCliente.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 76 (Insertar Clientes)
        // 77 (Actualizar Datos Cliente)
        // 78 (Eliminar Clientes)
        boolean prohibited = !(App.checkPermiso(76) && App.checkPermiso(77) && App.checkPermiso(78));
        if (prohibited) {
            listaClientes.removeIf(i -> i.getId() == 0);
        }

        // inicializar TableView
        TablV_Clientes.getColumns().clear(); // limpiar tabla
        TablV_Clientes.setEditable(true);
        List<TableColumn<Cliente, String>> listaColumnas = new ArrayList<TableColumn<Cliente, String>>();
        String[] atributos = {"dni", "nombre", "apellidos", "email", "direccion"};
        for (String str : atributos) { // generar columnas
            TableColumn<Cliente, String> taco = new TableColumn<Cliente, String>(str);
            taco.setCellValueFactory(new PropertyValueFactory<Cliente, String>(str));
            taco.setCellFactory(TextFieldTableCell.forTableColumn());
            switch (str) {
                case "dni":
                    taco.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Cliente,String>>() {
                        @Override
                        public void handle(CellEditEvent<Cliente, String> event) {
                            // recopilar variables
                            Cliente cliente = event.getRowValue();
                            String oldValue = event.getOldValue();
                            String newValue = event.getNewValue();
                            // actualizar fila de la tabla
                            cliente.setDni(newValue);
                            // alerta de confirmacion
                            Alert a = new Alert(AlertType.CONFIRMATION);
                            a.setHeaderText("¿Desea guardar cambios?");
                            a.setContentText("Al aceptar, todos los cambios se guardarán el la base de datos de forma automática.");
                            a.showAndWait();
                            // si btn = OK, actualizar cliente
                            ButtonType btn = a.getResult();
                            if (btn.equals(ButtonType.OK)){
                               daoCliente.update(cliente);
                            } else {
                                cliente.setDni(oldValue);
                            }
                        }
                    });
                    break;
                case "nombre":
                    taco.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Cliente,String>>() {
                        @Override
                        public void handle(CellEditEvent<Cliente, String> event) {
                            // recopilar variables
                            Cliente cliente = event.getRowValue();
                            String oldValue = event.getOldValue();
                            String newValue = event.getNewValue();
                            // actualizar fila de la tabla
                            cliente.setNombre(newValue);
                            // alerta de confirmacion
                            Alert a = new Alert(AlertType.CONFIRMATION);
                            a.setHeaderText("¿Desea guardar cambios?");
                            a.setContentText("Al aceptar, todos los cambios se guardarán el la base de datos de forma automática.");
                            a.showAndWait();
                            // si btn = OK, actualizar cliente
                            ButtonType btn = a.getResult();
                            if (btn.equals(ButtonType.OK)){
                               daoCliente.update(cliente);
                            } else {
                                cliente.setNombre(oldValue);
                            }
                        }
                    });
                    break;
                case "apellidos":
                    taco.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Cliente,String>>() {
                        @Override
                        public void handle(CellEditEvent<Cliente, String> event) {
                            // recopilar variables
                            Cliente cliente = event.getRowValue();
                            String oldValue = event.getOldValue();
                            String newValue = event.getNewValue();
                            // actualizar fila de la tabla
                            cliente.setApellidos(newValue);
                            // alerta de confirmacion
                            Alert a = new Alert(AlertType.CONFIRMATION);
                            a.setHeaderText("¿Desea guardar cambios?");
                            a.setContentText("Al aceptar, todos los cambios se guardarán el la base de datos de forma automática.");
                            a.showAndWait();
                            // si btn = OK, actualizar cliente
                            ButtonType btn = a.getResult();
                            if (btn.equals(ButtonType.OK)){
                               daoCliente.update(cliente);
                            } else {
                                cliente.setApellidos(oldValue);
                            }
                        }
                    });
                    break;
                case "email":
                    taco.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Cliente,String>>() {
                        @Override
                        public void handle(CellEditEvent<Cliente, String> event) {
                            // recopilar variables
                            Cliente cliente = event.getRowValue();
                            String oldValue = event.getOldValue();
                            String newValue = event.getNewValue();
                            // actualizar fila de la tabla
                            cliente.setEmail(newValue);
                            // alerta de confirmacion
                            Alert a = new Alert(AlertType.CONFIRMATION);
                            a.setHeaderText("¿Desea guardar cambios?");
                            a.setContentText("Al aceptar, todos los cambios se guardarán el la base de datos de forma automática.");
                            a.showAndWait();
                            // si btn = OK, actualizar cliente
                            ButtonType btn = a.getResult();
                            if (btn.equals(ButtonType.OK)){
                               daoCliente.update(cliente);
                            } else {
                                cliente.setEmail(oldValue);
                            }
                        }
                    });
                    break;
                case "direccion":
                    taco.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Cliente,String>>() {
                        @Override
                        public void handle(CellEditEvent<Cliente, String> event) {
                            // recopilar variables
                            Cliente cliente = event.getRowValue();
                            String oldValue = event.getOldValue();
                            String newValue = event.getNewValue();
                            // actualizar fila de la tabla
                            cliente.setDireccion(newValue);
                            // alerta de confirmacion
                            Alert a = new Alert(AlertType.CONFIRMATION);
                            a.setHeaderText("¿Desea guardar cambios?");
                            a.setContentText("Al aceptar, todos los cambios se guardarán el la base de datos de forma automática.");
                            a.showAndWait();
                            // si btn = OK, actualizar cliente
                            ButtonType btn = a.getResult();
                            if (btn.equals(ButtonType.OK)){
                               daoCliente.update(cliente);
                            } else {
                                cliente.setDireccion(oldValue);
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
            listaColumnas.add(taco);
        }
        TablV_Clientes.getColumns().setAll(listaColumnas); // asignar columnas a tabla
        TablV_Clientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // ajustar columnas al ancho de la pantalla

        // inicializar listas observables, y rellenar filas tabla con 'filterClientes'
        obserClientes = FXCollections.observableArrayList(listaClientes);
        filterClientes = new FilteredList<>(obserClientes);
        TablV_Clientes.setItems(filterClientes);

        // deshabilitar 'Buton_Agregar' si no se tiene permiso 76 (Insertar Clientes)
        if (!App.checkPermiso(76)){
            Buton_Agregar.setVisible(false);
            Buton_Agregar.setDisable(true);
            Buton_Agregar.setManaged(false);
        }
    }
    
    // METODO CALCULAR PREDICADO

    private void Func_Calculate_Predicate(){
        // recopilar valores de entrada
        String regex = Input_Email.getText();

        // aplicar predicado
        filterClientes.setPredicate(i -> i.getEmail().contains(regex));
    }

    // METODO INSERTAR CLIENTE

    private void Func_Insert_Cliente(){
        mainController.openFormulary("facturacion", "form_insertar_cliente", 480, 360, InsertarClienteFormWare.class, this, null);
    }

    // METODO REINICIAR LISTA CLIENTES

    public void Func_Reboot_ObserClientes(){
        // recopilar TODOS los clientes
        listaClientes = daoCliente.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 76 (Insertar Clientes)
        // 77 (Actualizar Datos Cliente)
        // 78 (Eliminar Clientes)
        boolean prohibited = !(App.checkPermiso(76) && App.checkPermiso(77) && App.checkPermiso(78));
        if (prohibited) {
            listaClientes.removeIf(i -> i.getId() == 0);
        }

        // actualizar listas observables
        obserClientes.clear();
        obserClientes.setAll(listaClientes);

        // refrescar TableView
        TablV_Clientes.refresh();
    }

}
