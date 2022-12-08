package view;

import model.card.Card;
import model.player.factory.Player;

public interface UI {
    void printTable();
    void printHand(Player player);

    void printCurrentCard(Card card);

    void printWinners();
}
