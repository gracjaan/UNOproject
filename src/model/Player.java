package model;

public class Player {
    private String nickname;
    private Hand hand;

    //--------------------------GETTERS--------------------------

    public String getNickname() {
        return nickname;
    }

    public Hand getHand() {
        return hand;
    }

    //--------------------------SETTERS--------------------------

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
