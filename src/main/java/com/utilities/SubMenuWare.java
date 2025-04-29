package com.utilities;

import com.menu.middleware.MainMiddleWare;

public abstract class SubMenuWare {

    // ATRIBUTOS

    private MainMiddleWare mainController;

    // CONSTRUCTORES

    public SubMenuWare(){}

    public SubMenuWare(MainMiddleWare mainController){
        this.mainController = mainController;
    }

    // METODO CAMBIAR CONTROLADOR PRINCIPAL

    public MainMiddleWare getMainController(){
        return mainController;
    }

    public void setMainController(MainMiddleWare mainController){
        this.mainController = mainController;
    }

}
