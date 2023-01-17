package controller;

import model.card.Card;
import model.deck.Deck;
import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import view.TUI;

import java.util.*;

public class UNO {
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
        System.out.print(">> Please enter number of players: ");
        int playersAmount = scanner.nextInt();
        players = new ArrayList<Player>();
        for (int i = 0; i < playersAmount; i++) {
            System.out.print(">> Enter name of Player " + (i + 1) + ": ");
            String name = scanner.next();
            players.add(new HumanPlayer(name));
        }
        System.out.print(">> Please enter mode: ");
        String playingMode = scanner.next();
    }

    /**
     * Plays a game until not finished
     */
    public void play() {
        boolean exit = false;
        while (!gameOver()) {
            this.roundOver = false;
            while (!this.roundOver) {
                System.out.println(table.isDrawFourPlayable());
                tablePrinter();
                Scanner scan = new Scanner(System.in);
                System.out.println(">> " + table.getCurrentPlayer().getNickname() + " make your move: ");
                String input1 = scan.nextLine();
                if (!handleMove(input1)) {
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
//                }
                table.nextTurn();
                this.roundOver();
            }
            }
            System.out.println(">> GAME OVER!!!");
            System.out.println(">> Would you like to play another one (y/n): ");
            String input2 = scanner.next();
            if (!input2.equals("y")) {
            exit = true;
        }
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
        if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size() - 1), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
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
            System.out.println("Challenge unsuccessful! You're punished with additional 2 cards so it is +6 in total");
            table.getCurrentPlayer().draw(2);
        }
        return true;
    }

    public boolean inputCard(String input){
        if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(input)), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
            table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(input)));
            return true;
        }
        System.out.println("Invalid Move. Please try again!");
        return false;

    }

    public boolean handleMove(String input){
        String [] splitted = input.split(" ");
        boolean b = true;

        if (splitted[0].equals("draw")){
            b = inputDraw(input);
        }
        else if (splitted[0].equals("challenge")){
            b = inputChallenge(input);
        }
        else if (splitted.length == 2 && splitted[1].equals("uno")){
            b = inputCard(splitted[0]);
        }
        else if (table.getCurrentPlayer().getHand().size() == 2 && !(splitted.length == 2 && splitted[1].equals("uno"))){
            b = inputCard(splitted[0]);
            System.out.println("You didn't say UNO. You are punished with two cards");
            table.getCurrentPlayer().draw(2);
        }
        else {
            b = inputCard(input);
        }
        return b;
    }

    /**
     * Changes state of game to gamover when there is last player with cards
     * */
    public boolean gameOver() {
        int position = 0;
        for (Player player: table.getScoreBoard().keySet()) {
            if (table.getScoreBoard().get(player) >= 500){
                System.out.println(">> Player " + player.getNickname());
                return true;
            }
        }
        return false;
    }

    public void roundOver(){
        if (table.isHasWinner()) {
            System.out.println("aldfgh");
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
}
