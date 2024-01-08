package at.ac.fhcampuswien.synapsenchat.logs;

import at.ac.fhcampuswien.synapsenchat.logic.Chat;

public class Main {
    public static void main(String[] args) {
        Chat test = Chat.deserializeChat("src/main/java/at/ac/fhcampuswien/synapsenchat/logs/1.txt");

        try {
            test.getAllMessages().forEach(System.out::println);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException caught: " + e.getMessage());
        }

    }
}