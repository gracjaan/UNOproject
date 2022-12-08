package model.table.gameModes;

import model.card.Card;
import model.deck.Deck;
import model.table.gameModes.factory.PlayingMode;
import model.player.factory.Player;

import java.awt.*;
import java.util.ArrayList;

public class Normal extends PlayingMode {

    // give it the player you want the action to be performed by if Pick color that is the current player, otherwise the next.
    @Override
    public void performWildCardAction(Card card, Player player) {
        switch (card.getValue()) {
            case DRAW_TWO:
                player.draw(2);
            case DRAW_FOUR:
                player.draw(4);
            case SKIP:
                player.getTable().skip();
            case PICK_COLOR:
                // call player.pickColor()
            case CHANGE_DIRECTION:
                player.getTable().reversePlayers();
        }
    }


    // if you pick a color, give it null as a value.
    @Override
    public boolean validMove(Card cardToPlay, Card.Color color, Card.Value value) {
        if (cardToPlay.getColor()== Card.Color.WILD&&color== Card.Color.WILD) {
            return false;
        }
        if (color==cardToPlay.getColor()) {
            return true;
        }
        if (value==cardToPlay.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public void distributeHands(ArrayList<Player> players, Deck deck) {
        ArrayList<Card> tempDeck = deck.getPlayingCards();
        for (Player player: players) {
            ArrayList<Card> tempHand = new ArrayList<>();
            for (int i=0;i<7;i++) {
                tempHand.add(tempDeck.get(0));
                tempDeck.remove(0);
            }
            player.setHand(tempHand);
        }
        deck.setPlayingCards(tempDeck);
    }
}
