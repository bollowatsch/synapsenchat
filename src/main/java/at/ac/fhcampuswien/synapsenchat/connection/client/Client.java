package at.ac.fhcampuswien.synapsenchat.connection.client;
import at.ac.fhcampuswien.synapsenchat.connection.Message;

import java.net.*;
import java.util.*;
import java.io.*;
public class Client {
    private Socket clientSocket;
    //private PrintWriter out;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    //private BufferedReader in;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    public void startConnection(String ip, int port) {
        // while(true){

        try {
            Socket clientSocket = new Socket(ip, port);

            //out = new PrintWriter(clientSocket.getOutputStream(), true);
            //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outputStream = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            inputStream = clientSocket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            System.out.println("Connection established");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //   receiveMessage();
        // }
    }
    public void stopConnection() {
        try {
            inputStream.close();
            objectOutputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(Message msg) {
        try {
            objectOutputStream.writeObject(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void receiveMessage() {
        try {
            System.out.println(inputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
