package org.example;

import infra.ConnectDB;
import infra.JDBC;
import server.ServerChat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {

        Connection databaseConnection = null;
        try {
            ConnectDB database = new ConnectDB();
            databaseConnection =  database.connect();
            System.out.println(" -- DataBase Connected -- ");

            JDBC connector = new JDBC();
            connector.createTable(databaseConnection);
            System.out.println(" -- Users Created -- ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(" -- Server Console -- ");
        try {
            ServerChat serverChat = new ServerChat(databaseConnection);
            serverChat.start();
        } catch (IOException error) {
            System.out.println("Error on starting server: " + error.getMessage());
        }
        System.out.println(" -- Server Stooped -- ");
    }
}