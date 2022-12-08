package model.table.gameModes.contract;

import model.card.Card;
import model.deck.Deck;
import model.player.factory.Player;

import java.util.ArrayList;

public interface Mode {
    void performWildCardAction(Card card, Player player);

    boolean validMove(Card cardToPlay, Card.Color color, Card.Value value, Card.Color indicatedColor);

    void distributeHands(ArrayList<Player> players, Deck deck);


}
