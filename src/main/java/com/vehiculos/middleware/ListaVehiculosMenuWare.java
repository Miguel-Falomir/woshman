package com.vehiculos.middleware;

import java.util.List;

import com.menu.middleware.MainMiddleWare;
import com.utilities.SubMenuWare;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Vehiculo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ListaVehiculosMenuWare extends SubMenuWare {

    // CONSTRUCTOR

    public ListaVehiculosMenuWare(){
        super();
    }

    public ListaVehiculosMenuWare(MainMiddleWare mainController){
        super(mainController);
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Click;

    // EVENTOS

    @FXML
    void OnAction_Buton_Click(){
        prueba();
    }

    // METODO LISTAR TODOS LOS VEHICULOS

    public void prueba(){
        DAO_Vehiculo daoVehiculo = (DAO_Vehiculo) super.getMainController().getDao();

        List<Vehiculo> listaPrueba = daoVehiculo.searchAll();

        for (Vehiculo veh : listaPrueba) {
            System.out.println(veh.toString());
        }
    }

}
