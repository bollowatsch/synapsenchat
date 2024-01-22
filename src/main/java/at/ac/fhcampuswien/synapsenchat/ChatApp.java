package at.ac.fhcampuswien.synapsenchat;

import at.ac.fhcampuswien.synapsenchat.connection.Client;
import at.ac.fhcampuswien.synapsenchat.connection.Server;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatApp extends Application {
    /**
     * Main class fur GUI to start application
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        synchronized (this) {
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    Platform.exit();

                    Client.terminate();
                    Server.terminate();

                    //TODO: Save all chats!

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }

                    System.out.println("EXITING...");
                    System.exit(0);
                }
            });
        }

        UserAgentBuilder.builder().themes(JavaFXThemes.MODENA).themes(MaterialFXStylesheets.forAssemble(true)).setDeploy(true).setResolveAssets(true).build().setGlobal();

        // set minimum width and height for application
        stage.setMinWidth(800.0);
        stage.setMinHeight(650.0);

        FXMLLoader fxmlLoader = new FXMLLoader(ChatApp.class.getResource("home.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Synapsenchat");
        stage.setScene(scene);
        scene.getStylesheets().add(ChatApp.class.getResource("styles.css").toExternalForm());
        stage.getIcons().add(new Image("at/ac/fhcampuswien/synapsenchat/brain.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}