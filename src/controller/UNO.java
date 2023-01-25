package controller;

import model.card.Card;
import model.deck.Deck;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import view.TUI;

import java.nio.Buffer;
import java.util.*;

public class UNO implements Runnable{
    private TUI tui = new TUI();
    private ArrayList<Player> players;
    private Table table;
    private Card card;
    private boolean roundOver = false;

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UNO uno = new UNO();
        uno.start();
        uno.setup(uno.players);
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
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.next();
            }
        }
    }



    public void informAll(){
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
                    System.out.println(np.getTranslation());
                    System.out.println("brum");
                    //maybe souts
                }
                if (!handleMove(input1)) {
                    //tell clienthandler move was invlaid
                    continue;
                }
//                if (input1.equals("challenge") && table.getCurrentCard().getColor() == Card.Color.WILD && table.getCurrentCard().getValue() == Card.Value.DRAW_FOUR){
//                    if (!table.isDrawFourPlayable()){
//                        System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
//                        table.getPreviousPlayer().draw(4);
//                        for (int i = 0; i < 4; i++){
//                            table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size()-1);
//                            table.setDrawFourPlayable(true);
//                        }
//                        continue;
//                    }
//

                table.nextTurn();
//                for (Player p: players) {
//                    if (p instanceof NetworkPlayer) {
//                        ((NetworkPlayer) p).broadcastAfterTurn();
//                    }
//                }
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

