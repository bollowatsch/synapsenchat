package at.ac.fhcampuswien.synapsenchat.connection.server;

import java.io.IOException;

public class ServerHandler {
    public static void main(String[] args) throws IOException {
        int port = 12322;

        Server server = new Server();
        server.start(port);
    }
}
