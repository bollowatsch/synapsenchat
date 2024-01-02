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

        Chat chat2 = new Chat("Chat2");
        chat2.addMessage(new Message("Message of chat 2", "tintifax"));
        chat2.addMessage(new Message("Another message of chat 2", "fredi faulich"));

        for (Chat chats: Chat.getChats()) {
            ArrayList<Message> list = chats.getAllMessages();
            for (Message ms : list) System.out.printf("[%d] %s\n", chats.getID(), ms);
        }

    }
}
