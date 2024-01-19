package at.ac.fhcampuswien.synapsenchat.connection;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public abstract class Instance {
    protected ServerSocket serverSocket;
    protected Socket socket;
    protected Chat chat;
    protected ArrayList<Message> messageQueue;

    protected static boolean terminate = false;

    /**
     * Initializes a socket connection to the specified IP address and port, sets up a message queue,
     * and associates the instance with the provided Chat object.
     *
     * @param ip   The IP address for the socket connection.
     * @param port The port number for the socket connection.
     * @param chat The Chat object associated with this Instance.
     * @throws NullPointerException If ip or chat is null.
     */
    protected Instance(String ip, int port, Chat chat) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            this.socket = new Socket(inetAddress, port);
            this.messageQueue = new ArrayList<>();
            this.chat = chat;
        } catch (IOException e) {
            System.out.println("Error occurred while trying to create client: " + e.getMessage());
        }
    }

    /**
     * Initializes a server socket on the specified port, sets up a message queue, and associates the instance
     * with the provided Chat object.
     *
     * @param port The port number for the server socket.
     * @param chat The Chat object associated with this server-side Instance.
     */
    protected Instance(int port, Chat chat) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.messageQueue = new ArrayList<>();
            this.chat = chat;
        } catch (IOException e) {
            System.out.println("Error occurred while trying to create server: " + e.getMessage());
        }
    }

    protected Runnable run = this::runLogic;

    abstract void runLogic();

    /**
     * Initiates the execution of the instance by starting a new thread to run its associated logic.
     */
    protected void start() {
        new Thread(run).start();
    }

    public void sendMessage(Message message) {
        messageQueue.add(message);
    }

    public static synchronized void terminate() {
        terminate = true;
    }
}