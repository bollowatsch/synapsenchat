package at.ac.fhcampuswien.synapsenchat;

import io.github.palexdev.materialfx.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;



public class HelloController {

    @FXML
    BorderPane newChat;
    @FXML
    Label startLabel;
    @FXML
    AnchorPane chatContent;
    @FXML
    protected BorderPane homePane;
    @FXML
    protected AnchorPane view;
    @FXML
    MFXButton startConnection;
    @FXML
    MFXTextField chatName;
    @FXML
    MFXTextField ipAddress;
    @FXML
    MFXTextField port;
    @FXML
    Label errorLabel;
    @FXML
    MFXButton sendMessage;
    @FXML
    MFXTextField newMessage;
    @FXML
    VBox chatContentBox;


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
    protected void showChatContent() throws IOException {
        if (chatName.getText().isEmpty() || ipAddress.getText().isEmpty() || port.getText().isEmpty() ) {
            errorLabel.setText("Please fill in all fields!");
        } else {
            view = FXMLLoader.load(getClass().getResource("chatContent.fxml"));
            BorderPane chatPane = (BorderPane) startConnection.getScene().getRoot();
            chatPane.setCenter(view);
        }
    }

    @FXML
    protected void showNewChat() throws IOException {
        view = FXMLLoader.load(getClass().getResource("newChat.fxml"));
        homePane.setCenter(view);
    }

    @FXML
    private void getClientIp() throws UnknownHostException {
        Inet4Address my_localhost = (Inet4Address) Inet4Address.getLocalHost();
        String ipv4Address = my_localhost.getHostAddress().trim();
        ipAddress.clear();
        ipAddress.appendText(ipv4Address);
    }

    @FXML
    private void clearIpAddress() {
        ipAddress.clear();
    }

    @FXML
    protected void onStartConnectionButtonClick() {
        if (startLabel.isVisible())
            startLabel.setVisible(false);
        if (newChat.isVisible())
            newChat.setVisible(false);
//        chatContent.setVisible(true);
    }

    @FXML
    protected void onSendMessage() {
        chatContentBox.setAlignment(Pos.TOP_RIGHT);
        String text = newMessage.getText().trim();
        if (!text.isEmpty()) {
            Label message = new Label(text);
            message.getStyleClass().add("chat-content-label");
            chatContentBox.getChildren().add(message);
            newMessage.clear();
        }
    }

}