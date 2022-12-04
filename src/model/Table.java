package model;

import model.factory.Player;

import java.util.ArrayList;

public class Table {
    private ArrayList<Player> players;

    public Table(ArrayList<Player> players) {
        this.players = players;
    }
    //--------------------------METHODS--------------------------

    // host all queue functionalities in here? skip nextTurn etc. and then return the player or work with turnIndex in game.

    //--------------------------GETTERS--------------------------

    public ArrayList<Player> getPlayers() {
        return players;
    }

    //--------------------------SETTERS--------------------------


    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}

