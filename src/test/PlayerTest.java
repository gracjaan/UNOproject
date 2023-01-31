package test;
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

public class PlayerTest {
    private Table table;
    private ArrayList<Player> players;
    @BeforeEach
    public void setUp() {
        players = new ArrayList<Player>();
        players.add(new HumanPlayer("KD"));
        players.add(new HumanPlayer("MJ"));
        players.add(new HumanPlayer("Luca Doncic"));
        table = new Table(players, new Normal());
        table.setCurrentCard(new Card(Card.Color.WILD, Card.Value.PICK_COLOR));
        for (Player player: players) {
            player.setTable(table);
        }
    }
    @Test
    public void testPlayCard() {
        table.setCurrentCard(new Card(Card.Color.BLUE, Card.Value.THREE));
        Card card = new Card(Card.Color.BLUE, Card.Value.SEVEN);
        players.get(0).playCard(card);
        assertEquals(card, table.getCurrentCard());
    }
    @Test
    public void testPlayPickColorCard() {
        Card card = new Card(Card.Color.YELLOW, Card.Value.SEVEN);
        table.getCurrentPlayer().playCard(card);
        table.setIndicatedColor(Card.Color.BLUE);
        assertEquals(table.getIndicatedColor(), Card.Color.BLUE);
        card = new Card(Card.Color.BLUE, Card.Value.EIGHT);
        assertTrue(table.getPlayingMode().validMove(card, table));
        table.getCurrentPlayer().playCard(card);
        assertEquals(null, table.getIndicatedColor());
    }

    @Test
    public void testPlayTwoWildCards() {
        Card card = new Card(Card.Color.WILD, Card.Value.PICK_COLOR);
        table.setIndicatedColor(Card.Color.BLUE);
        assertFalse(table.getPlayingMode().validMove(card, table));
    }
    @Test
    public void testIsWinner() {
        assertFalse(players.get(0).isWinner());
        players.get(0).setHand(new ArrayList<>());
        assertTrue(players.get(0).isWinner());
    }
}
