package com.facturacion.middleware;

import java.util.List;

import com.empleados.model.Empleado;
import com.facturacion.controller.DAO_Averia;
import com.facturacion.model.Averia;
import com.facturacion.model.Estado_Averia;
import com.utilities.FormWare;
import com.utilities.SubMenuWare;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AsignarAveriaFormWare extends FormWare {

    // OBJETOS PUNTERO

    private ListaAveriasSubMenuWare menuWare;
    private DAO_Averia daoAveria;

    // OBJETOS ALMACENAR DATOS INTERNOS

    private Averia averia;
    private Estado_Averia estAsignado;
    private List<Empleado> listEmpleados;
    private ObservableList<Empleado> obserEmpleados;

    // CONSTRUCTORES

    public AsignarAveriaFormWare(){}

    public AsignarAveriaFormWare(SubMenuWare menuWare, Averia averia){
        this.menuWare = (ListaAveriasSubMenuWare) menuWare;
        this.daoAveria = this.menuWare.getDaoAveria();
        this.estAsignado = this.menuWare.getDaoEstado().search(1);
        this.listEmpleados = this.menuWare.getDaoEmpleado().searchMecanicos();
        this.averia = averia;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Aceptar;

    @FXML
    private Button Buton_Cancelar;

    @FXML
    private TableColumn<Empleado, String> TVcol_Cantidad_Averias;

    @FXML
    private TableColumn<Empleado, String> TVcol_Nombre_Empleado;

    @FXML
    private TableColumn<Empleado, String> TVcol_Rol_Empleado;

    @FXML
    private TableView<Empleado> TablV_Empleados;

    // METODO INICIALIZAR

    public void initialize(){
        // inicializar TableColumns
        TVcol_Nombre_Empleado.setCellValueFactory(lambda -> new SimpleStringProperty(
            (lambda.getValue().getNombre() == null) ? "null" : lambda.getValue().getNombre() + " " + lambda.getValue().getApellidos()
        ));
        TVcol_Rol_Empleado.setCellValueFactory(lambda -> new SimpleStringProperty(
            (lambda.getValue().getRol() == null) ? "null" : lambda.getValue().getRol().getNombre()
        ));
        TVcol_Cantidad_Averias.setCellValueFactory(lambda -> new SimpleStringProperty(
            (lambda.getValue().getCantAverias() == null) ? "null" : String.valueOf(lambda.getValue().getCantAverias())
        ));

        // inicializar Tableview
        TablV_Empleados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        TablV_Empleados.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // inicializar lista observable...
        obserEmpleados = FXCollections.observableArrayList(listEmpleados);
        // ...y asignar a tabla
        TablV_Empleados.setItems(obserEmpleados);

        // asignar manejadores de eventos a elementos UI
        // al seleccionar item en 'TablV_Empleados', asignar a 'averia.empleado'
        TablV_Empleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                averia.setEmpleado(newSelection);
                averia.setEstado(estAsignado);
            }
        });
        // al pulsar 'Buton_Aceptar', se inserta pieza en base de datos
        Buton_Aceptar.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Func_Assign_Empleado();
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

    // METODO ASIGNAR EMPLEADO

    private void Func_Assign_Empleado(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.ERROR);

        // comprobar que se ha seleccionado un empleado
        boolean empleadoMissing = averia.getEmpleado() == null || averia.getEmpleado().getId() == 0;
        if (empleadoMissing) { // falta 'Empleado'
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("ELIGE UN EMPLEADO");
        } else { // (intentar) ejecutar asingacion
            boolean completed = daoAveria.assign(averia);
            if (completed) {
                alert.setAlertType(AlertType.INFORMATION);
                alert.setHeaderText("OPERACIÓN COMPLETADA");
                alert.setContentText("La asignación de la avería al empleado se ha guardado en la base de datos.");
            } else {
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("ERROR SQL");
                alert.setContentText("No se han podido guardar datos en base de datos.");
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
