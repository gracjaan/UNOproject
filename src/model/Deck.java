package model;

import java.util.ArrayList;
public class Deck {

    //should be an array with fixed amount of cards - how many of each? Nested for loop through values and colors to initialize?
    private ArrayList<Card> cards;

    //--------------------------GETTERS--------------------------
    public ArrayList<Card> getCards() {
        return cards;
    }
    //--------------------------SETTERS--------------------------
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
