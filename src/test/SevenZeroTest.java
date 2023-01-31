package test;
import controller.UNO;
import model.card.Card;
import model.deck.Deck;
import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import model.table.gameModes.Progressive;
import model.table.gameModes.SevenZero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

public class SevenZeroTest {
    private ArrayList<Player> players;
    private Table table;
    private UNO uno;
    @BeforeEach
    public void setUp() {
        players = new ArrayList<Player>();
        players.add(new HumanPlayer("a"));
        players.add(new HumanPlayer("b"));
        players.add(new HumanPlayer("c"));
        table = new Table(players, new SevenZero());
        uno = new UNO();
        uno.setPlayers(players);
        uno.setTable(table);
        for (Player player: players) {
            player.setTable(table);
            player.setUNO(uno);
        }
    }

    @Test
    public void testSwapHands() {
        ArrayList<Card> hand1 = players.get(0).getHand();
        ArrayList<Card> hand2 = players.get(1).getHand();
        players.get(0).swapHands(players.get(1));
        assertEquals(hand1, players.get(1).getHand());
        assertEquals(hand2, players.get(0).getHand());
    }

    //everybody gets the same hand!!!
    @Test
    public void testPassDownHands() {
        ArrayList<Card> hand1 = players.get(0).getHand();
        ArrayList<Card> hand2 = players.get(1).getHand();
        ArrayList<Card> hand3 = players.get(2).getHand();
        System.out.println("Hand1:"+ hand1);
        System.out.println("Hand2:"+ hand2);
        System.out.println("Hand3:"+ hand3);
//        table.setCurrentCard(new Card(Card.Color.GREEN, Card.Value.FOUR));
//        players.get(0).playCard(new Card(Card.Color.GREEN, Card.Value.ZERO));
        ((SevenZero)table.getPlayingMode()).passDownHands(table);
        System.out.println("Hand1:"+ players.get(0).getHand());
        System.out.println("Hand2:"+ players.get(1).getHand());
        System.out.println("Hand3:"+ players.get(2).getHand());
        assertEquals(hand3, players.get(0).getHand());
        assertEquals(hand1, players.get(1).getHand());
        assertEquals(hand2, players.get(2).getHand());
    }
}
