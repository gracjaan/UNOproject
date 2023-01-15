package controller;

import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import view.TUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UNO {
    private TUI tui = new TUI();
    private ArrayList<Player> players;
    private Table table;

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UNO uno = new UNO();
        uno.start();
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
            players.add(new HumanPlayer(name) {
            });
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
                tablePrinter();
                Scanner scan = new Scanner(System.in);
                System.out.println(">> " + table.getCurrentPlayer().getNickname() + " make your move: ");
                String input1 = scan.nextLine();
                if (!evaluateMove(input1)){
                    continue;
                }
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
    public boolean evaluateMove (String input){
        String [] splitted = input.split(" ");
        if (splitted[0].equals("draw")) {
            table.getCurrentPlayer().draw(1);
        }
        else if (table.getCurrentPlayer().getHand().size() == 2) {
            System.out.println(Arrays.toString(splitted));
            if (splitted.length == 2 && splitted[1].equals("uno")) {
                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
            } else if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
            } else {
                System.out.println("Invalid Move. Please try again!");
                return false;
            }
        }
        else {
            if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(splitted[0])));
            }else {
                System.out.println("Invalid Move. Please try again!");
                return false;
            }
        }
        return true;
    }

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

}
