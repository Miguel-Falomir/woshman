package com.vehiculos.middleware;

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

public class ListaVehiculosMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Vehiculo> obserVehiculos;

    // CONSTRUCTOR

    public ListaVehiculosMenuWare(){}

    public ListaVehiculosMenuWare(MainMiddleWare mainController, DAO dao){
        this.mainController = mainController;
        this.dao = dao;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Click;

    @FXML
    private Button Buton_Agregar;

    @FXML
    private ListView<Vehiculo> ListV_Vehiculos;

    // EVENTOS

    @FXML
    void OnAction_Buton_Click(ActionEvent event){

    }

    @FXML
    void OnAction_Buton_Agregar(ActionEvent event) {

    }

    // INICIALIZAR

    public void initialize(){
        // inicializar lista observable
        DAO_Vehiculo daoVehiculo = (DAO_Vehiculo) dao;
        List<Vehiculo> lista = daoVehiculo.searchAll();
        obserVehiculos = FXCollections.observableArrayList(lista);

        // asignar lista observable a ListView interfaz
        ListV_Vehiculos.setItems(obserVehiculos);

        // definir formato de las celdas de ListView
        ListV_Vehiculos.setCellFactory(lambda -> {
            return new VehiculoCellFactory();
        });

        // si se trata de un 'mecanico', deshabilitar 'Buton_Agregar'
        Integer rol = this.mainController.getCurrentRol();
        if(rol == 1){
            Buton_Agregar.setVisible(false);
        }
    }

    // METODO LISTAR VEHICULOS

}
