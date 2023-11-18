module at.ac.fhcampuswien.synapsenchat.synapsenchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.ac.fhcampuswien.synapsenchat to javafx.fxml;
    exports at.ac.fhcampuswien.synapsenchat;
}