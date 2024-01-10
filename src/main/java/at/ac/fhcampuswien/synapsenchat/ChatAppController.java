package at.ac.fhcampuswien.synapsenchat;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.Message;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;


import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChatAppController extends ChatApp {

    @FXML
    MFXRadioButton radioServer, radioClient;
    @FXML
    protected BorderPane outerPane;
    @FXML
    private AnchorPane chatContentPane;
    @FXML
    private AnchorPane newChatFormPane;
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

    /**
     * update center view of outerPane to contain either newChatForm or chatContent
     * @param filename filename for anchorPane that will be added to center of outerPane
     * @return outerPane with updated center scene
     */
    private BorderPane updateCenterView(String filename) throws IOException {
        try {
            if(!(filename.equals("chatContent.fxml") || filename.equals("newChat.fxml")))
                throw new IOException("worng filename provided!");
            AnchorPane newCenterPane = FXMLLoader.load(getClass().getResource(filename));
            if(filename.equals("chatContent.fxml"))
                chatContentPane = newCenterPane;
            else
                newChatFormPane = newCenterPane;
            outerPane.setCenter(newCenterPane);
        } catch (IOException e){
            System.out.println("Error loading new xml file : " + filename);
            e.printStackTrace();
        }
        return outerPane;
    }

    /**
     * shows the "new chat" interface by loading it into the center of borderPane
     */
    @FXML
    protected void showNewChatForm() throws IOException {
        outerPane = updateCenterView("newChat.fxml");
    }

    /**
     * validates input fields in new chat form
     * @return Error message to be displayed in GUI
     */
    private String validateNewChatForm(){
        if (chatName.getText().isEmpty() || ipAddress.getText().isEmpty() || port.getText().isEmpty())
            return "Please fill in all fields!";
        else if (Integer.parseInt(port.getText()) < 1024 || Integer.parseInt(port.getText()) > 65535)
            return "Choose a port in the range of [1024 : 65535]!";
        else if (!validateIPv4(ipAddress.getText()))
            return "Unvalid ipv4 format!";
        return "";
    }

    /**
     * error handling for input form
     * create new chat object and update view to display chatContent
     */
    @FXML
    protected void onSubmitNewChatForm() throws IOException {
        if(!validateNewChatForm().isEmpty())
            errorLabel.setText(validateNewChatForm());
        else {
            // instantiate new chat object
            Chat newChat;
            if (radioServer.isSelected())
                newChat = new Chat(chatName.getText(), Integer.parseInt(port.getText()), this);
            else newChat = new Chat(chatName.getText(), ipAddress.getText(), Integer.parseInt(port.getText()), this);
            currentChat = newChat;

            addNewChatLabel(currentChat.getChatName(), currentChat.getID());
            outerPane = updateCenterView("chatContent.fxml");
        }
    }

    /**
     * create a new message object and send it with currentChat
     */
    public void onSendMessage() {
        newMessage = (MFXTextField) chatContentPane.lookup("#new-message-field");
        String text = newMessage.getText().trim();

        if (!text.isEmpty()) {
            Message message = new Message(text, getUsername());
            currentChat.sendMessage(message);

            //TODO: CHECK SERIALIZATION METHOD-SERVERSIDE (second message sent from server throws error???)
            //Chat.serializeChat(new Chat(currentChat), "src/main/java/at/ac/fhcampuswien/synapsenchat/logs/" + chatID + ".txt");
            newMessage.clear();
        }
    }

    /**
     * create a new clickable label in sidebar to navigate between active chats
     * @param chatName name of the chat, is displayed in sidebar
     * @param chatId ID to be stored in chat object. Is used to link to corresponding label
     */
    private void addNewChatLabel(String chatName, int chatId) {
        outerPane = (BorderPane) startConnection.getScene().getRoot();
        VBox chatList = (VBox) outerPane.lookup("#chatList");
        Label newLabel = new Label(chatName);
        newLabel.getStyleClass().add("chat-label");
        newLabel.setId(String.valueOf(chatId));
        newLabel.setOnMouseClicked(e -> {
            Label label = (Label) e.getSource();
            try {
                outerPane = updateCenterView("chatContent.fxml");
                showExistingContent(Integer.parseInt(label.getId()));
                currentChat = Chat.getChatByID(Integer.parseInt(label.getId()));
                System.out.println("Label Id: " + label.getId() + "chat Id: " + currentChat.getID());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        chatList.getChildren().add(newLabel);
    }

    /**
     *
     * @param id
     * @return
     * @throws IOException
     */
    public void showExistingContent(int id) throws IOException {
        currentChat = Chat.getChatByID(id);
        //currentChat = Chat.deserializeChat("src/main/java/at/ac/fhcampuswien/synapsenchat/logs/" + id + ".txt");
        ObservableList<Message> oldMessages = currentChat.getAllMessages();
        System.out.println("print old messages: " + oldMessages + "\n size: " + oldMessages.size());
        for (Message oldMessage : oldMessages)
                onReceivedMessage(oldMessage);
    }

    /**
     * displays message in center view
     * aligns messages left / right depending on sender
     * @param message
     */
    public void onReceivedMessage(Message message) {
        synchronized (this) {
            MFXScrollPane chatContentScrollPane = (MFXScrollPane) chatContentPane.lookup("#chat-content");
            VBox chatContentBox = (VBox) chatContentScrollPane.getContent();
            chatContentBox.setAlignment(Pos.TOP_RIGHT);
            String text = message.toString();

            Label messageLabel = new Label(text);
            messageLabel.getStyleClass().add("chat-content-label");

            //add HBox to dynamically change the alignment of the message labels
            HBox messageContainer = new HBox();
            if (username.equals(message.getSenderName()))
                messageContainer.setAlignment(Pos.CENTER_RIGHT);
            else
                messageContainer.setAlignment(Pos.CENTER_LEFT);

            // Add label to the message container
            messageContainer.getChildren().add(messageLabel);

            // Add the message container to the VBox
            chatContentBox.getChildren().add(messageContainer);
        }
    }

    /**
     * helper function to call onReceivedMessage with appropriate parameters
     * @param message
     * @param chatId
     */
    public void updateChat(Message message, int chatId) {
        System.out.println("Update detected!! ChatID: " + chatId + "==" + currentChat.getID() + " Message: " + message.toString());
        if (chatId == currentChat.getID())
            onReceivedMessage(message);
    }

    /**
     * validate entered IP address
     * @param IP IP address entered in form
     * @return true if valid (syntax), false otherwise
     */
    public boolean validateIPv4(String IP) {
        try {
            if (IP == null || IP.isEmpty()) {
                return false;
            }
            String[] octets = IP.split("\\.");
            if (octets.length != 4) {
                return false;
            }
            for (String number : octets) {
                int octetToCheck = Integer.parseInt(number);
                if ((octetToCheck < 0) || (octetToCheck > 255)) {
                    return false;
                }
            }
            if (IP.endsWith(".")) {
                return false;
            }
            return true;
        } catch (NumberFormatException exception) {
            System.out.println(exception);
            return false;
        }
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
    @FXML
    private void setUsername() {
        username = usernameField.getText();
    }

    private String getUsername() {
        if (username != null) return username;
        return "Sender";
    }
}