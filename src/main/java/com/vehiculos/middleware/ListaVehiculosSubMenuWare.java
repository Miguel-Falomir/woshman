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

    private DAO_Vehiculo daoVehiculo;
    private DAO_Modelo daoModelo;
    private DAO_Marca daoMarca;

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

    public DAO_Modelo getDaoModelo() {
        return daoModelo;
    }

    public void setDaoModelo(DAO_Modelo daoModelo) {
        this.daoModelo = daoModelo;
    }

    public DAO_Marca getDaoMarca() {
        return daoMarca;
    }

    public void setDaoMarca(DAO_Marca daoMarca) {
        this.daoMarca = daoMarca;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Agregar;

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
        Func_Calculate_Predicate();
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

        // solo 'admin' puede leer objetos undefined
        boolean prohibited = !(App.checkRol(0));
        if (prohibited){
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
                Titem.Tipo_Item.MARCA
            ));
            List<Modelo> auxModelos = daoModelo.searchByMarca(marca.getId());
            for (Modelo modelo : auxModelos) { // recorrer modelos de cada marca
                TreeItem<Titem> Titem_Modelo = new TreeItem<Titem>( new Titem(
                    modelo.getId(),
                    modelo.getNombre(),
                    Titem.Tipo_Item.MODELO
                ));
                Titem_Marca.getChildren().add(Titem_Modelo); // agregar modelo a marca
            }
            Titem_Root.getChildren().add(Titem_Marca); // agregar marca a root
        }
        
        // Asignar manejadores de evento:
        // Al seleccionar Titem, se filtra la lista
        TreeV_Marca_Modelo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Func_Calculate_Predicate();
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

    // METODO CALCULAR PREDICADO

    private void Func_Calculate_Predicate(){
        // recopilar TODOS los valores de entrada
        Titem titem = TreeV_Marca_Modelo.getSelectionModel().getSelectedItem().getValue();
        String regex = Input_Matricula.getText();
        Titem.Tipo_Item tipo = titem.getTipo();
        Integer id = titem.getId();

        // aplicar predicado
        switch (tipo){
            case MARCA:
                filterVehiculos.setPredicate(i -> i.getModelo().getMarca().getId() == id && i.getMatricula().contains(regex));
                break;
            case MODELO:
                filterVehiculos.setPredicate(i -> i.getModelo().getId() == id && i.getMatricula().contains(regex));
                break;
            case null:
                filterVehiculos.setPredicate(i -> i.getMatricula().contains(regex));
                break;
            default:
                System.out.println("Este texto no debería poder imprimirse");
                break;
        }
    }

    // METODO INSERTAR VEHICULO

    private void Func_Insert_Vehiculo(){
        mainController.openFormulary("vehiculos", "form_insertar_vehiculo", "insertar vehiculo", 480, 360, InsertarVehiculoFormWare.class, this, null);
    }

    // METODO REINICIAR LISTA VEHICULOS

    public void Func_Reboot_ObserVehiculos(){
        // recopilar TODOS los vehiculos, modelos y marcas
        listaVehiculos = daoVehiculo.searchAll();
        listaModelos = daoModelo.searchAll();
        listaMarcas = daoMarca.searchAllAlphabetically();

        // solo 'admin' puede leer objetos undefined
        boolean prohibited = !(App.checkRol(0));
        if (prohibited){
            listaMarcas.removeIf(marca -> marca.getId() == 0); // sugerencia de chatGPT
            listaModelos.removeIf(modelo -> modelo.getId() == 0);
            listaVehiculos.removeIf(vehiculo -> vehiculo.getId() == 0);
        }

        // actualizar listas observables
        obserVehiculos.clear();
        obserVehiculos.setAll(listaVehiculos);

        // refrescar ListView
        ListV_Vehiculos.refresh();
    }

}
