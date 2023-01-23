package model.player;

import model.player.factory.Player;
import server.ServerHandler;

public class NetworkPlayer extends Player {
    ServerHandler sh;
    public NetworkPlayer(String nickname, ServerHandler serverHandler) {
        super(nickname);
        this.sh = serverHandler;
    }
    public String translate() {

        return "translation";
    }
    @Override
    public void pickColor() {
        // pick color according to input from the network
    }
}
