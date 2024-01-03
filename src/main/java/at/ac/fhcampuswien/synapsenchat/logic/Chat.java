package at.ac.fhcampuswien.synapsenchat.logic;

import java.io.*;
import java.util.*;

public class Chat implements Serializable {
    private static int globalID = 0;
    private static final HashMap<Integer, Chat> chats = new HashMap<>();

    private final int id;
    private String chatName;
    private final ArrayList<Message> messages;

    public Chat(String chatName) {
        synchronized (chats) {
            this.chatName = chatName;
            this.id = ++globalID;
            this.messages = new ArrayList<>();
            chats.put(id, this);
        }
    }

    public static Chat getChatByID(int id) {
        synchronized (chats) {
            return chats.get(id);
        }
    }

    public int getID() {
        return id;
    }

    public static ArrayList<Chat> getChats() {
        synchronized (chats) {
            Collection<Chat> values = chats.values();
            return new ArrayList<>(values);
        }
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatName() {
        return chatName;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public ArrayList<Message> getAllMessages() {
        return messages;
    }

    public void printAllMessages() {
        System.out.println("---------------");
        System.out.printf("Printing chat history for chat %s...%n", this.chatName);
        messages.forEach(System.out::println);
        System.out.println("---------------");
    }

    public static void serializeChat(Chat chat, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {

            oos.writeObject(chat);
            System.out.println("Serialized Chat object!");

        } catch (IOException e) {
            System.out.println("Error occurred while serialization of chat object: " + e.getMessage());
        }
    }

    public static Chat deserializeChat(String filename) {
        Chat chat = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {

            chat = (Chat) ois.readObject();
            System.out.println("Deserialized Chat object!");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while deserialization of chat object: " + e.getMessage());
        }

        return chat;
    }


}
