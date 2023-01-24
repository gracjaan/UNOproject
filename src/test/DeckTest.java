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
    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }
    @Test
    public void testRandomNumber() {
        Card card = new Card(Card.Color.BLUE, Card.Value.SKIP);
        System.out.println(card.getValue());
        HashMap<Player, Integer> hash = new HashMap<>();
        hash.put(new HumanPlayer("tom"), 450);
        hash.put(new HumanPlayer("asdf"), 320);
        hash.put(new HumanPlayer("twr"), 430);
        hash.put(new HumanPlayer("t32r"), 270);
        String winner = Collections.max(hash.entrySet(), Map.Entry.comparingByValue()).getKey().getNickname();
        System.out.println(winner);
    }
    }

