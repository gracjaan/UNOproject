package view;

import model.card.Card;
import model.player.factory.Player;

public class TUI implements UI {


    @Override
    public void printTable() {
    }

    @Override
    public void printHand(Player player) {
        String s = "";
        for (Card card: player.getHand()){
            s+=card.toString();
            s+="  |      ";
        }
        System.out.println(s);
    }


    @Override
    public void printCurrentCard(Card card) {
        System.out.println(card.toString());
    }

    @Override
    public void printWinners() {

    }
}
