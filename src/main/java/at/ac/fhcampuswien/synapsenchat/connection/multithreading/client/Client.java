package at.ac.fhcampuswien.synapsenchat.connection.multithreading.client;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Chat chat;

    private boolean terminate = false;

    public Client(String ip, int port, Chat chat) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            this.socket = new Socket(inetAddress, port);
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
            Scanner sc = new Scanner(System.in);

            MessageManager messageManager = new MessageManager(socket, oos, chat);

            //TODO: Connect sending / receiving logic to GUI.
            //Entering through console
            while (!socket.isClosed() || terminate) {
                System.out.println();
                System.out.print("Message: ");
                String input = sc.nextLine();

                if (!input.isEmpty()) {
                    if (input.equals("exit")) break;

                    Message message = new Message(input, "Client");
                    messageManager.sendMessage(message);
                    Thread.sleep(500);
                }
            }

            /*
            // Sending 10 messages. (type 'exit' to exit)
            for (int i = 1; i < 11; i++) {
                sendMessage(new Message(i + " ", "Client"));
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