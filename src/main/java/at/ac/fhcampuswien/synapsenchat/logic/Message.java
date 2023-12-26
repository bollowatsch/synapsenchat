package at.ac.fhcampuswien.synapsenchat.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String message;
    private String senderName;
    private SimpleDateFormat formatter;

    public Message(String message, String senderName) {
        this.message = message;
        this.senderName = senderName;
        this.formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

    @Override
    public String toString() {
        String timestamp = formatter.format(new Date());
        return String.format("[%s] <%s>: %s", timestamp, senderName, message);
    }
}
