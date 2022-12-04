package model.contract;

import model.Card;

public interface PlayerActions {

    void playCard(Card card);

    void draw();

    Card.Color pickColor();





}
