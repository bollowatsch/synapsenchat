package at.ac.fhcampuswien.synapsenchat;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {

    @FXML
    BorderPane newChat;
    @FXML
    Label startLabel;
    @FXML
    VBox chatContent;
    @FXML
    protected BorderPane homePane;
    @FXML
    protected AnchorPane view;
    @FXML
    MFXButton startConnection;

    @FXML
    protected void onNewChatButtonClick() {
        System.out.println("New chat!");
        if (chatContent.isVisible())
            chatContent.setVisible(false);
        if (startLabel.isVisible())
            startLabel.setVisible(false);
        newChat.setVisible(true);
    }
    @FXML
    protected void showChatContent(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource("chatContent.fxml"));
        BorderPane chatPane = (BorderPane) startConnection.getScene().getRoot();
        chatPane.setCenter(view);

    }

    @FXML
    protected void showNewChat(ActionEvent event) throws IOException{
        view = FXMLLoader.load(getClass().getResource("newChat.fxml"));
        homePane.setCenter(view);
    }

    @FXML
    protected void onStartConnectionButtonClick(){
        if (startLabel.isVisible())
            startLabel.setVisible(false);
        if (newChat.isVisible())
            newChat.setVisible(false);
//        chatContent.setVisible(true);
    }


}