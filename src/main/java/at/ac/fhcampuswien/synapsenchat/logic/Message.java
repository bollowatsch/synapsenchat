package at.ac.fhcampuswien.synapsenchat.logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private final String timestamp;
    private final String message;
    private String senderName;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public Message(String message, String senderName) {
        this.message = message;
        this.senderName = senderName;
        this.timestamp = getTimestamp();
    }

    private String getTimestamp() {
        return formatter.format(new Date());
    }
    private void setSenderName(String senderName) {this.senderName = senderName;}
    private String getSenderName() {return senderName;}

    @Override
    public String toString() {
        return String.format("[%s] <%s>: %s", timestamp, senderName, message);
    }
}