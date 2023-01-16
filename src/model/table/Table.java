package model.table;

import model.card.Card;
import model.deck.Deck;
import model.player.factory.Player;
import model.table.gameModes.factory.PlayingMode;

import java.util.ArrayList;

public class Table {
    private ArrayList<Player> players;
    private ArrayList<Player> scoreBoard;
    private Deck deck;
    private int currentTurnIndex;
    private PlayingMode playingMode;
    private Card currentCard;
    private Card.Color indicatedColor;
    private boolean drawFourPlayable;

    public Table(ArrayList<Player> players, PlayingMode playingMode) {
        this.players = players;
        this.playingMode = playingMode;
        this.deck = new Deck();
        this.currentCard = this.deck.getPlayingCards().get(0);
        this.deck.getPlayingCards().remove(0);
        this.deck.getUsedCards().add(this.currentCard);
        this.indicatedColor = null;
        this.playingMode.distributeHands(this.players, this.deck);
        this.drawFourPlayable = true;
        adjustToFirstCard();
    }


    //--------------------------METHODS--------------------------

    /**
     * Reverses order of a players if there is more than 2 players, otherwise acts like a skip
     * */
    public void reversePlayers() {
        if (this.players.size()==2) {
            nextTurn();
        }
        else {
            ArrayList<Player> tempArr = new ArrayList<>();
            tempArr.add(players.get(currentTurnIndex));
            for (int i = currentTurnIndex - 1; i >= 0; i--) {
                tempArr.add(players.get(i));
            }
            for (int i = players.size() - 1; i > currentTurnIndex; i--) {
                tempArr.add(players.get(i));
            }
            players = tempArr;
            currentTurnIndex = 0;
        }
    }

    /**
     * Determines next turn
     * */
    public void nextTurn() {
        if (currentTurnIndex<players.size()-1) {
            currentTurnIndex++;
        }
        else {
            currentTurnIndex = 0;
        }
        resetIndicatedColor();
    }

    /**
     * @require indicvatedcolor!= null and currentCard.getValue()!=Card.Value.PICK_COLOR && currentCard.getValue()!= Card.Value.DRAW_FOUR
     * Sets indicatedcolor to null
     * */
    public void resetIndicatedColor(){
        if ((currentCard.getValue()!=Card.Value.PICK_COLOR && currentCard.getValue()!= Card.Value.DRAW_FOUR)&&indicatedColor!=null) {
            this.indicatedColor = null;
        }
    }

    public void drawFourEligibility() {
        System.out.println(this.getDeck().getUsedCards().get(this.getDeck().getUsedCards().size()-2).getColor());
        for (Card card: this.getCurrentPlayer().getHand()) {
            if (card.getColor().equals(this.getDeck().getUsedCards().get(this.getDeck().getUsedCards().size()-2).getColor())){
                this.drawFourPlayable = false;
            }
        }
        System.out.println(this.isDrawFourPlayable());
    }

    public void skip() {
        nextTurn();
    }

    /**
     * performs wild card actions for the starting card according to uno rules .
     */
    private void adjustToFirstCard() {
        if  (this.getCurrentCard().getColor()==Card.Color.WILD) {
            // Dealer is last person in players, first player is at index 0.
            switch (this.getCurrentCard().getValue()) {
                // Draw two: same as performWildCardAction
                case DRAW_TWO:
                    players.get(0).draw(2);
                    break;
                // wild draw four: return to the deck and play another card.
                case DRAW_FOUR:
                    Card card = this.getCurrentCard();
                    this.setCurrentCard(this.deck.getPlayingCards().get(0));
                    this.deck.getPlayingCards().remove(0);
                    this.deck.getPlayingCards().add(card);
                    this.deck.getUsedCards().add(this.getCurrentCard());
                    // call again if new card is a wild card too
                    if (this.getCurrentCard().getColor() == Card.Color.WILD) {
                        adjustToFirstCard();
                    }
                    break;
                // skip: the player to the left of the dealer is skipped (so same)
                case SKIP:
                    this.skip();
                    break;
                // wild card: the person to the left of the dealer chooses the color he would like to start with
                case PICK_COLOR:
                    this.getCurrentPlayer().pickColor();
                    break;
                // reverse: dealer goes first players[0] and counterclockwise now.
                case CHANGE_DIRECTION:
                    this.reversePlayers();
                    //ASSUMING: Dealer is the last in the player queue
                    this.setCurrentTurnIndex(1);
                    break;
            }
        }
    }

    public Player hasWinner(){
        for (Player player: players){
            if (player.isWinner()){
                return player;
            }
        }
        return null;
    }


    //--------------------------GETTERS--------------------------

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }

    public Player getPreviousPlayer(){
        if (currentTurnIndex == 0){
            return this.players.get(players.size()-1);
        }
        return this.players.get(currentTurnIndex-1);
    }

    public Player getCurrentPlayer() {
        return this.players.get(currentTurnIndex);
    }

    public PlayingMode getPlayingMode() {
        return playingMode;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public Card.Color getIndicatedColor() {
        return indicatedColor;
    }

    public ArrayList<Player> getScoreBoard() {
        return this.scoreBoard;
    }

    public boolean isDrawFourPlayable() {
        return drawFourPlayable;
    }

    //--------------------------SETTERS--------------------------

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setPlayingMode(PlayingMode playingMode) {
        this.playingMode = playingMode;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void setIndicatedColor(Card.Color indicatedColor) {
        this.indicatedColor = indicatedColor;
    }

    public void setScoreBoard(ArrayList<Player> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void setDrawFourPlayable(boolean drawFourPlayable) {
        this.drawFourPlayable = drawFourPlayable;
    }

    public void setCurrentTurnIndex(int currentTurnIndex) {
        this.currentTurnIndex = currentTurnIndex;
    }
}
