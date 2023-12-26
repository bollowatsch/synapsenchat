package at.ac.fhcampuswien.synapsenchat;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    MFXScrollPane chatContent;
    @FXML
    MFXScrollPane newChat;
    @FXML
    Label startLabel;

    @FXML
    protected void onNewChatButtonClick(){
        System.out.println("New chat!");
        if(chatContent.getContent().isVisible())
            chatContent.setVisible(false);
        if(startLabel.isVisible())
            startLabel.setVisible(false);
        newChat.setVisible(true);
    }
}