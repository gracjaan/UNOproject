package server;

import controller.UNO;
import model.card.Card;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import server.contract.ServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerHandler implements ServerProtocol, Runnable{
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<Player> players;
    private Server server;

    // create method to send mesg to all players.
    public ServerHandler(Socket connection, Server server) throws IOException {
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        players = new ArrayList<>();
        this.server = server;
    }

    // seperate commands and call appropriate functions

    private void seperateAndCall(String input) {
        //server.getUno().getTable().getCurrentPlayer();
        String[] splitted = input.split("[|]");
        try {
        switch (splitted[0]) {
            case "MH":
                handleHandshake(splitted[1], splitted[2]);
                break;
            case "ACP":
                handleAddComputerPlayer(splitted[1], splitted[2]);
                break;
            case "SG":
                handleStartGame(splitted[1]);
                break;
            case "PC":
                handlePlayCard(splitted[1]);
                break;
            case "DC":
                handleDrawCard();
                break;
            case "LG":
                handleLeaveGame();
                break;
            default:
                sendMessage(Errors.E001.getMessage());
                break;
        }
        }catch (IndexOutOfBoundsException e) {
            sendMessage(Errors.E001.getMessage());
        }
    }


    public void doHandshake() throws IOException {
        //send HS to client
        out.println("Tocjan");
        out.flush();
        String messageIn = in.readLine();
        if (!messageIn.equals("Tocjan")) {
            System.out.println("Wrong client connected");
        }
        System.out.println("Connection successful.");
    }
    public void sendMessage(String message) {
        System.out.println("SEND: " + message);
        out.println(message);
        out.flush();
        if(out.checkError()) {
            System.out.println("An error occured during transmission.");
        }
    }
    public void sendMessageToAll(String message) {
        for (ServerHandler s: server.getHandlers()) {
            s.sendMessage(message);
        }
    }
    public void receiveMessage()  {
        System.out.println("WAITING...");
        String messageIn = "";
        try {
            messageIn = in.readLine();

        } catch (IOException e) {
            sendMessage(Errors.E001.getMessage());
        }
        System.out.println("RECEIVED: " + messageIn);
        seperateAndCall(messageIn);
    }
    public void closeConnection() throws IOException {
        out.println("Closing the Server.");
        out.flush();
        connection.close();
        System.out.println("Connection Closed.");
        System.exit(1);
    }

    /**
     * This method is called when a connection is first made, and the client performs a "potentially valid" handshake.
     * <p>
     * The method assesses whether the client has performed a valid handshake, and if this is the case, the method returns an
     * appropriate correspondence containing relevant information that the verified UnoClient needs to know in the form of a
     * welcome message (AH).
     * Once the data packet is produced, it is sent.
     * <p>
     * If the handshake is not valid, the method itself invokes the sendErrorCode() method to send the appropriate error code.
     *
     * @param playerName of type {@code String} representing the unique name of the player
     * @param playerType of type {@code String} representing computer_player ot human_player
     */
    @Override
    public void handleHandshake(String playerName, String playerType) {
        // duplicate player names? as validation for playerName
        if (playerType.equals("human_player")) {
            Player p = new NetworkPlayer(playerName, this);
            players.add(p);
        } else if (playerType.equals("computer_player")){
            Player p = new ComputerPlayer(playerName);
            players.add(p);
        }else {
            out.println(Errors.E001);
            System.out.println(Errors.E001);
        }
        out.println("AH");
        out.flush();
        System.out.println(playerName + " connected successfully.");
        if (players.size()==1) {
            doInformAdmin();
        }
        }


    /**
     * This method handles the creation of a computerPlayer as requested by the client (admin) (ACP).
     * It relates heavily with the game-logic.
     *
     * @param playerName of type {@code String} representing the name of the computer player
     * @param strategy   of type {@code String} representing the strategy for the computer player
     */

    // in order for this to be called with the according parameters --> caller Method or sth.
    @Override
    public void handleAddComputerPlayer(String playerName, String strategy) {
        Player c = new ComputerPlayer(playerName);
        players.add(c);
    }

    /**
     * This method handles the command from the client (admin) to start the game (SG).
     * It relates heavily with the game-logic.
     *
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    @Override
    public void handleStartGame(String gameMode) {
        // check if there are necessary changes in UNO.
        // BGI?
        doGameStarted(gameMode);
        server.getUno().setup(this.players);
        Thread myUno = new Thread(server.getUno());
        myUno.start();
        //server.getUno().play();

    }

    /**
     * This method handles the response from a client regarding the card that they chose to play (PC).
     * It relates heavily with the game-logic.
     *
     * @param card of type {@code String} representing the card that the client wants to play
     */
    @Override
    public void handlePlayCard(String card) {
        // use the player instance of the current turn and use it to place the card: translate from card to index -> give to uno
        // -->
        // translate(String card) -> translate np. -> set up NP variable -> getter for that.

        NetworkPlayer p = (NetworkPlayer) this.server.getUno().getTable().getCurrentPlayer();
        p.translate(card);
        //this.server.getUno().getTable().getCurrentPlayer();
    }

    /**
     * This method handles the response from a client regarding the fact that they chose to draw a card (DC).
     * It relates heavily with the game-logic.
     */
    @Override
    public void handleDrawCard() {
        // same --> input = draw
        NetworkPlayer p = (NetworkPlayer) this.server.getUno().getTable().getCurrentPlayer();
        p.translate("draw");
        doBroadcastDrewCard(p.getNickname());
    }

    /**
     * This method handles the command from the client to leave the game (LG).
     */
    @Override
    public void handleLeaveGame() {

    }

    /**
     * This method handles the client-side request for the creation of a lobby, and responds in an appropriate manner (CL).
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void handleCreateLobby(String lobbyName) {

    }

    /**
     * This method handles the client-side request for joining a lobby, and responds in an appropriate manner (JL).
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void handleJoinLobby(String lobbyName) {

    }

    /**
     * The method processes the message and forwards it to all other clients within the chat (SM).
     *
     * @param message of type String, representing the message.
     */
    @Override
    public void handleSendMessage(String message) {

    }

    /**
     * The method processes the client saying Uno, which then needs to be processed (UNO).
     */
    @Override
    public void handleSayUno() {
        // add uno to the input that is given to uno.
    }

    /**
     * This method creates the appropriate tag and message corresponding to the player being informed that they are the admin (IAD).
     * Once the data packet is produced, it is sent.
     */
    @Override
    public void doInformAdmin() {
        // assuming we are informing the first player that joined to become an admin (has to be instance of HP)
        String msg = "IAD";
        this.sendMessage(msg);
        // when is someone informed that he is an admin? a message sent to all should probs be done in server.
    }

    /**
     * This method creates the appropriate tag and message corresponding to a new player joining the lobby (BPJ).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName Refers to the name of the player that joined the lobby.
     */
    @Override
    public void doBroadcastPlayerJoined(String playerName) {
        String msg = "BPJ|" + playerName;
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to the game commencing (GST).
     * Once the data packet is produced, it is sent.
     *
     * @param gameMode of type GameMode, referring to the gameMode of this particular game (normal, progressive, seven_o, jump)in).
     */
    @Override
    public void doGameStarted(String gameMode) {
        String msg = "GST|" + gameMode;
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to the round beginning (RST).
     * Once the data packet is produced, it is sent.
     */
    @Override
    public void doRoundStarted() {
        String msg = "RST";
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to the game information being sent to clients (BGI).
     * Once the data packet is produced, it is sent.
     *
     * @param topCard     of type String, representing the card.
     * @param playerHand  of type String, representing this particular network client player's hand.
     * @param playersList of type {@code String} representing the list of players of the game sorted by the order of turn
     * @param isYourTurn  of type {@code String} indicates if it is the player’s turn
     */
    @Override
    public void doBroadcastGameInformation(String topCard, String playerHand, String playersList, String isYourTurn) {
        String msg = "BGI|";
        msg += topCard + "|";
        // playerHand should be the hand of the specific player dont call sendToAll but when calling this method do it while looping through handlers.
        msg += playerHand + "|";
        msg += playersList + "|";
        msg += isYourTurn;
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to a card being played in the game (BCP).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type String, representing the name of the player who played the card.
     * @param playedCard of type {@code String} representing the card played
     */
    @Override
    public void doBroadcastCardPlayed(String playerName, String playedCard) {
        String result = "BCP|" + playerName + "|" + playedCard;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to a player drawing a card in the game (BDC).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type String, representing the name of the player who played the card.
     */
    @Override
    public void doBroadcastDrewCard(String playerName) {
        String result = "BDC|" + playerName;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to a player's turn being skipped in the game (BTS).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type String, representing the name of the player who played the card.
     */
    @Override
    public void doBroadcastTurnSkipped(String playerName) {
        String result = "BTS|" + playerName;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to the direction of the game reversing (BRS).
     * Once the data packet is produced, it is sent.
     *
     * @param direction of type String, representing the direction (clockwise, anti-clockwise).
     */
    @Override
    public void doBroadcastReverse(String direction) {
        String result = "BRS|" + direction;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message to inform other players that a network player has left/forfeited (BLG).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type String, representing the name of the player who left.
     */
    @Override
    public void doBroadcastLeftGame(String playerName) {
        String result = "BLG|" + playerName;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to reminding a player that it is his turn(RP).
     * Once the data packet is produced, it is sent.
     *
     * @param timeLeft of type int, representing the seconds that the player has left to make a move.
     */
    @Override
    public void doRemindPlay(String timeLeft) {
        String result = "RP|" + timeLeft;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to when a round has ended (RE).
     * Once the data packet is produced, it is sent.
     *
     * @param winnerName of type String, representing the name of the player who won that round.
     */
    @Override
    public void doRoundEnded(String winnerName) {
        String result = "RE|" + winnerName;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to a player winning the game (GE).
     * Once the data packet is produced, it is sent.
     *
     * @param winnerName of type String, representing the name of the player who won the game.
     */
    @Override
    public void doGameEnded(String winnerName) {
        String result = "GE|" + winnerName;
        sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to an error code (E***).
     * Once the data packet is produced, it is sent.
     *
     * @param errorCode of type Errors, representing the error code.
     */
    @Override
    public void doSendErrorCode(Errors errorCode) {
        String result = "ERR|" + errorCode;
        sendMessage(result);
    }

    /**
     * This method exists so that the server can implement a mechanism to handle an inactive player (RP can be used).
     */
    @Override
    public void doHandleInactivePlayer() {

    }

    /**
     * This method exists so that the server can handle a client that disconnected (by terminating the socket and adjusting the game).
     */
    @Override
    public void doHandleClientDisconnected() {

    }

    /**
     * This method creates the appropriate tag and message corresponding to listing the available lobbies (LOL).
     * The method lists the available lobbies, that clients can join.
     * Once the data packet is produced, it is sent.
     *
     * @param lobbiesList
     */
    @Override
    public void doBroadcastListOfLobbies(String lobbiesList) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a user creating a lobby (BCL).
     * The method returns a message if the creation of the lobby was successful.
     * Once the data packet is produced, it is sent.
     *
     * @param lobbyName of type {@code String} representing the unique name of the lobby
     */
    @Override
    public void doBroadcastCreatedLobby(String lobbyName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to player joining a lobby (BJL).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    @Override
    public void doBroadcastPlayerJoinedLobby(String playerName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to player sending a message (BM).
     * The method broadcasts a message sent my a client to the other clients.
     * Once the data packet is produced, it is sent.
     *
     * @param message of type String, representing the chat message.
     */
    @Override
    public void doBroadcastMessage(String message) {

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
            System.out.println("Connected.");
            while(true) {
                this.receiveMessage();
            }
//        }catch(IOException e) {
//            System.out.println("Sorry an error has occured, connection lost.");
//            System.out.println("Error: " + e);
//    }
}
}
