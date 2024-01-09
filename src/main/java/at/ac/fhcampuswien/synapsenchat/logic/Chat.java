package at.ac.fhcampuswien.synapsenchat.logic;


import at.ac.fhcampuswien.synapsenchat.HelloController;
import at.ac.fhcampuswien.synapsenchat.connection.multithreading.client.Client;
import at.ac.fhcampuswien.synapsenchat.connection.multithreading.server.Server;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class Chat implements Serializable {
    private static int globalID = 0;
    private static final HashMap<Integer, Chat> chats = new HashMap<>();

    private int id;
    private String chatName;
    private ObservableList<Message> messages;
    private HelloController controller;

    private transient Chat copy;

    private transient Client client;
    private transient Server server;

    public Chat(String chatName, HelloController controller) {
        synchronized (chats) {
            this.chatName = chatName;
            this.controller = controller;
            this.id = ++globalID;
            this.messages = FXCollections.observableArrayList();
            this.messages.addListener((ListChangeListener) c -> {
                while(c.next()) {
                    if (c.wasAdded()) {
                        Platform.runLater(() ->
                                controller.updateChat((getAllMessages().get(messages.size() - 1)), id));
                    }
                }
            });
            chats.put(id, this);
        }
    }

    public Chat(String chatName, int port, HelloController controller) {
        this(chatName, controller);
        this.server = new Server(port, this);
    }

    public Chat(String chatName, String ip, int port, HelloController controller) {
        this(chatName, controller);
        this.client = new Client(ip, port, this);
    }

    public Chat(Chat chat) {
        this.copy = chat.copy;
    }

    public static Chat getChatByID(int id) {
        synchronized (chats) {
            return chats.get(id);
        }
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

    public int getID() {
        return id;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Sends Message to Server / Client using {@code sendMessage} of client or server.
     * @param message
     */
    public void sendMessage(Message message) {
        if (server != null) server.sendMessage(message);
        if (client != null) client.sendMessage(message);
        messages.add(message);
    }

    public ObservableList<Message> getAllMessages() {
        return messages;
    }

    public void printAllMessages() {
        System.out.println("---------------");
        System.out.printf("Printing chat history for chat %s...%n", this.chatName);
        messages.forEach(System.out::println);
        System.out.println("---------------");
    }

    //FIXME: SERIALIZATION NOT WORKING CORRECT! (ArrayList of Messages is missing?)
    public synchronized static void serializeChat(Chat chat, String filename) {
        File file = new File(filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file,false))) {

            oos.writeObject(chat);
            System.out.println("Serialized Chat object!");

        } catch (IOException e) {
            System.out.println("Error occurred while serialization of chat object: " + e.getMessage());
        }
    }

    //FIXME: DESERIALIZATION NOT WORKING CORRECT! (ARRAY
    public synchronized static Chat deserializeChat(String filename) {
        Chat chat = null;
        File file = new File(filename);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

            chat = (Chat) ois.readObject();
            System.out.printf("Deserialized Chat object! (%s)%n", chat.getChatName());

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while deserialization of chat object: " + e.getMessage());
        }

        return chat;
    }
}