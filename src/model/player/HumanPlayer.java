package model.player;

import model.card.Card;
import model.player.factory.Player;

import java.util.Scanner;

public class HumanPlayer extends Player {
    //--------------------------CONSTRUCTOR--------------------------

    public HumanPlayer(String nickname) {
        super(nickname);
    }

    //--------------------------METHODS--------------------------

    /**
     * @param card receives card to be played
     * Removes card from players hand, sets it to currentCard and adds it to usedCards
     * Calls performWildCardAction to do specific task
     * Checks if player is a winner
     * */
    @Override
    public void playCard(Card card) {
            super.getTable().setDrawFourPlayable(true);
            super.getHand().remove(card);
            this.isWinner();
            super.getTable().setCurrentCard(card);
            super.getTable().getDeck().getUsedCards().add(card);
            Player nextPlayer;
            if (super.getTable().getCurrentTurnIndex()<super.getTable().getPlayers().size()-1) {
                nextPlayer = super.getTable().getPlayers().get(super.getTable().getCurrentTurnIndex()+1);
            }
            else {
                nextPlayer = super.getTable().getPlayers().get(0);
            }
            super.getTable().getPlayingMode().performWildCardAction(card, this, nextPlayer);
        }

    /**
     * @param amount receives amount of cards to draw
     * @require draw is possible to be performed
     * adds top card of playing cards to player and removes it form playing cards
     * */
//    @Override
//    public void draw(int amount) {
//        if(checkDrawPossibility(amount)){
//            for (int i=0; i<amount;i++) {
//                super.getHand().add(super.getTable().getDeck().getPlayingCards().get(0));
//                super.getTable().getDeck().getPlayingCards().remove(0);
//            }
//        }
//    }

    /**
     * @param amount amount of cards to possibly draw
     * @ensures size of playing cards is bigger than amount of cards to draw
     * @return true if size of playing cards is bigger than amount of cards to draw, and false otherwise
     * */
//    @Override
//    public boolean checkDrawPossibility(int amount){
//        if(amount > super.getTable().getDeck().getPlayingCards().size()){
//            super.getTable().getDeck().reShuffle();
//        }
//
//        if(super.getTable().getDeck().getPlayingCards().size() >= amount){
//            return true;
//        }
//        else{
//            System.out.println("Hmmmm....seems like you love drawing cards. Unfortunately, there are only 108 of them in deck and you all have it in hands");
//            return false;
//        }
//    }

    /**
     * Sets indicated color to value given in input
     * */
    @Override
    public void pickColor() {
        System.out.println(">> Please pick a color!");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        switch (input) {
            case "blue":
                super.getTable().setIndicatedColor(Card.Color.BLUE);
                break;
            case "red":
                super.getTable().setIndicatedColor(Card.Color.RED);
                break;
            case "green":
                super.getTable().setIndicatedColor(Card.Color.GREEN);
                break;
            case "yellow":
                super.getTable().setIndicatedColor(Card.Color.YELLOW);
                break;
            default:
                System.out.println(">> Invalid input: please try to type one of valid colors in small letters");
                pickColor();
        }
    }

//    /**
//     * @return true if player is a winner and false otherwise
//     * Adds player to scoreboard
//     * */
//    @Override
//    public boolean isWinner() {
//        if (super.getHand().size()==0) {
//            System.out.println("Player " + this.getNickname() + " won this round.");
//            super.getTable().calculateScores(this);
//            super.getTable().getPlayers().remove(this);
//            // end the game!
//            for (Player p: this.getTable().getScoreBoard().keySet()) {
//                System.out.println(p.getNickname() + " : " + this.getTable().getScoreBoard().get(p));
//            }
//            return true;
//        }
//        return false;
//    }

}
