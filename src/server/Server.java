package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int DEFAULT_PORT = 5050;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        ServerSocket listen;
        Socket connection = null;

        // wait for connection request and close the listener after
        try {
            listen = new ServerSocket(port);
            System.out.println("Listening on port " + port);
            connection = listen.accept();
            listen.close();
        }catch (IOException e) {
            System.out.println("Connection couldn't be established.");
        }

        // create a handler to do a handshake and exchange messages.
        try {
            ServerHandler SH = new ServerHandler(connection);
            SH.doHandshake();
            System.out.println("Connected.");
            while(true) {
                SH.receiveMessage();
                SH.sendMessage();
            }
        }catch(IOException e) {
            System.out.println("Sorry an error has occured, connection lost.");
            System.out.println("Error: " + e);
        }
    }
}