//    public void setup(ArrayList<Player> players) {
//        // Shuffle the deck
//        ArrayList<Card> d = new Deck().getPlayingCards();
//        Collections.shuffle(d);
//
//        // Deal one card to each player and find the player with the highest card value
//        int maxValue = 0;
//        int playerIndex = 0;
//        int maxPlayerIndex = 0;
//        for (Player player : players) {
//            Card dealtCard = d.remove(0);
//            System.out.println("Player " + players.get(playerIndex) + " drew " + dealtCard.toString());
//            if (dealtCard.getValue().ordinal() > maxValue) {
//                maxValue = dealtCard.getValue().ordinal();
//                maxPlayerIndex = playerIndex;
//            }
//            playerIndex++;
//        }
//
//        System.out.println("\nPlayer " + players.get(maxPlayerIndex) + " is a dealer!");
//
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
//        System.out.println("\n\n");
//
//        table = new Table(players, new Normal());
//        for (Player player : players) {
//            player.setTable(table);
//        }
//        System.out.println();
//
//        table.adjustToFirstCard();
//
//    }
    public void setup(ArrayList<Player> players) {
        this.players = players;
        ArrayList<Card> d = new Deck().getPlayingCards();
        Collections.shuffle(d);
        int mpi = findDealer(d);
        setPlayingOrder(mpi);
        createTable();
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
        // set currentTurnIndex to maxPlayerIndex + 1
        ArrayList<Player> tempArr = new ArrayList<>();
        for (int i = maxPlayerIndex + 1; i < players.size(); i++) {
            tempArr.add(players.get(i));
        }
        for (int i = 0; i < maxPlayerIndex; i++) {
            tempArr.add(players.get(i));
        }
        tempArr.add(players.get(maxPlayerIndex));
        players = tempArr;

        System.out.println("Following order applies: ");
        for (int i = 0; i < players.size(); i++) {
            System.out.print(players.get(i) + "; ");
        }
        System.out.println("\n\n");
    }

    private void createTable() {
        table = new Table(players, new Normal());
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
//    public boolean evaluateMove(String input) {
////        String[] splitted = input.split(" ");
////        if (splitted[0].equals("challenge") && table.getCurrentCard().getColor() == Card.Color.WILD && table.getCurrentCard().getValue() == Card.Value.DRAW_FOUR) {
////            if (!table.isDrawFourPlayable()) {
////                System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
////                table.getPreviousPlayer().draw(4);
////                for (int i = 0; i < 4; i++) {
////                    table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size() - 1);
////                    table.setDrawFourPlayable(true);
////                }
////            } else {
////                System.out.println("Challenge unsuccessful!");
////            }
////            return false;
////        } else if (table.getCurrentPlayer().getHand().size() != 2) {
////            if (splitted[0].equals("draw")) {
////                table.getCurrentPlayer().draw(1);
////                table.setDrawFourPlayable(true);
////                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size() - 1), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                    System.out.println("Would you like to play now?");
////                    return false;
////                } else {
////                    System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
////                }
////            } else {
////                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                    table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
////                } else {
////                    System.out.println("Invalid Move. Please try again!");
////                    return false;
////                }
////            }
////        } else {
////            if (splitted[0].equals("draw")) {
////                table.getCurrentPlayer().draw(1);
////                table.setDrawFourPlayable(true);
////                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size() - 1), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                    System.out.println("Would you like to play now?");
////                    return false;
////                } else {
////                    System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
////                }
////            }
////            else if (table.getCurrentPlayer().getHand().size() == 2 && splitted.length == 2 && splitted[1].equals("uno")) {
////                //evaluateMove(splitted[0]);
////                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                    table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
////                } else {
////                    System.out.println("Invalid Move. Please try again!");
////                    return false;
////                }
////            } else if (table.getCurrentPlayer().getHand().size() == 2) {
////                System.out.println("You didn't say UNO. The punishment is drawing 2 cards!");
////                table.getCurrentPlayer().draw(2);
////            }
////        }
////        //return true;
////
////
////
////        if (splitted.length == 1) {
////            if (splitted[0].equals("challenge") && table.getCurrentCard().getColor() == Card.Color.WILD && table.getCurrentCard().getValue() == Card.Value.DRAW_FOUR) {
////                if (!table.isDrawFourPlayable()) {
////                    System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
////                    table.getPreviousPlayer().draw(4);
////                    for (int i = 0; i < 4; i++) {
////                        table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size() - 1);
////                        table.setDrawFourPlayable(true);
////                    }
////                } else {
////                    System.out.println("Challenge unsuccessful!");
////                }
////                return false;
////            }
////            else if (splitted[0].equals("draw")) {
////                table.getCurrentPlayer().draw(1);
////                table.setDrawFourPlayable(true);
////                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size() - 1), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                    System.out.println("Would you like to play now?");
////                    return false;
////                } else {
////                    System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
////                }
////            }
////            else {
////                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                    table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
////                } else {
////                    System.out.println("Invalid Move. Please try again!");
////                    return false;
////                }
////            }
////        }
////        return true;
////    }
//        //________________prev
////        if (splitted[0].equals("draw")) {
////            table.getCurrentPlayer().draw(1);
////            table.setDrawFourPlayable(true);
////            if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size()-1),table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                System.out.println("Would you like to play now?");
////                return false;
////            }
////            else {
////                System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
////            }
////        } else if (splitted[0].equals("challenge") && table.getCurrentCard().getColor() == Card.Color.WILD && table.getCurrentCard().getValue() == Card.Value.DRAW_FOUR) {
////            if (!table.isDrawFourPlayable()) {
////                System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
////                table.getPreviousPlayer().draw(4);
////                for (int i = 0; i < 4; i++) {
////                    table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size() - 1);
////                    table.setDrawFourPlayable(true);
////                }
////            }
////            return false;
////        } else if (splitted[0].equals("challenge")) {
////            System.out.println("challenge unsuccessful");
////        } else if (table.getCurrentPlayer().getHand().size() == 2) {
////
////            if (!(table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor()))) {
////                System.out.println("Invalid Move. Please try again!");
////                return false;
////            }
////
////            if (splitted.length == 2 && splitted[1].equals("uno")) {
////                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
////            } else {
////                System.out.println("You didn't say UNO. The punishment is drawing 2 cards!");
////                table.getCurrentPlayer().draw(2);
////            }
////        } else {
////            if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
////                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
////            } else {
////                System.out.println("Invalid Move. Please try again!");
////                return false;
////            }
////        }
////        table.setDrawFourPlayable(true);
////        return true;
////    }
//        // _________ last version
//        boolean b = true;
//        if (table.getCurrentPlayer().getHand().size() == 2){
//            b = helperTwoCards(input);
//        }
//        else {
//            b = helperNotTwoCards(input);
//        }
//        return b;
//    }

//    public boolean helperNotTwoCards(String input){
//        String[] splitted = input.split(" ");
//        if (splitted.length == 1) {
//            if (splitted[0].equals("challenge") && table.getCurrentCard().getColor() == Card.Color.WILD && table.getCurrentCard().getValue() == Card.Value.DRAW_FOUR) {
//                if (!table.isDrawFourPlayable()) {
//                    System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
//                    table.getPreviousPlayer().draw(4);
//                    for (int i = 0; i < 4; i++) {
//                        table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size() - 1);
//                        table.setDrawFourPlayable(true);
//                    }
//                } else {
//                    System.out.println("Challenge unsuccessful!");
//                }
//                return false;
//            }
//            else if (splitted[0].equals("draw")) {
//                table.getCurrentPlayer().draw(1);
//                table.setDrawFourPlayable(true);
//                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size() - 1), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
//                    System.out.println("Would you like to play now?");
//                    return false;
//                } else {
//                    System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
//                }
//            }
//            else {
//                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
//                    table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
//                } else {
//                    System.out.println("Invalid Move. Please try again!");
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public boolean helperTwoCards (String input){
//        boolean b = true;
//        String[] splitted = input.split(" ");
//        if (splitted.length == 2 && splitted[1].equals("uno")){
//            b = helperNotTwoCards(splitted[0]);
//        }
//        else {
//            System.out.println("You didn't say UNO. You get punished with two cards");
//            table.getCurrentPlayer().draw(2);
//        }
//        return b;
//    }

    public boolean inputDraw (String input){
        table.getCurrentPlayer().draw(1);
        table.setDrawFourPlayable(true);
        if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size() - 1), this.table)) {
            System.out.println("Would you like to play now?");
            return false;
        } else {
            System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
        }
        return true;
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
        return false;

    }

    public boolean handleMove(String input) {
        System.out.println(input);
        boolean b = false;
        if (input != null) {
            String[] splitted = input.split(" ");
            System.out.println(Arrays.toString(splitted));
            System.out.println("not null");
            b = true;
            if (splitted[0].equals("draw")) {
                b = inputDraw(input);
            } else if (splitted[0].equals("challenge")) {
                b = inputChallenge(input);
            } else if (splitted.length == 2 && splitted[1].equals("uno")) {
                b = inputCard(splitted[0]);
            } else if (table.getCurrentPlayer().getHand().size() == 2 && !(splitted.length == 2 && splitted[1].equals("uno"))) {
                b = inputCard(splitted[0]);
                System.out.println("You didn't say UNO. You are punished with two cards");
                table.getCurrentPlayer().draw(2);
            } else if (isInRange(splitted[0])) {
                b = inputCard(input);
            } else {
                System.out.println("Query not recognized. Please try again following listed queries!");
                b = false;
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

    /**
     * Changes state of game to gamover when there is last player with cards
     * */
    public Player gameOver() {
        String winner = "";
        for (Player player: table.getScoreBoard().keySet()) {
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
