module at.ac.fhcampuswien.synapsenchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens at.ac.fhcampuswien.synapsenchat to javafx.fxml;
    exports at.ac.fhcampuswien.synapsenchat;
}