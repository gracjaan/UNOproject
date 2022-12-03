package model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<Player> winners;
    private int turnIndex;
    private int cardsPerHand;

    //--------------------------GETTERS--------------------------

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getWinners() {
        return winners;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public int getCardsPerHand() {
        return cardsPerHand;
    }


    //--------------------------SETTERS--------------------------

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setWinners(ArrayList<Player> winners) {
        this.winners = winners;
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public void setCardsPerHand(int cardsPerHand) {
        this.cardsPerHand = cardsPerHand;
    }
}
