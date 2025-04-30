package com.utilities;

import com.menu.middleware.MainMiddleWare;

public abstract class SubMenuWare {

    // ATRIBUTOS

    protected MainMiddleWare mainController;
    protected DAO dao;

    // CONSTRUCTORES

    public SubMenuWare(){}

    public SubMenuWare(MainMiddleWare mainController, DAO dao){
        this.mainController = mainController;
        this.dao = dao;
    }

    // METODO CAMBIAR CONTROLADOR PRINCIPAL

    public MainMiddleWare getMainController(){
        return mainController;
    }

    public void setMainController(MainMiddleWare mainController){
        this.mainController = mainController;
    }

}
