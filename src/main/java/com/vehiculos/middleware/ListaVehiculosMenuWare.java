package com.vehiculos.middleware;

import java.util.List;

import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Vehiculo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

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
    private ListView<Vehiculo> Lview_Vehiculos;

    // EVENTOS

    @FXML
    void OnAction_Buton_Click(){}

    // INICIALIZAR

    public void initialize(){
        // inicializar lista observable
        obserVehiculos = FXCollections.observableArrayList(prueba());

        // asignar lista observable a ListView interfaz
        Lview_Vehiculos.setItems(obserVehiculos);
    }

    // METODO LISTAR VEHICULOS

    public List<Vehiculo> prueba(){
        DAO_Vehiculo daoVehiculo = (DAO_Vehiculo) dao;

        List<Vehiculo> listaVehiculos = daoVehiculo.searchAll();

        return listaVehiculos;
    }

}
