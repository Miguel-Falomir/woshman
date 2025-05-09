package com.facturacion.middleware;

import com.facturacion.controller.DAO_Cliente;
import com.facturacion.model.Cliente;
import com.utilities.CellWare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

public class EditarClienteCellWare extends CellWare {

    // OBJETOS PUNTERO

    DAO_Cliente daoCliente;
    Cliente cliente;
    ListaClientesSubMenuWare menuWare;

    // CONSTRUCTOR

    public EditarClienteCellWare(ListaClientesSubMenuWare menuWare){
        this.menuWare = menuWare;
        this.daoCliente = this.menuWare.getDaoCliente();
    }

    // GETTERS Y SETTERS

    public Cliente getCliente(){
        return this.cliente;
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    // ELEMENTOS UI

    @FXML
    private Button Buton_Borrar;

    @FXML
    private Button Buton_Editar;

    // EVENTOS

    
    @FXML
    void OnAction_Buton_Borrar(ActionEvent event) {
        Func_Delete_Cliente();
    }

    @FXML
    void OnAction_Buton_Editar(ActionEvent event) {
        Func_Update_Cliente();
    }

    // METODO BORRAR CLIENTE

    private void Func_Delete_Cliente(){
        // inicializar ventana alert
        Alert alert = new Alert(AlertType.ERROR);
        String nombre = cliente.getNombre() + " " + cliente.getApellidos();

        // (intentar) ejecutar borrado
        if (daoCliente.delete(cliente)) {
            alert.setAlertType(AlertType.INFORMATION);
            alert.setHeaderText("OPERACIÓN COMPLETADA");
            alert.setContentText("El cliente " + nombre + " ha sido eliminado de la base de datos.");
        } else {
            alert.setHeaderText("ERROR SQL");
            alert.setContentText("No se puede eliminar cliente " + nombre + " porque tiene asignadas 1 o más ventas/averías");
        }

        // pase lo que pase, mostrarlo mediante la alert
        // .showAndWait() bloquea las siguientes instrucciones hasta cerrar la alert
        alert.showAndWait();

        // reiniciar la lista para que se muestre la ausencia del vehiculo eliminado
        menuWare.Func_Reboot_ObserClientes();
    }

    // METODO ACTUALIZAR CLIENTE

    private void Func_Update_Cliente(){
        this.menuWare.getMainController().openFormulary("facturacion", "form_editar_cliente", 480, 360, EditarClienteFormWare.class, menuWare, this.cliente);
    }

}
