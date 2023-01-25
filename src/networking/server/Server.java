package networking.server;

import controller.UNO;
import model.player.factory.Player;
import networking.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    public Server() {
        uno = new UNO();
    }
    static final int DEFAULT_PORT = 5050;
    private static ArrayList<ServerHandler> handlers = new ArrayList<>();
    private UNO uno;
    private ArrayList<Lobby> lobbies = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

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
                ServerHandler sh = new ServerHandler(connection, this);
                handlers.add(sh);
                Thread sHThread = new Thread(sh,"Gracjan");
                sHThread.start();
            }
        }catch (IOException e) {
            System.out.println("Connection couldn't be established.");
        }

    }
    //-----------------------------GETTERS & SETTERS-----------------------------
    public void addLobby(Lobby lobby) {
        this.lobbies.add(lobby);
    }
    public Lobby getLobby(String lobbyName) {
        for (Lobby l: lobbies) {
            if (l.getName().equals(lobbyName)) {
                return l;
            }
        }
        return null;
    }

    public UNO getUno() {
        return uno;
    }

    public ArrayList<ServerHandler> getHandlers() {
        return handlers;
    }

    public void setUno(UNO uno) {
        this.uno = uno;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}

