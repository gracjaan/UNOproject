package controller;

import model.Card;
import model.Game;
import model.factory.Player;

import java.util.ArrayList;

public class GameLogic extends Game{
    public GameLogic () {
      super(new ArrayList<>());
    }


    public Game startGame(Game game) {
        return game;
    }
    public void runGame() {}

    public Game endGame(Game game) {
        return game;
    }
    public void distributeHands() {
        ArrayList<Card> tempDeck = super.getDeck().getPlayingCards();
        for (Player player: super.getPlayers()) {
            ArrayList<Card> tempHand = new ArrayList<>();
            for (int i=0;i<7;i++) {
                tempHand.add(tempDeck.get(0));
                tempDeck.remove(0);
            }
            // remove em from playing cards
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


    // add color as argument to deal with wild card pick color
    public boolean validMove(Card cardToPlay) {
        if (cardToPlay.getColor()== Card.Color.WILD&&super.getCurrentCard().getColor()== Card.Color.WILD) {
            return false;
        }
        if (super.getCurrentCard().getColor()==cardToPlay.getColor()) {
            return true;
        }
        if (super.getCurrentCard().getValue()==cardToPlay.getValue()) {
            return true;
        }
        return false;
    }

    // methods should be called in following order: playCard - if validMove - evaluateNewCard - change currentCard
    public void performWildCardAction(Card card) {
       switch (card.getValue()) {
           case DRAW_TWO:
               // call draw functions of next player two times
           case DRAW_FOUR:
               // call draw function four times + pick Color next Player
           case SKIP:
               // call nextTurn twice
           case PICK_COLOR:
               // call player.pickColor()
           case CHANGE_DIRECTION:
               // change directions (Table)
       }
    }



}
