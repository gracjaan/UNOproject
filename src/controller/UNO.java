package controller;

import model.card.Card;
import model.deck.Deck;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import model.table.gameModes.Progressive;
import model.table.gameModes.SevenZero;
import model.table.gameModes.factory.PlayingMode;
import view.TUI;

import java.nio.Buffer;
import java.util.*;

public class UNO implements Runnable{
    private TUI tui = new TUI();
    private ArrayList<Player> players;
    private Table table;
    private Card card;
    private boolean roundOver = false;
    private PlayingMode gameMode;

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UNO uno = new UNO();
        uno.start();
        uno.setup(uno.players, uno.gameMode);
        uno.play();
    }

    /**
     * Starts the game asking user for basic input to set up a game
     */
    public void start() {
        while (true) {
            System.out.print(">> Please enter number of players: ");
            try {
                int playersAmount = scanner.nextInt();
                players = new ArrayList<Player>();
                Scanner scanner1 = new Scanner(System.in);
                for (int i = 0; i < playersAmount; i++) {
                    System.out.print(">> Enter name of Player " + (i + 1) + ": ");
                    String name = scanner1.nextLine();
                    String[] spl = name.split(" ");
                    if (spl[spl.length-1].equals("c")) {
                        StringBuffer sb = new StringBuffer(name);
                        sb.deleteCharAt(sb.length()-1);
                        sb.deleteCharAt(sb.length()-1);
                        players.add(new ComputerPlayer(sb.toString()));
                    }else {
                        players.add(new HumanPlayer(name));
                    }
                }
                System.out.print(">> Please enter mode: ");
                String playingMode = scanner.next();
                if (playingMode.equals("normal")) {
                    this.gameMode = new Normal();
                } else if (playingMode.equals("progressive")) {
                    this.gameMode = new Progressive();
                } else if (playingMode.equals("sevenZero")) {
                    this.gameMode = new SevenZero();
                }
                else {
                    System.out.println("Please enter a valid gameMode.");
                    continue;
                }
                // start uno with different playing modes.
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.next();
            }
        }
    }



    public void informAll(){
        // this should already be restricted to lobby -> players here should only be players inside the lobby
        for (Player player: this.players){
            if (player instanceof NetworkPlayer){
                ((NetworkPlayer) player).broadcastAfterTurn();
            }
        }
    }
    /**
     * Plays a game until not finished
     */
    public void play() {
        while (gameOver()==null) {
            this.roundOver = false;
            while (!this.roundOver) {
                tablePrinter();
                informAll();
                String input1 = createInput();
//                if (!handleMove(input1)) {
//                    //tell clienthandler move was invlaid
//                    continue;
//
                while (!handleMove(input1)) {
                    if (table.getCurrentPlayer()instanceof NetworkPlayer) {
                        NetworkPlayer np = ((NetworkPlayer)table.getCurrentPlayer());
                        np.broadcastAfterTurn();
                    } else {
                        tablePrinter();
                    }
                    input1 = createInput();
                }

                table.nextTurn();

                if (gameOver()!=null) {
                    System.out.println(">> Player " + gameOver().getNickname() + " has ultimately won the game!");

                    break;
                }
                this.roundOver();
            }
            }
            System.out.println(">> GAME OVER!!!");
//            System.out.println(">> Would you like to play another one (y/n): ");
//            String input2 = scanner.next();
    }

    private String createInput() {
        String input1 = null;
        if (table.getCurrentPlayer() instanceof HumanPlayer) {
            Scanner scan = new Scanner(System.in);
            System.out.println(">> " + table.getCurrentPlayer().getNickname() + " make your move: ");
            input1 = scan.nextLine();
        } else if (table.getCurrentPlayer() instanceof ComputerPlayer) {
            System.out.print(">> " + table.getCurrentPlayer().getNickname() + " make your move: ");
            ComputerPlayer cp = (ComputerPlayer) table.getCurrentPlayer();
            input1 = cp.translator();
            System.out.println(input1);
        } else {
            NetworkPlayer np = (NetworkPlayer) table.getCurrentPlayer();
            //np.broadcastAfterTurn();
            System.out.println(Thread.currentThread().getName());
            input1 = np.getTranslation();
            System.out.println(input1);
            np.resetTranslation();
            //maybe souts
        }
        return input1;
    }

