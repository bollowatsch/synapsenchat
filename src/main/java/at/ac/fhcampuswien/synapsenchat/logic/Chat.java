package at.ac.fhcampuswien.synapsenchat.logic;

import java.util.ArrayList;

public class Chat {
    static int globalID = 0;

    private final int id;
    private String chatName;
    private ArrayList<Message> msgList;

    public Chat(String chatName) {
        id = ++globalID;
        this.chatName = chatName;
        msgList = new ArrayList<>();
    }

    public int getID() {
        return id;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatName() {
        return chatName;
    }

    public void addMessage(Message message) {
        msgList.add(message);
    }

    public ArrayList<Message> getAllMessages() {
        return msgList;
    }
}
