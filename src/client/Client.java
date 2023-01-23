package client;

import server.Server;

import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.currentThread;

public class Client implements Runnable {
    static final int DEFAULT_PORT = 5050;
    public static void main(String[] args) {
        Client client = new Client();
        Thread myThread = new Thread(client);
        myThread.start();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
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
            Thread cht = new Thread(ch);
            cht.start();

        }catch (IOException e) {
            System.out.println("gotcha again");
        }
    }
}
