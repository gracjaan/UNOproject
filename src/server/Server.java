package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    static final int DEFAULT_PORT = 5050;
    private static ArrayList<ServerHandler> handlers = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
        Thread myServer = new Thread(server);
        myServer.start();
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
        int port = DEFAULT_PORT;
        ServerSocket listen = null;
        Socket connection = null;

        // wait for connection request and add the connection to handlers and start a new Thread and finally close the listener
        try {
            while (true) {
                listen = new ServerSocket(port);
                System.out.println("Listening on port " + port);
                connection = listen.accept();
                listen.close();
                ServerHandler sh = new ServerHandler(connection);
                handlers.add(sh);
                Thread sHThread = new Thread(sh);
                sHThread.start();
            }
        }catch (IOException e) {
            System.out.println("Connection couldn't be established.");
        }

    }
}

