package at.ac.fhcampuswien.synapsenchat;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.Message;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChatAppController extends ChatApp {

    @FXML
    MFXRadioButton radioServer, radioClient;
    @FXML
    protected BorderPane chatPane;
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
    MFXTextField usernameField;
    public static String username;
    public static Chat currentChat;
    @FXML
    protected void showChatContent() throws IOException {
        if (chatName.getText().isEmpty() || ipAddress.getText().isEmpty() || port.getText().isEmpty()) {
            errorLabel.setText("Please fill in all fields!");
        } else {
            // instantiate new chat object
            Chat newChat;
            if (radioServer.isSelected()) newChat = new Chat(chatName.getText(), Integer.parseInt(port.getText()), this);
            else newChat = new Chat(chatName.getText(), ipAddress.getText(), Integer.parseInt(port.getText()), this);
            currentChat = newChat;
            System.out.println("TEST");

            // add new chat label in sidebar
            chatPane = (BorderPane) startConnection.getScene().getRoot();
            VBox chatList = (VBox) chatPane.lookup("#chatList");
            Label newLabel = new Label(chatName.getText());
            newLabel.getStyleClass().add("chat-label");
            newLabel.setId(String.valueOf(newChat.getID()));
            newLabel.setOnMouseClicked(e -> {
                Label label = (Label) e.getSource();
                try {
                    showExistingContent(Integer.parseInt(label.getId()));
                    currentChat = Chat.getChatByID(Integer.parseInt(label.getId()));
                    System.out.println("Label Id: " + label.getId() + "chat Id: " + currentChat.getID());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            chatList.getChildren().add(newLabel);
            // change view to chatContent
//            AnchorPane view =  FXMLLoader.load(getClass().getResource("chatContent.fxml"));
//            chatPane.setCenter(view);
            chatContentScene(chatPane, newChat);
            showExistingContent(currentChat.getID());
        }
    }

    protected void showExistingContent(int id) throws IOException {
        ArrayList<Chat> chatList = Chat.getChats();
        currentChat = Chat.getChatByID(id);
//        currentChat = Chat.deserializeChat("src/main/java/at/ac/fhcampuswien/synapsenchat/logs/" + id + ".txt");

        // change view to chatContent
        chatContentScene(chatPane, currentChat);

    }

    /**
     * shows the "new chat" interface by laoding it into the center of borderPane
     * @throws IOException
     */
    @FXML
    protected void showNewChat() throws IOException {
        view = FXMLLoader.load(getClass().getResource("newChat.fxml"));
        chatPane.setCenter(view);
    }

    @FXML
    private void getServerIp() throws UnknownHostException {
        Inet4Address my_localhost = (Inet4Address) Inet4Address.getLocalHost();
        String ipv4Address = my_localhost.getHostAddress().trim();
        ipAddress.clear();
        ipAddress.appendText(ipv4Address);
    }

    @FXML
    private void clearIpAddress() {
        ipAddress.clear();
    }

    public void onSendMessage(AnchorPane view) {
        int chatID = currentChat.getID();

        chatContentBox = (VBox) view.lookup("#chatContentBox");
//        MFXScrollPane chatContentPane = (MFXScrollPane) view.lookup("#chat-content");
//        VBox chatContentBox = new VBox();
//        chatContentPane.setContent(chatContentBox);

        chatContentBox.setAlignment(Pos.TOP_RIGHT);
        String text = newMessage.getText().trim();

        if (!text.isEmpty()) {
            String username = getUsername();
            // set username to chat name for now for better testing
            // username = (String) currentChat.getChatName();
            Message message = new Message(text, username);
            Label messageLabel = new Label(message.toString());
            messageLabel.getStyleClass().add("chat-content-label");
            //chatContentBox.getChildren().add(messageLabel);
//            currentChat.addMessage(message);
            currentChat.sendMessage(message);

            //TODO: CHECK SERIALIZATION METHOD-SERVERSIDE (second message sent from server throws error???)
            //Chat.serializeChat(new Chat(currentChat), "src/main/java/at/ac/fhcampuswien/synapsenchat/logs/" + chatID + ".txt");
            newMessage.clear();
        }
    }

    @FXML
    private void setUsername() {
        username = usernameField.getText();
        System.out.println(username);
    }

    private String getUsername() {
        if (username != null) return username;
        return "Sender";
    }

    private void chatContentScene(BorderPane pane, Chat chat) throws IOException {
        // Create and display the main scene
        chatPane = pane;
        AnchorPane view = FXMLLoader.load(getClass().getResource("chatContent.fxml"));
        chatPane.setCenter(view);
        VBox chatList = (VBox) chatPane.lookup("#chatList");

        //load old messages to the chat
        ObservableList<Message> oldMessages = chat.getAllMessages();
        if (!oldMessages.isEmpty()) {
            view = loadOldMessages(view, oldMessages);
        }

        // functionality to switch between the chats
        for (javafx.scene.Node node : chatList.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                // set action for each label
                label.setOnMouseClicked(e -> {
                    try {
                        showExistingContent(Integer.parseInt(label.getId()));
                        currentChat = Chat.getChatByID(Integer.parseInt(label.getId()));
                        System.out.println("Label Id: " + label.getId() + "chat Id: " + currentChat.getID());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        }

        newMessage = (MFXTextField) view.lookup("#new-message-field");
        sendMessage = (MFXButton) view.lookup("#send-message");

        AnchorPane finalView = view;
        newMessage.setOnAction(event -> {
            onSendMessage(finalView);
        }); // Attach to Enter key press

        sendMessage.setOnAction(event -> {
            onSendMessage(finalView);
        }); // Attach to Send button click
        int id = currentChat.getID();
    }

    private AnchorPane loadOldMessages(AnchorPane view, ObservableList<Message> oldMessages) {
        BorderPane chatPane = (BorderPane) view.getScene().getRoot();
        chatPane.setCenter(view);
        MFXScrollPane chatContentPane = (MFXScrollPane) view.lookup("#chat-content");
        VBox chatContentBox = (VBox) chatContentPane.getContent();
        chatContentBox.setAlignment(Pos.TOP_RIGHT);

        for (Message oldMessage : oldMessages) {
            String text = oldMessage.toString();
            Label messageLabel = new Label(text);
            messageLabel.getStyleClass().add("chat-content-label");

            //add HBox to dynamically change the alignment of the message labels
            HBox messageContainer = new HBox();
            messageContainer.setAlignment(Pos.CENTER_LEFT);

            //TODO: needs to be change to user name
            if ("Server".equals(oldMessage.getSenderName())) {
                messageContainer.setAlignment(Pos.CENTER_RIGHT);
            }

            // Add label to the message container
            messageContainer.getChildren().add(messageLabel);

            // Add the message container to the VBox
            chatContentBox.getChildren().add(messageContainer);

//            chatContentBox.getChildren().add(messageLabel);
        }
        return view;
    }

    public void onReceivedMessage(Message message) {
        synchronized (this) {
            //chatContentBox = (VBox) view.lookup("#chatContentBox");
            String text = message.toString();
            Label messageLabel = new Label(text);
            messageLabel.getStyleClass().add("chat-content-label");
            chatContentBox.setAlignment(Pos.TOP_LEFT);
            chatContentBox.getChildren().add(messageLabel);
        }
    }

    public void updateChat(Message message, int chatId){
        System.out.println("Update detected!! ChatID: " + chatId + "==" + currentChat.getID() + " Message: " + message.toString());
        if(chatId == currentChat.getID())
            onReceivedMessage(message);
    }
}