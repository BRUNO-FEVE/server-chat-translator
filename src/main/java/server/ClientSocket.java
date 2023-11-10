package server;
import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientSocket {
    private final Socket socket;
    private final BufferedReader entry;
    private final PrintWriter exit;

    public ClientSocket(final Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Connected to: " + this.socket.getRemoteSocketAddress());

        this.entry = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.exit = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketAddress getRemoteSocketAddress () {
        return socket.getRemoteSocketAddress();
    }

    public void close() {
        try {
            entry.close();
            exit.close();
            socket.close();
        } catch (IOException error) {
            System.out.println("Error on Close: " + error.getMessage() );
        }
    }

    public String getMessage() {
        try {
            return entry.readLine();
        } catch (IOException error) {
            return null;
        }
    }

    public boolean sendMessage(String message) {
        exit.println(message);
        return !exit.checkError();
    }
}
