package test;
import controller.UNO;
import model.card.Card;
import model.deck.Deck;
import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import model.table.gameModes.Progressive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class ProgressiveTest {
    private ArrayList<Player> players;
    private Table table;
    private UNO uno;
    @BeforeEach
    public void setUp() {
        players = new ArrayList<Player>();
        players.add(new HumanPlayer("a"));
        players.add(new HumanPlayer("b"));
        players.add(new HumanPlayer("c"));
        table = new Table(players, new Progressive());
        uno = new UNO();
        uno.setPlayers(players);
        uno.setTable(table);
        for (Player player: players) {
            player.setTable(table);
            player.setUNO(uno);
        }
    }
    @Test
    public void testForwardCount() {
        table.setCurrentCard(new Card(Card.Color.GREEN, Card.Value.ZERO));
        for (Player p: players) {
            p.getHand().add(new Card(Card.Color.GREEN, Card.Value.DRAW_TWO));
        }
        int lastIndex = players.get(0).getHand().size()-1;
        players.get(0).playCard(players.get(0).getHand().get(lastIndex));
        assertEquals(2, table.getPlayingMode().getForwardCount());
        players.get(1).playCard(players.get(1).getHand().get(lastIndex));
        assertEquals(4, table.getPlayingMode().getForwardCount());
        players.get(2).playCard(players.get(2).getHand().get(lastIndex));
        assertEquals(6, table.getPlayingMode().getForwardCount());
    }

    @Test
    public void testValidMoveAfterDrawTwo() {
        table.setCurrentCard(new Card(Card.Color.GREEN, Card.Value.DRAW_TWO));
        table.getPlayingMode().setForwardCount(2);
        Card c1 = new Card(Card.Color.GREEN, Card.Value.THREE);
        assertFalse(table.getPlayingMode().validMove(c1, table));
        Card c2 = new Card(Card.Color.BLUE, Card.Value.DRAW_TWO);
        assertTrue(table.getPlayingMode().validMove(c2, table));
    }
}
