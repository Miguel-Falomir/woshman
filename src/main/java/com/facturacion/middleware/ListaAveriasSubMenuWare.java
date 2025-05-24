package com.facturacion.middleware;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import com.App;
import com.almacen.controller.DAO_Pieza;
import com.empleados.controller.DAO_Empleado;
import com.empleados.model.Empleado;
import com.facturacion.controller.DAO_Averia;
import com.facturacion.controller.DAO_Cliente;
import com.facturacion.controller.DAO_Estado_Averia;
import com.facturacion.controller.DAO_Tipo_Averia;
import com.facturacion.model.Averia;
import com.facturacion.model.Estado_Averia;
import com.facturacion.model.Tipo_Averia;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;
import com.vehiculos.controller.DAO_Vehiculo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Averia averia;

    // DAOs

    private DAO_Averia daoAveria;
    private DAO_Empleado daoEmpleado;
    private DAO_Estado_Averia daoEstado;
    private DAO_Tipo_Averia daoTipo;
    private DAO_Vehiculo daoVehiculo;
    private DAO_Cliente daoCliente;
    private DAO_Pieza daoPieza;

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

    public DAO_Vehiculo getDaoVehiculo(){
        return daoVehiculo;
    }

    public void setDaoVehiculo(DAO_Vehiculo daoVehiculo){
        this.daoVehiculo = daoVehiculo;
    }

    public DAO_Cliente getDaoCliente(){
        return daoCliente;
    }

    public void setDaoCliente(DAO_Cliente daoCliente){
        this.daoCliente = daoCliente;
    }

    public DAO_Pieza getDaoPieza(){
        return daoPieza;
    }

    public void setDaoPieza(DAO_Pieza daoPieza){
        this.daoPieza = daoPieza;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Agregar;

    @FXML
    private Button Buton_Asignar;

    @FXML
    private Button Buton_Borrar;

    @FXML
    private Button Buton_Consultar;

    @FXML
    private Button Buton_Resolver;

    @FXML
    private Button Buton_Clear_Entrada_Min;

    @FXML
    private Button Buton_Clear_Entrada_Max;

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
    private TextField Input_Nombre_Cliente;

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
    private TableColumn<Averia, String> TVcol_Estado;

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
        daoVehiculo = (DAO_Vehiculo) daoHashMap.get("vehiculo");
        daoCliente = (DAO_Cliente) daoHashMap.get("cliente");
        daoPieza = (DAO_Pieza) daoHashMap.get("pieza");

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
            (lambda.getValue().getEmpleado().getId() == 0 || lambda.getValue().getEstado().getId() == 0) ? "" : (lambda.getValue().getEmpleado().getNombre() + " " + lambda.getValue().getEmpleado().getApellidos())
        ));
        TVcol_Vehiculo.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getVehiculo().getMatricula() + " [" + lambda.getValue().getVehiculo().getModelo().getMarca().getNombre() + " " + lambda.getValue().getVehiculo().getModelo().getNombre() + "]"
        ));
        TVcol_Cliente.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getCliente().getNombre() + " " + lambda.getValue().getCliente().getApellidos()
        ));
        TVcol_Estado.setCellValueFactory(lambda -> new SimpleStringProperty(
            lambda.getValue().getEstado().getNombre()
        ));
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
        Empleado clearEmpleadoSelection = new Empleado(-1, "Elegir Empleado");
        listEmpleados.removeFirst();
        listEmpleados.addFirst(clearEmpleadoSelection);
        obserEmpleados = FXCollections.observableArrayList(listEmpleados);
        Combo_Empleados.setItems(obserEmpleados);
        Combo_Empleados.getSelectionModel().select(clearEmpleadoSelection);

        // asignar estados a 'Combo_Estados'
        Estado_Averia clearEstadoSelection = new Estado_Averia(-1, "Elegir Estado Av.");
        listEmpleados.removeFirst();
        listEstados.addFirst(clearEstadoSelection);
        obserEstados = FXCollections.observableArrayList(listEstados);
        Combo_Estados.setItems(obserEstados);
        Combo_Estados.getSelectionModel().select(clearEstadoSelection);

        // asignar tipos a 'Combo_Tipos'
        Tipo_Averia clearTipoSelection = new Tipo_Averia(-1, "Elegir Tipo Av.");
        listTipos.removeFirst();
        listTipos.addFirst(clearTipoSelection);
        obserTipos = FXCollections.observableArrayList(listTipos);
        Combo_Tipos.setItems(obserTipos);
        Combo_Tipos.getSelectionModel().select(clearTipoSelection);

        // asignar manejadores de eventos
        // al escribir en 'Input_Correo_Cliente', filtrar lista 'filterAverias'
        Input_Nombre_Cliente.textProperty().addListener((obs, oldSelection, newSelection) -> {
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
        // al pulsar 'Buton_Agregar', se abre el formulario para agregar averias
        Buton_Agregar.setOnAction((action) -> {
            mainController.openFormulary("facturacion", "form_insertar_averia", "Insertar Avería", 480, 360, InsertarAveriaFormWare.class, this, null);
        });
        // al pulsar 'Buton_Asignar', se abre el formulario para asignar averias a empleados
        Buton_Asignar.setOnAction((action) -> {
            mainController.openFormulary("facturacion", "form_asignar_averia", "Asignar Avería", 480, 360, AsignarAveriaFormWare.class, this, TablV_Averia.getSelectionModel().getSelectedItem());
        });
        // al pulsar 'Buton_Borrar', se elimina la averia seleccionada
        Buton_Borrar.setOnAction((action) -> {
            Func_Delete_Averia();
        });
        // al pulsar 'Buton_Resolver', se abre el formulario para resolver averias
        Buton_Resolver.setOnAction((action) -> {
            mainController.openFormulary("facturacion", "form_resolver_averia", "Resolver Avería", 480, 360, ResolverAveriaFormWare.class, this, TablV_Averia.getSelectionModel().getSelectedItem());
        });
        // al pulsar 'Buton_Consultar', se muestra la solucion de la averia
        Buton_Consultar.setOnAction((action) -> {
            mainController.openFormulary("facturacion", "form_consultar_averia", "Consultar Avería", 480, 360, ConsultarAveriaFormWare.class, this, TablV_Averia.getSelectionModel().getSelectedItem());
        });
        // al pulsar 'Buton_Clear_Entrada_Min', se limpia la fecha de 'Dpick_Entrada_Min'
        Buton_Clear_Entrada_Min.setOnAction((action) -> {
            Dpick_Entrada_Min.setValue(null);
            Func_Calculate_Predicate();
        });
        // al pulsar 'Buton_Clear_Entrada_Max', se limpia la fecha de 'Dpick_Entrada_Max'
        Buton_Clear_Entrada_Max.setOnAction((action) -> {
            Dpick_Entrada_Max.setValue(null);
            Func_Calculate_Predicate();
        });
        // al seleccionar averia en 'TablV_Averia':
        TablV_Averia.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // deshabilitar botones
                Buton_Asignar.setDisable(true);
                Buton_Resolver.setDisable(true);
                Buton_Consultar.setDisable(true);
                switch (newSelection.getEstado().getId()) {
                    case 0: // si averia.estado.id == 0, se habilita 'Buton_Asignar
                        Buton_Asignar.setDisable(false);
                        break;
                    case 1: // si averia.estado.id != 1, se habilita 'Buton_Resolver'
                        Buton_Resolver.setDisable(false);
                        break;
                    case 2: // si averia.estado.id != 2, se habilita 'Buton_Consultar'
                        Buton_Consultar.setDisable(false);
                        break;
                    default:
                        break;
                }
                // habilitar 'Buton_Borrar'
                Buton_Borrar.setDisable(false);
            }
        });
        // al seleccionar fecha en 'Dpick_Entrada_Min', filtrar lista 'filterAverias'
        Dpick_Entrada_Min.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            // variables auxiliares
            LocalDate max = Dpick_Entrada_Max.getValue();
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
                        Dpick_Entrada_Min.setValue(oldSelection);
                        alert.setHeaderText("ERROR FECHAS");
                        alert.setContentText("Fecha mínima no puede ser posterior a fecha máxima");
                        alert.showAndWait();
                    }
                } else {
                    Dpick_Entrada_Min.setValue(oldSelection);
                    alert.setHeaderText("FECHA INVÁLIDA");
                    alert.setContentText("La fecha introducida es posterior al día de hoy");
                    alert.showAndWait();
                }
            }
        });
        // al seleccionar fecha en 'Dpick_Entrada_Max', filtrar lista 'filterAverias'
        Dpick_Entrada_Max.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            // variables auxiliares
            LocalDate min = Dpick_Entrada_Min.getValue();
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
                        Dpick_Entrada_Max.setValue(oldSelection);
                        alert.setHeaderText("ERROR FECHAS");
                        alert.setContentText("Fecha máxima no puede ser anterior a fecha mínima");
                        alert.showAndWait();
                    }
                } else {
                    Dpick_Entrada_Min.setValue(oldSelection);
                    alert.setHeaderText("FECHA INVÁLIDA");
                    alert.setContentText("La fecha introducida es posterior al día de hoy");
                    alert.showAndWait();
                }
            }
        });

        // deshabilitar elementos UI segun permisos
        Buton_Asignar.setDisable(true);
        Buton_Resolver.setDisable(true);
        Buton_Consultar.setDisable(true);
        Buton_Borrar.setDisable(true);
        if (App.checkRol(1)){
            Buton_Asignar.setManaged(false);
        }
        if (!(App.checkPermiso(1))) {
            Buton_Agregar.setManaged(false);
        }
        if (!(App.checkPermiso(2))) {
            Buton_Resolver.setManaged(false);
        }
        if (!(App.checkPermiso(3))) {
            Buton_Borrar.setManaged(false);
        }
    }

    // METODO CALCULAR PREDICADO

    private void Func_Calculate_Predicate(){
        // capturar datos entrada
        String nombre = Input_Nombre_Cliente.getText();
        Empleado empleado = Combo_Empleados.getSelectionModel().getSelectedItem();
        Estado_Averia estado = Combo_Estados.getSelectionModel().getSelectedItem();
        Tipo_Averia tipo = Combo_Tipos.getSelectionModel().getSelectedItem();
        LocalDate min = Dpick_Entrada_Min.getValue();
        LocalDate max = Dpick_Entrada_Max.getValue();

        // definir predicado basico
        Predicate<Averia> pred = (lambda -> lambda.getId() > -1);
        
        // aplicar predicados adicionales segun valores no vacios
        if (!(nombre.equals(""))) { // nombre
            pred = pred.and(i -> {
                String str = i.getCliente().getNombre() + " " + i.getCliente().getApellidos();
                return str.contains(nombre);
            });
        }
        if (empleado.getId() > -1) { // empleado
            pred = pred.and(i -> i.getEmpleado().equals(empleado));
        }
        if (estado.getId() > -1) { // estado
            pred = pred.and(i -> i.getEstado().equals(estado));
        }
        if (tipo.getId() > -1) { // tipo
            pred = pred.and(i -> i.getTipo().equals(tipo));
        }
        if (min != null && max != null) { // fechas
            pred = pred.and(i -> !(i.getEntrada().isAfter(max)) && !(i.getEntrada().isBefore(min)));
        } else if (min != null) {
            pred = pred.and(i -> !(i.getEntrada().isBefore(min)));
        } else if (max != null) {
            pred = pred.and(i -> !(i.getEntrada().isAfter(max)));
        } else {
            System.out.println("No se aplican filtros de fechas");
        }

        // implementar predicado en 'filterAverias'
        filterAverias.setPredicate(pred);
        
    }

    // METODO INSERTAR AVERIA

    // METODO RESOLVER AVERIA

    // METODO CONSULTAR AVERIA

    // METODO BORRAR AVERIA

    private void Func_Delete_Averia(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.NONE);

        // comprobar seleccion
        boolean selected = !(TablV_Averia.getSelectionModel().isEmpty());
        if (selected) {
            // tomar valores
            averia = TablV_Averia.getSelectionModel().getSelectedItem();
            // pregunta de seguridad
            alert.setAlertType(AlertType.CONFIRMATION);
            alert.setHeaderText("¿ESTÁ SEGURO?");
            alert.setContentText("Esta acción borrará la avería seleccionada");
            alert.showAndWait();
            boolean confirm = alert.getResult().equals(ButtonType.OK);
            if (confirm) {
                // (intentar) ejecutar borrado
                boolean completed = daoAveria.delete(averia);
                if (completed){
                    alert.setAlertType(AlertType.INFORMATION);
                    alert.setHeaderText("OPERACIÓN COMPLETADA");
                    alert.setContentText("La avería ha sido eliminada de la base de datos.");
                } else {
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("ERROR SQL");
                    alert.setContentText("No se puede eliminar la avería");
                }
                // reiniciar lista piezas
                alert.showAndWait();
                Func_Reboot_ObserAverias();
            } else {
                // mostrar advertencia pieza no elegida
                alert.setAlertType(AlertType.WARNING);
                alert.setHeaderText("ELIGE UNA AVERÍA");
                alert.setContentText("");
            }
        }
    }

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
        
        // limpiar filtros
        Input_Nombre_Cliente.setText("");
        Combo_Empleados.getSelectionModel().selectFirst();
        Combo_Estados.getSelectionModel().selectFirst();
        Combo_Tipos.getSelectionModel().selectFirst();

        // refrescar TableView
        TablV_Averia.refresh();
    }
}
