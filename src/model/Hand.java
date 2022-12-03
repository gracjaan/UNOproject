package model;

import java.util.ArrayList;

public class Hand {

    // initial value = 7?
    private ArrayList<Card> cards;

    //--------------------------GETTERS--------------------------
    public ArrayList<Card> getCards() {
        return cards;
    }
    //--------------------------SETTERS--------------------------
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    public void addCard(Card card) {
        this.cards.add(card);
    }
}
