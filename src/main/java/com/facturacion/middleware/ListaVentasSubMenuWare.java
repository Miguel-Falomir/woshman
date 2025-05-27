package com.facturacion.middleware;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import com.App;
import com.almacen.controller.DAO_Pieza;
import com.almacen.model.Pieza;
import com.facturacion.controller.DAO_Cliente;
import com.facturacion.controller.DAO_Venta;
import com.facturacion.model.Venta;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ListaVentasSubMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private List<Venta> listVentas;
    private ObservableList<Venta> obserVentas;
    private FilteredList<Venta> filterVentas;
    private List<Pieza> listPiezas;
    private ObservableList<Pieza> obserPiezas;
    private Venta venta;

    // DAOs

    private DAO_Venta daoVenta;
    private DAO_Pieza daoPieza;
    private DAO_Cliente daoCliente;

    // CONSTRUCTORES

    public ListaVentasSubMenuWare(){}

    public ListaVentasSubMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
    }

    // GETTERS Y SETTERS

    public DAO_Venta getDaoVenta() {
        return daoVenta;
    }

    public void setDaoVenta(DAO_Venta daoVenta) {
        this.daoVenta = daoVenta;
    }

    public DAO_Pieza getDaoPieza() {
        return daoPieza;
    }

    public void setDaoPieza(DAO_Pieza daoPieza) {
        this.daoPieza = daoPieza;
    }

    public DAO_Cliente getDaoCliente(){
        return daoCliente;
    }

    public void setDaoCliente(DAO_Cliente daoCliente) {
        this.daoCliente = daoCliente;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Agregar;

    @FXML
    private Button Buton_Borrar;

    @FXML
    private Button Buton_Clear_Venta_Max;

    @FXML
    private Button Buton_Clear_Venta_Min;

    @FXML
    private Button Buton_Editar;

    @FXML
    private Button Buton_List_Piezas;

    @FXML
    private ComboBox<Pieza> Combo_Pieza;

    @FXML
    private DatePicker Dpick_Venta_Max;

    @FXML
    private DatePicker Dpick_Venta_Min;

    @FXML
    private TextField Input_Nombre_Cliente;

    @FXML
    private TableColumn<Venta, String> TVcol_Cliente;

    @FXML
    private TableColumn<Venta, String> TVcol_Fecha;

    @FXML
    private TableColumn<Venta, String> TVcol_Precio;

    @FXML
    private TableView<Venta> TablV_Ventas;

    // EVENTOS

    // INICIALIZAR
    
    public void initialize(){
        // booleano permisos
        boolean prohibited;

        // inicializar DAOs
        daoVenta = (DAO_Venta) daoHashMap.get("venta");
        daoPieza = (DAO_Pieza) daoHashMap.get("pieza");
        daoCliente = (DAO_Cliente) daoHashMap.get("cliente");

        // recopilar todas las ventas y piezas
        listVentas = daoVenta.searchAll();
        listPiezas = daoPieza.searchAll();

        // solo 'admin' puede leer objetos undefined
        prohibited = !(App.checkRol(0));
        if (prohibited) {
            listVentas.removeIf(i -> i.getId() == 0);
        }

        // inicializar TableColumns y TableView
        TVcol_Cliente.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getCliente().getNombre() + " " + lambda.getValue().getCliente().getApellidos()
        ));
        TVcol_Fecha.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getFechaVenta().toString()
        ));
        TVcol_Precio.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getPrecio().toString()
        ));
        TablV_Ventas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        TablV_Ventas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // inicializar lista observable ventas y asignar a tabla 'TablV_Ventas'
        obserVentas = FXCollections.observableArrayList(listVentas);
        filterVentas = new FilteredList<>(obserVentas);
        TablV_Ventas.setItems(filterVentas);

        // inicializar lista observable piezas y asignar a 'Combo_Piezas'
        Pieza clearPiezaSelection = new Pieza (-1, "Elegir Pieza");
        if (!App.checkRol(0)) {listPiezas.removeFirst();}
        listPiezas.addFirst(clearPiezaSelection);
        obserPiezas = FXCollections.observableArrayList(listPiezas);
        Combo_Pieza.setItems(obserPiezas);
        Combo_Pieza.getSelectionModel().select(clearPiezaSelection);

        // asignar manejadores de eventos
        // al escribir en 'Input_Nombre_Cliente', invocar 'Func_Calculate_Predicate'
        Input_Nombre_Cliente.setOnKeyTyped((action) -> {
            Func_Calculate_Predicate();
        });
        // al seleccionar item en 'Combo_Pieza', invocar 'Func_Calculate_Predicate'
        Combo_Pieza.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Func_Calculate_Predicate();
            }
        });
        // al pulsar 'Buton_Agregar', invocar 'Func_Insert_Venta'
        Buton_Agregar.setOnAction((action) -> {
            Func_Insert_Pieza();
        });
        // al pulsar 'Buton_Editar', invocar 'Func_Update_Venta'
        Buton_Editar.setOnAction((action) -> {
            Func_Update_Venta();
        });
        // al pulsar 'Buton_List_Piezas', invocar 'Func_List_Piezas'
        Buton_List_Piezas.setOnAction((action) -> {
            Func_List_Piezas();
        });
        // al pulsar 'Buton_Borrar', invocar 'Func_Delete_Venta'
        Buton_Borrar.setOnAction((action) -> {
            Func_Delete_Venta();
        });
        // al pulsar 'Buton_Clear_Venta_Max', limpiar 'Dpick_Venta_Max'
        Buton_Clear_Venta_Max.setOnAction((action) -> {
            Dpick_Venta_Max.setValue(null);
            Func_Calculate_Predicate();
        });
        // al pulsar 'Buton_Clear_Venta_Min', limpiar 'Dpick_Venta_Min'
        Buton_Clear_Venta_Min.setOnAction((action) -> {
            Dpick_Venta_Min.setValue(null);
            Func_Calculate_Predicate();
        });
        // al seleccionar fecha en 'Dpick_Venta_Min', invocar 'Func_Calculate_Predicate'
        Dpick_Venta_Min.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            LocalDate max = Dpick_Venta_Max.getValue();
            Alert alert = new Alert(AlertType.WARNING);
            boolean passed = true;
            // realizar comprobaciones antes de ejecutar filtrado
            if (newSelection != null) {
                if (!(newSelection.isAfter(LocalDate.now()))){
                    if (max != null){
                        if (newSelection.isAfter(max)) {passed = false;}
                    }
                    if (passed) {
                        Func_Calculate_Predicate();
                    } else {
                        Dpick_Venta_Min.setValue(oldSelection);
                        alert.setHeaderText("ERROR FECHAS");
                        alert.setContentText("Fecha mínima no puede ser posterior a fecha máxima");
                        alert.showAndWait();
                    }
                } else {
                    Dpick_Venta_Min.setValue(oldSelection);
                    alert.setHeaderText("FECHA INVÁLIDA");
                    alert.setContentText("La fecha introducida es posterior al día de hoy");
                    alert.showAndWait();
                }
            }
        });
        // al seleccionar fecha en 'Dpick_Venta_Max', invocar 'Func_Calculate_Predicate'
        Dpick_Venta_Max.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            LocalDate min = Dpick_Venta_Min.getValue();
            Alert alert = new Alert(AlertType.WARNING);
            boolean passed = true;
            // realizar comprobaciones antes de ejecutar filtrado
            if (newSelection != null) {
                if (!(newSelection.isAfter(LocalDate.now()))){
                    if (min != null) {
                        if (newSelection.isBefore(min)) {passed = false;}
                    }
                    if (passed) {
                        Func_Calculate_Predicate();
                    } else {
                        Dpick_Venta_Max.setValue(oldSelection);
                        alert.setHeaderText("ERROR FECHAS");
                        alert.setContentText("Fecha máxima no puede ser anterior a fecha mínima");
                        alert.showAndWait();
                    }
                } else {
                    Dpick_Venta_Max.setValue(oldSelection);
                    alert.setHeaderText("FECHA INVÁLIDA");
                    alert.setContentText("La fecha introducida es posterior al día de hoy");
                    alert.showAndWait();
                }
            }
        });
        // al seleccionar fila 'TablV_Venta', habilitar botones
        TablV_Ventas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Buton_Editar.setDisable(true);
            Buton_Borrar.setDisable(true);
            Buton_List_Piezas.setDisable(true);
            if (newSelection != null) {
                Buton_Editar.setDisable(false);
                Buton_Borrar.setDisable(false);
                Buton_List_Piezas.setDisable(false);
                venta = newSelection;
            }
        });

        // deshabilitar elementos UI
        Buton_Editar.setDisable(true);
        Buton_Borrar.setDisable(true);
        Buton_List_Piezas.setDisable(true);

        // ocultar elementos UI segun si no se tienen permisos:
        if (!App.checkPermiso(13)){ // 13) agregar venta
            Buton_Agregar.setManaged(false);
        }
        if (!App.checkPermiso(14)){ // 14) modificar venta
            Buton_Editar.setManaged(false);
        }
        if (!App.checkPermiso(15)){ // 15) eliminar venta
            Buton_Borrar.setManaged(false);
        }
    }

    // METODO CALCULAR PREDICADO

    private void Func_Calculate_Predicate(){
        // capturar datos entrada
        String nombre = Input_Nombre_Cliente.getText();
        Pieza pieza = Combo_Pieza.getSelectionModel().getSelectedItem();
        LocalDate min = Dpick_Venta_Min.getValue();
        LocalDate max = Dpick_Venta_Max.getValue();

        // definir predicado basico
        Predicate<Venta> pred = (lambda -> lambda.getId() > -1);

        // aplicar predicados adicionales segun valores no vacios
        if (!(nombre.equals(""))) { // nombre cliente
            pred = pred.and(i -> {
                String str = i.getCliente().getNombre() + " " + i.getCliente().getApellidos();
                return str.contains(nombre);
            });
        }
        if (pieza.getId() > -1) { // pieza
            pred = pred.and(i -> i.getListPiezas().contains(pieza));
        }
        if (min != null && max != null) { // fechas
            pred = pred.and(i -> !(i.getFechaVenta().isAfter(max)) && !(i.getFechaVenta().isBefore(min)));
        } else if (min != null) {
            pred = pred.and(i -> !(i.getFechaVenta().isBefore(min)));
        } else if (max != null) {
            pred = pred.and(i -> !(i.getFechaVenta().isAfter(max)));
        } else {
            System.out.println("No se aplican filtros de fechas");
        }

        // implementar predicado en 'filterAverias'
        filterVentas.setPredicate(pred);
    }

    // METODO REFRESCAR LISTA VENTAS

    public void Func_Reboot_ObserVentas(){
        // recopilar Ventas
        listVentas = daoVenta.searchAll();
        listPiezas = daoPieza.searchAll();

        // solo 'admin' puede leer objetos undefined
        boolean prohibited = !(App.checkRol(0));
        if (prohibited) {
            listPiezas.removeIf(i -> i.getId() == 0);
            listVentas.removeIf(i -> i.getId() == 0);
        }

        // actualizar lista observable
        obserVentas.clear();
        obserVentas.setAll(listVentas);

        // limpiar filtros
        Input_Nombre_Cliente.setText("");
        Combo_Pieza.getSelectionModel().selectFirst();
        Dpick_Venta_Max.setValue(null);
        Dpick_Venta_Min.setValue(null);

        // refrescar TableView
        TablV_Ventas.refresh();
    }

    // METODO INSERTAR VENTA

    private void Func_Insert_Pieza(){
        this.mainController.openFormulary("facturacion", "form_insertar_venta", "Insertar Venta", 480, 360, InsertarVentaFormWare.class, this, null);
    }

    // METODO ACTUALIZAR VENTA

    private void Func_Update_Venta(){

    }

    // METODO BORRAR VENTA

    private void Func_Delete_Venta(){

    }

    // METODO LISTAR PIEZAS VENTA

    private void Func_List_Piezas(){

    }
}
