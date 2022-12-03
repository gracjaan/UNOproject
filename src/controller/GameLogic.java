package controller;

import model.Card;
import model.Deck;
import model.Game;
import model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class GameLogic extends Game{
    public GameLogic () {
        super();
    }

    public Game startGame(Game game) {
        return game;
    }
    public void runGame() {}

    public Game endGame(Game game) {
        return game;
    }
    public void shuffleDeck (){
        Collections.shuffle(super.getDeck().getCards());
    }
    public void distributeHands() {
        ArrayList<Card> tempDeck = super.getDeck().getCards();
        for (Player player: super.getPlayers()) {
            ArrayList<Card> tempHand = new ArrayList<>();
            for (int i=0;i<7;i++) {
                tempHand.add(tempDeck.get(0));
                tempDeck.remove(0);
            }
        player.setHand(tempHand);
        }
    }
    public void nextTurn() {
        if (super.getTurnIndex()<super.getPlayers().size()-1) {
            super.setTurnIndex(super.getTurnIndex()+1);
        }
        else {
            super.setTurnIndex(0);
        }
    }
    // the color argument will be needed when implementing the pickColor functionality.
    public boolean validMove(Card cardToPlay, Card.Color color) {
        if (cardToPlay.getColor()== Card.Color.WILD&&super.getCurrentCard().getColor()!= Card.Color.WILD) {
            return true;
        }
        if (color==cardToPlay.getColor()) {
            return true;
        }
        return false;
    }
    public void evaluateNewCard() {
       // switch case for all wild cards
    }



}
