package at.ac.fhcampuswien.synapsenchat;

import java.net.*;
import java.io.*;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    private void stop() throws IOException {
        in.close();
        out.close();
        serverSocket.close();
        clientSocket.close();
    }


}
