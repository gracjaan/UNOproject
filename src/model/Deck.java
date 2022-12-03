package model;

import java.util.ArrayList;
public class Deck {

    //should be an array with fixed amount of cards - how many of each? Nested for loop through values and colors to initialize? - Constructor
    private ArrayList<Card> cards;
    public Deck() {
        this.cards = new ArrayList<>();
        // add everything except wild cards
        for (Card.Color color: Card.Color.values()) {
            if (color == Card.Color.WILD) {
                break;
            }
            for (Card.Value value: Card.Value.values()) {
                if (value==Card.Value.DRAW_FOUR) {
                    break;
                }
                else {
                    Card card = new Card(color, value);
                    this.cards.add(card);
                }
            }
            }
        // add wild cards
        for (int i=0;i<2;i++) {
            // how many wild cards? - default: 2
                Card card1 = new Card(Card.Color.WILD, Card.Value.DRAW_FOUR);
                this.cards.add(card1);
                Card card2 = new Card(Card.Color.WILD, Card.Value.PICK_COLOR);
                this.cards.add(card2);
        }
    }


    //--------------------------GETTERS--------------------------
    public ArrayList<Card> getCards() {
        return cards;
    }
    //--------------------------SETTERS--------------------------
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
