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
        this.gameInProgress = false;
    }
    private ArrayList<Player> players;
    private String name;
    private UNO game;
    private boolean gameInProgress;

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

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}
