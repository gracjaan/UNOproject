package test;
import controller.UNO;
import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.HashMap;

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
        table = new Table(players, new NormalTestVersion());
        uno = new UNO();
        uno.setPlayers(players);
        uno.setTable(table);
        for (Player player: players) {
            player.setTable(table);
            player.setUNO(uno);
        }
    }

    @Test
    public void playAfterDrawingValid() {
        String input = "draw";
        Player player = table.getCurrentPlayer();
        boolean condition = uno.handleMove(input);
        if (!condition) {
            input = Integer.toString(table.getCurrentPlayer().getHand().size()-1);
            assertEquals(true, uno.handleMove(input));
        }
        else {
            table.nextTurn();
            assertFalse(player.getNickname().equals(table.getCurrentPlayer().getNickname()));
        }
    }


}

