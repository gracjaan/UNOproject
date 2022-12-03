package controller;

import model.Card;

import java.util.ArrayList;

public class ComputerLogic {
    public ArrayList<Card> getValidMoves (ArrayList<Card> hand, Card middleCard) {
        return hand;
    }
    public Card evaluateBestValidMove(ArrayList<Card> validMoves) {
        return validMoves.get(0);
    }
}
