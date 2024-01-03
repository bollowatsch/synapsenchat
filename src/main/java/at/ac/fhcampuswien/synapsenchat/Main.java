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

        Chat.serializeChat(chat, "src/main/java/at/ac/fhcampuswien/synapsenchat/logs/chat.txt");

        Chat chat2 = new Chat("Chat2");
        chat2.addMessage(new Message("Message of chat 2", "tintifax"));
        chat2.addMessage(new Message("Another message of chat 2", "fredi faulich"));

        Chat.serializeChat(chat2, "src/main/java/at/ac/fhcampuswien/synapsenchat/logs/chat2.txt");

        System.out.println("Original chat 2 object: ");
        System.out.println(chat2);

        Chat chat2copy = Chat.getChatByID(2);
        System.out.println("Copy of chat 2: ");
        System.out.println(chat2copy);

        Chat chat2copyCOPY = Chat.deserializeChat("src/main/java/at/ac/fhcampuswien/synapsenchat/logs/chat2.txt");
        System.out.println("COPY COPY of chat2: ");
        System.out.println(chat2copyCOPY);

        for (Chat chats: Chat.getChats()) {
            ArrayList<Message> list = chats.getAllMessages();
            for (Message ms : list) System.out.printf("[%d] %s\n", chats.getID(), ms);
        }

        System.out.println("Get chat by id 2:");
        System.out.println(chat2);
        System.out.println(Chat.getChatByID(2));
        if (chat2.equals(Chat.getChatByID(1))) System.out.println("Chats are equal!");

        System.out.println(Chat.getChats());

        //System.out.println(Chat.getChatByID(2).getAllMessages().get(1));

    }
}