package model.table;

import model.card.Card;
import model.deck.Deck;
import model.player.factory.Player;
import model.table.gameModes.factory.PlayingMode;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {
    private ArrayList<Player> players;
    private HashMap<Player, Integer> scoreBoard;
    private Deck deck;
    private int currentTurnIndex;
    private PlayingMode playingMode;
    private Card currentCard;
    private Card.Color indicatedColor;
    private boolean drawFourPlayable;
    private boolean hasWinner;
    private boolean clockWise;

    public Table(ArrayList<Player> players, PlayingMode playingMode) {
        this.players = players;
        this.playingMode = playingMode;
        setUpRound(new Deck());
        this.scoreBoard = new HashMap<>();
        for(Player player: this.players) {
            scoreBoard.put(player, 0);
        }
        clockWise=true;
    }

    public void setUpRound(Deck deckArg){
        this.deck = deckArg;
        this.currentCard = this.deck.getPlayingCards().get(0);
        this.deck.getPlayingCards().remove(0);
        this.deck.getUsedCards().add(this.currentCard);
        this.indicatedColor = null;
        this.playingMode.distributeHands(this.players, this.deck);
        this.drawFourPlayable = true;
    }

    //--------------------------METHODS--------------------------

    /**
     * Reverses order of a players if there is more than 2 players, otherwise acts like a skip
     * */
    public void reversePlayers() {
        if (this.players.size()==2) {
            nextTurn();
        }
        clockWise = !clockWise;
//        else {
//            ArrayList<Player> tempArr = new ArrayList<>();
//            tempArr.add(players.get(currentTurnIndex));
//            for (int i = currentTurnIndex - 1; i >= 0; i--) {
//                tempArr.add(players.get(i));
//            }
//            for (int i = players.size() - 1; i > currentTurnIndex; i--) {
//                tempArr.add(players.get(i));
//            }
//            players = tempArr;
//            currentTurnIndex = 0;
//        }
    }

    /**
     * Determines next turn
     * */
    public void nextTurn() {
        if(clockWise) {
            if (currentTurnIndex < players.size() - 1) {
                currentTurnIndex++;
            } else {
                currentTurnIndex = 0;
            }
        }else {
            if (currentTurnIndex > 0) {
                currentTurnIndex--;
            }else {
                currentTurnIndex = players.size()-1;
            }
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
        for (Card card: this.getCurrentPlayer().getHand()) {
            if (card.getColor().equals(this.getDeck().getUsedCards().get(this.getDeck().getUsedCards().size()-2).getColor())){
                this.drawFourPlayable = false;
            }
        }
    }

    public void skip() {
        nextTurn();
    }


    /**
     * performs wild card actions for the starting card according to uno rules .
     */
    public void adjustToFirstCard() {
        // Dealer is last person in players, first player is at index 0.
        switch (this.getCurrentCard().getValue()) {
            case DRAW_TWO:
                System.out.println("Unfortunately you've been punished with two cards at very beginning");
                this.getCurrentPlayer().draw(2);
                break;
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
            case SKIP:
                System.out.println(">> Player " + getCurrentPlayer().getNickname() + "was skipped hahahha");
                this.skip();
                break;
            case PICK_COLOR:
                System.out.println(">> Player: " + getCurrentPlayer().getNickname());
                this.getCurrentPlayer().pickColor();
                break;
            case CHANGE_DIRECTION:
                System.out.println("First card was 'change direction' therefore direction was changed");
                this.reversePlayers();
                //ASSUMING: Dealer is the last in the player queue
                this.setCurrentTurnIndex(1);
                break;
        }

    }


    public void calculateScores(Player winner) {
        //should we rethink that a player is removed from players when he won because we play more rounds?
        int score = 0;
        for (Player player: players) {
            if (player.getHand().size() > 0) {
                for (Card card : player.getHand()) {
                    if (card.getValue() == Card.Value.DRAW_FOUR || card.getValue() == Card.Value.PICK_COLOR) {
                        score += 50;
                    } else if (card.getValue() == Card.Value.DRAW_TWO || card.getValue() == Card.Value.SKIP || card.getValue() == Card.Value.CHANGE_DIRECTION) {
                        score += 20;
                    }
                    else {
                        score += card.getValue().ordinal() - 5;
                    }
                }
            }
        }
        // add score to the scoreboard
        int i = this.scoreBoard.get(winner) + score;
        this.scoreBoard.put(winner, i);
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
       if (clockWise) {
           if (currentTurnIndex == 0) {
               return this.players.get(players.size() - 1);
           }
           return this.players.get(currentTurnIndex - 1);
       }else {
           if (currentTurnIndex == players.size()-1) {
               return this.players.get(0);
           } else {
               return this.players.get(currentTurnIndex+1);
           }
       }
    }

    public Player getNextPlayer() {
        if (clockWise) {
            if (currentTurnIndex == players.size()-1) {
                return this.players.get(0);
            }
            return this.players.get(currentTurnIndex + 1);
        }else {
            if (currentTurnIndex == 0) {
                return this.players.get(players.size()-1);
            } else {
                return this.players.get(currentTurnIndex-1);
            }
        }
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

    public HashMap<Player, Integer> getScoreBoard() {
        return this.scoreBoard;
    }

    public boolean isDrawFourPlayable() {
        return drawFourPlayable;
    }

    public boolean isHasWinner() {
        return hasWinner;
    }

    public boolean isClockWise() {
        return clockWise;
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

    public void setScoreBoard(HashMap<Player, Integer> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void setDrawFourPlayable(boolean drawFourPlayable) {
        this.drawFourPlayable = drawFourPlayable;
    }

    public void setCurrentTurnIndex(int currentTurnIndex) {
        this.currentTurnIndex = currentTurnIndex;
    }

    public void setHasWinner(boolean hasWinner) {
        this.hasWinner = hasWinner;
    }
}
