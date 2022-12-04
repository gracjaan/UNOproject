package model;

import model.factory.Player;

public class ComputerPlayer extends Player {
    public ComputerPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public void playCard(Card card) {

    }

    @Override
    public void draw() {

    }

    @Override
    public Card.Color pickColor() {
        return Card.Color.YELLOW;
    }
}
