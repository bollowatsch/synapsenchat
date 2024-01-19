package at.ac.fhcampuswien.synapsenchat.logic;

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

    public MessageManager(Socket socket, ObjectOutputStream oos, Chat chat) throws IOException {
        this.socket = socket;
        this.messageQueue = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
        this.messageToSend = false;
        this.chat = chat;


        this.oos = oos;
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.startReceiver(ois);
        this.startSender(oos);
    }

    private synchronized boolean close() {
        try {
            oos.close();
            ois.close();
        } catch (IOException ignored) {
            return false;
        }
        return true;
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
        while (socket.isConnected()) {

            synchronized (this) {

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
                        break;
                    } catch (IOException e) {
                        System.out.println("Error occurred while trying to send message.");
                        System.out.println(e.getMessage());
                        break;
                    }
                }
            }
        }

        if (close()) {
            System.out.println("MM (sender) closed!");
        }
    };

    private final Runnable receiver = () -> {
        while (socket.isConnected()) {

            //TODO: Proper Exception handling and terminating Server -> MessageManager -> HelloApplication!
            try {

                Message message = (Message) ois.readObject();
                receivedMessages.add(message);
                chat.addMessage(message);

            } catch (ClassNotFoundException e) {
                System.out.println("Error occurred while receiving message.");
                break;
            } catch (EOFException e) {
                System.out.println("Stream ended!");
                break;
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                break;
            } catch (RuntimeException e) {
                System.out.println("RuntimeException: " + e.getMessage());
                break;
            }
        }

        if (close()) {
            System.out.println("MM (receiver) closed!");
        }
    };
}