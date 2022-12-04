package test;
import controller.GameLogic;
import model.Card;
import model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class GameLogicTest {

    private Deck deck;
    private Card card;
    private GameLogic game;
    @BeforeEach
    public void setUp() {
        this.game = new GameLogic();
        this.deck  = game.getDeck();
    }

    @Test
    public void testSameColor() {
        this.card = new Card(Card.Color.BLUE, Card.Value.SIX);
        game.setCurrentCard(card);
        this.card = new Card(Card.Color.BLUE, Card.Value.TWO);
        assertTrue(game.validMove(card));
    }

    @Test
    public void testSameValue() {
        this.card = new Card(Card.Color.BLUE, Card.Value.SIX);
        game.setCurrentCard(card);
        this.card = new Card(Card.Color.YELLOW, Card.Value.SIX);
        assertTrue(game.validMove(card));
    }

    @Test
    public void testTwoWildCards() {
        this.card = new Card(Card.Color.WILD, Card.Value.DRAW_FOUR);
        game.setCurrentCard(card);
        this.card = new Card(Card.Color.WILD, Card.Value.DRAW_FOUR);
        assertFalse(game.validMove(card));
    }
    @Test
    public void testWrongAction() {
        this.card = new Card(Card.Color.BLUE, Card.Value.SIX);
        game.setCurrentCard(card);
        this.card = new Card(Card.Color.YELLOW, Card.Value.SEVEN);
        assertFalse(game.validMove(card));
    }

}
