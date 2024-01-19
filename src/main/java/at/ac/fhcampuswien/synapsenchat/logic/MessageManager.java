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
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final Chat chat;

    /**
     * Manages communication between the client and server by handling message sending and receiving.
     * This class is responsible for setting up the communication streams and initiating separate threads
     * for sending and receiving messages.
     *
     * @param socket The Socket object representing the communication link with the server.
     * @param oos    The ObjectOutputStream for sending messages to the server.
     * @param chat   The Chat object associated with this MessageManager.
     * @throws IOException If an I/O error occurs while initializing the input/output streams.
     */
    public MessageManager(Socket socket, ObjectOutputStream oos, Chat chat) throws IOException {
        this.socket = socket;
        this.messageQueue = new ArrayList<>();
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

    private synchronized void addMessageToChat(Message message) {
        try {
            chat.addMessage(message);
        } catch (Exception e) {
            throw new RuntimeException("Error adding Message to chat", e);
        }
    }

    /**
     * A Runnable implementation for handling message sending in a separate thread.
     * This sender continually checks for messages in the message queue and sends them over the network.
     * It operates in a synchronized manner to ensure thread safety during message sending.
     * Upon encountering exceptions, such as SocketException or IOException, the sender will break out of the loop
     * and print corresponding error messages. Additionally, it checks for a graceful termination request by
     * calling the 'close()' method. The method logs the closure of the MessageManager (sender) upon completion.
     */
    private final Runnable sender = () -> {
        while (socket.isConnected()) {

            synchronized (this) {

                try {

                    if (!messageQueue.isEmpty()) {
                        Message message = messageQueue.remove(0);
                        oos.writeObject(message);
                        addMessageToChat(message);
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

        if (close()) {
            System.out.println("MM (sender) closed!");
        }
    };

    /**
     * A Runnable implementation for handling message reception in a separate thread.
     * This receiver continuously reads incoming messages from the network, adds them to the receivedMessages list,
     * and updates the associated Chat using the addMessageToChat method.
     * It operates within a try-catch block to gracefully handle potential exceptions during the reception process.
     * The supported exceptions include ClassNotFoundException, EOFException, IOException, and RuntimeException.
     * If an exception occurs, an error message is printed, providing details about the issue.
     * The receiver checks for a graceful termination request by calling the 'close()' method. Upon completion,
     * it logs the closure of the MessageManager (receiver).
     */
    private final Runnable receiver = () -> {
        while (socket.isConnected()) {

            //TODO: Proper Exception handling and terminating Server -> MessageManager -> HelloApplication!
            try {

                Message message = (Message) ois.readObject();
                addMessageToChat(message);

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