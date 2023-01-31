package model.table.gameModes;

import model.card.Card;
import model.deck.Deck;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.factory.PlayingMode;

import java.util.ArrayList;

public class SevenZero extends PlayingMode {
    @Override
    public void performWildCardAction(Card card, Player player, Player nextPlayer) {
        switch (card.getValue()) {
            case ZERO:
                System.out.println("hands have been passed in the order of play");
                // inform the np with gameMessage.
                passDownHands(player.getTable());
                break;
            case SEVEN:
                player.chooseSwitchHands();
                break;
            case DRAW_TWO:
                nextPlayer.draw(2);
                player.getTable().skip();
                break;
            case DRAW_FOUR:
                if (player.getTable().isHasWinner()){
                    break;
                }
                if (player instanceof NetworkPlayer) {
                    NetworkPlayer np = (NetworkPlayer) player;
                    np.getSh().doAskColour();
                    // wait until pickColor method is called before proceeding
                }else {
                    player.pickColor();
                }
                nextPlayer.draw(4);
                player.getTable().drawFourEligibility();
                break;
            case SKIP:
                player.getTable().skip();
                for (Player p: player.getTable().getPlayers()) {
                    if (p instanceof NetworkPlayer) {
                        ((NetworkPlayer) p).getSh().doBroadcastTurnSkipped(nextPlayer.getNickname());
                    }
                }
                break;
            case PICK_COLOR:
                if (player.getTable().isHasWinner()){
                    break;
                }
                if (player instanceof NetworkPlayer) {
                    NetworkPlayer np = (NetworkPlayer) player;
                    np.getSh().doAskColour();
                    // wait until pickColor method is called before proceeding

                }else {
                    player.pickColor();
                }

                break;
            case CHANGE_DIRECTION:
                for (Player p: player.getTable().getPlayers()) {
                    if (p instanceof NetworkPlayer) {
                        ((NetworkPlayer) p).getSh().doBroadcastReverse(String.valueOf(player.getTable().isClockWise()));
                    }
                }
                player.getTable().reversePlayers();
                break;
        }
    }
    // todo everybody gets the same hand(first init of tempHand)
    public void passDownHands(Table table) {
        if (table.isClockWise()) {
            // temphand = last players hand
            ArrayList<Card> tempHand = table.getPlayers().get(table.getPlayers().size()-1).getHand();
            for (int i = 0;i<table.getPlayers().size();i++) {
                Player p = table.getPlayers().get(i);
                p.setHand(tempHand);
                tempHand = p.getHand();
            }
            } else {
            ArrayList<Card> tempHand = table.getPlayers().get(0).getHand();
            for (int i=table.getPlayers().size()-1;i>=0;i--) {
                Player p = table.getPlayers().get(i);
                p.setHand(tempHand);
                tempHand = p.getHand();
            }
        }
    }

    @Override
    public boolean validMove(Card cardToPlay, Table table) {
        Card.Color color = table.getCurrentCard().getColor();
        Card.Value value = table.getCurrentCard().getValue();
        Card.Color indicatedColor = table.getIndicatedColor();

        if(indicatedColor==null) {
//            System.out.println("Color of top card "+color);
//            System.out.println("Value of top card "+value);
//            System.out.println("Color of card to play "+cardToPlay.getColor());
//            System.out.println("Value of card to play "+cardToPlay.getValue());
            if (cardToPlay.getColor() == Card.Color.WILD && color == Card.Color.WILD) {
                return false;
            } else if (cardToPlay.getColor() == Card.Color.WILD) {
                return true;
            }
            if (color == cardToPlay.getColor()) {
                return true;
            }
            if (value == cardToPlay.getValue()) {
                return true;
            }
        }else {
            if (indicatedColor.equals(cardToPlay.getColor())) {
                // reset the indicatedColor
                table.resetIndicatedColor();
                return true;
            }
        }
        return false;
    }

}
