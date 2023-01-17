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
    @Test
    public void reversePlayerArr() {
        String s = "";
        for (int i = 0; i < table.getPlayers().size(); i++) {
            s+=table.getPlayers().get(i).getNickname();
            s+=", ";
        }
        s+="\n";
        table.reversePlayers();
        for (int i = 0; i < table.getPlayers().size(); i++) {
            s+=table.getPlayers().get(i).getNickname();
            s+=", ";
        }
        s+="\n";
        table.reversePlayers();
        for (int i = 0; i < table.getPlayers().size(); i++) {
            s+=table.getPlayers().get(i).getNickname();
            s+=", ";
        }
        s+="\n";
        System.out.println(s);
        System.out.println(table.getCurrentTurnIndex());
    }

    // pre defined values
    @Test
    public void testRunThroughWithFirstValid() {
        boolean condition = false;
        HashMap<Player, ArrayList<Integer>> moves = new HashMap<>();
        for (Player player: players) {
            moves.put(player, new ArrayList<>());
        }
        // it will still ask you to pick a color;
        while (!condition) {
            for (Player player: players) {
                if (player.getHand().isEmpty()) {
                    condition = true;
                }
            }
            moves.get(table.getCurrentPlayer()).clear();
            for (int i = 0; i< table.getCurrentPlayer().getHand().size()-1; i++) {
                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(i), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
                    moves.get(table.getCurrentPlayer()).add(i);
                }
            }
            String input;
            if (!moves.get(table.getCurrentPlayer()).isEmpty()) {
                input = Integer.toString(moves.get(table.getCurrentPlayer()).get(0));
            }
            else {
                input = "draw";
            }
            if (!uno.handleMove(input)) {
                continue;
            }
            table.nextTurn();
            // up until here it is a simulated round so we can test a bunch of stuff here
        }
        // and here for hasWinner
        System.out.println(table.getScoreBoard().get(0));
        //assertEquals(table.hasWinner().getNickname(), table.getScoreBoard().get(0));
    }

}

