package com.vehiculos.model;

import java.io.IOException;

import com.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class VehiculoCellFactory extends ListCell<Vehiculo> {

    // ELEMENTOS UI

    FXMLLoader loader;
    Parent root;

    // CONSTRUCTOR

    public VehiculoCellFactory(){
        // preparar archivo .fxml
        loader = new FXMLLoader(
            App.class.getResource("vehiculos/gui/cell_vehiculo.fxml")
        );
        
        try {
            // cargar archivo .fxml
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // METODO MODIFICAR CELDAS LISTCELL

    @Override
    protected void updateItem(Vehiculo item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null){
            setText(null);
            setGraphic(null);
        } else {
            try {
                // tomar atributos del vehiculo
                String matricula = item.getMatricula();
                String marca = item.getModelo().getMarca().getNombre();
                String modelo = item.getModelo().getNombre();

                // cargar elementos Label
                Label Label_Matricula = (Label) loader.getNamespace().get("Label_Matricula");
                Label Label_Marca_Modelo = (Label) loader.getNamespace().get("Label_Marca_Modelo");

                // asignar atributos a Labels
                Label_Matricula.setText(matricula);
                Label_Marca_Modelo.setText("[" + marca + " " + modelo + "]");

                // asignar elemento root a la interfaz
                setGraphic(root);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
