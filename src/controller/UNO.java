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

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UNO uno = new UNO();
        uno.start();
        uno.setup(uno.players);
        uno.play();
    }

    /**
     * Starts the game asking user for basic input to set up a game
     * */
    public void start() {
        System.out.print(">> Please enter number of players: ");
        int playersAmount = scanner.nextInt();
        players = new ArrayList<Player>();
        for (int i=0; i<playersAmount;i++) {
            System.out.print(">> Enter name of Player " + (i+1) + ": ");
            String name = scanner.next();
            players.add(new HumanPlayer(name));
        }
        System.out.print(">> Please enter mode: ");
        String playingMode = scanner.next();
        //switch case statement for different playing modes
        table = new Table(players, new Normal());
        for (Player player: players) {
            player.setTable(table);
        }
        System.out.println();
    }

    /**
     * Plays a game until not finished
     * */
    public void play() {
        boolean exit = false;
        while(!exit){
            while(!this.gameOver()) {
                System.out.println(table.isDrawFourPlayable());
                tablePrinter();
                Scanner scan = new Scanner(System.in);
                System.out.println(">> " + table.getCurrentPlayer().getNickname() + " make your move: ");
                String input1 = scan.nextLine();
                if (!evaluateMove(input1)){
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
            }
            System.out.println(">> GAME OVER!!!");
            System.out.println(">> Would you like to play another one (y/n): ");
            String input2 = scanner.next();
            if (!input2.equals("y")){
                exit = true;
            }
        }
    }

    public void setup(ArrayList<Player> players) {
        // Shuffle the deck
        ArrayList<Card> d = new Deck().getPlayingCards();
        Collections.shuffle(d);

        // Deal one card to each player and find the player with the highest card value
        int maxValue = 0;
        int playerIndex = 0;
        int maxPlayerIndex = 0;
        for (Player player : players) {
            Card dealtCard = d.remove(0);
            System.out.println("Player " + players.get(playerIndex) + " drew " + dealtCard.toString());
            if (dealtCard.getValue().ordinal() > maxValue) {
                maxValue = dealtCard.getValue().ordinal();
                maxPlayerIndex = playerIndex;
            }
            playerIndex++;
        }

        System.out.println("\nPlayer " + players.get(maxPlayerIndex) + " is a dealer!" );

        ArrayList<Player> tempArr = new ArrayList<>();
        for (int i = maxPlayerIndex + 1; i < players.size(); i++ ){
            tempArr.add(players.get(i));
        }
        for (int i = 0; i < maxPlayerIndex; i++ ){
            tempArr.add(players.get(i));
        }
        tempArr.add(players.get(maxPlayerIndex));
        players = tempArr;

        System.out.println("Following order applies: ");
        for (int i = 0; i < players.size(); i++){
            System.out.print(players.get(i) + "; ");
        }
        System.out.println("\n\n");

    }
    /**
     * Prints current card and player's hand
     * */
    public void tablePrinter() {
        System.out.println();
        System.out.println("========================================NEW TURN==================================================");
        tui.printCurrentCard(table.getCurrentCard());
        tui.printHand(table.getCurrentPlayer());
    }

    /**
     * @param input receives input given by player
     * Handles input
     * */
    public boolean evaluateMove (String input) {
        String[] splitted = input.split(" ");
        if (splitted[0].equals("challenge") && table.getCurrentCard().getColor() == Card.Color.WILD && table.getCurrentCard().getValue() == Card.Value.DRAW_FOUR){
            if (!table.isDrawFourPlayable()){
                System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
                table.getPreviousPlayer().draw(4);
                for (int i = 0; i < 4; i++){
                    table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size()-1);
                    table.setDrawFourPlayable(true);
                }
            }
            else {
                System.out.println("Challenge unsuccessful!");
            }
            return false;
        }
        else if (table.getCurrentPlayer().getHand().size() != 2) {
            if (splitted[0].equals("draw")) {
                table.getCurrentPlayer().draw(1);
                table.setDrawFourPlayable(true);
                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(table.getCurrentPlayer().getHand().size()-1),table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
                    System.out.println("Would you like to play now?");
                    return false;
                }
                else {
                    System.out.println("One card was added to " + table.getCurrentPlayer().getNickname() + "'s hand, it cannot be played.");
                }
            } else {
                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
                    table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
                } else {
                    System.out.println("Invalid Move. Please try again!");
                    return false;
                }
            }
        }
        else {
            if (table.getCurrentPlayer().getHand().size() == 2 && splitted.length == 2 && splitted[1].equals("uno")) {
                evaluateMove(splitted[0]);
            } else if (table.getCurrentPlayer().getHand().size() == 2) {
                System.out.println("You didn't say UNO. The punishment is drawing 2 cards!");
                table.getCurrentPlayer().draw(2);
            }
        }
        return true;
    }

//        if (splitted[0].equals("draw")) {
//            table.getCurrentPlayer().draw(1);
//            table.setDrawFourPlayable(true);
//        }
//        else if (splitted[0].equals("challenge") && table.getCurrentCard().getColor() == Card.Color.WILD && table.getCurrentCard().getValue() == Card.Value.DRAW_FOUR){
//            if (!table.isDrawFourPlayable()){
//                System.out.println("Previous player was punished with drawing 4 cards, since he placed it illegally.");
//                table.getPreviousPlayer().draw(4);
//                for (int i = 0; i < 4; i++){
//                    table.getCurrentPlayer().getHand().remove(table.getCurrentPlayer().getHand().size()-1);
//                    table.setDrawFourPlayable(true);
//                }
//            }
//        }
//        else if (splitted[0].equals("challenge")){
//
//        }
//        else if (table.getCurrentPlayer().getHand().size() == 2) {
//
//            if (!(table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor()))){
//                System.out.println("Invalid Move. Please try again!");
//                return false;
//            }
//
//            if (splitted.length == 2 && splitted[1].equals("uno")) {
//                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
//            }
//            else {
//                System.out.println("You didn't say UNO. The punishment is drawing 2 cards!");
//                table.getCurrentPlayer().draw(2);
//            }
//        }
//        else {
//            if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
//                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
//            }else {
//                System.out.println("Invalid Move. Please try again!");
//                return false;
//            }
//        }
//        table.setDrawFourPlayable(true);
//        return true;
//    }

    /**
     * Changes state of game to gamover when there is last player with cards
     * */
    public boolean gameOver() {
        if (table.getPlayers().size()<=1) {
            int position = 0;
            for (Player player: table.getPlayers()) {
                System.out.println(position+ ". " + player.toString());
                position++;
            }
            return true;
        }
        return false;
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
