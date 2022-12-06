package model;

public class Card {
    private Color color;
    private Value value;
    public enum Color {
        BLUE, GREEN, YELLOW, RED, WILD
    }
    public enum Value {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, DRAW_TWO, SKIP, CHANGE_DIRECTION, DRAW_FOUR, PICK_COLOR
    }
    public Card (Card.Color color, Card.Value value) {
        this.color = color;
        this.value = value;
    }

    //--------------------------GETTERS--------------------------
    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

}
