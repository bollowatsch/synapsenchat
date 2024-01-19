package at.ac.fhcampuswien.synapsenchat.logic;

import at.ac.fhcampuswien.synapsenchat.ChatAppController;
import at.ac.fhcampuswien.synapsenchat.connection.Client;
import at.ac.fhcampuswien.synapsenchat.connection.Instance;
import at.ac.fhcampuswien.synapsenchat.connection.Server;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.HashMap;

public class Chat implements Serializable {
    private static int globalID = 0;
    private static final HashMap<Integer, Chat> chats = new HashMap<>();

    private int id;
    private String chatName;
    private ObservableList<Message> messages;

    private transient Chat copy;
    private transient Instance instance;

    /**
     * Constructs a new Chat object with the given chatName and associates it with the provided ChatAppController.
     * The constructor is designed to ensure thread safety by synchronizing access to the shared resource (chats).
     *
     * @param chatName   The name of the chat.
     * @param controller The ChatAppController associated with this chat.
     * @throws NullPointerException If chatName or controller is null.
     */
    public Chat(String chatName, ChatAppController controller) {
        // synchronized keyword is used to lock the resource (chats) to one thread, so that no other thread can access it at a time.
        synchronized (chats) {
            this.chatName = chatName;
            this.id = ++globalID;
            this.messages = FXCollections.observableArrayList();
            this.messages.addListener((ListChangeListener<Message>) c -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        Platform.runLater(() -> controller.updateChat((getAllMessages().get(messages.size() - 1)), id));
                    }
                }
            });
            chats.put(id, this);
        }
    }

    /**
     * Constructs a new Chat object with the given chatName, associates it with the provided ChatAppController,
     * and initializes a server instance with the specified port for handling chat communication.
     *
     * @param chatName   The name of the chat.
     * @param port       The port number for communication within the chat.
     * @param controller The ChatAppController associated with this chat.
     * @throws NullPointerException If chatName or controller is null.
     */
    public Chat(String chatName, int port, ChatAppController controller) {
        this(chatName, controller);
        this.instance = new Server(port, this);
    }

    /**
     * Constructs a new Chat object with the given chatName, associates it with the provided ChatAppController,
     * and initializes a client instance with the specified IP address and port for connecting to a chat.
     *
     * @param chatName   The name of the chat.
     * @param ip         The IP address of the chat server for communication.
     * @param port       The port number for communication within the chat.
     * @param controller The ChatAppController associated with this chat.
     * @throws NullPointerException If chatName, ip, or controller is null.
     */
    public Chat(String chatName, String ip, int port, ChatAppController controller) {
        this(chatName, controller);
        this.instance = new Client(ip, port, this);
    }

    /**
     * Constructs a new Chat object by copying the properties of the specified Chat object.
     *
     * @param chat The Chat object to be copied.
     * @throws NullPointerException If the provided chat object is null.
     */
    public Chat(Chat chat) {
        this.copy = chat.copy;
    }

    /**
     * Retrieves a Chat object with the specified ID from the collection of chats.
     *
     * @param id The unique identifier of the Chat object to retrieve.
     * @return The Chat object with the specified ID, or null if no such Chat object exists.
     */
    public static Chat getChatByID(int id) {
        return chats.get(id);
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
     * Sends a message using the communication instance associated with this Chat.
     * If no communication instance is set (null), the method does nothing.
     *
     * @param message The Message object to be sent.
     * @throws NullPointerException If the provided message is null.
     */
    public void sendMessage(Message message) {
        if (instance != null) {
            instance.sendMessage(message);
        }
    }

    public ObservableList<Message> getAllMessages() {
        return messages;
    }

    //FIXME: SERIALIZATION NOT WORKING CORRECT! (ArrayList of Messages is missing?)

    /**
     * This method allows the serialization of a Chat object to a file.
     * The method is marked as 'synchronized' to ensure coordinated access by multiple threads,
     * avoiding potential race conditions.
     *
     * @param chat     The Chat object to be serialized.
     * @param filename The name of the file to which the Chat object is to be serialized.
     * @throws NullPointerException If the provided Chat object or filename is null.
     */
    public static synchronized void serializeChat(Chat chat, String filename) {
        File file = new File(filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false))) {

            oos.writeObject(chat);
            System.out.println("Serialized Chat object!");

        } catch (IOException e) {
            System.out.println("Error occurred while serialization of chat object: " + e.getMessage());
        }
    }

    //FIXME: DESERIALIZATION NOT WORKING CORRECT! (ARRAY

    /**
     * This method allows the deserialization of a Chat object from a file.
     * The method is marked as 'synchronized' to ensure coordinated access by multiple threads,
     * avoiding potential race conditions.
     *
     * @param filename The name of the file from which the Chat object is to be deserialized.
     * @return The deserialized Chat object or null if an error occurs.
     * @throws NullPointerException If the provided filename is null.
     */
    public static synchronized Chat deserializeChat(String filename) {
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