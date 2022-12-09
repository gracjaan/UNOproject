package test;
import model.card.Card;
import model.deck.Deck;
import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class DeckTest {
    private Deck deck;
    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }
    @Test
    public void testFirstCard() {
        for (int i=0; i<100; i++) {
            deck = new Deck();
            assertFalse(deck.getPlayingCards().get(0).getColor()==Card.Color.WILD);
        }
        }
    }
