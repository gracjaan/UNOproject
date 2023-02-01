package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.controller.UNO;
import server.model.player.ComputerPlayer;
import server.model.player.factory.Player;
import server.model.table.Table;
import server.model.table.gameModes.Normal;
import server.model.table.gameModes.Progressive;
import server.model.table.gameModes.SevenZero;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class GameRunThrough {
    /**
     * test variables
     */
    private ArrayList<Player> players;
    private Table table;
    private UNO uno;

    /**
     * Initializes the players ArrayList and adds three computerplayers to it. These will be the players the test run through methods
     * will use to simulate an entire game in the corresponding game mode.
     */
    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add(new ComputerPlayer("Kyrie"));
        players.add(new ComputerPlayer("Lebron"));
        players.add(new ComputerPlayer("Luca"));

    }

    /**
     * this test case simulates an entire run through of the game with three computer players in normal mode,
     * the moves can and should be followed thoroughly in the Console, as every move is printed there by the TUI and in case of debugging a specific case printed debugging messages
     * will also appear there. The test then only asserts that the game indeed has a winner was therefore successfully completed.
     */
    @Test
    public void testCPRunThroughNormal() {
        uno = new UNO();
        uno.setPlayers(players);
        table = new Table(players, new Normal(),uno);
        table.getUno().setTable(table);
        for (Player player: players) {
            player.setTable(table);
            player.setUNO(uno);
        }
        uno.play();
        assertNotNull(uno.gameOver());
    }

    /**
     * this test case simulates an entire run through of the game with three computer players in progressive mode,
     * the moves can and should be followed thoroughly in the Console, as every move is printed there by the TUI and in case of debugging a specific case printed debugging messages
     * will also appear there. The test then only asserts that the game indeed has a winner was therefore successfully completed.
     */
    @Test
    public void testCPRunThroughProgressive() {
        uno = new UNO();
        uno.setPlayers(players);
        table = new Table(players, new Progressive(), uno);
        uno.setTable(table);
        for (Player player: players) {
            player.setTable(table);
            player.setUNO(uno);
        }
        uno.play();
        assertNotNull(uno.gameOver());
    }
    /**
     * this test case simulates an entire run through of the game with three computer players in sevenZero mode,
     * the moves can and should be followed thoroughly in the Console, as every move is printed there by the TUI and in case of debugging a specific case printed debugging messages
     * will also appear there. The test then only asserts that the game indeed has a winner was therefore successfully completed.
     */
    @Test
    public void testCPRunThroughSevenZero() {
        uno = new UNO();
        uno.setPlayers(players);
        table = new Table(players, new SevenZero(), uno);
        uno.setTable(table);
        for (Player player: players) {
            player.setTable(table);
            player.setUNO(uno);
        }
        uno.play();
        assertNotNull(uno.gameOver());
    }

}
