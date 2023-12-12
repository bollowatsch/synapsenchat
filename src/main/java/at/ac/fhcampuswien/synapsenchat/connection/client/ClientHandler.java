package at.ac.fhcampuswien.synapsenchat.connection.client;

import java.io.IOException;
import java.util.Scanner;

public class ClientHandler {
    public static void main(String[] args){

        int port;
        String ip;
        String msg;

        Scanner scanner = new Scanner(System.in);

        Client client = new Client();

        System.out.println("Inport the server IP you want to connect: ");
        ip = scanner.nextLine();

        System.out.println("Input your Port for Connection: ");
        port = scanner.nextInt();

        client.startConnection(ip,port);

        System.out.println("Input Message(Enter to send): ");
        msg = scanner.nextLine();
        client.sendMessage(msg);

        client.receiveMessage();
    }

}
