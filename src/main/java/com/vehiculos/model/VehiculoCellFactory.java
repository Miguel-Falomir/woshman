package com.vehiculos.model;

import java.io.IOException;

import com.App;
import com.vehiculos.middleware.ListaVehiculosSubMenuWare;
import com.vehiculos.middleware.VehiculoCellWare;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class VehiculoCellFactory extends ListCell<Vehiculo> {

    // ELEMENTOS UI

    FXMLLoader loader;
    Parent root;

    // CONSTRUCTOR

    /*
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
    */

    public VehiculoCellFactory(ListaVehiculosSubMenuWare menuWare){
        // preparar archivo .fxml
        loader = new FXMLLoader(
            App.class.getResource("vehiculos/gui/cell_vehiculo.fxml")
        );

        // asignar instancia controlador
        loader.setControllerFactory(lambda -> {
            return new VehiculoCellWare(menuWare);
        });
        
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
                // tomar el VehiculoCellWare de la celda actual...
                VehiculoCellWare cellWare = loader.getController();

                // ...y asignarle el vehiculo 'item'
                cellWare.setVehiculo(item);

                // tomar atributos del vehiculo 'item'
                String matricula = item.getMatricula();
                String marca = item.getModelo().getMarca().getNombre();
                String modelo = item.getModelo().getNombre();

                // cargar elementos UI
                Label Label_Matricula = (Label) loader.getNamespace().get("Label_Matricula");
                Label Label_Marca_Modelo = (Label) loader.getNamespace().get("Label_Marca_Modelo");
                Button Buton_Editar = (Button) loader.getNamespace().get("Buton_Editar");
                Button Buton_Borrar = (Button) loader.getNamespace().get("Buton_Borrar");

                // asignar atributos a Labels
                Label_Matricula.setText(matricula);
                Label_Marca_Modelo.setText("[" + marca + " " + modelo + "]");

                // desactivar botones 'editar' y 'borrar' si no se tienen permisos 26 y 27
                if (!App.checkPermiso(26)) {
                    Buton_Editar.setManaged(false);
                }
                if (!App.checkPermiso(27)) {
                    Buton_Borrar.setManaged(false);
                }

                // asignar elemento root a la interfaz
                setGraphic(root);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
