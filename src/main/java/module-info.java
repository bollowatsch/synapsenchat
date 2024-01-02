module at.ac.fhcampuswien.synapsenchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens at.ac.fhcampuswien.synapsenchat to javafx.fxml;
    exports at.ac.fhcampuswien.synapsenchat;
    exports at.ac.fhcampuswien.synapsenchat.connection.multithreading.client;
    opens at.ac.fhcampuswien.synapsenchat.connection.multithreading.client to javafx.fxml;
    exports at.ac.fhcampuswien.synapsenchat.connection.multithreading.server;
    opens at.ac.fhcampuswien.synapsenchat.connection.multithreading.server to javafx.fxml;
}