package networking;

import model.player.factory.Player;

import java.util.ArrayList;

public class Lobby {
    public Lobby(String name) {
        this.players = new ArrayList<>();
        this.name = name;
    }
    private ArrayList<Player> players;
    private String name;

    public void addPlayer(Player p) {
        this.players.add(p);
    }
    public void removePlayer(Player p) {
        this.players.remove(p);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public String getName() {
        return name;
    }
}
