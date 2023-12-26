module at.ac.fhcampuswien.synapsenchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens at.ac.fhcampuswien.synapsenchat to javafx.fxml;
    exports at.ac.fhcampuswien.synapsenchat;
    exports at.ac.fhcampuswien.synapsenchat.connection.server;
    opens at.ac.fhcampuswien.synapsenchat.connection.server to javafx.fxml;
    exports at.ac.fhcampuswien.synapsenchat.connection.client;
    opens at.ac.fhcampuswien.synapsenchat.connection.client to javafx.fxml;
}