package test;
import controller.UNO;
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
public class UnoTest {
    private ArrayList<Player> players;
    private Table table;
    private UNO uno;

    // corresponds to the Uno start method
    @BeforeEach
    public void setUp() {
        players = new ArrayList<Player>();
        players.add(new HumanPlayer("Pat Mahomes"));
        players.add(new HumanPlayer("Cam Newton"));
        table = new Table(players, new Normal());
        for (Player player: players) {
            player.setTable(table);
        }
        uno = new UNO();
        uno.setPlayers(players);
        uno.setTable(table);
    }

    @Test
    public void playAfterDrawingValid() {
        String input = "draw";
        Player player = table.getCurrentPlayer();
        boolean condition = uno.evaluateMove(input);
        if (!condition) {
            input = Integer.toString(table.getCurrentPlayer().getHand().size()-1);
            assertEquals(true, uno.evaluateMove(input));
        }
        else {
            table.nextTurn();
            assertFalse(player.getNickname().equals(table.getCurrentPlayer().getNickname()));
        }
    }

    public void testRunThrough() {

    }
    // i can make an entire test game run through by applying the while loop as in play
}

