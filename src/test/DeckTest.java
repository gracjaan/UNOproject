package test;

import controller.GameLogic;
import model.Card;
import model.Deck;
import model.Game;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
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
    private GameLogic game;
    ArrayList<Player> players;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
        game = new GameLogic();
        players = new ArrayList<>();
        players.add(new Player("kd7"));
        players.add(new Player("jjredick"));
        players.add(new Player("The Brow"));
        game.setPlayers(players);
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
//    @Test
//    public void testShuffleDeck () {
//        for (Card card1: deck.getCards()) {
//            System.out.println(card1.getColor());
//            System.out.println(card1.getValue());
//        }
//        Collections.shuffle(deck.getCards());
//        for (Card card1: deck.getCards()) {
//            System.out.println(card1.getColor());
//            System.out.println(card1.getValue());
//        }
//    }
    @Test
    public void testDeckAfterDistributing() {
        game.shuffleDeck();
        game.distributeHands();
        assertEquals(27, game.getDeck().getCards().size());
    }
    @Test
    public void testPlayerHands() {
        game.shuffleDeck();
        game.distributeHands();
        assertEquals(7, this.players.get(0).getHand().size());
        assertEquals(7, this.players.get(1).getHand().size());
        assertEquals(7, this.players.get(2).getHand().size());
    }
    @Test
    public void printPlayerHands() {
        game.shuffleDeck();
        game.distributeHands();
        for (int i=0;i<this.players.get(0).getHand().size();i++) {
            System.out.println(this.players.get(0).getHand().get(i).getColor());
            System.out.println(this.players.get(0).getHand().get(i).getValue());
        }
    }
}
