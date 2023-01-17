package model.table.gameModes;

import model.card.Card;
import model.deck.Deck;
import model.table.gameModes.factory.PlayingMode;
import model.player.factory.Player;

import java.awt.*;
import java.util.ArrayList;

public class Normal extends PlayingMode {

    /**
     * Give it the player you want the action to be performed by if Pick color that is the current player, otherwise the next.
     * */
    @Override
    public void performWildCardAction(Card card, Player player, Player nextPlayer) {
        switch (card.getValue()) {
            case DRAW_TWO:
                nextPlayer.draw(2);
                player.getTable().skip();
                break;
            case DRAW_FOUR:
                if (player.getTable().isHasWinner()){
                    break;
                }
                player.pickColor();
                nextPlayer.draw(4);
                player.getTable().drawFourEligibility();
                break;
            case SKIP:
                player.getTable().skip();
                break;
            case PICK_COLOR:
                if (player.getTable().isHasWinner()){
                    break;
                }
                player.pickColor();
                break;
            case CHANGE_DIRECTION:
                player.getTable().reversePlayers();
                break;
        }
    }


    /**
     * @param cardToPlay card to be played
     * @param color color of a card on the table
     * @param value value of the card o the table
     * @param indicatedColor color indicated by last player
     * @ensures that move is valid
     * @return true if move is valid, otherwise false
     * */
    @Override
    public boolean validMove(Card cardToPlay, Card.Color color, Card.Value value, Card.Color indicatedColor) {
        if(indicatedColor==null) {
            if (cardToPlay.getColor() == Card.Color.WILD && color == Card.Color.WILD) {
                return false;
            } else if (cardToPlay.getColor() == Card.Color.WILD) {
                return true;
            }
            if (color == cardToPlay.getColor()) {
                return true;
            }
            if (value == cardToPlay.getValue()) {
                return true;
            }
            }else {
            if (indicatedColor == cardToPlay.getColor()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param players receives all players
     * @param deck receives deck
     * Distributes hands
     * */
    @Override
    public void distributeHands(ArrayList<Player> players, Deck deck) {
        ArrayList<Card> tempDeck = deck.getPlayingCards();
        for (Player player: players) {
            ArrayList<Card> tempHand = new ArrayList<>();
            for (int i=0;i<1;i++) {
                tempHand.add(tempDeck.get(0));
                tempDeck.remove(0);
            }
            player.setHand(tempHand);
        }
        deck.setPlayingCards(tempDeck);
    }
}
