package model.player.contract;

import model.card.Card;

public interface PlayerActions {
    boolean playCard(Card card);

    void draw(int amount);

    Card.Color pickColor(); //needs to call determineColor in UNO!
}
