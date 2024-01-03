package at.ac.fhcampuswien.synapsenchat;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;
import at.ac.fhcampuswien.synapsenchat.logic.Message;

public class ChatTestSerialization {
    public static void main(String[] args) {
        Chat chat = new Chat("Chat#1");
        chat.addMessage(new Message("TestMessage#1", "Server"));
        chat.addMessage(new Message("TestMessage#2", "Server"));
        chat.addMessage(new Message("TestMessage#3", "Server"));
        chat.addMessage(new Message("TestMessage#4", "Server"));

        Chat chat2 = new Chat("Chat#2");
        chat2.addMessage(new Message("TestMessage#1", "Client"));
        chat2.addMessage(new Message("TestMessage#2", "Client"));
        chat2.addMessage(new Message("TestMessage#3", "Client"));
        chat2.addMessage(new Message("TestMessage#4", "Client"));

        Chat.serializeChat(chat, "chat1.txt");
        Chat.serializeChat(chat2, "chat2.txt");

        System.out.println(chat);
        System.out.println(chat2);

        chat.printAllMessages();
        chat2.printAllMessages();

        Chat chatCopy = Chat.deserializeChat("chat1.txt");
        Chat chat2Copy = Chat.deserializeChat("chat2.txt");

        System.out.println(chatCopy);
        System.out.println(chat2Copy);

        chatCopy.printAllMessages();
        chat2Copy.printAllMessages();

    }

}
