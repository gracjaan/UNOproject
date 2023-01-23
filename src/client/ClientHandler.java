package client;

import client.contract.ClientProtocol;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import server.contract.ServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements ClientProtocol, Runnable {
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    public ClientHandler(Socket connection) throws IOException {
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
        Scanner scannner = new Scanner(System.in);
        String messageOut = scannner.nextLine();
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
     * This method informs the client that the handshake was accepted (AH).
     */
    @Override
    public void handleAcceptHandshake() {
        String messageIn = null;
        try {
            messageIn = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!messageIn.equals("key")) {
            out.println(ServerProtocol.Errors.E001);
            System.out.println(ServerProtocol.Errors.E001);
        } else {
            out.println("AH");
            out.flush();
            System.out.println("Connection successful.");
        }

    }

    /**
     * This method handles the message being sent by the server regarding informing the client that they are the admin (IAD).
     */
    @Override
    public void handleInformAdmin() {
        // create computer players, start game etc...
    }

    /**
     * This method handles the message being sent by the broadcast player joined (BPJ).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastPlayerJoined(String playerName) {
    }

    /**
     * This method handles the message being sent by the game started (GST).
     *
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    @Override
    public void handleGameStarted(String gameMode) {

    }

    /**
     * This method handles the message being sent by the round started (RST).
     */
    @Override
    public void handleRoundStarted() {

    }

    /**
     * This method handles the message being sent by the broadcast game information method (BGI).
     *
     * @param topCard     of type {@code String} representing the top card on the pile visible to players
     * @param playerHand  of type {@code String} representing the corresponding playre's hand
     * @param playersList of type {@code String} representing the list of players of the game sorted by the order of turn
     * @param isYourTurn  of type {@code String} indicates if it is the player’s turn
     */
    @Override
    public void handleBroadcastGameInformation(String topCard, String playerHand, String playersList, String isYourTurn) {

    }

    /**
     * This method handles the message being sent by the broadcast card played (BCP).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     * @param playedCard of type {@code String} representing the card played
     */
    @Override
    public void handleBroadcastCardPlayed(String playerName, String playedCard) {

    }

    /**
     * This method handles the message being set by the broadcast drew card (BDC).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastDrewCard(String playerName) {

    }

    /**
     * This method handles the message being sent by the broadcast turn skipped(BTS).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastTurnSkipped(String playerName) {

    }

    /**
     * This method handles the message being sent by the broadcast reverse (BRS).
     *
     * @param direction of type {@code String} representing the direction of the game
     */
    @Override
    public void handleBroadcastReverse(String direction) {

    }

    /**
     * This method handles the message being sent by the broadcast left game (BLG).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastLeftGame(String playerName) {

    }

    /**
     * This method handles the message being sent by the server, reminding the client to play (RP).
     *
     * @param timeLeft of type {@code String} representing the time left to play
     */
    @Override
    public void handleRemindPlay(String timeLeft) {

    }

    /**
     * This method handles the message being sent by the round ended (RE).
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the round
     */
    @Override
    public void handleRoundEnded(String playerName) {

    }

    /**
     * This method handles the message being sent by the game ended (GE).
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    @Override
    public void handleGameEnded(String playerName) {

    }

    /**
     * This method handles the message being sent by send error code (E***).
     *
     * @param errorCode of type {@code String} containing the error code
     */
    @Override
    public void handleSendErrorCode(String errorCode) {

    }

    /**
     * This method handles the message being sent by the broadcast list of lobbies (LOL).
     *
     * @param lobbiesList of type String, representing the list of existing lobbies.
     */
    @Override
    public void handleBroadcastListOfLobbies(String lobbiesList) {

    }

    /**
     * This method handles the message being sent by the broadcast created lobby (BCL).
     *
     * @param lobbyName of type {@code String} representing the unique name of the lobby
     */
    @Override
    public void handleBroadcastCreatedLobby(String lobbyName) {

    }

    /**
     * This method handles the message being sent by the server about a player joining the lobby (BJL).
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    @Override
    public void handleBroadcastPlayerJoinedLobby(String playerName) {

    }

    /**
     * This method handles the message being sent by the broadcast message (BM).
     *
     * @param message of type String, representing the chat message.
     */
    @Override
    public void handleBroadcastMessage(String message) {

    }

    /**
     * This method handles the message being sent by the server after a player says UNO (BUNO).
     */
    @Override
    public void handleBroadcastSayUNO() {

    }

    /**
     * This method creates the appropriate tag and message corresponding to the make handshake (MH)..
     * The method initializes the handshake of the client and the server with the parameters provided.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param playerName of type {@code String} representing the unique name of the player
     * @param playerType of type {@code String} representing computer_player ot human_player
     */
    @Override
    public void doMakeHandshake(String playerName, String playerType) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to the add computer player (ACP).
     * The method adds a computer player to the created game with the provided name and strategy
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param playerName of type {@code String} representing the name of the computer player
     * @param strategy   of type {@code String} representing the strategy for the computer player
     */
    @Override
    public void doAddComputerPlayer(String playerName, String strategy) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to the start game (SG).
     * The method initializes the game
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    @Override
    public void doStartGame(String gameMode) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a card being played (PC).
     * The method is being used when it is the client's turn, and he needs to play a card. The chosen card is passed as a parameter to the method.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param card of type {@code String} representing the card that the client wants to play
     */
    @Override
    public void doPlayCard(String card) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a card being drawn (DC).
     * The method is being used when it is the client's turn, and he wants to draw a card.
     * Once the data packet is produced, the sender() method is invoked.
     */
    @Override
    public void doDrawCard() {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a client leaving the game (LG).
     * The method is being used when the client wants to leave the game.
     * Once the data packet is produced, the sender() method is invoked.
     */
    @Override
    public void doLeaveGame() {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a client creating a lobby (CL).
     * The method is being used when the client wants to create a lobby.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void doCreateLobby(String lobbyName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a client joining a lobby (JL).
     * The method is being used when the client wants to join a lobby.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void doJoinLobby(String lobbyName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a client sending a message in the chat (SM).
     * The method is being used when the client wants to send a message in the chat.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param message of type String, representing the message.
     */
    @Override
    public void doSendMessage(String message) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a client saying UNO to avoid punishment(UNO).
     * The method is being used when the client wants to say UNO.
     * Once the data packet is produced, the sender() method is invoked.
     */
    @Override
    public void doSayUno() {

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

    }
}
