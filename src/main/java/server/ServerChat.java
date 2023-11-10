package server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServerChat {
    public static String ADDRESS;
    public static final int PORT = 4000;

    private ServerSocket serverSocket;
    private final List<ClientSocket> clients = new LinkedList<>();

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
                System.out.println(clientSocket.getRemoteSocketAddress() + ": " + message );
                sendMessageToAll(clientSocket, message);
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

}
