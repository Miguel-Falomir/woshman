package com.vehiculos.middleware;

import java.util.List;

import com.utilities.FormWare;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Marca;
import com.vehiculos.model.Modelo;
import com.vehiculos.model.Vehiculo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InsertarVehiculoFormWare extends FormWare {
    
    // OBJETOS PUNTERO

    ListaVehiculosSubMenuWare menuWare;
    DAO_Vehiculo dao;

    // OBJETOS ALMACENAR DATOS INTERNOS

    private Vehiculo vehiculo;
    private List<Marca> listaMarcas = null;
    private List<Modelo> listaModelos = null;
    private ObservableList<Marca> obserMarcas;
    private ObservableList<Modelo> obserModelos;
    private FilteredList<Modelo> filterModelos;

    // CONSTRUCTORES

    public InsertarVehiculoFormWare(){}

    public InsertarVehiculoFormWare(ListaVehiculosSubMenuWare menuWare){
        this.vehiculo = new Vehiculo();
        this.menuWare = menuWare;
        this.dao = this.menuWare.getDaoVehiculo();
        this.listaMarcas = this.menuWare.getDaoMarca().searchAll();
        this.listaModelos = this.menuWare.getDaoModelo().searchAll();
    }

    // ELEMENTOS UI

    @FXML
    private ComboBox<Marca> Combo_Marca;

    @FXML
    private ComboBox<Modelo> Combo_Modelo;

    @FXML
    private TextField Input_Matricula;

    @FXML
    private Button Buton_Cancelar;

    @FXML
    private Button Buton_Aceptar;

    // METODO INICIALIZAR

    public void initialize(){
        // inicializar 'obserMarcas'...
        obserMarcas = FXCollections.observableArrayList(listaMarcas);
        // ...y asignar a elemento 'Combo_Marca'
        Combo_Marca.setItems(obserMarcas);

        // inicializar 'obserModelos' y 'filterModelos'...
        obserModelos = FXCollections.observableArrayList(listaModelos);
        filterModelos = new FilteredList<>(obserModelos);
        // ...y asignar 'filterModelos' a elemento 'Combo_Modelo'
        Combo_Modelo.setItems(filterModelos);

        // antes de elegir Marca, 'filterModelos' no debe mostrar ningun modelo
        filterModelos.setPredicate(i -> i.getId() == -1);

        // asignar manejadores de eventos a elementos UI
        // al elegir marca, se filtran los modelos de 'Combo_Modelo'
        Combo_Marca.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                int id = newSelection.getId();
                filterModelos.setPredicate(i -> i.getMarca().getId() == id);
                Combo_Modelo.getSelectionModel().clearSelection();
                Combo_Modelo.valueProperty().set(null);
            }
        });
        // al elegir modelo, este se asigna a 'vehiculo.modelo'
        Combo_Modelo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                vehiculo.setModelo(newSelection);
            }
        });
        // al escribir texto en campo 'Input_Matricula', el contenido se asigna a 'vehiculo.matricula'
        Input_Matricula.textProperty().addListener((observable, oldValue, newValue) -> {
            vehiculo.setMatricula(newValue);
        });
    }

    // EVENTOS

    @FXML
    void OnAction_Buton_Cancelar(ActionEvent event){
        Func_Close();
    }

    @FXML
    void OnAction_Buton_Aceptar(ActionEvent event){
        Func_Insert_Vehiculo();
    }

    // METODO CERRAR FORMULARIO

    private void Func_Close(){
        Stage thisStage = (Stage) Buton_Cancelar.getScene().getWindow();
        thisStage.close();
    }

    // METODO INSERTAR VEHICULO

    private void Func_Insert_Vehiculo(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.NONE);

        // (intentar) ejecutar actualizacion
        boolean notFulfilled = vehiculo.getId() == null || vehiculo.getMatricula() == null || vehiculo.getModelo() != null;
        boolean completed = dao.insert(vehiculo);
        if(completed){
            alert.setAlertType(AlertType.INFORMATION);
            alert.setHeaderText("OPERACIÓN COMPLETADA");
            alert.setContentText("El vehiculo " + vehiculo.getMatricula() + " se ha guardado en la base de datos.");
        } else if (notFulfilled) {
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR FORMULARIO");
            alert.setContentText("Deben rellenarse TODOS los datos del formulario");
        } else {
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("ERROR SQL");
            alert.setContentText("Ya existe un vehiculo com matrícula " + vehiculo.getMatricula() + ". recuerde que las matrículas no pueden repetirse.");
        }

        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        alert.showAndWait();

        // si se ha completado la operacion, reiniciar lista y cerrar ventana
        boolean success = (alert.getAlertType().equals(AlertType.INFORMATION));
        if (success) {
            menuWare.Func_Reboot_ObserVehiculos();
            Func_Close();
        }
    }
}
