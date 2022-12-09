package model.player.contract;

import model.card.Card;

public interface PlayerActions {
    void playCard(Card card);

    void draw(int amount);

    void pickColor(); //needs to call determineColor in UNO!

    boolean isWinner();
}
