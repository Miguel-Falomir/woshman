package com.vehiculos.model;

import javafx.scene.control.ListCell;

public class VehiculoCellFactory extends ListCell<Vehiculo> {

    // METODO MODIFICAR CELDAS LISTCELL

    @Override
    protected void updateItem(Vehiculo item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null){
            setText("NULL");
        } else {
            String matricula = item.getMatricula();
            String marca = item.getModelo().getMarca().getNombre();
            String modelo = item.getModelo().getNombre();
            setText(matricula + " [" + marca + " " + modelo + "]");
        }
    }

}
