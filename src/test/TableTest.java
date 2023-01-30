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
public class TableTest {
    private ArrayList<Player> players = new ArrayList<>();
    private Table table;
    private Player p;
    @BeforeEach
    public void setUp() {
        players = new ArrayList<Player>();
        p = new HumanPlayer("KD");
        players.add(p);
        players.add(new HumanPlayer("MJ"));
        players.add(new HumanPlayer("Lebron"));
        players.add(new HumanPlayer("Luca"));
        table = new Table(players, new Normal());
        for (Player player: players) {
            player.setTable(table);
        }
    }

    @Test
    public void testReversePlayers() {
        table.reversePlayers();
        table.nextTurn();
        assertEquals("Luca",table.getCurrentPlayer().getNickname());
        table.nextTurn();
        assertEquals("Lebron", table.getCurrentPlayer().getNickname());
        table.nextTurn();
        assertEquals("MJ", table.getCurrentPlayer().getNickname());
    }
    @Test
    public void testPreviousNextPlayer() {
        assertEquals("Luca", table.getPreviousPlayer());
        assertEquals("MJ", table.getNextPlayer());
    }
    @Test
    public void testChangeDirAfterPickFour() {
        Card card = new Card(Card.Color.BLUE, Card.Value.FIVE);
        table.getCurrentPlayer().playCard(card);
        table.nextTurn();
        card = new Card(Card.Color.BLUE, Card.Value.CHANGE_DIRECTION);
        table.getCurrentPlayer().playCard(card);
        table.nextTurn();
        assertEquals("KD", table.getCurrentPlayer().toString());
    }
    @Test
    public void testCalculateScores() {
        ArrayList<Card> hand = new ArrayList<>();
        // expected: 100 * 3
        hand.add(new Card(Card.Color.WILD, Card.Value.DRAW_FOUR));
        hand.add(new Card(Card.Color.GREEN, Card.Value.DRAW_TWO));
        hand.add(new Card(Card.Color.RED, Card.Value.SKIP));
        hand.add(new Card(Card.Color.YELLOW, Card.Value.SIX));
        hand.add(new Card(Card.Color.BLUE, Card.Value.FOUR));
        for (Player p: players) {
            p.setHand(hand);
        }
        table.calculateScores(p);
        assertEquals(300, table.getScoreBoard().get(p));
    }
}
