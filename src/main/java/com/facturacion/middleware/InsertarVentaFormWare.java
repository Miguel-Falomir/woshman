package com.facturacion.middleware;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.App;
import com.almacen.controller.DAO_Pieza;
import com.almacen.model.Pieza;
import com.facturacion.controller.DAO_Cliente;
import com.facturacion.controller.DAO_Venta;
import com.facturacion.model.Cliente;
import com.facturacion.model.Venta;
import com.utilities.FormWare;
import com.utilities.SubMenuWare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class InsertarVentaFormWare extends FormWare {

    // OBJETOS PUNTERO

    ListaVentasSubMenuWare menuWare;
    DAO_Venta daoVenta;

    // OBJETOS ALMACENAR DATOS INTERNOS

    private Venta venta;
    private Pieza pieza;
    private int max;
    private List<Cliente> listClientes;
    private List<Pieza> listPiezas;
    private ObservableList<Cliente> obserClientes;
    private ObservableList<Pieza> obserPiezas;

    // CONSTRUCTORES

    public InsertarVentaFormWare(){}

    public InsertarVentaFormWare(SubMenuWare menuWare){
        this.venta = new Venta();
        this.menuWare = (ListaVentasSubMenuWare) menuWare;
        this.daoVenta = this.menuWare.getDaoVenta();
        this.listPiezas = this.menuWare.getDaoPieza().searchAll();
        this.listClientes = this.menuWare.getDaoCliente().searchAll();
    }

    public InsertarVentaFormWare(DAO_Venta daoVenta, DAO_Pieza daoPieza, DAO_Cliente daoCliente){
        this.venta = new Venta();
        this.menuWare = null;
        this.daoVenta = daoVenta;
        this.listClientes = daoCliente.searchAll();
        this.listPiezas = daoPieza.searchAll();
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Aceptar;

    @FXML
    private Button Buton_Cancelar;

    @FXML
    private Button Buton_Minus;

    @FXML
    private Button Buton_Plus;

    @FXML
    private ComboBox<Pieza> Combo_Pieza;

    @FXML
    private ComboBox<Cliente> Combo_Cliente;

    @FXML
    private TextField Input_Cantidad_Pieza;

    // EVENTOS

    // METODO INICIALIZAR

    public void initialize(){
        // incializar listas observables
        if (!(App.checkRol(0))) {
            listClientes.removeIf(i -> i.getId() == 0);
            listPiezas.removeIf(i -> i.getId() == 0);
        }
        obserClientes = FXCollections.observableArrayList(listClientes);
        obserPiezas = FXCollections.observableArrayList(listPiezas);

        // asignar listas observables a elementos ComboBox
        Combo_Cliente.setItems(obserClientes);
        Combo_Pieza.setItems(obserPiezas);

        // asignar eventos a elementos UI
        // al pulsar 'Buton_Plus', pieza.cantidad += 1
        Buton_Plus.setOnAction((action) -> {
            int number = Integer.parseInt(Input_Cantidad_Pieza.getText());
            if (number < max) {
                number++;
                if (pieza != null) {pieza.setCantidad(number);}
            }
        });
        // al pulsar 'Buton_Minus', pieza.cantidad -= 1
        Buton_Minus.setOnAction((action) -> {
            int number = Integer.parseInt(Input_Cantidad_Pieza.getText());
            if (number > 1) {
                number--;
                if (pieza != null) {pieza.setCantidad(number);}
            }
        });
        // al seleccionar item 'Combo_Cliente', asignar a 'venta.cliente'
        Combo_Cliente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                venta.setCliente(newSelection);
            } else {
                venta.setCliente(oldSelection);
            }
        });
        // al seleccionar item 'Combo_Pieza', asignar a 'venta.pieza' y reiniciar 'Input_Cantidad_Pieza'
        Combo_Pieza.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                pieza = newSelection;
                pieza.setCantidad(1);
                max = pieza.getCantidad();
                Input_Cantidad_Pieza.setText(String.valueOf(1));
                venta.setListPiezas( new ArrayList<>( Arrays.asList( pieza )));
            } else {
                venta.setListPiezas( new ArrayList<>( Arrays.asList( oldSelection )));
            }
        });
        // al pulsar 'Buton_Cancelar', invocar 'Func_Close()'
        Buton_Cancelar.setOnAction((action) -> {
            Func_Close();
        });
        // al pulsar 'Buton_Aceptar', invocar 'Func_Insert_Venta()'
        Buton_Aceptar.setOnAction((action) -> {
            Func_Insert_Venta();
        });
    }

    // METODO CERRAR FORMULARIO

    private void Func_Close(){
        Stage thisStage = (Stage) Buton_Cancelar.getScene().getWindow();
        thisStage.close();
    }

    // METODO INSERTAR VENTA

    private void Func_Insert_Venta(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.ERROR);

        // definir precio y fecha_venta
        float precio = 0.0f;
        for (Pieza pieza : venta.getListPiezas()) {
            precio += pieza.getPrecio() * pieza.getCantidad();
        }
        venta.setPrecio(precio);
        venta.setFechaVenta(LocalDate.now());

        System.out.println(venta);

        // comprobar que se han rellenado todos los campos
        boolean clienteMissing = (venta.getCliente() == null || venta.getCliente().getId() <= -1);
        boolean piezaMissing = (venta.getListPiezas().size() <= 0 || venta.getListPiezas() == null);
        if (clienteMissing) { // falta 'Cliente'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Cliente' es obligatorio.");
        } else if (piezaMissing) { // falta 'Pieza'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Pieza' es obligatorio.");
        } else {
            boolean completed = daoVenta.insert(venta);
            if (completed) {
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("La venta se ha insertado en la base de datos.");
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
            if (App.checkPermiso(12)) {menuWare.Func_Reboot_ObserVentas();}
            Func_Close();
        }
    }
}
