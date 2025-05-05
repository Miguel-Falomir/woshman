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
        this.listaMarcas = this.menuWare.daoMarca.searchAll();
        this.listaModelos = this.menuWare.daoModelo.searchAll();
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
        Combo_Marca.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                int id = newSelection.getId();
                filterModelos.setPredicate(i -> i.getMarca().getId() == id);
            }
        });
        Combo_Modelo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                vehiculo.setModelo(newSelection);
            }
        });
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

    // METODOS

    private void Func_Close(){
        Stage thisStage = (Stage) Buton_Cancelar.getScene().getWindow();
        thisStage.close();
    }

    private void Func_Insert_Vehiculo(){
        // inicializar ventana alert
        Alert a = new Alert(AlertType.NONE);

        // (intentar) ejecutar actualizacion
        if(dao.insert(vehiculo)){
            a.setAlertType(AlertType.INFORMATION);
            a.setHeaderText("OPERACIÓN COMPLETADA");
            a.setContentText("El vehiculo " + vehiculo.getMatricula() + " ha sido actualizado.");
        } else {
            a.setAlertType(AlertType.ERROR);
            a.setHeaderText("ERROR SQL");
            a.setContentText("Ya existe un vehiculo com matrícula " + vehiculo.getMatricula() + ". recuerde que las matrículas no pueden repetirse.");
        }

        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        a.showAndWait();

        // reiniciar la lista para que se muestre la ausencia del vehiculo eliminado
        menuWare.Func_Reboot_ObserVehiculos();

        // cerrar ventana
        Func_Close();
    }
}
