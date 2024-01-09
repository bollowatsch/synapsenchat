package at.ac.fhcampuswien.synapsenchat.connection;

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

    private static boolean terminate = false;

    public Client(String ip, int port, Chat chat) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            this.socket = new Socket(inetAddress, port);
            this.messageQueue = new ArrayList<>();
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
            while (socket.isConnected() && !terminate) {
                synchronized (this) {

                    if (!messageQueue.isEmpty()) {
                        Message toSend = messageQueue.get(0);
                        messageManager.sendMessage(toSend);
                        messageQueue.remove(toSend);
                        Thread.sleep(500);
                    }
                }
            }

            oos.close();
            System.out.println("Client terminated!");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    public void sendMessage(Message message) {
        messageQueue.add(message);
    }

    public synchronized static void terminate() {
        terminate = true;
    }
}