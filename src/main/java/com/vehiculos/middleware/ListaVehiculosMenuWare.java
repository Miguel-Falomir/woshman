package com.vehiculos.middleware;

import java.util.HashMap;
import java.util.List;

import com.App;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;
import com.vehiculos.controller.DAO_Marca;
import com.vehiculos.controller.DAO_Modelo;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Marca;
import com.vehiculos.model.Modelo;
import com.vehiculos.model.Vehiculo;
import com.vehiculos.model.VehiculoCellFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ListaVehiculosMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Vehiculo> obserVehiculos;

    // DAOs

    DAO_Vehiculo daoVehiculo;
    DAO_Modelo daoModelo;
    DAO_Marca daoMarca;

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
    private TreeView<String> TreeV_Marca_Modelo;

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
        // inicializar DAOs
        daoVehiculo = (DAO_Vehiculo) daoHashMap.get("vehiculo");
        daoModelo = (DAO_Modelo) daoHashMap.get("modelo");
        daoMarca = (DAO_Marca) daoHashMap.get("marca");

        // recopilar TODOS los vehiculos, modelos y marcas
        List<Vehiculo> listaVehiculos = daoVehiculo.searchAll();
        List<Modelo> listaModelos = daoModelo.searchAll();
        List<Marca> listaMarcas = daoMarca.searchAll();

        // inicializar listas observables
        obserVehiculos = FXCollections.observableArrayList(listaVehiculos);
        
        // no me interesa que cualquier usuario pueda ver el vehiculo de prueba...
        obserVehiculos.remove(0);
        listaMarcas.remove(0);

        // asignar lista observable a ListView interfaz
        ListV_Vehiculos.setItems(obserVehiculos);

        // definir formato de las celdas de ListView
        ListV_Vehiculos.setCellFactory(lambda -> {
            return new VehiculoCellFactory();
        });

        // rellenar TreeItem principal
        TreeItem<String> Titem_Root = new TreeItem<>("Marca-Modelo");
        for (Marca marca : listaMarcas) { // recorrer marcas
            TreeItem<String> Titem_Marca = new TreeItem<String>(marca.getNombre());
            List<Modelo> auxModelos = daoModelo.searchByMarca(marca.getId());
            for (Modelo modelo : auxModelos) { // recorrer modelos de cada marca
                TreeItem<String> Titem_Modelo = new TreeItem<String>(modelo.getNombre());
                Titem_Marca.getChildren().add(Titem_Modelo);
            }
            Titem_Root.getChildren().add(Titem_Marca);
        }

        Titem_Root.setExpanded(true);

        // asignar TreeItem al TreeView
        TreeV_Marca_Modelo.setRoot(Titem_Root);

        // deshabilitar 'Buton_Agregar' si no se tiene permiso 25
        if (!App.checkPermiso(25)){
            Buton_Agregar.getParent().setVisible(false);
            Buton_Agregar.getParent().setDisable(true);
            Buton_Agregar.getParent().setManaged(false);
        }
    }

    // 

}
