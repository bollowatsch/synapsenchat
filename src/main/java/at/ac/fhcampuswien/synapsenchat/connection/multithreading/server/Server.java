package at.ac.fhcampuswien.synapsenchat.connection.multithreading.server;

import at.ac.fhcampuswien.synapsenchat.HelloController;
import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Chat chat;
    private ArrayList<Message> messageQueue;

    private HelloController helloController;

    private boolean terminate = false;

    public Server(int port, Chat chat) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.messageQueue = new ArrayList<>();
            this.helloController = new HelloController();
            this.chat = chat;
            startServer();
        } catch (IOException e) {
            System.out.println("Error occurred while trying to create server: " + e.getMessage());
        }
    }

    private void startServer() {
        new Thread(run).start();
    }

    private final Runnable run = () -> {
        try {
            System.out.println("Server started in new Thread! Waiting for connections...");

            Socket socket = serverSocket.accept();
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            MessageManager messageManager = new MessageManager(socket, oos, chat, this);

            //TODO: Connect sending / receiving logic to GUI.
            while (socket.isConnected() || !terminate) {
                synchronized (this) {

                    if (!messageQueue.isEmpty()) {
                        Message toSend = messageQueue.get(0);
                        messageManager.sendMessage(toSend);
                        messageQueue.remove(toSend);
                        Thread.sleep(500);
                    }
                }
            }

            chat.printAllMessages();
            oos.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * Adds Message to messageQueue and sends Message to Client or Server.
     *
     * @param message Message to be sent.
     */

    public void sendMessage(Message message) {
        messageQueue.add(message);
    }

    public void terminate() {
        this.terminate = true;
        //Thread.currentThread().interrupt();
    }
}