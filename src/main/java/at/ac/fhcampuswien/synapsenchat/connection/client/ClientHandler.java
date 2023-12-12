package at.ac.fhcampuswien.synapsenchat.connection.client;

import java.io.IOException;

public class ClientHandler {
    public static void main(String[] args) throws IOException {
        int port = 12322;
        String ip = "127.0.0.1";

        Client client = new Client(ip, port);
        client.sendMessage(client.getSocket(), "TESTING!");
    }
}
