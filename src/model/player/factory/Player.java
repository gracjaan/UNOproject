package model.player.factory;

import controller.UNO;
import model.card.Card;
import model.player.contract.PlayerActions;
import model.table.Table;

import java.util.ArrayList;

public abstract class Player implements PlayerActions {
    private String nickname;
    private ArrayList<Card> hand;
    private UNO UNO;
    private Table table;


    /**
     * @param amount receives amount of cards to draw
     * @require draw is possible to be performed
     * adds top card of playing cards to player and removes it form playing cards
     * */
    @Override
    public void draw(int amount) {
        if(checkDrawPossibility(amount)){
            for (int i=0; i<amount;i++) {
                getHand().add(getTable().getDeck().getPlayingCards().get(0));
                getTable().getDeck().getPlayingCards().remove(0);
            }
        }
    }
    /**
     * @param card receives card to be played
     * Removes card from players hand, sets it to currentCard and adds it to usedCards
     * Calls performWildCardAction to do specific task
     * Checks if player is a winner
     * */
    @Override
    public void playCard(Card card) {
        placeCard(card);
        Player nextPlayer = this.getTable().getNextPlayer();
        this.getTable().getPlayingMode().performWildCardAction(card, this, nextPlayer);

    }
    @Override
    public void placeCard(Card card) {
        getTable().setDrawFourPlayable(true);
        getHand().remove(card);
        isWinner();
        getTable().setCurrentCard(card);
        getTable().getDeck().getUsedCards().add(card);
    }
    /**
     * @param amount amount of cards to possibly draw
     * @ensures size of playing cards is bigger than amount of cards to draw
     * @return true if size of playing cards is bigger than amount of cards to draw, and false otherwise
     * */
    @Override
    public boolean checkDrawPossibility(int amount){
        if(amount > getTable().getDeck().getPlayingCards().size()){
            getTable().getDeck().reShuffle();
        }

        if(getTable().getDeck().getPlayingCards().size() >= amount){
            return true;
        }
        else{
            System.out.println("Hmmmm....seems like you love drawing cards. Unfortunately, there are only 108 of them in deck and you all have it in hands");
            return false;
        }
    }

    /**
     * @return true if player is a winner and false otherwise
     * Adds player to scoreboard
     * */
    @Override
    public boolean isWinner() {
        if (getHand().size()==0) {
            System.out.println("Player " + this.getNickname() + " won this round.");
            getTable().calculateScores(this);
            table.setHasWinner(true);
            for (Player p: this.getTable().getScoreBoard().keySet()) {
                System.out.println(p.getNickname() + " : " + this.getTable().getScoreBoard().get(p));
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getNickname();
    }

    //--------------------------CONSTRUCTOR--------------------------

    public Player (String nickname) {
        this.nickname = nickname;
    }

    //--------------------------GETTERS--------------------------

    public String getNickname() {
        return nickname;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public UNO getUNO() {
        return UNO;
    }

    public Table getTable() {
        return table;
    }


    //--------------------------SETTERS--------------------------

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setUNO(UNO UNO) {
        this.UNO = UNO;
    }

    public void setTable(Table table) {
        this.table = table;
    }

}
