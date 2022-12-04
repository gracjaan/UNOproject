package model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    // Do we need an arraylist storing all existing cards?
    private ArrayList<Card> playingCards;
    private ArrayList<Card> usedCards;
    public Deck() {
        this.playingCards = new ArrayList<>();
        // add everything except wild cards

        // add and modify Change directions
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
                    this.playingCards.add(card);
                }
            }
            }
        // add wild cards
        for (int i=0;i<4;i++) {
            // how many wild cards? - default: 2
                Card card1 = new Card(Card.Color.WILD, Card.Value.DRAW_FOUR);
                this.playingCards.add(card1);
                Card card2 = new Card(Card.Color.WILD, Card.Value.PICK_COLOR);
                this.playingCards.add(card2);
        }
        Collections.shuffle(playingCards);
    }
    //--------------------------METHODS--------------------------
    public Card draw() {
        Card top = this.playingCards.get(this.playingCards.size()-1);
        this.playingCards.remove(top);
       // this.usedCards.add(top); ------ shouldnt we add them only to usedCards once they have been played by the player?
        return top;
    }

    //called when we run out of cards to play with
    public ArrayList<Card> reShuffle() {
        ArrayList<Card> tempArr = this.usedCards;
        this.playingCards = tempArr;
        this.usedCards = playingCards;
        Collections.shuffle(playingCards);
        return playingCards;
    }

    //--------------------------GETTERS--------------------------
    public ArrayList<Card> getPlayingCards() {
        return playingCards;
    }

    public ArrayList<Card> getUsedCards() {
        return usedCards;
    }

    //--------------------------SETTERS--------------------------
    public void setPlayingCards(ArrayList<Card> cards) {
        this.playingCards = cards;
    }

    public void setUsedCards(ArrayList<Card> usedCards) {
        this.usedCards = usedCards;
    }
}
