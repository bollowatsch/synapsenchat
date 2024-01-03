package at.ac.fhcampuswien.synapsenchat.connection.multithreading.server;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread{
    private final MessageManager messageManager;
    private Scanner sc = new Scanner(System.in);
    private boolean terminate = false;

    public Server(int port, Chat chat) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server online! Waiting for connections.");

            Socket socket = serverSocket.accept();
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            messageManager = new MessageManager(socket, oos, chat);
            messageManager.setChat(chat);

            //letting the ioManager start
            Thread.sleep(10);



            //Entering through console
            while (!socket.isClosed() || terminate) {
                System.out.println();
                System.out.print("Message: ");
                String input = sc.nextLine();

                if (!input.isEmpty()) {
                    if (input.equals("exit")) break;

                    Message message = new Message(input, "Server");
                    sendMessage(message);
                }
            }


            /*
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
    }

    private synchronized void sendMessage(Message message) throws IOException, InterruptedException {
        messageManager.sendMessage(message);
        Thread.sleep(50);
    }

    public void terminate() {
        this.terminate = true;
    }
}