package server;

import com.mysql.cj.jdbc.JdbcConnection;
import infra.JDBC;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServerChat {
    public static String ADDRESS;
    public static final int PORT = 4000;

    private ServerSocket serverSocket;
    private final List<ClientSocket> clients = new LinkedList<>();
    private Connection databaseConnection;

    public ServerChat(Connection connection) {
        this.databaseConnection = connection;
    }

    public void start() throws IOException {
        InetAddress host = InetAddress.getLocalHost();
        this.ADDRESS = host.getHostAddress();

        serverSocket = new ServerSocket(PORT);
        System.out.println("Connected on HOST -> " + ADDRESS + ": " + PORT);
        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        System.out.println("No Connections Yet.");
        while (true) {
            ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
            clients.add(clientSocket);
            new Thread(() -> clientMessageLoop(clientSocket)).start();
        }
    }

    private void clientMessageLoop(ClientSocket clientSocket) {
        String message;
        try {
            while ((message = clientSocket.getMessage()) != null ) {
                if ("stop".equalsIgnoreCase(message)) return;

                String[] messages = message.split(";");
                if (messages[0].equalsIgnoreCase("Register") && messages.length > 3 ) {
                    registerUser(message);
                } if (messages[0].equalsIgnoreCase("Login") && messages.length < 4) {
                    String loginResponse = loginUser(message);
                    System.out.println(loginResponse);
                    clientSocket.sendMessage("Response;" + loginResponse);
                } else {
                    System.out.println(clientSocket.getRemoteSocketAddress() + ": " + message );
                    sendMessageToAll(clientSocket, message);
                }
            }
        } finally {
             clientSocket.close();
        }
    }

    private void sendMessageToAll(ClientSocket owner, String message) {
        Iterator<ClientSocket> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientSocket clientSocket = iterator.next();
            if (!owner.equals(clientSocket)) {
                if (!clientSocket.sendMessage("Client " + owner.getRemoteSocketAddress() + ": " + message))
                    iterator.remove();
            }
        }
    }

    public void registerUser(String message) {
        String[] userData = message.split(";");

        JDBC databaseConnector = new JDBC(userData[1], userData[2], userData[3], userData[4], userData[5]);
        databaseConnector.createUser(this.databaseConnection);
    }

    public String loginUser(String message) {
        System.out.print("Usuario Logado:");
        String[] userData = message.split(";");

        JDBC databaseConnector = new JDBC(userData[1], userData[2]);
        boolean response = databaseConnector.login(this.databaseConnection);

        if (response) {
            return databaseConnector.getUserData();
        }
        return "Failed";
    }

}
