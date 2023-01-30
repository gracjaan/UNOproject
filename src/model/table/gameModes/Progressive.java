package model.table.gameModes;

import model.card.Card;
import model.deck.Deck;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import model.table.Table;
import model.table.gameModes.factory.PlayingMode;

import java.util.ArrayList;

public class Progressive extends PlayingMode {
    // inform the NP.
    // todo progressive uno also for +4 cards?
    // check if player has a plus 2 then give him the opportunity to forward it, otherwise do the same thing.
    @Override
    public void performWildCardAction(Card card, Player player, Player nextPlayer) {
        switch (card.getValue()) {
            case DRAW_TWO:
                if (hasDrawTwo(nextPlayer)) {
                    if (nextPlayer instanceof NetworkPlayer) {
                        NetworkPlayer np = (NetworkPlayer) nextPlayer;
                        np.getSh().doBroadcastGameMessage("You can forward drawing two cards, by placing your draw two card.");
                    }else {
                        System.out.println("You can forward drawing two cards, by placing your draw two card.");
                    }
                    // give him the chance to play it. --> can be done just by increasing the forwardCount? (but then in valid move we have to check if the forward count is above 0, only +2 cards are valid moves)
                    super.setForwardCount(super.getForwardCount()+2);
                    System.out.println(super.getForwardCount());
                }else {
                    // also different cp depending on the gameMode?
                    nextPlayer.draw(super.getForwardCount()+2);
                    super.setForwardCount(0);
                    player.getTable().skip();
                }
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

    private boolean hasDrawTwo(Player p) {
        for (Card c: p.getHand()) {
            if (c.getValue()== Card.Value.DRAW_TWO) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean validMove(Card cardToPlay, Table table) {
        Card.Color color = table.getCurrentCard().getColor();
        Card.Value value = table.getCurrentCard().getValue();
        Card.Color indicatedColor = table.getIndicatedColor();

        if(indicatedColor==null&&super.getForwardCount()==0) {
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
        }else if (indicatedColor!=null&&super.getForwardCount()==0) {
            if (indicatedColor.equals(cardToPlay.getColor())) {
                // reset the indicatedColor
                table.resetIndicatedColor();
                return true;
            }
        } else {
            if (cardToPlay.getValue() == Card.Value.DRAW_TWO) {
                return true;
            }
        }
        return false;
    }

}
