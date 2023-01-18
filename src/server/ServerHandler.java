package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerHandler {
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    public ServerHandler(Socket connection) throws IOException {
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
    }
    public void doHandshake() throws IOException {
        //send HS to client
        out.println("Tocjan");
        out.flush();
        String messageIn = in.readLine();
        if (!messageIn.equals("Tocjan")) {
            throw new IOException("Wrong client connected.");
        }
        System.out.println("Connection successful.");
    }
    public void sendMessage() throws IOException {
        System.out.print("SEND: ");
        String messageOut = "hi wassup";
        out.println(messageOut);
        out.flush();
        if(out.checkError()) {
            System.out.println("An error occured during transmission.");
        }
    }
    public void receiveMessage() throws IOException {
        System.out.println("WAITING...");
        String messageIn = in.readLine();
        System.out.println("RECEIVED: " + messageIn);
    }
    public void closeConnection() throws IOException {
        out.println("Closing the Server.");
        out.flush();
        connection.close();
        System.out.println("Connection Closed.");
        System.exit(1);
    }
}
