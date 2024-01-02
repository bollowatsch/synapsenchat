package at.ac.fhcampuswien.synapsenchat.connection.multithreading.client;

import at.ac.fhcampuswien.synapsenchat.logic.IOManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private InetAddress inetAddress;
    private Socket socket;
    private IOManager ioManager;
    private Scanner sc = new Scanner(System.in);

    Client(String ip, int port) throws UnknownHostException {
        inetAddress = InetAddress.getByName(ip);

        try {
            socket = new Socket(inetAddress, port);
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            ioManager = new IOManager(socket);
            ioManager.startReceiver(new ObjectInputStream(socket.getInputStream()));
            ioManager.startSender(oos);

            //letting the ioManager start
            Thread.sleep(10);

            while (!socket.isClosed()) {
                System.out.println();
                System.out.print("Message: ");
                String input = sc.nextLine();

                if (!input.isEmpty()) {
                    if (input.equals("exit")) break;

                    Message message = new Message(input, "Client");
                    sendMessage(message);
                }
            }

            oos.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendMessage(Message message) throws IOException, InterruptedException {
        ioManager.sendMessage(message);
        Thread.sleep(500);
    }
}