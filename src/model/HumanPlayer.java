package model;

import model.factory.Player;

public class HumanPlayer extends Player {

    public HumanPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public void playCard(Card card) {
        // check for valid in gameLogic?
        this.getHand().remove(card);
    }

    @Override
    public void draw() {
        // Deck.draw
    }

    @Override
    public Card.Color pickColor() {
        // reference to TUI listen()
        return Card.Color.GREEN;
    }
}
