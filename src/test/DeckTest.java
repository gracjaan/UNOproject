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
import java.util.Random;

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
    public void testRandomNumber() {
        for (int i=0;i<10;i++) {
            Random r = new Random();
            int random = r.nextInt(4) + 1;
            System.out.println(random);
        }
    }
    }

