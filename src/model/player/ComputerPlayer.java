package model.player;

import model.card.Card;
import model.player.factory.Player;

import java.util.*;

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
        Card.Color color = cardColors();
        switch (color) {
            case BLUE:
                System.out.println("computer chose color blue");
                super.getTable().setIndicatedColor(Card.Color.BLUE);
                break;
            case RED:
                System.out.println("computer chose color red");
                super.getTable().setIndicatedColor(Card.Color.RED);
                break;
            case GREEN:
                System.out.println("computer chose color green");
                super.getTable().setIndicatedColor(Card.Color.GREEN);
                break;
            case YELLOW:
                System.out.println("computer chose color yellow");
                super.getTable().setIndicatedColor(Card.Color.YELLOW);
                break;
            default:
                pickColor();
        }
    }

    @Override
    public void chooseSwitchHands() {
        for (Player p: super.getTable().getPlayers()) {
            if (!p.getNickname().equals(super.getNickname())) {
                super.swapHands(p);
                break;
            }
        }
    }

    public Card.Color cardColors(){
        HashMap<Card.Color, Integer> map = new HashMap<>();
        for (Card card: super.getHand()){
            if (map.containsKey(card.getColor())){
                int i = map.get(card.getColor()) + 1;
                map.put(card.getColor(), i);
            }
            else {
                map.put(card.getColor(), 1);
            }
        }

        map.remove(Card.Color.WILD);

        int maxOccurances = 0;
        Card.Color max = Card.Color.YELLOW;
        for (Map.Entry<Card.Color, Integer> entry: map.entrySet()){
            if (entry.getValue() > maxOccurances){
                maxOccurances = entry.getValue();
                max = entry.getKey();
            }
        }
        System.out.println(max);
        return max;
    }

    public void getValidMoves() {
        for (int i = 0; i < super.getHand().size(); i++) {
            //System.out.println("here");
//            System.out.println(super.getTable().getCurrentPlayer().getHand().get(i)+ " | " + super.getTable().getCurrentCard().toString() + " | " + super.getTable().getIndicatedColor());
            if (super.getTable().getPlayingMode().validMove(super.getTable().getCurrentPlayer().getHand().get(i), super.getTable())) {
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
        int best = 0;
        for (int i = 0; i < possibleMoves.size(); i++){
            if (getHand().get(possibleMoves.get(i)).getValue() != Card.Value.DRAW_FOUR && getHand().get(possibleMoves.get(i)).getValue() != Card.Value.PICK_COLOR){
                best = i;
            }
        }
        return possibleMoves.get(best);
    }
}
