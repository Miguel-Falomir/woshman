package com.facturacion.middleware;

import com.almacen.model.Pieza;
import com.facturacion.model.Averia;
import com.utilities.FormWare;
import com.utilities.SubMenuWare;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ConsultarAveriaFormWare extends FormWare {

    // OBJETOS PUNTERO

    private ListaAveriasSubMenuWare menuWare;

    // OBJETOS ALMACENAR DATOS INTERNOS

    private Averia averia;

    // CONSTRUCTORES

    public ConsultarAveriaFormWare(){}

    public ConsultarAveriaFormWare(SubMenuWare menuWare, Averia averia){
        this.menuWare = (ListaAveriasSubMenuWare) menuWare;
        this.averia = averia;
    }

    // ELEMENTOS UI

    @FXML
    private TextField Field_Cantidad_Pieza;

    @FXML
    private TextArea Field_Descripcion;

    @FXML
    private TextField Field_Nombre_Pieza;

    @FXML
    private TextArea Field_Solucion;

    @FXML
    private TextArea Field_Observaciones;

    // EVENTOS

    // METODO INICIALIZAR

    public void initialize(){
        // hacer que VSCode se calle un minuto
        System.out.println(menuWare.getClass());

        // guardar primera pieza
        Pieza pieza = averia.getListaPiezas().getFirst();

        // asignar datos a elementos UI
        Field_Descripcion.setText(averia.getDescripcion());
        Field_Solucion.setText(averia.getSolucion());
        Field_Observaciones.setText(averia.getObservaciones());
        Field_Nombre_Pieza.setText(pieza.getNombre());
        Field_Cantidad_Pieza.setText(String.valueOf(pieza.getCantidad()));
    }
}
