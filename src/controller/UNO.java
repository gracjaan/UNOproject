package controller;

import model.player.HumanPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.Normal;
import view.TUI;
import view.UI;

import java.util.ArrayList;
import java.util.Scanner;

public class UNO {
    private TUI tui = new TUI();
    Scanner scanner = new Scanner(System.in);
    private Table table;

    private ArrayList<Player> players;


    public static void main(String[] args) {
        UNO uno = new UNO();
        uno.start();
        uno.play();
    }

    public void start() {
        System.out.println("Please enter how many players you would like to play with");
        int playersAmount = scanner.nextInt();
        players = new ArrayList<Player>();
        for (int i=0; i<playersAmount;i++) {
            System.out.println("Enter name of Player " + (i+1));
            String name = scanner.next();
            players.add(new HumanPlayer(name) {
            });
        }
        System.out.println("What mode would you like to play in?");
        String playingMode = scanner.next();
        //switch case statement for different playing modes
        table = new Table(players, new Normal());
        for (Player player: players) {
            player.setTable(table);
        }
    }
    public void play() {
        while(!this.gameOver()) {
            evaluateMove();
            String ind = scanner.next();
            if (ind.equals("draw")) {
                table.getCurrentPlayer().draw(1);
            }else {
                if (table.getPlayingMode().validMove(table.getCurrentPlayer().getHand().get(Integer.parseInt(ind)), table.getCurrentCard().getColor(), table.getCurrentCard().getValue(), table.getIndicatedColor())) {
                  table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(ind)));
                }else {
                    System.out.println("Invalid Move. Please try again!");
                    continue;
                }
            }
            table.nextTurn();
        }
    }
    public void evaluateMove() {
        tui.printCurrentCard(table.getCurrentCard());
        tui.printHand(table.getCurrentPlayer());
        System.out.println(table.getCurrentPlayer().getNickname() + " make your move!");
    }
    public void reset() {

    }
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