//
    public void setup(ArrayList<Player> players, PlayingMode gameMode) {
        this.players = players;
        ArrayList<Card> d = new Deck().getPlayingCards();
        Collections.shuffle(d);
        int mpi = findDealer(d);
        createTable(gameMode);
        setPlayingOrder(mpi);
        table.adjustToFirstCard();
    }

    private int findDealer(ArrayList<Card> d) {
        int maxValue = -1;
        int playerIndex = 0;
        int maxPlayerIndex = 0;

        for (Player player : players) {
            Card dealtCard = d.remove(0);
            System.out.println("Player " + players.get(playerIndex) + " drew " + dealtCard.toString());
            if (dealtCard.getValue().ordinal() > maxValue && dealtCard.getValue().ordinal() > 4) {
                maxValue = dealtCard.getValue().ordinal();
                maxPlayerIndex = playerIndex;
            }
            playerIndex++;
        }

        if (maxValue == -1){
            System.out.println("You drew only actioncards!");
            findDealer(d);
        }

        System.out.println("\nPlayer " + players.get(maxPlayerIndex) + " is a dealer!");
        return maxPlayerIndex;
    }

    private void setPlayingOrder(int maxPlayerIndex) {
        if (maxPlayerIndex==players.size()-1) {
            this.table.setCurrentTurnIndex(0);
        }else {
            this.table.setCurrentTurnIndex(maxPlayerIndex+1);
        }
        System.out.println(table.getCurrentPlayer() + " starts.");
        System.out.println("\n\n");
//        // set currentTurnIndex to maxPlayerIndex + 1
//        ArrayList<Player> tempArr = new ArrayList<>();
//        for (int i = maxPlayerIndex + 1; i < players.size(); i++) {
//            tempArr.add(players.get(i));
//        }
//        for (int i = 0; i < maxPlayerIndex; i++) {
//            tempArr.add(players.get(i));
//        }
//        tempArr.add(players.get(maxPlayerIndex));
//        players = tempArr;
//
//        System.out.println("Following order applies: ");
//        for (int i = 0; i < players.size(); i++) {
//            System.out.print(players.get(i) + "; ");
//        }

    }

    private void createTable(PlayingMode gameMode) {
        table = new Table(players, gameMode);
        for (Player player : players) {
            player.setTable(table);
        }
        System.out.println();
    }

    /**
     * Prints current card and player's hand
     */
    public void tablePrinter() {
        System.out.println();
        System.out.println("========================================NEW TURN==================================================");
        tui.printCurrentCard(table.getCurrentCard());
        tui.printHand(table.getCurrentPlayer());
    }

    /**
     * @param input receives input given by player
     *              Handles input
     */


    public boolean inputDraw (String input){
        table.getCurrentPlayer().draw(1);
        table.setDrawFourPlayable(true);
        if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size() - 1), this.table)) {
            System.out.println("Would you like to play now?");
            // we need to call the SH corresponding method.
            if (table.getCurrentPlayer()instanceof NetworkPlayer) {
                NetworkPlayer np = (NetworkPlayer) table.getCurrentPlayer();
                Card c = np.getHand().get(np.getHand().size() - 1);
                String card = c.getColor() + " " + c.getValue().toString();
                np.getSh().doDrewPlayableCard(card);
                String choice = np.getTranslation();
                if (choice.equals("skip")) {
                    np.resetTranslation();
                    return true;
                } else if (choice.equals("proceed")) {
                    np.playCard(np.getHand().get(np.getHand().size()-1));
                    np.resetTranslation();
                    return true;
                }
                np.resetTranslation();
            }
            return false;
        } else {
            System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
            return true;
        }

    }

    public boolean inputChallenge(String input){
        if (!table.isDrawFourPlayable()) {
            System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
            table.getPreviousPlayer().draw(4);
            for (int i = 0; i < 4; i++) {
                table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size() - 1);
                table.setDrawFourPlayable(true);
            }
        } else {
            System.out.println("Challenge unsuccessful! You're punished with additional 2 cards ");
            table.getCurrentPlayer().draw(2);
        }
        return true;
    }

    public boolean inputCard(String input){
        if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(input)), this.table)) {
            table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(input)));
            return true;
        }
        System.out.println("Invalid Move. Please try again!");
        if (this.table.getCurrentPlayer()instanceof NetworkPlayer) {
            ((NetworkPlayer)this.table.getCurrentPlayer()).getSh().sendMessage("ERR|E006");
        }

        return false;

    }

    public boolean handleMove(String input) {
        System.out.println(input);
        boolean b = false;
        if (input != null) {
            String[] splitted = input.split(" ");
            if (this.getTable().getPlayingMode().getForwardCount()==0) {
                // do this if playingMode.forwardCount = 0, otherwise only accept draw two cards.
                b = true;
                if (splitted[0].equals("draw")) {
                    b = inputDraw(input);
                } else if (splitted[0].equals("skip")) {
                    b = true;
                } else if (splitted[0].equals("challenge")) {
                    b = inputChallenge(input);
                } else if (splitted.length == 2 && splitted[1].equals("uno")) {
                    b = inputCard(splitted[0]);
                } else if (table.getCurrentPlayer().getHand().size() == 2 && !(splitted.length == 2 && splitted[1].equals("uno"))) {
                    b = inputCard(splitted[0]);
                    // todo implement for NP.
                    System.out.println("You didn't say UNO. You are punished with two cards");
                    table.getCurrentPlayer().draw(2);
                } else if (isInRange(splitted[0])) {
                    b = inputCard(input);
                } else {
                    System.out.println("Query not recognized. Please try again following listed queries!");
                    b = false;
                }
            } else {
                if (isInRange(splitted[0])) {
                    b = inputCard(splitted[0]);
                }
                else if (splitted.length == 2 && splitted[1].equals("uno")) {
                    b = inputCard(splitted[0]);
                } else if (table.getCurrentPlayer().getHand().size() == 2 && !(splitted.length == 2 && splitted[1].equals("uno"))) {
                    b = inputCard(splitted[0]);
                    // todo implement for NP.
                    System.out.println("You didn't say UNO. You are punished with two cards");
                    table.getCurrentPlayer().draw(2);
                }
            }
        }
            return b;
    }

    public boolean isInRange(String str) {
        if (str == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(str);
            if (i < table.getCurrentPlayer().getHand().size()) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    public void removePlayer() {

    }

    /**
     * Changes state of game to gamover when there is last player with cards
     * */

    public Player gameOver() {
        String winner = "";
        if (this.players.size()==1) {
            Player p = this.players.get(0);
            winner = p.getNickname();
            if (p instanceof NetworkPlayer) {
                ((NetworkPlayer) p).getSh().doGameEnded(winner);
            }
            return p;
        }

        for (Player player: table.getScoreBoard().keySet()) {
            // in case there is only 1 player left should also return the player.
            if (table.getScoreBoard().get(player) >= 500){
                winner = player.getNickname();
                for (Player p: players) {
                    if (p instanceof NetworkPlayer) {
                        ((NetworkPlayer) p).getSh().doGameEnded(winner);
                    }
                }
                return player;
            }
        }

        return null;
    }

    public void roundOver(){
        if (table.isHasWinner()) {
            String winner = Collections.max(table.getScoreBoard().entrySet(), Map.Entry.comparingByValue()).getKey().getNickname();
            for (Player p: players) {
                if (p instanceof NetworkPlayer) {
                    ((NetworkPlayer) p).getSh().doRoundEnded(winner);
                }
            }
            Deck d = new Deck();
            Collections.shuffle(d.getPlayingCards());
            int mpi = findDealer(d.getPlayingCards());
            setPlayingOrder(mpi);
            table.setPlayers(players);
            table.setCurrentTurnIndex(0);
            table.setUpRound(d);
            table.adjustToFirstCard();
            this.roundOver = true;
            table.setHasWinner(false);
        }
    }


    //____________________GETTERS AND SETTERS_______________________


    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        play();
    }
}
