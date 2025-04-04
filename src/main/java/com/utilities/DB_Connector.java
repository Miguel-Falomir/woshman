package com.utilities;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connector {

    // ATRIBUTOS

    private static String DRIVER;
    private static String URL;
    private static String USR;
    private static String PWD;
    private static Connection CON = null;

    // CONSTRUCTORES

    public DB_Connector(){}

    public DB_Connector(String driver, String url, String usr, String pwd) {
        DRIVER = driver;
        URL = url;
        USR = usr;
        PWD = pwd;
    }

    // METODOS

    public void StartConnection(){
        try {
            Class.forName(DRIVER).getDeclaredConstructor().newInstance();
            CON = DriverManager.getConnection(URL, USR, PWD);
        } catch (InstantiationException |
                 IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException |
                 NoSuchMethodException |
                 SecurityException |
                 ClassNotFoundException |
                 SQLException e) {
            e.printStackTrace();
        }
    }

    public void CloseConnection(){
        try {
            if (CON.isClosed() || CON == null){
                System.out.println("Ya estaba cerrada");
            } else {
                CON.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GETTERS Y SETTERS

    public Connection getConnection(){
        return CON;
    }

}
