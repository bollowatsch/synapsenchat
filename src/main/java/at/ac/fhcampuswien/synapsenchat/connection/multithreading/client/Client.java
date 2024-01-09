package at.ac.fhcampuswien.synapsenchat.connection.multithreading.client;

import at.ac.fhcampuswien.synapsenchat.HelloController;
import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private Socket socket;
    private Chat chat;
    private ArrayList<Message> messageQueue;
    private ArrayList<Message> receivedMessages;

    private HelloController helloController;

    private boolean terminate = false;

    public Client(String ip, int port, Chat chat) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            this.socket = new Socket(inetAddress, port);
            this.messageQueue = new ArrayList<>();
            this.receivedMessages = new ArrayList<>();
            this.helloController = new HelloController();
            this.chat = chat;
            startClient();
        } catch (IOException e) {
            System.out.println("Error occurred while trying to create client: " + e.getMessage());
        }
    }

    private void startClient() {
        new Thread(run).start();
    }

    private final Runnable run = () -> {
        try {
            System.out.println("Client started in new Thread! ");
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            MessageManager messageManager = new MessageManager(socket, oos, chat, this);

            //TODO: Connect sending / receiving logic to GUI.
            //Entering through console
            while (socket.isConnected() || !terminate) {
                synchronized (this) {

                    if (!messageQueue.isEmpty()) {
                        Message toSend = messageQueue.get(0);
                        messageManager.sendMessage(toSend);
                        messageQueue.remove(toSend);
                    }

                    if (!receivedMessages.isEmpty()) {
                        try {
                            //helloController.onReceivedMessage(receivedMessages.get(0));
                            System.out.println("Client run loop: " + receivedMessages.get(0));
                            receivedMessages.remove(0);
                        } catch (Exception e) {
                            System.out.println("ERROR WHILE SENDING RECEIVED MESSAGE TO GUI!");
                            System.out.println(e.getMessage());
                        }
                    }

                    Thread.sleep(500);
                }
            }

            chat.printAllMessages();
            oos.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    public void sendMessage(Message message) {
        messageQueue.add(message);
    }

    public void receiveMessage(Message message) {
        receivedMessages.add(message);
    }

    public void terminate() {
        this.terminate = true;
        Thread.currentThread().interrupt();
    }
}