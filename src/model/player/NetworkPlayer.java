package model.player;

import model.card.Card;
import model.player.factory.Player;
import server.ServerHandler;

public class NetworkPlayer extends Player {
    private ServerHandler sh;
    private String translation;

    public NetworkPlayer(String nickname, ServerHandler serverHandler) {
        super(nickname);
        this.sh = serverHandler;
    }
    public void translate(String card) {
        int ind = 0;
        String[] spl = card.split(" ");
        for (Card c: super.getHand()) {
            if (spl[0].equals(c.getColor().toString())&&spl[1].equals(c.getValue().toString())) {
                translation = Integer.toString(ind);
            }
            ind++;
        }
    }

    @Override
    public void pickColor() {
        // where does the server command come from? protocol needs update.
        // pick color according to input from the network
    }

    public String getTranslation() {
        return this.translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
