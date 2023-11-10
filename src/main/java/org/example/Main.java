package org.example;

import server.ServerChat;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(" -- Server Console -- ");
        try {
            ServerChat serverChat = new ServerChat();
            serverChat.start();
        } catch (IOException error) {
            System.out.println("Error on starting server: " + error.getMessage());
        }
        System.out.println(" -- Server Stooped -- ");
    }
}