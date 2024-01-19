package at.ac.fhcampuswien.synapsenchat.connection;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.Message;
import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Instance {
    /**
     * Class for starting the server socket using the port and chat instance.
     * @param port The port to open a connection.
     * @param chat The Chat instance for using.
     */

    public Server(int port, Chat chat) {
        super(port, chat);
        start();
    }

    @Override
    void runLogic() {
        try {
            System.out.println("Server started in new Thread! Waiting for connections...");

            Socket socket = serverSocket.accept();
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            MessageManager messageManager = new MessageManager(socket, oos, chat);

            //TODO: Connect sending / receiving logic to GUI.

            while (socket.isConnected() && !terminate) {
                synchronized (this) {

                    if (!messageQueue.isEmpty()) {
                        Message toSend = messageQueue.remove(0);
                        messageManager.sendMessage(toSend);
                        wait(500);
                    }
                }
            }

            oos.close();
            System.out.println("Server terminated!");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Server terminated!");
        }
    }
}