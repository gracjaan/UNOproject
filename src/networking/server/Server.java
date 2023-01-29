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
        lobbies = new ArrayList<>();
        Lobby l = new Lobby("main");
        lobbies.add(l);
        currentGames = new ArrayList<>();
    }
    static final int DEFAULT_PORT = 5050;
    private static ArrayList<ServerHandler> handlers = new ArrayList<>();
    private ArrayList<Lobby> lobbies;
    // todo for multiple games at the same time you should access the correct currentGame instead of uno. correspondingPlayer as argument
    private ArrayList<UNO> currentGames;
    // todo current Games property
    public static void main(String[] args) {
        Server server = new Server();
        Thread myServer = new Thread(server);
        myServer.start();
        }
    public Lobby getLobby(Player p) {
        return this.lobbies.get(getLobbyIndex(p));
    }
    public int getLobbyIndex(Player p) {
        // todo check if correct
        for(int i = 0; i<lobbies.size();i++) {
            for (Player player: lobbies.get(i).getPlayers()) {
                if (p.getNickname().equals(player.getNickname())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ArrayList<Player> getPlayersInLobby(Player p) {
        return this.lobbies.get(getLobbyIndex(p)).getPlayers();
    }
    public Lobby getMainLobby() {
        return this.lobbies.get(0);
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

    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public ArrayList<UNO> getCurrentGames() {
        return currentGames;
    }

    public UNO getUno(Player p) {
        // todo add and remove uno from current games when ended.
       Lobby l = this.lobbies.get(getLobbyIndex(p));
        return l.getGame();
    }

    public ArrayList<ServerHandler> getHandlers() {
        return handlers;
    }

}

