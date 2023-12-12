package at.ac.fhcampuswien.synapsenchat.connection.server;

import java.net.*;
import java.io.*;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Connection established");

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String msg = in.readLine();

                System.out.println(msg);

                out.println(msg);

                stop();
            }
        } catch (IOException e){
            System.out.println("Error " + e.getMessage());
        }
    }

    private void stop() throws IOException {
        in.close();
        out.close();
        serverSocket.close();
        clientSocket.close();
    }
}