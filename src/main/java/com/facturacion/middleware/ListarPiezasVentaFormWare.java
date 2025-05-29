package com.facturacion.middleware;

import java.util.List;

import com.almacen.model.Pieza;
import com.almacen.model.Proveedor;
import com.facturacion.model.Venta;
import com.utilities.FormWare;
import com.utilities.SubMenuWare;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ListarPiezasVentaFormWare extends FormWare {

    // OBJETOS PUNTERO

    private ListaVentasSubMenuWare menuWare;
    
    // OBJETOS ALMACENAR DATOS INTERNOS
    
    private Venta venta;
    private List<Pieza> listPiezas;
    private ObservableList<Pieza> obserPiezas;

    // CONSTRUCTORES

    public ListarPiezasVentaFormWare(){}

    public ListarPiezasVentaFormWare(SubMenuWare menuWare){
        this.menuWare = (ListaVentasSubMenuWare) menuWare;
        this.venta = this.menuWare.getVenta();
        this.listPiezas = venta.getListPiezas();
    }

    // ELEMENTOS UI

    @FXML
    private TableColumn<Pieza, String> TVcol_Cantidad;

    @FXML
    private TableColumn<Pieza, String> TVcol_Nombre;

    @FXML
    private TableColumn<Pieza, String> TVcol_Precio;

    @FXML
    private TableColumn<Pieza, String> TVcol_Proveedor;

    @FXML
    private TableColumn<Pieza, String> TVcol_Total;

    @FXML
    private TableView<Pieza> TablV_Piezas;

    // EVENTOS

    // METODO INICIALIZAR

    public void initialize(){
        // inicializar lista observable
        obserPiezas = FXCollections.observableArrayList(listPiezas);

        // inicializar TableView y TableColumns
        TVcol_Nombre.setCellValueFactory(lambda -> {
            String nombre = lambda.getValue().getNombre();
            return new SimpleStringProperty((nombre == null) ? "null" : nombre);
        });
        TVcol_Proveedor.setCellValueFactory(lambda -> {
            Proveedor prov = lambda.getValue().getProveedor();
            return new SimpleStringProperty((prov == null) ? "null" : prov.getNombre());
        });
        TVcol_Precio.setCellValueFactory(lambda -> {
            Float precio = lambda.getValue().getPrecio();
            return new SimpleStringProperty((precio == null) ? "null" : String.valueOf(precio));
        });
        TVcol_Cantidad.setCellValueFactory(lambda -> {
            Integer cant = lambda.getValue().getCantidad();
            return new SimpleStringProperty((cant == null) ? "null" : String.valueOf(cant));
        });
        TVcol_Total.setCellValueFactory(lambda -> {
            Float precio = lambda.getValue().getPrecio();
            Integer cant = lambda.getValue().getCantidad();
            Float total = (precio == null || cant == null) ? 0.0f: precio * cant;
            return new SimpleStringProperty(String.valueOf(total));
        });
        TablV_Piezas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        TablV_Piezas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TablV_Piezas.setItems(obserPiezas);
    }

}
