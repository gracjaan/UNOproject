package controller;

import model.player.HumanPlayer;
import model.player.factory.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class UNO {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter how many players you would like to play with");
        int playersAmount = scanner.nextInt();
        ArrayList<String> playerNames = new ArrayList<String>();
        for (int i=0; i<playersAmount;i++) {
            System.out.println("Enter name of Player " + i);
            String name = scanner.next();
            playerNames.add(name);
        }
        System.out.println("What mode would you like to play in?");
    }






}
