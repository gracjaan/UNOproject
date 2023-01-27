package networking;

import controller.UNO;
import model.player.factory.Player;

import java.util.ArrayList;

public class Lobby {
    // to do Lobby should hold the game property -> since each Lobby has its own game
    public Lobby(String name) {
        this.players = new ArrayList<>();
        this.name = name;
        this.game = new UNO();
    }
    private ArrayList<Player> players;
    private String name;
    private UNO game;

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

    public UNO getGame() {
        return game;
    }

}
