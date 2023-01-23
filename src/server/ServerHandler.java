package server;

import server.contract.ServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerHandler implements ServerProtocol, Runnable{
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    public ServerHandler(Socket connection) throws IOException {
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
    }
    public void doHandshake() throws IOException {
        //send HS to client
        out.println("Tocjan");
        out.flush();
        String messageIn = in.readLine();
        if (!messageIn.equals("Tocjan")) {
            throw new IOException("Wrong client connected.");
        }
        System.out.println("Connection successful.");
    }
    public void sendMessage() throws IOException {
        System.out.print("SEND: ");
        String messageOut = "hi wassup";
        out.println(messageOut);
        out.flush();
        if(out.checkError()) {
            System.out.println("An error occured during transmission.");
        }
    }
    public void receiveMessage() throws IOException {
        System.out.println("WAITING...");
        String messageIn = in.readLine();
        System.out.println("RECEIVED: " + messageIn);
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
        out.println("key");
        out.flush();
        String messageIn = null;
        try {
            messageIn = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!messageIn.equals("AH")) {
            out.println(Errors.E001);
            System.out.println(Errors.E001);
        } else {
            System.out.println("Connection successful.");
        }

    }

    /**
     * This method handles the creation of a computerPlayer as requested by the client (admin) (ACP).
     * It relates heavily with the game-logic.
     *
     * @param playerName of type {@code String} representing the name of the computer player
     * @param strategy   of type {@code String} representing the strategy for the computer player
     */
    @Override
    public void handleAddComputerPlayer(String playerName, String strategy) {

    }

    /**
     * This method handles the command from the client (admin) to start the game (SG).
     * It relates heavily with the game-logic.
     *
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    @Override
    public void handleStartGame(String gameMode) {

    }

    /**
     * This method handles the response from a client regarding the card that they chose to play (PC).
     * It relates heavily with the game-logic.
     *
     * @param card of type {@code String} representing the card that the client wants to play
     */
    @Override
    public void handlePlayCard(String card) {

    }

    /**
     * This method handles the response from a client regarding the fact that they chose to draw a card (DC).
     * It relates heavily with the game-logic.
     */
    @Override
    public void handleDrawCard() {

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

    }

    /**
     * This method creates the appropriate tag and message corresponding to the player being informed that they are the admin (IAD).
     * Once the data packet is produced, it is sent.
     */
    @Override
    public void doInformAdmin() {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a new player joining the lobby (BPJ).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName Refers to the name of the player that joined the lobby.
     */
    @Override
    public void doBroadcastPlayerJoined(String playerName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to the game commencing (GST).
     * Once the data packet is produced, it is sent.
     *
     * @param gameMode of type GameMode, referring to the gameMode of this particular game (normal, progressive, seven_o, jump)in).
     */
    @Override
    public void doGameStarted(String gameMode) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to the round beginning (RST).
     * Once the data packet is produced, it is sent.
     */
    @Override
    public void doRoundStarted() {

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

    }

    /**
     * This method creates the appropriate tag and message corresponding to a player drawing a card in the game (BDC).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type String, representing the name of the player who played the card.
     */
    @Override
    public void doBroadcastDrewCard(String playerName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a player's turn being skipped in the game (BTS).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type String, representing the name of the player who played the card.
     */
    @Override
    public void doBroadcastTurnSkipped(String playerName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to the direction of the game reversing (BRS).
     * Once the data packet is produced, it is sent.
     *
     * @param direction of type String, representing the direction (clockwise, anti-clockwise).
     */
    @Override
    public void doBroadcastReverse(String direction) {

    }

    /**
     * This method creates the appropriate tag and message to inform other players that a network player has left/forfeited (BLG).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type String, representing the name of the player who left.
     */
    @Override
    public void doBroadcastLeftGame(String playerName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to reminding a player that it is his turn(RP).
     * Once the data packet is produced, it is sent.
     *
     * @param timeLeft of type int, representing the seconds that the player has left to make a move.
     */
    @Override
    public void doRemindPlay(String timeLeft) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to when a round has ended (RE).
     * Once the data packet is produced, it is sent.
     *
     * @param winnerName of type String, representing the name of the player who won that round.
     */
    @Override
    public void doRoundEnded(String winnerName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a player winning the game (GE).
     * Once the data packet is produced, it is sent.
     *
     * @param winnerName of type String, representing the name of the player who won the game.
     */
    @Override
    public void doGameEnded(String winnerName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to an error code (E***).
     * Once the data packet is produced, it is sent.
     *
     * @param errorCode of type Errors, representing the error code.
     */
    @Override
    public void doSendErrorCode(Errors errorCode) {

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
        try {
            this.doHandshake();
            System.out.println("Connected.");
            while(true) {
                this.receiveMessage();
                this.sendMessage();
            }
        }catch(IOException e) {
            System.out.println("Sorry an error has occured, connection lost.");
            System.out.println("Error: " + e);
    }
}
}
