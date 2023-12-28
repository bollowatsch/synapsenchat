package at.ac.fhcampuswien.synapsenchat.connection.server;

import at.ac.fhcampuswien.synapsenchat.connection.Message;

import java.net.*;
import java.io.*;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    //private BufferedReader in;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    void start(int port) {
        try {
            serverSocket = new ServerSocket(port);

            clientSocket = serverSocket.accept();
            System.out.println("Connection established");
            while (true) {


                //out = new PrintWriter(clientSocket.getOutputStream(), true);
                //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                outputStream = clientSocket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);

                inputStream = clientSocket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);

                System.out.println("Connection established");


                Message msg = null;
                try {
                    msg = (Message) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //String msg = in.readLine();

                System.out.println(msg);

                out.println(msg);

            }
        } catch (IOException e){
            System.out.println("Error " + e.getMessage());
        }
    }

    /*public Message receiveMessage(msg){
        Message msg = new Message(ObjectInputStream);
        return msg;
    }*/

    private void stop() throws IOException {
        objectInputStream.close();
        objectOutputStream.close();
        serverSocket.close();
        clientSocket.close();
    }
}