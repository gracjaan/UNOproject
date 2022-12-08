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

    public static void main(String[] args) {
        TUI tui = new TUI();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter how many players you would like to play with");
        int playersAmount = scanner.nextInt();
        ArrayList<Player> players = new ArrayList<Player>();
        for (int i=0; i<playersAmount;i++) {
            System.out.println("Enter name of Player " + (i+1));
            String name = scanner.next();
            players.add(new HumanPlayer(name) {
            });
        }
        System.out.println("What mode would you like to play in?");
        String playingMode = scanner.next();
        //switch case statement for different playing modes
        Table table = new Table(players, new Normal());
        for (Player player: players) {
            player.setTable(table);
        }
        boolean exit = false;
        while(!exit) {
            tui.printCurrentCard(table.getCurrentCard());
            tui.printHand(table.getCurrentPlayer());
            System.out.println(table.getCurrentPlayer().getNickname() + " make your move!");
            String ind = scanner.next();
            if (ind.equals("draw")) {
                table.getCurrentPlayer().draw(1);
            }else {
                table.getCurrentPlayer().playCard(table.getCurrentPlayer().getHand().get(Integer.parseInt(ind)));
            }
            table.nextTurn();
        }
    }
}
