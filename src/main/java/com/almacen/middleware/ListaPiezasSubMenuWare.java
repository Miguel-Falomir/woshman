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
    private List<Pieza> listaPiezas;
    private List<Tipo_Pieza> listaTipos;
    private List<Proveedor> listaProveedores;
    private Pieza pieza;

    // DAOs

    DAO_Pieza daoPieza;
    DAO_Tipo_Pieza daoTipo;
    DAO_Proveedor daoProveedor;

    // CONSTRUCTORES

    public ListaPiezasSubMenuWare(){}

    public ListaPiezasSubMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
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
        listaPiezas = daoPieza.searchAll();
        //listaTipos = daoTipo.searchAll();
        //listaProveedores = daoProveedor.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 32 (Insertar Pieza)
        // 33 (Cambiar Proveedor Pieza)
        // 34 (Modificar Pieza)
        // 35 (Eliminar Pieza)
        boolean prohibited = (!App.checkPermiso(32) && !App.checkPermiso(33) && !App.checkPermiso(34) && !App.checkPermiso(35));
        if (prohibited) {
            listaPiezas.removeIf(pieza -> pieza.getId() == 0);
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

        // inicializar listas observables
        obserPiezas = FXCollections.observableArrayList(listaPiezas);
        filterPiezas = new FilteredList<>(obserPiezas);
        TablV_Piezas.setItems(filterPiezas);

        // agregar manejadores de eventos
        // al deslizar 'Slide_Cantidad', 'Label_Cantidad' cambia de valor
        Slide_Cantidad.valueProperty().addListener( new ChangeListener<Number>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Label_Cantidad.textProperty().setValue( String.valueOf( newValue.intValue()));
                Func_Calculate_Predicate();
            };
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
        int cantidad = (int) Math.floor( Slide_Cantidad.getValue());

        // aplicar predicado
        filterPiezas.setPredicate(i -> i.getNombre().contains(regex) && i.getCantidad() >= cantidad);
    }

    // METODO INSERTAR PIEZA

    private void Func_Insert_Pieza(){
        //mainController.openFormulary("almacen", "form_insertar_pieza", 480, 360, InsertarPiezaFormWare.class, this, null);
    }

    // METODO EDITAR PIEZA

    private void Func_Update_Pieza(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.WARNING);

        // (intentar) ejecutar actualizacion
        boolean selected =!(TablV_Piezas.getSelectionModel().isEmpty());
        if (selected) {
            pieza = TablV_Piezas.getSelectionModel().getSelectedItem();
            //mainController.openFormulary("almacen", "form_insertar_pieza", 480, 360, EditarPiezaFormWare.class, this, pieza);
        } else {
            alert.setHeaderText("ELIGE UN CLIENTE");
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
                alert.setContentText("La pieza " + nombre + " ha sido eliminado de la base de datos.");
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
        listaPiezas = daoPieza.searchAll();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 32 (Insertar Pieza)
        // 33 (Cambiar Proveedor Pieza)
        // 34 (Modificar Pieza)
        // 35 (Eliminar Pieza)
        boolean prohibited = (!App.checkPermiso(32) && !App.checkPermiso(33) && !App.checkPermiso(34) && !App.checkPermiso(35));
        if (prohibited) {
            listaPiezas.removeIf(pieza -> pieza.getId() == 0);
            //listaTipos.removeIf(tipo -> tipo.getId() == 0);
            //listaProveedores.removeIf(prov -> prov.getId() == 0);
        }

        // actualizar listas observables
        obserPiezas.clear();
        obserPiezas.setAll(listaPiezas);

        // refrescar TableView
        TablV_Piezas.refresh();
    }

}
