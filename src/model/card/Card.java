package model.card;

public class Card {
    private Color color;
    private Value value;

    //--------------------------ENUMS--------------------------
    public enum Color {
        BLUE, GREEN, YELLOW, RED, WILD
    }
    public enum Value {
        DRAW_TWO, SKIP, CHANGE_DIRECTION, DRAW_FOUR, PICK_COLOR, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
    }

    //--------------------------CONSTRUCTOR--------------------------
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

    //--------------------------toString--------------------------
    public String toString(){
        return "[" + this.color + " "  + this.value + "]";
    }

}
