package com.facturacion.model;

import java.io.IOException;

import com.App;
import com.facturacion.middleware.EditarClienteCellWare;
import com.facturacion.middleware.ListaClientesSubMenuWare;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;

public class EditarClienteCellFactory extends TableCell<Cliente, String> {

    // ELEMENTOS UI

    FXMLLoader loader;
    Parent root;

    // CONSTRUCTOR

    public EditarClienteCellFactory(ListaClientesSubMenuWare menuWare){
        // preparar archivo .fxml
        loader = new FXMLLoader(
            App.class.getResource("facturacion/gui/cell_editar_cliente.fxml")
        );

        // asignar instancia controlador
        loader.setControllerFactory(lambda -> {
            return new EditarClienteCellWare(menuWare);
        });
        
        try {
            // cargar archivo .fxml
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 

    

}
