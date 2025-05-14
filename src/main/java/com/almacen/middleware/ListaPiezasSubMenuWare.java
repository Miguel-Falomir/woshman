package com.almacen.middleware;

import java.util.HashMap;
import java.util.List;

import com.App;
import com.almacen.controller.DAO_Pieza;
import com.almacen.controller.DAO_Proveedor;
import com.almacen.controller.DAO_Tipo_Pieza;
import com.almacen.model.Pieza;
import com.almacen.model.Proveedor;
import com.almacen.model.Tipo_Pieza;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ListaPiezasSubMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Pieza> obserPiezas;
    private FilteredList<Pieza> filterPiezas;
    private ObservableList<Tipo_Pieza> obserTipos;
    private ObservableList<Proveedor> obserProveedores;
    private List<Pieza> listPiezas;
    private List<Tipo_Pieza> listTipos;
    private List<Proveedor> listProveedores;
    private Pieza pieza;

    // DAOs

    private DAO_Pieza daoPieza;
    private DAO_Tipo_Pieza daoTipo;
    private DAO_Proveedor daoProveedor;

    // CONSTRUCTORES

    public ListaPiezasSubMenuWare(){}

    public ListaPiezasSubMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
    }

    // GETTERS Y SETTERS

    public DAO_Pieza getDaoPieza() {
        return daoPieza;
    }

    public void setDaoPieza(DAO_Pieza daoPieza) {
        this.daoPieza = daoPieza;
    }

    public DAO_Tipo_Pieza getDaoTipo() {
        return daoTipo;
    }

    public void setDaoTipo(DAO_Tipo_Pieza daoTipo) {
        this.daoTipo = daoTipo;
    }

    public DAO_Proveedor getDaoProveedor() {
        return daoProveedor;
    }

    public void setDaoProveedor(DAO_Proveedor daoProveedor) {
        this.daoProveedor = daoProveedor;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Agregar;

    @FXML
    private Button Buton_Borrar;

    @FXML
    private Button Buton_Editar;

    @FXML
    private TextField Input_Nombre;

    @FXML
    private Text Label_Cantidad;

    @FXML
    private Slider Slide_Cantidad;

    @FXML
    private ComboBox<Tipo_Pieza> Combo_Tipos;

    @FXML
    private ComboBox<Proveedor> Combo_Proveedores;

    @FXML
    private TableView<Pieza> TablV_Piezas;

    @FXML
    private TableColumn<Pieza, String> TVcol_Nombre;

    @FXML
    private TableColumn<Pieza, String> TVcol_Tipo;

    @FXML
    private TableColumn<Pieza, String> TVcol_Proveedor;

    @FXML
    private TableColumn<Pieza, Integer> TVcol_Cantidad;

    @FXML
    private TableColumn<Pieza, Float> TVcol_Precio;

    @FXML
    private TableColumn<Pieza, String> TVcol_Descripcion;

    // EVENTOS

    @FXML
    void OnAction_Buton_Agregar(ActionEvent event) {
        Func_Insert_Pieza();
    }

    @FXML
    void OnAction_Buton_Borrar(ActionEvent event) {
        Func_Delete_Pieza();
    }

    @FXML
    void OnAction_Buton_Editar(ActionEvent event) {
        Func_Update_Pieza();
    }

    @FXML
    void OnKeyTyped_Input_Nombre(KeyEvent event) {
        Func_Calculate_Predicate();
    }

    // INICIALIZAR

    public void initialize(){
        // inicializar DAOs
        daoPieza = (DAO_Pieza) daoHashMap.get("pieza");
        daoTipo = (DAO_Tipo_Pieza) daoHashMap.get("tipo");
        daoProveedor = (DAO_Proveedor) daoHashMap.get("proveedor");

        // recopilar Piezas, Tipos y Proveedores
        listPiezas = daoPieza.searchAll();
        listTipos = daoTipo.searchAll();
        listProveedores = daoProveedor.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 32 (Insertar Pieza)
        // 33 (Cambiar Proveedor Pieza)
        // 34 (Modificar Pieza)
        // 35 (Eliminar Pieza)
        boolean prohibited = (!App.checkPermiso(32) && !App.checkPermiso(33) && !App.checkPermiso(34) && !App.checkPermiso(35));
        if (prohibited) {
            listPiezas.removeIf(pieza -> pieza.getId() == 0);
            //listaTipos.removeIf(tipo -> tipo.getId() == 0);
            //listaProveedores.removeIf(prov -> prov.getId() == 0);
        }

        // inicializar TableColumns y TableView
        TVcol_Nombre.setCellValueFactory(
            new PropertyValueFactory<Pieza, String>("nombre")
        );
        TVcol_Tipo.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getTipo().getNombre()
        ));
        TVcol_Proveedor.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getProveedor().getNombre()
        ));
        TVcol_Cantidad.setCellValueFactory(
            new PropertyValueFactory<Pieza, Integer>("cantidad")
        );
        TVcol_Precio.setCellValueFactory(
            new PropertyValueFactory<Pieza, Float>("precio")
        );
        TVcol_Descripcion.setCellValueFactory(
            new PropertyValueFactory<Pieza, String>("descripcion")
        );
        TablV_Piezas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        TablV_Piezas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // asignar lista piezas a 'TablV_Piezas'
        obserPiezas = FXCollections.observableArrayList(listPiezas);
        filterPiezas = new FilteredList<>(obserPiezas);
        TablV_Piezas.setItems(filterPiezas);

        // asignar lista tipos a 'Combo_Tipos'
        obserTipos = FXCollections.observableArrayList(listTipos);
        Tipo_Pieza clearTipoSelection = new Tipo_Pieza(-1, "Elegir Tipo Pieza");
        obserTipos.addFirst(clearTipoSelection);
        Combo_Tipos.setItems(obserTipos);
        Combo_Tipos.getSelectionModel().select(clearTipoSelection);

        // asignar lista proveedores a 'Combo_Proveedores'
        obserProveedores = FXCollections.observableArrayList(listProveedores);
        Proveedor clearProvSelection = new Proveedor(-1, "Elegir Proveedor");
        obserProveedores.addFirst(clearProvSelection);
        Combo_Proveedores.setItems(obserProveedores);
        Combo_Proveedores.getSelectionModel().select(clearProvSelection);

        // agregar manejadores de eventos
        // al deslizar 'Slide_Cantidad', cambiar valor 'Label_Cantidad' y filtrar piezas
        Slide_Cantidad.valueProperty().addListener( new ChangeListener<Number>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Label_Cantidad.textProperty().setValue( String.valueOf( newValue.intValue()));
                Func_Calculate_Predicate();
            };
        });
        // al elegir tipo pieza en 'Combo_Tipos', filtrar piezas
        Combo_Tipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Func_Calculate_Predicate();
        });
        // al elegir proveedor en 'Combo_Proveedores', filtrar piezas
        Combo_Proveedores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Func_Calculate_Predicate();
        });

        // deshabilitar 'Buton_Agregar' si no se tiene permiso 32 (Insertar Pieza)
        prohibited = (!App.checkPermiso(32));
        if (prohibited){
            Buton_Agregar.setVisible(false);
            Buton_Agregar.setDisable(true);
            Buton_Agregar.setManaged(false);
        }

        // deshabilitar 'Buton_Editar' si no se tiene permiso 34 (Modificar Pieza)
        prohibited = !(App.checkPermiso(34));
        if (prohibited){
            Buton_Editar.setVisible(false);
            Buton_Editar.setDisable(true);
            Buton_Editar.setManaged(false);
        }

        // deshabilitar 'Buton_Borrar' si no se tiene permiso 35 (Eliminar Pieza)
        prohibited = !(App.checkPermiso(35));
        if (prohibited) {
            Buton_Borrar.setVisible(false);
            Buton_Borrar.setDisable(true);
            Buton_Borrar.setManaged(false);
        }

        // deshabilitar HBox inferior si no se tienen permisos:
        // 34 (Modificar Pieza)
        // 35 (Eliminar Pieza)
        prohibited = (!App.checkPermiso(34) && !App.checkPermiso(35));
        if (prohibited) {
            HBox hbox = (HBox) Buton_Borrar.getParent();
            hbox.setVisible(false);
            hbox.setDisable(true);
            hbox.setManaged(false);
        }
    }

    // METODO CALCULAR PREDICADO

    private void Func_Calculate_Predicate(){
        // recopilar valores
        String regex = Input_Nombre.getText();
        int tipoID = Combo_Tipos.getSelectionModel().getSelectedItem().getId();
        int provID = Combo_Proveedores.getSelectionModel().getSelectedItem().getId();
        int cantidad = (int) Math.floor( Slide_Cantidad.getValue());

        // aplicar predicado
        boolean selectTipo = (tipoID > -1);
        boolean selectProv = (provID > -1);
        boolean selectBoth = selectTipo && selectProv;
        boolean selectNone = !(selectTipo) && !(selectProv);
        if (selectNone) {
            filterPiezas.setPredicate(i -> i.getNombre().contains(regex) && i.getCantidad() >= cantidad);
        } else if (selectBoth) {
            filterPiezas.setPredicate(i -> i.getNombre().contains(regex) && i.getCantidad() >= cantidad && i.getTipo().getId() == tipoID && i.getProveedor().getId() == provID);
        } else if (selectTipo) {
            filterPiezas.setPredicate(i -> i.getNombre().contains(regex) && i.getCantidad() >= cantidad && i.getTipo().getId() == tipoID);
        } else if (selectProv) {
            filterPiezas.setPredicate(i -> i.getNombre().contains(regex) && i.getCantidad() >= cantidad && i.getProveedor().getId() == provID);
        }
    }

    // METODO INSERTAR PIEZA

    private void Func_Insert_Pieza(){
        mainController.openFormulary("almacen", "form_insertar_pieza", "insertar pieza", 480, 360, InsertarPiezaFormWare.class, this, null);
    }

    // METODO EDITAR PIEZA

    private void Func_Update_Pieza(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.WARNING);

        // (intentar) ejecutar actualizacion
        boolean selected =!(TablV_Piezas.getSelectionModel().isEmpty());
        if (selected) {
            pieza = TablV_Piezas.getSelectionModel().getSelectedItem();
            mainController.openFormulary("almacen", "form_insertar_pieza", "editar pieza", 480, 360, EditarPiezaFormWare.class, this, pieza);
        } else {
            alert.setHeaderText("ELIGE UNA PIEZA");
            alert.showAndWait();
        }

        // si se ha completado la operacion, reiniciar listas observables
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            Func_Reboot_ObserPiezas();
            TablV_Piezas.getSelectionModel().clearSelection();
        }
    }

    // METODO BORRAR PIEZA

    private void Func_Delete_Pieza(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.WARNING);

        // (intentar) ejecutar borrado
        boolean selected = !(TablV_Piezas.getSelectionModel().isEmpty());
        if (selected) {
            pieza = TablV_Piezas.getSelectionModel().getSelectedItem();
            String nombre = pieza.getNombre();
            boolean completed = daoPieza.delete(pieza);
            if (completed){
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("La pieza " + nombre + " ha sido eliminada de la base de datos.");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText("No se puede eliminar pieza " + nombre + " porque tiene asignadas 1 o más ventas/averías");
            }
        } else {
            alert.setHeaderText("ELIGE UNA PIEZA");
        }

        // mostrar alert
        alert.showAndWait();

        // si se ha completado la operacion, reiniciar listas observables
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            Func_Reboot_ObserPiezas();
            TablV_Piezas.getSelectionModel().clearSelection();
        }
    }

    // METODO REINICIAR LISTA PIEZAS

    public void Func_Reboot_ObserPiezas(){
        // recopilar todas las piezas
        listPiezas = daoPieza.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 32 (Insertar Pieza)
        // 33 (Cambiar Proveedor Pieza)
        // 34 (Modificar Pieza)
        // 35 (Eliminar Pieza)
        boolean prohibited = (!App.checkPermiso(32) && !App.checkPermiso(33) && !App.checkPermiso(34) && !App.checkPermiso(35));
        if (prohibited) {
            listPiezas.removeIf(pieza -> pieza.getId() == 0);
            //listaTipos.removeIf(tipo -> tipo.getId() == 0);
            //listaProveedores.removeIf(prov -> prov.getId() == 0);
        }

        // actualizar listas observables
        obserPiezas.clear();
        obserPiezas.setAll(listPiezas);

        // refrescar TableView
        TablV_Piezas.refresh();
    }

}
