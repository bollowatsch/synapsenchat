package at.ac.fhcampuswien.synapsenchat.connection;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.Message;
import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;

import java.io.IOException;
import java.io.ObjectOutputStream;


public class Client extends Instance {
    /**
     * Class for client connection with socket.
     * @param ip The IP address of the server to connect.
     * @param port The port of the server to connect.
     * @param chat The Chat instance for using.
     */

    public Client(String ip, int port, Chat chat) {
        super(ip, port, chat);
        start();
    }

    @Override
    void runLogic() {
        try {
            System.out.println("Client started in new Thread! ");
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            MessageManager messageManager = new MessageManager(socket, oos, chat);

            //TODO: Connect sending / receiving logic to GUI.
            //Entering through console
            while (socket.isConnected() && !terminate) {
                synchronized (this) {

                    if (!messageQueue.isEmpty()) {
                        Message toSend = messageQueue.remove(0);
                        messageManager.sendMessage(toSend);
                        // wait to fix deadlock
                        wait(500);
                    }
                }
            }

            oos.close();
            System.out.println("Client terminated!");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Client terminated!");
        }
    }
}