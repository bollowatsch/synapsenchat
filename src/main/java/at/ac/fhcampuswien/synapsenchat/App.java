package at.ac.fhcampuswien.synapsenchat;

import java.io.IOException;
import java.net.ServerSocket;

public class App {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(2828);

        Client client = new Client();
        client.startConnection("127.0.0.1", 2828);
    }
}
