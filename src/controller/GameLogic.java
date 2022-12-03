package controller;

import model.Card;
import model.Deck;
import model.Game;
import model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class GameLogic extends Game{

    public Game startGame(Game game) {
        return game;
    }
    public void runGame() {}

    public Game endGame(Game game) {
        return game;
    }
    public Deck shuffleDeck (Deck deck){
        Collections.shuffle(deck.getCards());
        return deck;
    }
    public void distributeHands() {
        for (Player player: super.getPlayers()) {
            for (int i=0;i<7;i++) {

            }
        }
    }
    public void nextTurn() {}
    public boolean validMove() {
        return false;
    }
    public void evaluateNewCard() {}



}
