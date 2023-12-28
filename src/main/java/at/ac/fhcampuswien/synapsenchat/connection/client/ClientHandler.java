package at.ac.fhcampuswien.synapsenchat.connection.client;

import at.ac.fhcampuswien.synapsenchat.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class ClientHandler {
    public static void main(String[] args) {

        int port = 0;
        String ip = null;
        //String msg = null;
        String username = null;

        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        System.out.println("Input your Username: ");
        username = scanner.nextLine();

        System.out.println("Input the server IP you want to connect: ");
        ip = scanner.nextLine();

        System.out.println("Input your Port for Connection: ");
        port = scanner.nextInt();

        client.startConnection(ip, port);

        while (true) {

            System.out.println("Input Message(Enter to send): ");
            Message msg = new Message(scanner.nextLine());
            msg.setSenderName(username);
            msg.setTimestamp();
            //msg = new SimpleDateFormat("[dd.MM.yyyy HH:mm]").format(new Date()) + username + ": " + scanner.nextLine();
            client.sendMessage(msg);
            //client.receiveMessage();

        }

        //System.out.println("Input Message(Enter to send): ");
        //msg = scanner.nextLine();


    }
}