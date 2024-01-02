package at.ac.fhcampuswien.synapsenchat.logic;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class IOManager {
    private Socket socket;
    private ArrayList<Message> messageQueue;
    private ArrayList<Message> sentMessages;
    private ArrayList<Message> receivedMessages;
    private boolean messageToSend;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public IOManager(Socket socket) {
        this.socket = socket;
        this.messageQueue = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
        this.messageToSend = false;
    }

    public synchronized void sendMessage(Message message) {
        messageToSend = true;
        messageQueue.add(message);
    }

    public synchronized void startSender(ObjectOutputStream oos) {
        this.oos = oos;
        new Thread(sender).start();
    }

    public synchronized void startReceiver(ObjectInputStream ois) {
        this.ois = ois;
        new Thread(receiver).start();
    }

    Runnable sender = () -> {
        System.out.println("IOManager (sender) started!");
        while (!socket.isClosed()) {

            synchronized (this) {

                if (messageToSend) {
                    try {
                        Message message = messageQueue.get(0);
                        messageQueue.remove(0);
                        oos.writeObject(message);
                        sentMessages.add(message);
                        messageToSend = false;
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

        printSentMessages();
    };

    Runnable receiver = () -> {
        System.out.println("IOManager (receiver) started!");
        while (!socket.isClosed()) {

            try {

                Message message = (Message) ois.readObject();
                System.out.println(message);
                receivedMessages.add(message);

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

        printReceivedMessages();
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
