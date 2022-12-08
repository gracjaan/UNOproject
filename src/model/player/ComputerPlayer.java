package model.player;

import model.card.Card;
import model.player.factory.Player;
import model.table.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class ComputerPlayer extends Player {
    public ComputerPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public boolean playCard(Card card) {

        return false;
    }

    @Override
    public void draw(int amount) {

    }

    @Override
    public Card.Color pickColor() {
        return null;
    }

    public ArrayList<Card> getValidMoves() {
        return null;
    }
    // make a dictionary with score as key and card as value
    public HashMap<Integer, Card> assignScores() {
        return null;
    }
    public Card determineBestMove() {
        return null;
    }
}
