package com.facturacion.middleware;

import java.time.LocalDate;
import java.util.List;

import com.facturacion.controller.DAO_Averia;
import com.facturacion.model.Averia;
import com.facturacion.model.Cliente;
import com.facturacion.model.Tipo_Averia;
import com.utilities.FormWare;
import com.utilities.SubMenuWare;
import com.vehiculos.model.Vehiculo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class InsertarAveriaFormWare extends FormWare {

    // OBJETOS PUNTERO

    ListaAveriasSubMenuWare menuWare;
    DAO_Averia daoAveria;

    // OBJETOS ALMACENAR DATOS INTERNOS

    Averia averia;
    List<Cliente> listClientes = null;
    List<Vehiculo> listVehiculos = null;
    List<Tipo_Averia> listTipos = null;
    ObservableList<Cliente> obserClientes;
    ObservableList<Vehiculo> obserVehiculos;
    ObservableList<Tipo_Averia> obserTipos;

    // CONSTRUCTORES

    public InsertarAveriaFormWare(){}

    public InsertarAveriaFormWare(SubMenuWare menuWare){
        this.averia = new Averia();
        this.menuWare = (ListaAveriasSubMenuWare) menuWare;
        this.daoAveria = this.menuWare.getDaoAveria();
        this.listClientes = this.menuWare.getDaoCliente().searchAll();
        this.listVehiculos = this.menuWare.getDaoVehiculo().searchAll();
        this.listTipos = this.menuWare.getDaoTipo().searchAll();
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Aceptar;

    @FXML
    private Button Buton_Cancelar;

    @FXML
    private ComboBox<Cliente> Combo_Cliente;

    @FXML
    private ComboBox<Tipo_Averia> Combo_Tipo_Averia;

    @FXML
    private ComboBox<Vehiculo> Combo_Vehiculo;

    @FXML
    private DatePicker Dpick_Fecha_Entrada;

    @FXML
    private TextArea Input_Descripcion;

    // EVENTOS

    // METODO INICIALIZAR

    public void initialize(){
        // inicializar listas observables...
        // obserClientes
        Cliente clearClienteSelection = new Cliente(-1,"Cliente");
        listClientes.removeFirst();
        listClientes.addFirst(clearClienteSelection);
        obserClientes = FXCollections.observableArrayList(listClientes);
        // obserVehiculos
        Vehiculo clearVehiculoSelection = new Vehiculo(-1, "Vehiculo");
        listVehiculos.removeFirst();
        listVehiculos.addFirst(clearVehiculoSelection);
        obserVehiculos = FXCollections.observableArrayList(listVehiculos);
        // obserTipos
        Tipo_Averia clearTipoSelection = new Tipo_Averia(-1, "Tipo");
        listTipos.removeFirst();
        listTipos.addFirst(clearTipoSelection);
        obserTipos = FXCollections.observableArrayList(listTipos);
        // ...y asignar a elementos ComboBox
        Combo_Cliente.setItems(obserClientes);
        Combo_Cliente.getSelectionModel().select(clearClienteSelection);
        Combo_Vehiculo.setItems(obserVehiculos);
        Combo_Vehiculo.getSelectionModel().select(clearVehiculoSelection);
        Combo_Tipo_Averia.setItems(obserTipos);

        // asignar manejadores de eventos a elementos UI
        // al escribir en 'Input_Descripcion', texto se asigna a 'averia.descripcion'
        Input_Descripcion.setOnKeyTyped((action) -> {
            String str = Input_Descripcion.getText();
            if (!(str.equals(""))) {
                averia.setDescripcion(str);
            }
        });
        // al seleccionar fecha en 'Dpick_Fecha_Entrada', esta se asigna a 'averia.entrada'
        Dpick_Fecha_Entrada.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                averia.setEntrada(newSelection);
            }
        });
        // al seleccionar item en 'Combo_Cliente', este se asigna a 'averia.cliente'
        Combo_Cliente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                averia.setCliente(newSelection);
            }
        });
        // al seleccionar item en 'Combo_Tipo_Averia', este se asigna a 'averia.tipo'
        Combo_Tipo_Averia.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                averia.setTipo(newSelection);
            }
        });
        // al seleccionar item en 'Combo_Vehiculo', este se asigna a 'averia.vehiculo'
        Combo_Vehiculo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                averia.setVehiculo(newSelection);
            }
        });
        // al pulsar 'Buton_Aceptar', se inserta pieza en base de datos
        Buton_Aceptar.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Func_Insert_Averia();
            }
        });
        // al pulsar 'Buton_Cancelar', se cierra el formulario
        Buton_Cancelar.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // mostrar mensaje cancelacion
                Alert alert = new Alert(AlertType.WARNING);
                alert.setHeaderText("OPERACIÓN CANCELADA");
                alert.setContentText("");
                alert.showAndWait();
                // cerrar ventana
                Func_Close();
            }
        });
    }

    // METODO CERRAR FORMULARIO

    private void Func_Close(){
        Stage thisStage = (Stage) Buton_Cancelar.getScene().getWindow();
        thisStage.close();
    }

    // METODO INSERTAR AVERIA

    private void Func_Insert_Averia(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.ERROR);

        // forzar que averia no tenga ningun empleado
        //averia.setEmpleado(null);

        // comprobar que se han rellenado todos los campos obligatorios
        boolean vehiculoMissing = (averia.getVehiculo() == null || averia.getVehiculo().getId() <= -1);
        boolean clienteMissing = (averia.getCliente() == null || averia.getCliente().getId() <= -1);
        boolean tipoMissing = (averia.getTipo() == null || averia.getTipo().getId() <= -1);
        boolean entradaMissing = (averia.getEntrada() == null);
        boolean entradaFuture = (entradaMissing) ? false : averia.getEntrada().isAfter(LocalDate.now());
        boolean descripcionMissing = (averia.getDescripcion() == null) ? true : averia.getDescripcion().length() <= 0;
        if (vehiculoMissing) { // falta 'Cliente'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Vehiculo' es obligatorio.");
        } else if (clienteMissing) { // falta 'Vehiculo'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Cliente' es obligatorio.");
        } else if (tipoMissing) { // falta 'Tipo Averia'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Tipo' es obligatorio.");
        } else if (entradaMissing) { // falta 'Fecha Entrara'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Fecha Entrada' es obligatorio.");
        } else if (entradaFuture) { // no se admiten fechas del futuro
            alert.setHeaderText("FECHA INVÁLIDA");
            alert.setContentText("La fecha introducida es posterior al día de hoy.");
        } else if (descripcionMissing) { // falta 'Descripcion'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Descripcion' es obligatorio.");
        } else { // (intentar) ejecutar insercion
            boolean completed = daoAveria.insert(averia);
            if (completed) {
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("La avería se ha insertado en la base de datos.");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText("Se ha producido un error en la base de datos.");
            }
        }

        // pase lo que pase, mostrarlo mediante la alert
        alert.showAndWait();

        // si se ha completado la operacion, reiniciar lista y cerrar ventana
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            menuWare.Func_Reboot_ObserAverias();
            Func_Close();
        }
    }
}
