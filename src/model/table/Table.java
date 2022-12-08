package model.table;

import model.card.Card;
import model.deck.Deck;
import model.player.factory.Player;
import model.table.gameModes.factory.PlayingMode;

import java.util.ArrayList;

public class Table {
    private ArrayList<Player> players;
    private ArrayList<Player> winners;
    private Deck deck;
    private int currentTurnIndex;
    private PlayingMode playingMode;
    private Card currentCard;

    public Table(ArrayList<Player> players, PlayingMode playingMode) {
        this.players = players;
        this.playingMode = playingMode;
        this.deck = new Deck();
        this.playingMode.distributeHands(this.players, this.deck);
        this.currentCard = this.deck.getPlayingCards().get(0);
        this.deck.getPlayingCards().remove(0);
        this.deck.getUsedCards().add(this.currentCard);

    }
    //--------------------------METHODS--------------------------
    public void reversePlayers() {
        ArrayList<Player> tempArr = new ArrayList<>();
        for (int i=currentTurnIndex; i>=0;i--) {
            tempArr.add(players.get(i));
        }
        for (int i=players.size()-1;i>=currentTurnIndex;i--) {
            tempArr.add(players.get(i));
        }
        players = tempArr;
    }

    public void nextTurn() {
        if (currentTurnIndex<players.size()-1) {
            currentTurnIndex++;
        }
        else {
            currentTurnIndex = 0;
        }
    }

    public void skip() {
        nextTurn();
    }

    //--------------------------GETTERS--------------------------

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
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
}