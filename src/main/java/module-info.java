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
    opens com.login.middleware to javafx.fxml;
    opens com.login.controller to javafx.fxml;
    //main_menu
    opens com.main_menu.middleware to javafx.fxml;
    //opens com.main_menu.controller to javafx.fxml;

    // Exportar paquetes
    exports com;
}