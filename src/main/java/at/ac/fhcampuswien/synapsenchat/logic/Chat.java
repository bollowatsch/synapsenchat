package at.ac.fhcampuswien.synapsenchat.logic;

import java.util.ArrayList;

public class Chat {
    private static int globalID = 0;
    private static ArrayList<Chat> chats = new ArrayList<>();

    private final int id;
    private String chatName;
    private ArrayList<Message> msgList;

    public Chat(String chatName) {
        chats.add(this);
        id = ++globalID;
        this.chatName = chatName;
        msgList = new ArrayList<>();
    }

    public int getID() {
        return id;
    }

    public static ArrayList<Chat> getChats() {
        return chats;
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
