package com.utilities;

import java.util.HashMap;

import com.menu.middleware.MainMiddleWare;

public abstract class SubMenuWare {

    // ATRIBUTOS

    protected MainMiddleWare mainController;
    protected HashMap<String, DAO> daoHashMap;

    // CONSTRUCTORES

    public SubMenuWare(){}

    public SubMenuWare(MainMiddleWare mainController, HashMap<String, DAO> daoHashMap){
        this.mainController = mainController;
        this.daoHashMap = daoHashMap;
    }

    // GETTERS Y SETTERS

    public MainMiddleWare getMainController(){
        return mainController;
    }

    public void setMainController(MainMiddleWare mainController){
        this.mainController = mainController;
    }

}
