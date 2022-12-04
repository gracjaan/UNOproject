package model.factory;

import model.Card;
import model.contract.PlayerActions;

import java.util.ArrayList;

public abstract class Player implements PlayerActions {
    private String nickname;
    private ArrayList<Card> hand;
    public Player (String nickname) {
        this.nickname = nickname;
    }
    // is that needed? when do we initialize a player with a hand
    public Player(String nickname, ArrayList<Card> hand) {
        this.nickname = nickname;
        this.hand = hand;
    }


    //--------------------------GETTERS--------------------------

    public String getNickname() {
        return nickname;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    //--------------------------SETTERS--------------------------

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
}
