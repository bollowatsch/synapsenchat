package at.ac.fhcampuswien.synapsenchat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {


        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        // set minimum width and height for application
        stage.setMinWidth(800.0);
        stage.setMinHeight(650.0);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
     //   scene.getStylesheets().add(getClass().getResource("").toExternalForm());
        stage.setTitle("Synapsenchat");
        stage.setScene(scene);
        scene.getStylesheets().add(HelloApplication.class.getResource("styles.css").toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}