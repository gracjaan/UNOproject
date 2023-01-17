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
    private ArrayList<Integer> possibleMoves = new ArrayList<>();
    @Override
    public void playCard(Card card) {
        for (int i = 0; i< this.getTable().getCurrentPlayer().getHand().size()-1; i++) {
            if (this.getTable().getPlayingMode().validMove(this.getTable().getCurrentPlayer().getHand().get(i), this.getTable().getCurrentCard().getColor(), this.getTable().getCurrentCard().getValue(), this.getTable().getIndicatedColor())) {
                possibleMoves.add(i);
            }
        }
        placeCard(determineBestMove());
        Player nextPlayer;
        if (super.getTable().getCurrentTurnIndex()<super.getTable().getPlayers().size()-1) {
            nextPlayer = super.getTable().getPlayers().get(super.getTable().getCurrentTurnIndex()+1);
        }
        else {
            nextPlayer = super.getTable().getPlayers().get(0);
        }
        super.getTable().getPlayingMode().performWildCardAction(card, this, nextPlayer);

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

    public ArrayList<Card> getValidMoves() {
        return null;
    }
    // make a dictionary with score as key and card as value
    public HashMap<Integer, Card> assignScores() {
        return null;
    }
    // currently random move
    public Card determineBestMove() {
        Random r = new Random();
        int random = r.nextInt(this.getHand().size()) ;
        Card card = super.getHand().get(random);
        return card;
    }
}
