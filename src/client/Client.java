package client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    static final int DEFAULT_PORT = 5050;
    public static void main(String[] args) {
        String computer = "localhost";
        int port;
        Socket connection = null;

        // get Computer and port number
        port = DEFAULT_PORT;

        // Open connection to server
        try {
            System.out.println("connecting to localhost on port 5050");
            connection = new Socket(computer, port);
        } catch (IOException e) {
            System.out.println("Hahah caught u");
        }
        // create handler to do the handshake and exchange messages.
        try {
            ClientHandler ch = new ClientHandler(connection);
            ch.doHandshake();
            System.out.println("connected");

            while(true) {
                ch.sendMessage();
                ch.receiveMessage();
            }
        }catch (IOException e) {
            System.out.println("gotcha again");
        }


    }
}
