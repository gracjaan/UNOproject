package model.table.gameModes.factory;

import model.card.Card;
import model.deck.Deck;
import model.player.factory.Player;
import model.table.gameModes.contract.Mode;

import java.util.ArrayList;

public abstract class PlayingMode implements Mode {

    // what?
    private int forwardCount=0;

    public int getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(int forwardCount) {
        this.forwardCount = forwardCount;
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
            for (int i=0;i<15;i++) {
                tempHand.add(tempDeck.get(0));
                tempDeck.remove(0);
            }
            player.setHand(tempHand);
        }
        deck.setPlayingCards(tempDeck);
    }
}
