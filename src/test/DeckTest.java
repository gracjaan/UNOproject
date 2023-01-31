package test;
import model.card.Card;
import model.deck.Deck;
import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class DeckTest {
    private Deck deck;
    private final int TOTAL_CARDS = 108;
    private final int TOTAL_WILD_CARDS = 8;
    private final int TOTAL_SPECIAL_CARDS = 24;
    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }
    @Test
    public void testGenerateDeck() {
        assertEquals(TOTAL_CARDS, deck.getPlayingCards().size());
        int wCount = 0;
        int specialCount = 0;
        for (Card c: deck.getPlayingCards()) {
            if (c.getColor()== Card.Color.WILD) {
                wCount++;
            } else if (c.getValue()== Card.Value.DRAW_TWO||c.getValue()== Card.Value.CHANGE_DIRECTION||c.getValue()== Card.Value.SKIP) {
                specialCount++;
            }
        }
        assertEquals(TOTAL_WILD_CARDS, wCount);
        assertEquals(TOTAL_SPECIAL_CARDS, specialCount);
    }

    /**
     * @requires playingcards==0
     */
    // todo
    // but still shouldnt only be equal to 54 instead of 108
    @Test
    public void testReShuffle() {
        System.out.println(deck.getUsedCards().size());
        System.out.println(deck.getPlayingCards().size());
        int j = deck.getPlayingCards().size();
        for (int i=0; i < j; i++) {
            deck.getUsedCards().add(deck.getPlayingCards().get(0));
            deck.getPlayingCards().remove(0);
        }
        deck.reShuffle();
        System.out.println(deck.getPlayingCards().size());
        System.out.println(deck.getUsedCards().size());
        assertEquals(106, deck.getPlayingCards().size());
        assertEquals(2, deck.getUsedCards().size());
    }
    }

