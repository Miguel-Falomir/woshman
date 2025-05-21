package com.facturacion.middleware;

import java.sql.Date;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.HashMap;
import java.util.List;

import com.App;
import com.empleados.controller.DAO_Empleado;
import com.empleados.model.Empleado;
import com.facturacion.controller.DAO_Averia;
import com.facturacion.controller.DAO_Estado_Averia;
import com.facturacion.controller.DAO_Tipo_Averia;
import com.facturacion.model.Averia;
import com.facturacion.model.Estado_Averia;
import com.facturacion.model.Tipo_Averia;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListaAveriasSubMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Averia> obserAverias;
    private FilteredList<Averia> filterAverias;
    private ObservableList<Empleado> obserEmpleados;
    private ObservableList<Estado_Averia> obserEstados;
    private ObservableList<Tipo_Averia> obserTipos;
    private List<Averia> listAverias;
    private List<Empleado> listEmpleados;
    private List<Estado_Averia> listEstados;
    private List<Tipo_Averia> listTipos;

    // DAOs

    private DAO_Averia daoAveria;
    private DAO_Empleado daoEmpleado;
    private DAO_Estado_Averia daoEstado;
    private DAO_Tipo_Averia daoTipo;

    // CONSTRUCTORES

    public ListaAveriasSubMenuWare(){}

    public ListaAveriasSubMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
    }

    // GETTERS Y SETTERS

    public DAO_Averia getDaoAveria() {
        return daoAveria;
    }

    public void setDaoAveria(DAO_Averia daoAveria) {
        this.daoAveria = daoAveria;
    }

    public DAO_Empleado getDaoEmpleado() {
        return daoEmpleado;
    }

    public void setDaoEmpleado(DAO_Empleado daoEmpleado) {
        this.daoEmpleado = daoEmpleado;
    }

    public DAO_Estado_Averia getDaoEstado() {
        return daoEstado;
    }

    public void setDaoEstado(DAO_Estado_Averia daoEstado) {
        this.daoEstado = daoEstado;
    }

    public DAO_Tipo_Averia getDaoTipo() {
        return daoTipo;
    }

    public void setDaoTipo(DAO_Tipo_Averia daoTipo) {
        this.daoTipo = daoTipo;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Agregar;

    @FXML
    private Button Buton_Borrar;

    @FXML
    private Button Buton_Consultar;

    @FXML
    private Button Buton_Resolver;

    @FXML
    private ComboBox<Empleado> Combo_Empleados;

    @FXML
    private ComboBox<Estado_Averia> Combo_Estados;

    @FXML
    private ComboBox<Tipo_Averia> Combo_Tipos;

    @FXML
    private DatePicker Dpick_Entrada_Min;

    @FXML
    private DatePicker Dpick_Entrada_Max;

    @FXML
    private TextField Input_Correo_Cliente;

    @FXML
    private TextField Input_Matricula_Vehiculo;

    @FXML
    private TableView<Averia> TablV_Averia;

    @FXML
    private TableColumn<Averia, String> TVcol_Cliente;

    @FXML
    private TableColumn<Averia, String> TVcol_Descripcion;

    @FXML
    private TableColumn<Averia, String> TVcol_Empleado;

    @FXML
    private TableColumn<Averia, Date> TVcol_Fecha_Entrada;

    @FXML
    private TableColumn<Averia, Date> TVcol_Fecha_Salida;

    @FXML
    private TableColumn<Averia, Float> TVcol_Precio;

    @FXML
    private TableColumn<Averia, String> TVcol_Vehiculo;

    // EVENTOS

    // METODO INICIALIZAR

    public void initialize(){
        // variables internas
        boolean prohibited;
        Integer id;

        // inicializar DAOs
        daoAveria = (DAO_Averia) daoHashMap.get("averia");
        daoEmpleado = (DAO_Empleado) daoHashMap.get("empleado");
        daoEstado = (DAO_Estado_Averia) daoHashMap.get("estado");
        daoTipo = (DAO_Tipo_Averia) daoHashMap.get("tipo");

        // recopilar Averias, Empleados, Estados y Tipos
        if (App.checkRol(1)){
            id = mainController.getApp().getUser().getId();
            listAverias = daoAveria.searchByUser(id);
        } else {
            listAverias = daoAveria.searchAll();
        }
        listEmpleados = daoEmpleado.searchAll();
        listEstados = daoEstado.searchAll();
        listTipos = daoTipo.searchAll();

        // solo 'admin' puede leer objetos undefined
        prohibited = !(App.checkRol(0));
        if (prohibited) {
            listAverias.removeIf(i -> i.getId() == 0);
            listEmpleados.removeIf(i -> i.getId() == 0);
            listTipos.removeIf(i -> i.getId() == 0);
        }

        // inicializar TableColumns
        TVcol_Empleado.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getEmpleado().getNombre() + " " + lambda.getValue().getEmpleado().getApellidos()
        ));
        TVcol_Vehiculo.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getVehiculo().getMatricula() + " [" + lambda.getValue().getVehiculo().getModelo().getMarca().getNombre() + " " + lambda.getValue().getVehiculo().getModelo().getNombre() + "]"
        ));
        TVcol_Cliente.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getCliente().getNombre() + " " + lambda.getValue().getCliente().getApellidos()
        ));
        TVcol_Precio.setCellValueFactory(
            new PropertyValueFactory<Averia, Float>("precio")
        );
        TVcol_Fecha_Entrada.setCellValueFactory(
            new PropertyValueFactory<Averia, Date>("entrada")
        );
        TVcol_Fecha_Salida.setCellValueFactory(
            new PropertyValueFactory<Averia, Date>("salida")
        );
        TVcol_Descripcion.setCellValueFactory(
            new PropertyValueFactory<Averia, String>("descripcion")
        );

        // inicializar Tableview
        TablV_Averia.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        TablV_Averia.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // asignar averias a 'TablV_Averias'
        obserAverias = FXCollections.observableArrayList(listAverias);
        filterAverias = new FilteredList<Averia>(obserAverias);
        TablV_Averia.setItems(filterAverias);

        // asignar empleados a 'Combo_Empleados'
        obserEmpleados = FXCollections.observableArrayList(listEmpleados);
        Empleado clearEmpleadoSelection = new Empleado(-1, "Elegir Empleado");
        obserEmpleados.addFirst(clearEmpleadoSelection);
        Combo_Empleados.setItems(obserEmpleados);
        Combo_Empleados.getSelectionModel().select(clearEmpleadoSelection);

        // asignar estados a 'Combo_Estados'
        obserEstados = FXCollections.observableArrayList(listEstados);
        Estado_Averia clearEstadoSelection = new Estado_Averia(-1, "Elegir Estado Av.");
        obserEstados.addFirst(clearEstadoSelection);
        Combo_Estados.setItems(obserEstados);
        Combo_Estados.getSelectionModel().select(clearEstadoSelection);

        // asignar tipos a 'Combo_Tipos'
        obserTipos = FXCollections.observableArrayList(listTipos);
        Tipo_Averia clearTipoSelection = new Tipo_Averia(-1, "Elegir Tipo Av.");
        obserTipos.addFirst(clearTipoSelection);
        Combo_Tipos.setItems(obserTipos);
        Combo_Tipos.getSelectionModel().select(clearTipoSelection);

        // asignar manejadores de eventos
        // al escribir en 'Input_Correo_Cliente', filtrar lista 'filterAverias'
        Input_Correo_Cliente.textProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection != oldSelection){
                Func_Calculate_Predicate();
            }
        });
        // al escribir en 'Input_Matricula_Vehiculo', filtrar lista 'filterAverias'
        Input_Matricula_Vehiculo.textProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection != oldSelection){
                Func_Calculate_Predicate();
            }
        });
        // al seleccionar empleado en 'Combo_Empleados', filtrar lista 'filterAverias'
        Combo_Empleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Func_Calculate_Predicate();
            }
        });
        // al seleccionar estado en 'Combo_Estados', filtrar lista 'filterAverias'
        Combo_Estados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Func_Calculate_Predicate();
            }
        });
        // al seleccionar tipo en 'Combo_tipos', filtrar lista 'filterAverias'
        Combo_Tipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                Func_Calculate_Predicate();
            }
        });
        // al seleccionar fecha en 'Dpick_Entrada_Min', filtrar lista 'filterAverias'
        Dpick_Entrada_Min.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            ChronoLocalDate maxFecha = Dpick_Entrada_Max.getValue();
            boolean notEmpty = newSelection != null;
            boolean notAfterMax = !(newSelection.isAfter(maxFecha));
            if (notEmpty && notAfterMax){
                Func_Calculate_Predicate();
            } else if (!notAfterMax){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setHeaderText("ERROR FECHAS");
                alert.setContentText("La fecha mínima no puede ser posterior a la fecha máxima");
                alert.showAndWait();
            }
        });
        // al seleccionar fecha en 'Dpick_Entrada_Max', filtrar lista 'filterAverias'
        Dpick_Entrada_Max.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            LocalDate minFecha = Dpick_Entrada_Min.getValue();
            boolean notEmpty = newSelection != null;
            boolean notBeforeMin = !(newSelection.isBefore(minFecha));
            if (notEmpty && notBeforeMin) {
                Func_Calculate_Predicate();
            } else if (!notBeforeMin) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setHeaderText("ERROR FECHAS");
                alert.setContentText("La fecha máxima no puede ser posterior a la fecha minima");
                alert.showAndWait();
            }
        });
    }

    // METODO CALCULAR PREDICADO

    private void Func_Calculate_Predicate(){
        String correo = Input_Correo_Cliente.getText();
        String matricula = Input_Matricula_Vehiculo.getText();
        Empleado empleado = Combo_Empleados.getSelectionModel().getSelectedItem();
        Estado_Averia estado = Combo_Estados.getSelectionModel().getSelectedItem();
        Tipo_Averia tipo = Combo_Tipos.getSelectionModel().getSelectedItem();
        LocalDate min = Dpick_Entrada_Min.getValue();
        LocalDate max = Dpick_Entrada_Max.getValue();
        System.out.println(correo + "\n" + matricula + "\n" + empleado + "\n" + estado + "\n" + tipo + "\n" + min + "\n" + max);
    }

    // METODO INSERTAR AVERIA

    // METODO RESOLVER AVERIA

    // METODO CONSULTAR AVERIA

    // METODO BORRAR AVERIA

    // METODO REFRESCAR LISTA

    public void Func_Reboot_ObserAverias(){
        // recopilar Averias, Empleados, Estados y Tipos
        if (App.checkRol(1)){
            Integer id = mainController.getApp().getUser().getId();
            listAverias = daoAveria.searchByUser(id);
        } else {
            listAverias = daoAveria.searchAll();
        }
        listEmpleados = daoEmpleado.searchAll();
        listEstados = daoEstado.searchAll();
        listTipos = daoTipo.searchAll();

        // solo 'admin' puede leer objetos undefined
        boolean prohibited = !(App.checkRol(0));
        if (prohibited) {
            listAverias.removeIf(i -> i.getId() == 0);
            listEmpleados.removeIf(i -> i.getId() == 0);
            listTipos.removeIf(i -> i.getId() == 0);
        }

        // acutalizar lista observable
        obserAverias.clear();
        obserAverias.setAll(listAverias);
        obserEmpleados.clear();
        obserEmpleados.setAll(listEmpleados);
        obserEstados.clear();
        obserEstados.setAll(listEstados);
        obserTipos.clear();
        obserTipos.setAll(listTipos);

        // refrescar TableView
        TablV_Averia.refresh();
    }
}
