package at.ac.fhcampuswien.synapsenchat.logic;

import at.ac.fhcampuswien.synapsenchat.connection.multithreading.client.Client;
import at.ac.fhcampuswien.synapsenchat.connection.multithreading.server.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MessageManager {
    private Socket socket;
    private ArrayList<Message> messageQueue;
    private ArrayList<Message> sentMessages;
    private ArrayList<Message> receivedMessages;
    private boolean messageToSend;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Chat chat;
    private Server server;
    private Client client;

    public MessageManager(Socket socket, ObjectOutputStream oos, Chat chat, Object object) throws IOException {
        this.socket = socket;
        this.messageQueue = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
        this.messageToSend = false;
        this.chat = chat;

        if (object instanceof Server) this.server = (Server) object;
        if (object instanceof Client) this.client = (Client) object;

        this.oos = oos;
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.startReceiver(ois);
        this.startSender(oos);
    }

    public synchronized void sendMessage(Message message) {
        messageToSend = true;
        messageQueue.add(message);
    }

    private void startSender(ObjectOutputStream oos) {
        this.oos = oos;
        new Thread(sender).start();
    }

    private void startReceiver(ObjectInputStream ois) {
        this.ois = ois;
        new Thread(receiver).start();
    }

    private final Runnable sender = () -> {
        //System.out.println("MessageManager (sender) started!");
        while (socket.isConnected()) {

            synchronized (this) {

                //ToDo: Alle Nachrichten senden, nicht nur die erste!
                if (messageToSend) {
                    try {
                        Message message = messageQueue.get(0);

                        messageQueue.remove(0);
                        oos.writeObject(message);
                        sentMessages.add(message);
                        messageToSend = false;

                        try {
                            chat.addMessage(message);
                        } catch (NullPointerException ignored) {
                        }

                    } catch (java.net.SocketException e) {
                        System.out.println("java.net.SocketException");
                        printSentMessages();
                        break;
                    } catch (IOException e) {
                        System.out.println("Error occurred while trying to send message.");
                        System.out.println(e.getMessage());
                        break;
                    }
                }
            }
        }
    };

    private final Runnable receiver = () -> {
        //System.out.println("MessageManager (receiver) started!");
        while (socket.isConnected()) {

                //TODO: Empfangene Nachrichten ins GUI übertragen!
                try {

                    Message message = (Message) ois.readObject();
                    receivedMessages.add(message);
                    chat.addMessage(message);

                } catch (ClassNotFoundException e) {
                    System.out.println("Error occurred while receiving message.");
                    System.out.println(e.getMessage());
                    printReceivedMessages();
                    break;
                } catch (EOFException e) {
                    System.out.println("Stream ended!");
                    break;
                } catch (IOException e) {
                    System.out.println("RuntimeException");
                    break;
                }
            }
    };

    private void printReceivedMessages() {
        System.out.println("Printing all received Messages...");
        receivedMessages.forEach(System.out::println);
    }

    private void printSentMessages() {
        System.out.println("Printing all sent Messages...");
        sentMessages.forEach(System.out::println);
    }
}