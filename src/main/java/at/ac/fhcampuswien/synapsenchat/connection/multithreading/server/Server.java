package at.ac.fhcampuswien.synapsenchat.connection.multithreading.server;

import at.ac.fhcampuswien.synapsenchat.logic.IOManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    ArrayList<Message> messages = new ArrayList<>();
    private IOManager ioManager;
    private Scanner sc = new Scanner(System.in);

    Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server online! Waiting for connections.");

            socket = serverSocket.accept();
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            ioManager = new IOManager(socket);
            ioManager.startSender(oos);
            ioManager.startReceiver(new ObjectInputStream(socket.getInputStream()));

            //letting the ioManager start
            Thread.sleep(10);

            /*
            for (int i = 1; i < 6; i++) {
                Message message = new Message(String.format("Test Message %d", i), "Server");
                sendMessage(message);
            }

             */

            while (!socket.isClosed()) {
                System.out.println();
                System.out.print("Message: ");
                String input = sc.nextLine();

                if (!input.isEmpty()) {
                    if (input.equals("exit")) break;

                    Message message = new Message(input, "Server");
                    sendMessage(message);
                }
            }

            oos.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendMessage(Message message) throws IOException, InterruptedException {
        messages.add(message);
        ioManager.sendMessage(message);
        Thread.sleep(500);
    }
}