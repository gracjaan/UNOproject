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
    @BeforeEach
    public void setUp() {
        players = new ArrayList<Player>();
        players.add(new HumanPlayer("KD"));
        players.add(new HumanPlayer("MJ"));
        table = new Table(players, new Normal());
        for (Player player: players) {
            player.setTable(table);
        }
    }
    @Test
    public void testChangeDirAfterPickFour() {
        System.out.println(table.getCurrentPlayer());
        Card card = new Card(Card.Color.BLUE, Card.Value.FIVE);
        table.getCurrentPlayer().playCard(card);
        table.nextTurn();
        card = new Card(Card.Color.BLUE, Card.Value.CHANGE_DIRECTION);
        table.getCurrentPlayer().playCard(card);
        table.nextTurn();
        assertEquals("MJ", table.getCurrentPlayer().toString());

    }
}
