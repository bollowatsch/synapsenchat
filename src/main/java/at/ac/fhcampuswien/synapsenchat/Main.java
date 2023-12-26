package at.ac.fhcampuswien.synapsenchat;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Chat chat = new Chat("Chat1");

        chat.addMessage(new Message("Hello World!", "Nico"));
        chat.addMessage(new Message("TEST", "Nico"));
        chat.addMessage(new Message("1234", "Nico"));

        ArrayList<Message> list = chat.getAllMessages();
        for (Message ms : list) System.out.printf("[%d] %s\n", chat.getID(), ms);
    }
}
