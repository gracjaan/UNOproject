package model.player;

import model.card.Card;
import model.player.factory.Player;

import java.util.Scanner;

public class HumanPlayer extends Player {
    public HumanPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public boolean playCard(Card card) {
        boolean valid = super.getTable().getPlayingMode().validMove(card, getTable().getCurrentCard().getColor(), getTable().getCurrentCard().getValue(), super.getTable().getIndicatedColor());
        if (valid) {
            super.getHand().remove(card);
            super.getTable().setCurrentCard(card);
            super.getTable().getDeck().getUsedCards().add(card);
            Player nextPlayer;
            if (super.getTable().getCurrentTurnIndex()<super.getTable().getPlayers().size()-1) {
                nextPlayer = super.getTable().getPlayers().get(super.getTable().getCurrentTurnIndex()+1);
            }
            else {
                nextPlayer = super.getTable().getPlayers().get(0);
            }
            super.getTable().getPlayingMode().performWildCardAction(card, this, nextPlayer);




        }
        else {
            System.out.println("Invalid Move! Please try again.");
            // ask for move with boolean in UNO
        }
        return valid;
    }

    @Override
    public void draw(int amount) {
        for (int i=0; i<amount;i++) {
            super.getHand().add(super.getTable().getDeck().getPlayingCards().get(0));
            super.getTable().getDeck().getPlayingCards().remove(0);
        }
    }

    @Override
    public void pickColor() {
        System.out.println("Please pick a color!");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        switch (input) {
            case "blue":
                super.getTable().setIndicatedColor(Card.Color.BLUE);
                break;
            case "red":
                super.getTable().setIndicatedColor(Card.Color.RED);
                break;
            case "green":
                super.getTable().setIndicatedColor(Card.Color.GREEN);
                break;
            case "yellow":
                super.getTable().setIndicatedColor(Card.Color.YELLOW);
                break;
            default:
                pickColor();
        }
//        super.getTable().nextTurn();
    }
}
