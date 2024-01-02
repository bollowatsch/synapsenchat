package at.ac.fhcampuswien.synapsenchat.connection.multithreading.server;


import at.ac.fhcampuswien.synapsenchat.connection.multithreading.server.Server;

public class ServerHandler {
    public static void main(String[] args) {
        Server server = new Server(5555);
    }
}
