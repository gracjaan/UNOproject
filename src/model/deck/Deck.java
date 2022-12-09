package model.deck;

import model.card.Card;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> playingCards;
    private ArrayList<Card> usedCards;
    public Deck() {
        this.playingCards = new ArrayList<>();
        this.usedCards = new ArrayList<>();
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
        for (int i=0;i<3;i++) {
            Collections.shuffle(playingCards);
        }
        // can be improved by mr gracjan who is a genius (set extraction)
        while ((playingCards.get(0).getColor()==Card.Color.WILD||playingCards.get(0).getValue()==Card.Value.CHANGE_DIRECTION||playingCards.get(0).getValue()==Card.Value.SKIP||playingCards.get(0).getValue()==Card.Value.DRAW_TWO)) {
            Collections.shuffle(playingCards);
        }
    }
    //--------------------------METHODS--------------------------
//    public Card draw() {
//        Card top = this.playingCards.get(0);
//        this.playingCards.remove(top);
//        // this.usedCards.add(top); ------ shouldnt we add them only to usedCards once they have been played by the player?
//        return top;
//    }

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
