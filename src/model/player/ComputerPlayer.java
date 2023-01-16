package model.player;

import model.card.Card;
import model.player.factory.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ComputerPlayer extends Player {
    public ComputerPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public void playCard(Card card) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i< this.getTable().getCurrentPlayer().getHand().size()-1; i++) {
            if (this.getTable().getPlayingMode().validMove(this.getTable().getCurrentPlayer().getHand().get(i), this.getTable().getCurrentCard().getColor(), this.getTable().getCurrentCard().getValue(), this.getTable().getIndicatedColor())) {
                possibleMoves.add(i);
            }
        }
    }

    @Override
    public void pickColor() {
        Random r = new Random();
        int random = r.nextInt(4)+1;
        switch (random) {
            case 1:
                super.getTable().setIndicatedColor(Card.Color.BLUE);
                break;
            case 2:
                super.getTable().setIndicatedColor(Card.Color.RED);
                break;
            case 3:
                super.getTable().setIndicatedColor(Card.Color.GREEN);
                break;
            case 4:
                super.getTable().setIndicatedColor(Card.Color.YELLOW);
                break;
            default:
                pickColor();
        }
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
