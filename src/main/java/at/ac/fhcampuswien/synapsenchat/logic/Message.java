package at.ac.fhcampuswien.synapsenchat.logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    private final String timestamp;
    private final String messageText;
    private final String senderName;

    /**
     * Represents a message in the chat application, including the actual message content, the sender's name,
     * and the timestamp indicating when the message was created.
     *
     * @param message    The content of the message.
     * @param senderName The name of the message sender.
     */
    public Message(String message, String senderName) {
        this.messageText = message;
        this.senderName = senderName;
        this.timestamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
    }

    @Override
    public String toString() {
        return String.format("[%s] <%s>: %s", timestamp, senderName, messageText);
    }

    public String getSenderName() {
        return senderName;
    }
}