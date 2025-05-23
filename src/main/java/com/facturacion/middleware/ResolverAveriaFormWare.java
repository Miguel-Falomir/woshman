package com.facturacion.middleware;

import java.util.List;

import com.almacen.model.Pieza;
import com.facturacion.controller.DAO_Averia;
import com.facturacion.model.Averia;
import com.utilities.FormWare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResolverAveriaFormWare extends FormWare {

    // OBJETOS PUNTERO

    private ListaAveriasSubMenuWare menuWare;
    private DAO_Averia daoAveria;

    // OBJETOS ALMACENAR DATOS INTERNOS

    private Averia averia;
    private Pieza pieza;
    private int max;
    private List<Pieza> listPiezas;
    private ObservableList<Pieza> obserPiezas;

    // CONSTRUCTORES

    public ResolverAveriaFormWare(){}

    public ResolverAveriaFormWare(ListaAveriasSubMenuWare menuWare, Averia averia){
        this.menuWare = menuWare;
        this.averia = averia;
        this.daoAveria = this.menuWare.getDaoAveria();
        this.listPiezas = this.menuWare.getDaoPieza().searchAll();
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
    private TextArea Field_Descripcion;

    @FXML
    private TextField Input_Cantidad_Pieza;

    @FXML
    private TextArea Input_Observaciones;

    @FXML
    private TextArea Input_Solucion;

    // EVENTOS

    // METODO INICIALIZAR

    public void initialize(){
        // inicializar lista observable
        obserPiezas = FXCollections.observableArrayList(listPiezas);
        listPiezas.removeIf(i -> i.getCantidad() < 1);
        Combo_Pieza.setItems(obserPiezas);

        // mostrar 'averia.descripcion' en 'Field_Descripcion'
        Field_Descripcion.setText(averia.getDescripcion());

        // asignar manejadores de eventos a elementos UI
        // al seleccionar item en 'Combo_Pieza', este se asigna a 'averia.listaPiezas'
        Combo_Pieza.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Input_Cantidad_Pieza.setText("1");
                int cantidad = Integer.parseInt(Input_Cantidad_Pieza.getText());
                max = newSelection.getCantidad();
                pieza = newSelection;
                pieza.setCantidad(cantidad);
                averia.getListaPiezas().clear();
                averia.getListaPiezas().add(pieza);
            }
        });
        // al pulsar 'Buton_Plus', incrementar cantidad
        Buton_Plus.setOnAction((action) -> {
            int cantidad = Integer.parseInt(Input_Cantidad_Pieza.getText());
            if (cantidad < max) {cantidad++;}
            Input_Cantidad_Pieza.setText(String.valueOf(cantidad));
        });
        // al pulsar 'Buton_Minus', decrementar cantidad
        Buton_Minus.setOnAction((action) -> {
            int cantidad = Integer.parseInt(Input_Cantidad_Pieza.getText());
            if (cantidad < max) {cantidad--;}
            Input_Cantidad_Pieza.setText(String.valueOf(cantidad));
        });
        // al escribir en 'Input_Solucion', asignar texto a 'averia.solucion'
        Input_Solucion.setOnKeyTyped((action) -> {
            String str = Input_Solucion.getText();
            if (!(str.equals(""))) {
                averia.setSolucion(str);
            }
        });
        // al escribir en 'Input_Observaciones', asignar texto a 'averia.observaciones'
        Input_Observaciones.setOnKeyTyped((action) -> {
            String str = Input_Observaciones.getText();
            if (!(str.equals(""))) {
                averia.setObservaciones(str);
            }
        });
        // al pulsar 'Buton_Aceptar', se inserta pieza en base de datos
        Buton_Aceptar.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Func_Update_Averia();
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

    // METODO RESOLVER AVERIA

    private void Func_Update_Averia(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.ERROR);

        // precio_averia = precio_pieza X cantidad_pieza
        Float precio = 0.0f;
        for (Pieza pieza : averia.getListaPiezas()) {
            precio += (pieza.getPrecio() * pieza.getCantidad());
        }
        averia.setPrecio(precio);

        // comprobar que se han rellenado todos los campos obligatorios
        boolean piezaMissing = (averia.getListaPiezas().isEmpty() || averia.getListaPiezas() == null);
        boolean solucionMissing = (averia.getSolucion() == null || averia.getSolucion().equals("") || averia.getSolucion().length() <= 0);
        if (piezaMissing) { // falta 'Pieza'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Pieza' es obligatorio.");
        } else if (solucionMissing) { // falta 'Solucion'
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Solucion' es obligatorio");
        } else { // (intentar) ejecutar resolucion
            boolean completed = daoAveria.resolve(averia);
            if (completed) {
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText(".");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText(".");
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
