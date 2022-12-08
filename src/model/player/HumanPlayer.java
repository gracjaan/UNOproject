package model.player;

import model.card.Card;
import model.player.factory.Player;
import model.table.Table;

public class HumanPlayer extends Player {
    public HumanPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public boolean playCard(Card card) {
        boolean valid = super.getTable().getPlayingMode().validMove(card, getTable().getCurrentCard().getColor(), getTable().getCurrentCard().getValue());
        if (valid) {
            super.getHand().remove(card);
            super.getTable().setCurrentCard(card);
            super.getTable().getDeck().getUsedCards().add(card);
            if (card.getValue()==Card.Value.PICK_COLOR) {
                super.getTable().getPlayingMode().performWildCardAction(card, this);
            }else {
                Player nextPlayer;
                if (super.getTable().getCurrentTurnIndex()<super.getTable().getPlayers().size()-1) {
                    nextPlayer = super.getTable().getPlayers().get(super.getTable().getCurrentTurnIndex()+1);
                }
                else {
                    nextPlayer = super.getTable().getPlayers().get(0);
                }
                super.getTable().getPlayingMode().performWildCardAction(card, nextPlayer);
            }
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
    public Card.Color pickColor() {
        return null;
    }
}
