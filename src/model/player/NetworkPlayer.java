package model.player;

import model.card.Card;
import model.player.factory.Player;
import networking.server.ServerHandler;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkPlayer extends Player {
    private ServerHandler sh;
    private String translation ="";
    private final ReentrantLock LOCK = new ReentrantLock();
    private final Condition canGet = LOCK.newCondition();
    private final Condition canSet = LOCK.newCondition();
    private boolean condition = false;

    public NetworkPlayer(String nickname, ServerHandler serverHandler) {
        super(nickname);
        this.sh = serverHandler;
    }
    public synchronized void translate(String card) {
        if (card.equals("draw")) {
            this.setTranslation("draw");
        }
        int ind = 0;
        String[] spl = card.split(" ");
        for (Card c: super.getHand()) {
            if (spl[0].equals(c.getColor().toString())&&spl[1].equals(c.getValue().toString())) {
                this.setTranslation(Integer.toString(ind));
            }
            ind++;
        }
    }

    public void broadcastAfterTurn() {
        String topCard = super.getTable().getCurrentCard().getColor().toString() + " " + super.getTable().getCurrentCard().getValue().toString();
        String playerHand = "";
        for (Card c: super.getHand()) {
            playerHand+=c.getColor().toString() + " " + c.getValue().toString();
            playerHand+=";";
        }
        String playersList = "";
        boolean yourTurn = false;
        // is there an order to follow for the playersList:
        for (Player p: super.getTable().getPlayers()) {
            playersList+=p.getNickname()+":"+p.getHand().size()+":"+p.getTable().getScoreBoard().get(p.getNickname());
        }
        // is this called before or after we call nextTurn?
        if (super.getTable().getCurrentPlayer().getNickname().equals(this.getNickname())) {
            yourTurn = true;
        }
        sh.doBroadcastGameInformation(topCard, playerHand,playersList, String.valueOf(yourTurn));
    }

    @Override
    public void pickColor() {
        // where does the networking.server command come from? protocol needs update.
        // pick color according to input from the network
    }
    public void pickColor(String color) {
        switch (color) {
            case "BLUE":
                super.getTable().setIndicatedColor(Card.Color.BLUE);
                break;
            case "RED":
                super.getTable().setIndicatedColor(Card.Color.RED);
                break;
            case "GREEN":
                super.getTable().setIndicatedColor(Card.Color.GREEN);
                break;
            case "YELLOW":
                super.getTable().setIndicatedColor(Card.Color.YELLOW);
                break;
            default:
                super.getTable().setIndicatedColor(Card.Color.YELLOW);
        }
    }


    public String getTranslation() {
        //System.out.println(Thread.currentThread().getName());
        //System.out.println("I lock to get");
        LOCK.lock();
        try {
            while ((this.translation == null) || this.translation.isEmpty()) {
                //System.out.println("I wait for it not to be empty");
                canGet.await();
            }
            canSet.signal();
            LOCK.unlock();
            return this.translation;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void setTranslation(String translation) {
        //System.out.println("I lock to write");
        LOCK.lock();
        try {
            //System.out.println(this.translation);
            while (!(this.translation== null) && !this.translation.isEmpty()) {
                System.out.println("I wait for it to be empty");
                canSet.await();
            }
            this.translation = translation;
            //System.out.println(translation);

            canGet.signal();
            LOCK.unlock();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetTranslation() {
        this.translation = null;
    }

    public ServerHandler getSh() {
        return sh;
    }

    public void setSh(ServerHandler sh) {
        this.sh = sh;
    }
}
