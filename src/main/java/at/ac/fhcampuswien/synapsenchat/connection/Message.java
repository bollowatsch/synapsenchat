package at.ac.fhcampuswien.synapsenchat.connection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class Message implements Serializable {

    private String msg;
    private String senderName;
    public String timestamp;

    public Message(String s) {
        setMsg(s);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setTimestamp() {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return new SimpleDateFormat("[dd.MM.yyyy HH:mm]").format(new Date());
    }
}
