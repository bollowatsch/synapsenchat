package at.ac.fhcampuswien.synapsenchat.connection.multithreading.client;

import at.ac.fhcampuswien.synapsenchat.logic.MessageManager;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private final MessageManager messageManager;
    private Scanner sc = new Scanner(System.in);
    private boolean terminate = false;

    Client(String ip, int port) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(ip);

        try {
            Socket socket = new Socket(inetAddress, port);
            System.out.println("Connection established!");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            messageManager = new MessageManager(socket, oos);

            //letting the ioManager start
            Thread.sleep(10);


            //Entering through console
            /*while (!socket.isClosed() || terminate) {
                System.out.println();
                System.out.print("Message: ");
                String input = sc.nextLine();

                if (!input.isEmpty()) {
                    if (input.equals("exit")) break;

                    Message message = new Message(input, "Client");
                    sendMessage(message);
                }
            }
            */

            for (int i = 1; i < 11; i++) {
                sendMessage(new Message(i + " ", "Client"));
            }

            while (!sc.nextLine().equals("exit")) {
                continue;
            }

            oos.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendMessage(Message message) throws IOException, InterruptedException {
        messageManager.sendMessage(message);
        Thread.sleep(500);
    }

    public void terminate() {
        this.terminate = true;
    }
}