package com.vehiculos.middleware;

import java.util.HashMap;
import java.util.List;

import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Vehiculo;
import com.vehiculos.model.VehiculoCellFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ListaVehiculosMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Vehiculo> obserVehiculos;

    // CONSTRUCTOR

    public ListaVehiculosMenuWare(){}

    public ListaVehiculosMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Agregar;

    @FXML
    private Button Buton_Buscar;

    @FXML
    private ListView<Vehiculo> ListV_Vehiculos;

    @FXML
    private TextField Input_Matricula;

    // EVENTOS

    @FXML
    void OnAction_Buton_Agregar(ActionEvent event) {

    }

    @FXML
    void OnAction_Buton_Buscar(ActionEvent event) {
        System.out.println(Input_Matricula.getText());
    }

    // INICIALIZAR

    public void initialize(){
        // inicializar lista observable
        DAO_Vehiculo daoVehiculo = (DAO_Vehiculo) daoHashMap.get("vehiculo");
        List<Vehiculo> lista = daoVehiculo.searchAll();
        obserVehiculos = FXCollections.observableArrayList(lista);

        // asignar lista observable a ListView interfaz
        ListV_Vehiculos.setItems(obserVehiculos);

        // definir formato de las celdas de ListView
        ListV_Vehiculos.setCellFactory(lambda -> {
            return new VehiculoCellFactory();
        });

        // si se trata de un 'mecanico', deshabilitar 'Buton_Agregar'
        /*
        Integer rol = this.mainController.getCurrentRol();
        if(rol == 1){
            Buton_Agregar.setVisible(false);
        }
        */
        Buton_Agregar.getParent().setVisible(false);
        Buton_Agregar.getParent().setDisable(true);
        Buton_Agregar.getParent().setManaged(false);
    }

    // METODO LISTAR VEHICULOS

}
