package model.table.gameModes;

import model.card.Card;
import model.deck.Deck;
import model.player.NetworkPlayer;
import model.table.Table;
import model.table.gameModes.factory.PlayingMode;
import model.player.factory.Player;

import java.util.ArrayList;

public class Normal extends PlayingMode {

    /**
     * Give it the player you want the action to be performed by if Pick color that is the current player, otherwise the next.
     * */
    @Override
    public void performWildCardAction(Card card, Player player, Player nextPlayer) {
        switch (card.getValue()) {
            case DRAW_TWO:
                nextPlayer.draw(2);
                player.getTable().skip();
                break;
            case DRAW_FOUR:
                if (player.getTable().isHasWinner()){
                    break;
                }
                player.pickColor();
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
                player.pickColor();
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


    /**
     * @param cardToPlay card to be played
     * @ensures that move is valid
     * @return true if move is valid, otherwise false
     * */
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
            if (indicatedColor == cardToPlay.getColor()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param players receives all players
     * @param deck receives deck
     * Distributes hands
     * */
    @Override
    public void distributeHands(ArrayList<Player> players, Deck deck) {
        ArrayList<Card> tempDeck = deck.getPlayingCards();
        for (Player player: players) {
            ArrayList<Card> tempHand = new ArrayList<>();
            for (int i=0;i<7;i++) {
                tempHand.add(tempDeck.get(0));
                tempDeck.remove(0);
            }
            player.setHand(tempHand);
        }
        deck.setPlayingCards(tempDeck);
    }
}
