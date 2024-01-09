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

    public Instance(String ip, int port, Chat chat) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            this.socket = new Socket(inetAddress, port);
            this.messageQueue = new ArrayList<>();
            this.chat = chat;
        } catch (IOException e) {
            System.out.println("Error occurred while trying to create client: " + e.getMessage());
        }
    }

    public Instance(int port, Chat chat) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.messageQueue = new ArrayList<>();
            this.chat = chat;
        } catch (IOException e) {
            System.out.println("Error occurred while trying to create server: " + e.getMessage());
        }
    }

    protected void run() {
        new Thread(run).start();
    }

    protected Runnable run = () -> {
        runLogic();
    };

    abstract void runLogic();

    protected void start() {
        new Thread(run).start();
    }

    public void sendMessage(Message message) {
        messageQueue.add(message);
    }

    public synchronized static void terminate() {
        terminate = true;
    }
}