package com.facturacion.middleware;

import java.util.HashMap;
import java.util.List;

import com.App;
import com.facturacion.controller.DAO_Cliente;
import com.facturacion.model.Cliente;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class ListaClientesSubMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Cliente> obserClientes;
    private FilteredList<Cliente> filterClientes;
    private List<Cliente> listaClientes;
    private Cliente cliente;

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

    @FXML
    void OnAction_Buton_Agregar(ActionEvent event){
        Func_Insert_Cliente();
    }

    @FXML
    void OnAction_Buton_Editar(ActionEvent event){
        Func_Update_Cliente();
    }

    @FXML
    void OnAction_Buton_Borrar(ActionEvent event){
        Func_Delete_Cliente();
    }

    @FXML
    void OnKeyTyped_Input_Email(KeyEvent event){
        Func_Calculate_Predicate();
    }

    // INICIALIZAR

    public void initialize(){
        // booleano permisos
        boolean prohibited;

        // inicializar DAOs y 'Cliente'
        daoCliente = (DAO_Cliente) daoHashMap.get("cliente");

        // recopilar todos los clientes
        listaClientes = daoCliente.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 76 (Insertar Clientes)
        // 77 (Actualizar Datos Cliente)
        // 78 (Eliminar Clientes)
        prohibited = !(App.checkPermiso(76) && App.checkPermiso(77) && App.checkPermiso(78));
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

        // aplicar predicado
        filterClientes.setPredicate(i -> i.getEmail().contains(regex));
    }

    // METODO INSERTAR CLIENTE

    private void Func_Insert_Cliente(){
        mainController.openFormulary("facturacion", "form_insertar_cliente", 480, 360, InsertarClienteFormWare.class, this, null);
    }

    // METODO EDITAR CLIENTE

    private void Func_Update_Cliente(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.WARNING);

        // (intentar) ejecutar actualizacion
        boolean selected = !(TablV_Clientes.getSelectionModel().isEmpty());
        if (selected) {
            cliente = TablV_Clientes.getSelectionModel().getSelectedItem();
            mainController.openFormulary("facturacion", "form_editar_cliente", 480, 360, EditarClienteFormWare.class, this, cliente);
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
        Alert alert = new Alert(AlertType.WARNING);

        // (intentar) ejecutar borrado
        boolean selected = !(TablV_Clientes.getSelectionModel().isEmpty());
        if (selected) {
            cliente = TablV_Clientes.getSelectionModel().getSelectedItem();
            String nombre = cliente.getNombre() + " " + cliente.getApellidos();
            boolean completed = daoCliente.delete(cliente);
            if (completed){
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("El cliente " + nombre + " ha sido eliminado de la base de datos.");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText("No se puede eliminar cliente " + nombre + " porque tiene asignadas 1 o más ventas/averías");
            }
        } else {
            alert.setHeaderText("ELIGE UN CLIENTE");
        }

        // mostrar alert
        alert.showAndWait();

        // si se ha completado la operacion, reiniciar listas observables
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            Func_Reboot_ObserClientes();
            TablV_Clientes.getSelectionModel().clearSelection();
        }
    }

    // METODO REINICIAR LISTA CLIENTES

    public void Func_Reboot_ObserClientes(){
        // recopilar TODOS los clientes
        listaClientes = daoCliente.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 76 (Insertar Clientes)
        // 77 (Actualizar Datos Cliente)
        // 78 (Eliminar Clientes)
        boolean prohibited = (!App.checkPermiso(76) && !App.checkPermiso(77) && !App.checkPermiso(78));
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
