package test;
import controller.UNO;
import model.card.Card;
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
public class UnoTest {
    private ArrayList<Player> players;
    private Table table;
    private UNO uno;

    // corresponds to the Uno start method
    @BeforeEach
    public void setUp() {
        players = new ArrayList<Player>();
        players.add(new HumanPlayer("a"));
        players.add(new HumanPlayer("b"));
        players.add(new HumanPlayer("c"));
        players.add(new HumanPlayer("d"));
        players.add(new HumanPlayer("e"));
        table = new Table(players, new Normal());
        uno = new UNO();
        uno.setPlayers(players);
        uno.setTable(table);
        for (Player player: players) {
            player.setTable(table);
            player.setUNO(uno);
        }
    }

    @Test
    public void testFindDealer() {
        Card c1 = new Card(Card.Color.BLUE, Card.Value.TWO);
        Card c2 = new Card(Card.Color.RED, Card.Value.THREE);
        Card c3 = new Card(Card.Color.BLUE, Card.Value.FOUR);
        Card c4 = new Card(Card.Color.RED, Card.Value.FIVE);
        Card c5 = new Card(Card.Color.YELLOW, Card.Value.SIX);
        ArrayList<Card> d = new ArrayList<>();
        d.add(c1);
        d.add(c2);
        d.add(c3);
        d.add(c4);
        d.add(c5);
        int mpi = uno.findDealer(d);
        assertEquals(4, mpi);
        // in case they draw equally high?
    }
    @Test
    public void testHandleMove() {
        // current player is a ind 0 in players
        table.setCurrentCard(new Card(Card.Color.BLUE, Card.Value.SEVEN));
        Card c1 = new Card(Card.Color.BLUE, Card.Value.TWO);
        Card c2 = new Card(Card.Color.RED, Card.Value.THREE);
        Card c3 = new Card(Card.Color.BLUE, Card.Value.FOUR);
        Card c4 = new Card(Card.Color.RED, Card.Value.FIVE);
        Card c5 = new Card(Card.Color.YELLOW, Card.Value.SIX);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        hand.add(c4);
        hand.add(c5);
        this.players.get(0).setHand(hand);
        assertEquals(true, uno.handleMove("0"));
        assertEquals(false,uno.handleMove("2"));
        assertEquals(false, uno.handleMove("3"));
    }

    @Test
    public void testInputDraw() {
        int originalHandCount = table.getCurrentPlayer().getHand().size();
        boolean c = uno.inputDraw("draw");
        int newHandCount = table.getCurrentPlayer().getHand().size();
        assertEquals(originalHandCount+1,newHandCount);
        if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(newHandCount-1),table)) {
            assertEquals(false, c);
        } else {
            assertEquals(true, c);
        }
    }

    @Test
    public void testGameOver() {
        Player w = players.get(3);
        table.getScoreBoard().put(w, 320);
        w = players.get(2);
        table.getScoreBoard().put(w, 420);
        assertNull(uno.gameOver());
        w = players.get(3);
        table.getScoreBoard().put(w, 520);
        assertEquals(w.getNickname(), uno.gameOver().getNickname());
    }

    @Test
    public void testRoundOver() {
        uno.roundOver();
        assertFalse(uno.isRoundOver());
        players.get(0).setHand(new ArrayList<>());
        assertTrue(players.get(0).isWinner());
        uno.roundOver();
        assertTrue( uno.isRoundOver());
    }

}

