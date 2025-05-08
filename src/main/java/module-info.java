module com {
    // Importar librerias requeridas
    //javafx
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    //sql
    requires transitive java.sql;

    // Otorgar acceso a librerias importadas
    //com
    opens com to javafx.fxml;
    //login
    opens com.empleados.middleware to javafx.fxml;
    opens com.empleados.controller to javafx.fxml;
    //menu
    opens com.menu.middleware to javafx.fxml;
    /* opens com.menu.controller to javafx.fxml; */
    //almacen
    opens com.almacen.middleware to javafx.fxml;
    // empleados
    
    // facturacion
    opens com.facturacion.middleware to javafx.fxml;
    opens com.facturacion.model to javafx.base;
    // vehiculos
    opens com.vehiculos.middleware to javafx.fxml;

    // Exportar paquetes
    exports com;
    exports com.menu.middleware;
    exports com.empleados.model;
    exports com.utilities;
}