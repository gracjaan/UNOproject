package model.player.factory;

import controller.UNO;
import model.card.Card;
import model.player.contract.PlayerActions;
import model.table.Table;

import java.util.ArrayList;

public abstract class Player implements PlayerActions {
    private String nickname;
    private ArrayList<Card> hand;
    private UNO UNO;
    private Table table;
    public Player (String nickname, Table table) {
        this.nickname = nickname;
        this.table = table;
    }

    //--------------------------GETTERS--------------------------

    public String getNickname() {
        return nickname;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public UNO getUNO() {
        return UNO;
    }

    public Table getTable() {
        return table;
    }
    //--------------------------SETTERS--------------------------

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setUNO(UNO UNO) {
        this.UNO = UNO;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
