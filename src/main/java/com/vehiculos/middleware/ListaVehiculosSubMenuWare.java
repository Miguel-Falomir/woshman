package com.vehiculos.middleware;

import java.util.HashMap;
import java.util.List;

import com.App;
import com.menu.middleware.MainMiddleWare;
import com.utilities.DAO;
import com.utilities.SubMenuWare;
import com.utilities.Titem;
import com.vehiculos.controller.DAO_Marca;
import com.vehiculos.controller.DAO_Modelo;
import com.vehiculos.controller.DAO_Vehiculo;
import com.vehiculos.model.Marca;
import com.vehiculos.model.Modelo;
import com.vehiculos.model.Vehiculo;
import com.vehiculos.model.VehiculoCellFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;

public class ListaVehiculosSubMenuWare extends SubMenuWare {

    // OBJETOS ALMACENAR DATOS INTERNOS

    private ObservableList<Vehiculo> obserVehiculos;
    private FilteredList<Vehiculo> filterVehiculos;
    private List<Vehiculo> listaVehiculos;
    private List<Modelo> listaModelos;
    private List<Marca> listaMarcas;

    // DAOs

    DAO_Vehiculo daoVehiculo;
    DAO_Modelo daoModelo;
    DAO_Marca daoMarca;

    // CONSTRUCTOR

    public ListaVehiculosSubMenuWare(){}

