package com.almacen.middleware;

import java.util.List;

import com.almacen.controller.DAO_Pieza;
import com.almacen.model.Pieza;
import com.almacen.model.Proveedor;
import com.almacen.model.Tipo_Pieza;
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

public class EditarPiezaFormWare extends FormWare {

    // OBJETOS PUNTERO

    ListaPiezasSubMenuWare menuWare;
    DAO_Pieza daoPieza;

    // OBJETOS ALMACENAR DATOS INTERNOS

    private Pieza pieza;
    private List<Tipo_Pieza> listTipos;
    private List<Proveedor> listProveedores;
    private ObservableList<Tipo_Pieza> obserTipos;
    private ObservableList<Proveedor> obserProveedor;

    // CONSTRUCTORES

    public EditarPiezaFormWare(){}

    public EditarPiezaFormWare(Pieza pieza, ListaPiezasSubMenuWare menuWare){
        this.pieza = pieza;
        this.menuWare = menuWare;
        this.daoPieza = this.menuWare.getDaoPieza();
        this.listTipos = this.menuWare.getDaoTipo().searchAll();
        this.listProveedores = this.menuWare.getDaoProveedor().searchAll();
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
    private ComboBox<Proveedor> Combo_Proveedor;

    @FXML
    private ComboBox<Tipo_Pieza> Combo_Tipo_Pieza;

    @FXML
    private TextField Input_Cantidad;

    @FXML
    private TextArea Input_Descripcion;

    @FXML
    private TextField Input_Nombre;

    @FXML
    private TextField Input_Precio;

    // EVENTOS

    // METODO INICIALIZAR

    public void initialize(){
        // rellenar campos de entrada con atributos pieza
        Input_Nombre.setText(pieza.getNombre());
        Combo_Tipo_Pieza.getSelectionModel().select(pieza.getTipo());
        Combo_Proveedor.getSelectionModel().select(pieza.getProveedor());
        Input_Precio.setText(String.valueOf(pieza.getPrecio()));
        Input_Cantidad.setText(String.valueOf(pieza.getCantidad()));
        Input_Descripcion.setText(pieza.getDescripcion());

        // inicializar listas observables
        obserTipos = FXCollections.observableArrayList(listTipos);
        obserProveedor = FXCollections.observableArrayList(listProveedores);

        // asignar listas observables a ComboBoxes
        Combo_Tipo_Pieza.setItems(obserTipos);
        Combo_Proveedor.setItems(obserProveedor);

        // asignar manejadores de eventos a elementos UI
        // al elegir tipo 'Combo_Tipos', eleccion se asigna a 'pieza.tipo'
        Combo_Tipo_Pieza.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection instanceof Tipo_Pieza) {pieza.setTipo(newSelection);}
        });
        // al elegir proveedor 'Combo_Proveedor', eleccion se asigna a 'pieza.proveedor'
        Combo_Proveedor.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection instanceof Proveedor) {pieza.setProveedor(newSelection);}
        });
        // al escribir en 'Input_Nombre', contenido se asigna a 'pieza.nombre'
        Input_Nombre.textProperty().addListener((observable, oldValue, newValue) -> {
            pieza.setNombre(newValue);
        });
        // al escribir en 'Input_Precio', contenido se transforma a decimal, y decimal se asigna a 'pieza.precio'
        Input_Precio.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                Float precio = Float.parseFloat(newValue);
                pieza.setPrecio(precio);
            } catch (NullPointerException e) {
                System.out.println("ERROR: CAMPO TEXTO NO DEBE ESTAR VACIO.");
            } catch (NumberFormatException e) {
                System.out.println("ERROR: LA CADENA INTRODUCIDA NO SE PUEDE TRANSFORMAR A DECIMAL.");
            }
        });
        // al escribir en 'Input_Descripcion', contenido se asigna a 'pieza.descripcion'
        Input_Descripcion.textProperty().addListener((observable, oldValue, newValue) -> {
            pieza.setDescripcion(newValue);
        });
        // al pulsar 'Buton_Plus', en 'Input_Cantidad' contenido se transforma a entero,
        // entero = entero + 1 si entero  < 100, y entero se asigna a 'pieza.cantidad'
        Buton_Plus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Integer cantidad = Integer.parseInt(Input_Cantidad.getText());
                    if (cantidad < 100) {cantidad++;}
                    Input_Cantidad.setText(String.valueOf(cantidad));
                    pieza.setCantidad(cantidad);
                } catch (NullPointerException e) {
                    System.out.println("ERROR: CAMPO TEXTO NO DEBE ESTAR VACIO.");
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: LA CADENA INTRODUCIDA NO SE PUEDE TRANSFORMAR A ENTERO.");
                }
            }
        });
        // al pulsar 'Buton_Minus', en 'Input_Cantidad' contenido se transforma a entero,
        // entero = entero - 1 si entero > 0, y entero se asigna a 'pieza.cantidad'
        Buton_Minus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Integer cantidad = Integer.parseInt(Input_Cantidad.getText());
                    if (cantidad > 0) {cantidad--;}
                    Input_Cantidad.setText(String.valueOf(cantidad));
                    pieza.setCantidad(cantidad);
                } catch (NullPointerException e) {
                    System.out.println("ERROR: CAMPO TEXTO NO DEBE ESTAR VACIO.");
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: LA CADENA INTRODUCIDA NO SE PUEDE TRANSFORMAR A ENTERO.");
                }
            }
        });
        // al pulsar 'Buton_Aceptar', se inserta pieza en base de datos
        Buton_Aceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Func_Update_Pieza();
            }
        });
        // al pulsar 'Buton_Cancelar', se cierra el formulario
        Buton_Cancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Func_Close();
            }
        });
    }

    // METODO CERRAR FORMULARIO

    private void Func_Close(){
        Stage thisStage = (Stage) Buton_Cancelar.getScene().getWindow();
        thisStage.close();
    }

    // METODO INSERTAR PIEZA

    private void Func_Update_Pieza(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.WARNING);
        String nombre = pieza.getNombre();

        // comprobar que se han rellenado todos los campos requeridos
        boolean nombreMissing = (pieza.getNombre() == "" || pieza.getNombre() == null || pieza.getNombre().length() == 0);
        boolean tipoMissing = (pieza.getTipo() == null);
        boolean proveedorMissing = (pieza.getProveedor() == null);
        boolean cantidadMissing = (pieza.getCantidad() == null || pieza.getCantidad() <= 0);
        boolean precioMissing = (pieza.getPrecio() == null || pieza.getPrecio() <= 0.0);
        if (nombreMissing) { // falta 'Nombre'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Nombre' es obligatorio.");
        } else if (tipoMissing) { // falta 'Tipo Pieza'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Tipo Pieza' es obligatorio.");
        } else if (proveedorMissing) { // falta 'Proveedor'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Proveedor' es obligatorio.");
        } else if (cantidadMissing) { // falta 'cantidad'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Cantidad' es obligatorio.");
        } else if (precioMissing) { // falta 'Precio'
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Campo 'Precio' es obligatorio.");
        } else { // con todos los campos rellenados, (intentar) ejecutar actualizacion
            boolean completed = daoPieza.update(pieza);
            if (completed) {
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("La pieza '" + nombre + "' ha sido actualizada");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText("Datos duplicados en pieza '" + nombre + "'. Recuerde que el nombre no puede repetirse.");
            }
        }

        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        alert.showAndWait();

        // si se ha completado la operacion, reiniciar lista y cerrar ventana
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            menuWare.Func_Reboot_ObserPiezas();
            Func_Close();
        }
    }

}
