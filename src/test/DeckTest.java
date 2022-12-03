package test;

import controller.GameLogic;
import model.Card;
import model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {
    // test values
    private Deck deck;
    private Card card;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }
    @Test
    public void checkSomeCards() {
        // unshuffled Deck
        this.card = deck.getCards().get(0);
        assertTrue(card.getColor()==Card.Color.BLUE&&card.getValue()== Card.Value.TWO);
        this.card = deck.getCards().get(11);
        assertTrue(card.getColor()==Card.Color.GREEN&&card.getValue()== Card.Value.TWO);
        this.card = deck.getCards().get(20);
        assertTrue(card.getColor()==Card.Color.GREEN&&card.getValue()== Card.Value.DRAW_TWO);
    }
    @Test
    public void checkSomeWildCards() {
        // unshuffled Deck
        this.card = deck.getCards().get(47);
        assertTrue(card.getColor()==Card.Color.WILD&&card.getValue()== Card.Value.PICK_COLOR);
        this.card = deck.getCards().get(44);
        assertTrue(card.getColor()==Card.Color.WILD&&card.getValue()== Card.Value.DRAW_FOUR);
    }
    @Test
    public void testShuffleDeck () {
        for (Card card1: deck.getCards()) {
            System.out.println(card1.getColor());
            System.out.println(card1.getValue());
        }
        Collections.shuffle(deck.getCards());
        for (Card card1: deck.getCards()) {
            System.out.println(card1.getColor());
            System.out.println(card1.getValue());
        }
    }
}
