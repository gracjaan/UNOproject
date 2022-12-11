package model.player;

import model.card.Card;
import model.player.factory.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ComputerPlayer extends Player {
    public ComputerPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public void playCard(Card card) {
        
    }

    @Override
    public void draw(int amount) {

    }

    @Override
    public boolean checkDrawPossibility(int amount) {
        return false;
    }

    @Override
    public void pickColor() {

    }

    @Override
    public boolean isWinner() {
        return false;
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
