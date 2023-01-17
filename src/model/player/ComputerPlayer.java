package model.player;

import model.card.Card;
import model.player.factory.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ComputerPlayer extends Player {
    public ComputerPlayer(String nickname) {
        super(nickname);
    }
    private ArrayList<Integer> possibleMoves = new ArrayList<>();

    public String translator () {
        getValidMoves();
        String s = "";
        if (possibleMoves.isEmpty()) {
            s+="draw";
        }else if (super.getHand().size()==2) {
            s+=determineBestMove();
            s+=" uno";
        }else {
            s+=determineBestMove();
        }
        possibleMoves.clear();
        return s;
    }

    @Override
    public void pickColor() {
        Random r = new Random();
        int random = r.nextInt(4)+1;
        switch (random) {
            case 1:
                System.out.println("computer chose color blue");
                super.getTable().setIndicatedColor(Card.Color.BLUE);
                break;
            case 2:
                System.out.println("computer chose color red");
                super.getTable().setIndicatedColor(Card.Color.RED);
                break;
            case 3:
                System.out.println("computer chose color green");
                super.getTable().setIndicatedColor(Card.Color.GREEN);
                break;
            case 4:
                System.out.println("computer chose color yellow");
                super.getTable().setIndicatedColor(Card.Color.YELLOW);
                break;
            default:
                pickColor();
        }
    }

    public void getValidMoves() {
        for (int i = 0; i < super.getHand().size(); i++) {
            //System.out.println("here");
//            System.out.println(super.getTable().getCurrentPlayer().getHand().get(i)+ " | " + super.getTable().getCurrentCard().toString() + " | " + super.getTable().getIndicatedColor());
            if (super.getTable().getPlayingMode().validMove(super.getTable().getCurrentPlayer().getHand().get(i), super.getTable().getCurrentCard().getColor(), super.getTable().getCurrentCard().getValue(), super.getTable().getIndicatedColor())) {
                possibleMoves.add(i);
            }
        }
    }
    // make a dictionary with score as key and card as value
    public HashMap<Integer, Card> assignScores() {
        return null;
    }
    // currently random move
    public int determineBestMove() {
        Random r = new Random();
        int random = r.nextInt(possibleMoves.size()) ;
        return possibleMoves.get(random);
    }
}
