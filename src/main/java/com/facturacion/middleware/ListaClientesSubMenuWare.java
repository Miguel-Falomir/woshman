package com.facturacion.middleware;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import com.App;
import com.facturacion.controller.DAO_Cliente;
import com.facturacion.model.Cliente;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ListaClientesSubMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Cliente> obserClientes;
    private FilteredList<Cliente> filterClientes;
    private List<Cliente> listaClientes;
    private Cliente cliente;

    // DAOs

    private DAO_Cliente daoCliente;

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
    Button Buton_Editar;

    @FXML
    Button Buton_Borrar;

    @FXML
    TableView<Cliente> TablV_Clientes;

    @FXML
    TableColumn<Cliente, String> TVcol_DNI;

    @FXML
    TableColumn<Cliente, String> TVcol_Nombre;

    @FXML
    TableColumn<Cliente, String> TVcol_Apellidos;

    @FXML
    TableColumn<Cliente, String> TVcol_Email;

    @FXML
    TableColumn<Cliente, String> TVcol_Direccion;

    // EVENTOS

    // INICIALIZAR

    public void initialize(){
        // booleano permisos
        boolean prohibited;

        // inicializar DAOs
        daoCliente = (DAO_Cliente) daoHashMap.get("cliente");

        // recopilar todos los clientes
        listaClientes = daoCliente.searchAll();

        // solo 'admin' puede leer objetos undefined
        prohibited = !(App.checkRol(0));
        if (prohibited) {
            listaClientes.removeIf(i -> i.getId() == 0);
        }

        // inicializar TableColumns y TableView
        TVcol_DNI.setCellValueFactory(
            new PropertyValueFactory<Cliente, String>("dni")
        );
        TVcol_Nombre.setCellValueFactory(
            new PropertyValueFactory<Cliente, String>("nombre")
        );
        TVcol_Apellidos.setCellValueFactory(
            new PropertyValueFactory<Cliente, String>("apellidos")
        );
        TVcol_Email.setCellValueFactory(
            new PropertyValueFactory<Cliente, String>("email")
        );
        TVcol_Direccion.setCellValueFactory(
            new PropertyValueFactory<Cliente, String>("direccion")
        );
        TablV_Clientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        TablV_Clientes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // inicializar listas observables, y rellenar filas tabla con 'filterClientes'
        obserClientes = FXCollections.observableArrayList(listaClientes);
        filterClientes = new FilteredList<>(obserClientes);
        TablV_Clientes.setItems(filterClientes);

        // asignar manejadores de eventos a elementos UI
        // al pulsar 'Buton_Agregar', se invoca el metodo 'Func_Insert_Cliente'
        Buton_Agregar.setOnAction((action) -> {
            Func_Insert_Cliente();
        });
        // al pulsar 'Buton_Editar', se invoca el metodo 'Func_Update_Cliente'
        Buton_Editar.setOnAction((action) -> {
            Func_Update_Cliente();
        });
        // al pulsar 'Buton_Borrar', se invoca el metodo 'Func_Delete_Cliente'
        Buton_Borrar.setOnAction((action) -> {
            Func_Delete_Cliente();
        });
        // al escribir en 'Input_Email', se invoca el metodo 'Func_Calculate_Predicate'
        Input_Email.setOnKeyTyped((action) -> {
            Func_Calculate_Predicate();
        });

        // deshabilitar 'Buton_Agregar' si no se tiene permiso 76 (Insertar Clientes)
        prohibited = (!App.checkPermiso(76));
        if (prohibited){
            Buton_Agregar.setVisible(false);
            Buton_Agregar.setDisable(true);
            Buton_Agregar.setManaged(false);
        }

        // deshabilitar 'Buton_Editar' si no se tiene permiso 77 (Actualizar datos Cliente)
        prohibited = !(App.checkPermiso(77));
        if (prohibited){
            Buton_Editar.setVisible(false);
            Buton_Editar.setDisable(true);
            Buton_Editar.setManaged(false);
        }

        // deshabilitar 'Buton_Borrar' si no se tiene permiso 78 (Eliminar cliente)
        prohibited = !(App.checkPermiso(78));
        if (prohibited) {
            Buton_Borrar.setVisible(false);
            Buton_Borrar.setDisable(true);
            Buton_Borrar.setManaged(false);
        }

        // deshabilitar HBox inferior si no se tienen permisos:
        // 77 (Actualizar datos Cliente)
        // 78 (Eliminar cliente)
        prohibited = (!App.checkPermiso(77) && !App.checkPermiso(78));
        if (prohibited) {
            HBox hbox = (HBox) Buton_Borrar.getParent();
            hbox.setVisible(false);
            hbox.setDisable(true);
            hbox.setManaged(false);
        }
    }
    
    // METODO CALCULAR PREDICADO

    private void Func_Calculate_Predicate(){
        // recopilar valores de entrada
        String regex = Input_Email.getText();

        // definir predicado basico
        Predicate<Cliente> pred = (lambda -> lambda.getId() > -1);

        // aplicar predicados adicionales segun valores no vacios
        if (!(regex.equals(""))){
            pred = pred.and(i -> i.getEmail().contains(regex));
        }

        // implementar predicado
        filterClientes.setPredicate(pred);
    }

    // METODO INSERTAR CLIENTE

    private void Func_Insert_Cliente(){
        mainController.openFormulary("facturacion", "form_insertar_cliente", "insertar cliente", 480, 360, InsertarClienteFormWare.class, this, null);
    }

    // METODO EDITAR CLIENTE

    private void Func_Update_Cliente(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.WARNING);

        // (intentar) ejecutar actualizacion
        boolean selected = !(TablV_Clientes.getSelectionModel().isEmpty());
        if (selected) {
            cliente = TablV_Clientes.getSelectionModel().getSelectedItem();
            mainController.openFormulary("facturacion", "form_editar_cliente", "editar cliente", 480, 360, EditarClienteFormWare.class, this, cliente);
        } else {
            alert.setHeaderText("ELIGE UN CLIENTE");
            alert.showAndWait();
        }

        // si se ha completado la operacion, reiniciar listas observables
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            Func_Reboot_ObserClientes();
            TablV_Clientes.getSelectionModel().clearSelection();
        }
    }

    // METODO BORRAR CLIENTE

    private void Func_Delete_Cliente(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.NONE);

        // comprobar seleccion
        boolean selected = !(TablV_Clientes.getSelectionModel().isEmpty());
        if (selected) {
            // tomar valores
            cliente = TablV_Clientes.getSelectionModel().getSelectedItem();
            String nombre = cliente.getNombre() + " " + cliente.getApellidos();
            // pregunta de seguridad
            alert.setAlertType(AlertType.CONFIRMATION);
            alert.setHeaderText("¿ESTÁ SEGURO?");
            alert.setContentText("Esta acción borrará al cliente '" + nombre + "'");
            alert.showAndWait();
            boolean confirm = alert.getResult().equals(ButtonType.OK);
            if (confirm) {
                boolean completed = daoCliente.delete(cliente);
                if (completed){
                    // (intentar) ejecutar borrado
                    alert.setAlertType(AlertType.INFORMATION);
                    alert.setHeaderText("OPERACIÓN COMPLETADA");
                    alert.setContentText("El cliente " + nombre + " ha sido eliminado de la base de datos.");
                } else {
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("ERROR SQL");
                    alert.setContentText("No se puede eliminar cliente " + nombre + " porque tiene asignadas 1 o más ventas/averías");
                }
                // reiniciar lista clientes
                Func_Reboot_ObserClientes();
            } else {
                // mostrar mensaje cancelacion
                alert.setAlertType(AlertType.WARNING);
                alert.setHeaderText("OPERACIÓN CANCELADA");
                alert.setContentText("");
            }
        } else {
            // mostrar advertencia cliente no elegido
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("ELIGE UN CLIENTE");
            alert.setContentText("");
        }

        // mostrar alert
        alert.showAndWait();

        // limpiar seleccion
        TablV_Clientes.getSelectionModel().clearSelection();
    }

    // METODO REINICIAR LISTA CLIENTES

    public void Func_Reboot_ObserClientes(){
        // recopilar TODOS los clientes
        listaClientes = daoCliente.searchAll();

        // solo 'admin' puede leer objetos undefined
        boolean prohibited = (!App.checkRol(0));
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
