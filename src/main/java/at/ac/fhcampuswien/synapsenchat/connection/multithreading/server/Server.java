package at.ac.fhcampuswien.synapsenchat.connection.multithreading.server;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Chat chat;

    private boolean terminate = false;

    public Server(int port, Chat chat) {
        try {
            this.serverSocket = new ServerSocket(port);
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
            Scanner sc = new Scanner(System.in);

            MessageManager messageManager = new MessageManager(socket, oos, chat);

            //TODO: Connect sending / receiving logic to GUI.
            //Entering through console
            while (!socket.isClosed() || !terminate) {
                System.out.println();
                System.out.print("Message: ");
                String input = sc.nextLine();

                if (!input.isEmpty()) {
                    if (input.equals("exit")) break;

                    Message message = new Message(input, "Server");
                    messageManager.sendMessage(message);
                    Thread.sleep(500);
                }
            }

            /*
            // Sending 10 messages. (type 'exit' to exit)
            for (int i = 1; i < 11; i++) {
                Message message = new Message(String.format("Message %d", i), "Server");
                sendMessage(message);
            }

            while (!sc.nextLine().equals("exit")) {
                continue;
            }
             */

            chat.printAllMessages();
            oos.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    public void terminate() {
        this.terminate = true;
    }
}