    public ListaVehiculosSubMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
    }

    // GETTERS Y SETTERS

    public DAO_Vehiculo getDaoVehiculo(){
        return this.daoVehiculo;
    }

    public void setDaoVehiculo(DAO_Vehiculo daoVehiculo){
        this.daoVehiculo = daoVehiculo;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Agregar;

    //@FXML
    //private Button Buton_Buscar;

    @FXML
    private ListView<Vehiculo> ListV_Vehiculos;

    @FXML
    private TreeView<Titem> TreeV_Marca_Modelo;

    @FXML
    private TextField Input_Matricula;

    // EVENTOS

    @FXML
    void OnAction_Buton_Agregar(ActionEvent event) {
        Func_Insert_Vehiculo();
    }

    @FXML
    void OnKeyTyped_Input_Matricula(KeyEvent event) {
        System.out.println("[ASMR teclado mecánico]: " + Input_Matricula.getText());
    }

    // INICIALIZAR

    public void initialize(){
        // inicializar DAOs
        // mentira, realmente no crea DAOs nuevos, sino punteros que referencian
        // a los DAOs contenidos el el daoHashMap del 'MainMiddleWare' actual
        daoVehiculo = (DAO_Vehiculo) daoHashMap.get("vehiculo");
        daoModelo = (DAO_Modelo) daoHashMap.get("modelo");
        daoMarca = (DAO_Marca) daoHashMap.get("marca");

        // recopilar TODOS los vehiculos, modelos y marcas
        listaVehiculos = daoVehiculo.searchAll();
        listaModelos = daoModelo.searchAll();
        listaMarcas = daoMarca.searchAllAlphabetically();

        // forzar que 'undefined' no sea visible si no se tiene permisos:
        // 25 (Insertar Vehiculos)
        // 26 (Modificar Vehiculos)
        // 27 (Eliminar Vehiculos)
        if (!(App.checkPermiso(25) && App.checkPermiso(26) && App.checkPermiso(27))){
            listaMarcas.removeIf(marca -> marca.getId() == 0); // sugerencia de chatGPT
            listaModelos.removeIf(modelo -> modelo.getId() == 0);
            listaVehiculos.removeIf(vehiculo -> vehiculo.getId() == 0);
        }

        // inicializar listas observables
        obserVehiculos = FXCollections.observableArrayList(listaVehiculos);
        filterVehiculos = new FilteredList<>(obserVehiculos);

        // asignar lista observable a ListView interfaz
        ListV_Vehiculos.setItems(filterVehiculos);

        // definir formato de las celdas de ListView
        ListV_Vehiculos.setCellFactory(lambda -> {
            return new VehiculoCellFactory(this);
        });

        // rellenar TreeItem principal
        TreeItem<Titem> Titem_Root = new TreeItem<Titem>( new Titem("Marca-Modelo"));
        for (Marca marca : listaMarcas) { // recorrer marcas
            TreeItem<Titem> Titem_Marca = new TreeItem<Titem>( new Titem(
                marca.getId(),
                marca.getNombre(),
                Titem.Tipo.MARCA
            ));
            List<Modelo> auxModelos = daoModelo.searchByMarca(marca.getId());
            for (Modelo modelo : auxModelos) { // recorrer modelos de cada marca
                TreeItem<Titem> Titem_Modelo = new TreeItem<Titem>( new Titem(
                    modelo.getId(),
                    modelo.getNombre(),
                    Titem.Tipo.MODELO
                ));
                Titem_Marca.getChildren().add(Titem_Modelo); // agregar modelo a marca
            }
            Titem_Root.getChildren().add(Titem_Marca); // agregar marca a root
        }
        
        // Asignar manejador de evento:
        // Al seleccionar Titem, se filtra la lista
        TreeV_Marca_Modelo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // recopilar datos Titem
                Titem.Tipo tipo = newSelection.getValue().getTipo();
                Integer id = newSelection.getValue().getId();
                // comparar tipo
                switch (tipo){
                    case MARCA: // filtrar marcas
                        filterVehiculos.setPredicate(i -> i.getModelo().getMarca().getId() == id);
                        break;
                    case MODELO: // filtrar modelos
                        filterVehiculos.setPredicate(i -> i.getModelo().getId() == id);
                        break;
                    case null: // deshacer filtros
                        if (!(App.checkPermiso(25) && App.checkPermiso(26) && App.checkPermiso(27))){
                            filterVehiculos.setPredicate(i -> i.getId() != 0);
                        } else {
                            filterVehiculos.setPredicate(null);
                        }
                        break;
                    default: // esto no hace nada, pero switch-case SIEMPRE necesita un default
                        break;
                }
            }
        });
        
        // asignar TreeItem al TreeView
        Titem_Root.setExpanded(true);
        TreeV_Marca_Modelo.setRoot(Titem_Root);

        // deshabilitar 'Buton_Agregar' si no se tiene permiso 25 (Insertar Vehiculo)
        if (!App.checkPermiso(25)){
            Buton_Agregar.setVisible(false);
            Buton_Agregar.setDisable(true);
            Buton_Agregar.setManaged(false);
        }
    }

    // METODO INSERTAR VEHICULO

    private void Func_Insert_Vehiculo(){
        mainController.openFormulary("vehiculos", "form_editar_vehiculo", 480, 360, InsertarVehiculoFormWare.class, this, null);
    }

    // METODO REINICIAR LISTA VEHICULOS

    public void Func_Reboot_ObserVehiculos(){
        // recopilar TODOS los vehiculos, modelos y marcas
        listaVehiculos = daoVehiculo.searchAll();
        listaModelos = daoModelo.searchAll();
        listaMarcas = daoMarca.searchAllAlphabetically();

        // forzar que 'undefined' no sea visible si no se tienen permisos:
        // 25 (Insertar Vehiculos)
        // 26 (Modificar Vehiculos)
        // 27 (Eliminar Vehiculos)
        if (!(App.checkPermiso(25) && App.checkPermiso(26) && App.checkPermiso(27))){
            listaMarcas.removeIf(marca -> marca.getId() == 0); // sugerencia de chatGPT
            listaModelos.removeIf(modelo -> modelo.getId() == 0);
            listaVehiculos.removeIf(vehiculo -> vehiculo.getId() == 0);
        }

        // inicializar listas observables
        obserVehiculos = FXCollections.observableArrayList(listaVehiculos);
        filterVehiculos = new FilteredList<>(obserVehiculos);

        // asignar lista observable a ListView interfaz
        ListV_Vehiculos.setItems(filterVehiculos);

        // definir formato de las celdas de ListView
        ListV_Vehiculos.setCellFactory(lambda -> {
            return new VehiculoCellFactory(this);
        });

        // refrescar ListView
        ListV_Vehiculos.refresh();
    }

}
