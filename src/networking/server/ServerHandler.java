package networking.server;

import model.card.Card;
import model.player.ComputerPlayer;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import networking.Lobby;
import networking.server.contract.ServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ServerHandler implements ServerProtocol, Runnable{
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;
    private Player correspondingPlayer;

    // create method to send mesg to all players.
    public ServerHandler(Socket connection, Server server) throws IOException {
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        this.server = server;
    }

    // seperate commands and call appropriate functions

    private void seperateAndCall(String input) {
        //networking.server.getUno().getTable().getCurrentPlayer();
        String[] splitted = input.split("[|]");
        System.out.println(Arrays.toString(splitted));
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
            case "CL":
                handleCreateLobby(splitted[1]);
                break;
            case "JL":
                handleJoinLobby(splitted[1]);
                break;
            case "CC":
                handleColorChoice(splitted[1]);
                break;
            case "RC":
                handleRetainCard(splitted[1]);
                break;
            case "UNO":
                handleSayUno();
                break;
            default:
                sendMessage(Errors.E001.getMessage());
                System.out.println(input);
                break;
        }
        }catch (IndexOutOfBoundsException e) {
            sendMessage(Errors.E001.getMessage());
        }
    }


    public void doHandshake() throws IOException {
        //send HS to networking.client
        out.println("Tocjan");
        out.flush();
        String messageIn = in.readLine();
        if (!messageIn.equals("Tocjan")) {
            System.out.println("Wrong networking.client connected");
        }
        System.out.println("Connection successful.");
    }
    public void sendMessage(String message) {
        System.out.println("SEND to "+ this.correspondingPlayer.getNickname() + ": " + message);
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
            if (messageIn==null) {
                handleLeaveGame();
                doHandleClientDisconnected();
                closeConnection();
            }

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
     * This method is called when a connection is first made, and the networking.client performs a "potentially valid" handshake.
     * <p>
     * The method assesses whether the networking.client has performed a valid handshake, and if this is the case, the method returns an
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
        for (Player p: server.getPlayers()) {
            if (p.getNickname().equals(playerName)) {
                sendMessage("ERR|E002");
            }
        }
        // duplicate player names? as validation for playerName
        if (playerType.equals("human_player")) {
            Player p = new NetworkPlayer(playerName, this);
            server.getPlayers().add(p);
            this.correspondingPlayer = p;
        } else if (playerType.equals("computer_player")){
            // make a networked computer player! --> especially regarding the tournament.
            Player p = new ComputerPlayer(playerName);
            server.getPlayers().add(p);
            this.correspondingPlayer = p;
        }else {
            sendMessage("ERR|E003");
            System.out.println(Errors.E003.getMessage());
        }
        out.println("AH");
        out.flush();
        System.out.println(playerName + " connected successfully.");
        // todo should be handled differently --> lobbies
        if (server.getHandlers().size()==3) {
            doInformAdmin();
        }
        }


    /**
     * This method handles the creation of a computerPlayer as requested by the networking.client (admin) (ACP).
     * It relates heavily with the game-logic.
     *
     * @param playerName of type {@code String} representing the name of the computer player
     * @param strategy   of type {@code String} representing the strategy for the computer player
     */

    // in order for this to be called with the according parameters --> caller Method or sth.
    @Override
    public void handleAddComputerPlayer(String playerName, String strategy) {
        Player c = new ComputerPlayer(playerName);
        server.getPlayers().add(c);
    }

    /**
     * This method handles the command from the networking.client (admin) to start the game (SG).
     * It relates heavily with the game-logic.
     *
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    @Override
    public void handleStartGame(String gameMode) {
        // check if there are necessary changes in UNO.
        // todo restricted to players in the lobby
        doGameStarted(gameMode);
        server.getUno().setup(this.server.getPlayers());
        Thread myUno = new Thread(server.getUno());
        myUno.start();
        //networking.server.getUno().play();

    }

    /**
     * This method handles the response from a networking.client regarding the card that they chose to play (PC).
     * It relates heavily with the game-logic.
     *
     * @param card of type {@code String} representing the card that the networking.client wants to play
     */
    @Override
    public void handlePlayCard(String card) {
        NetworkPlayer p = (NetworkPlayer) this.server.getUno().getTable().getCurrentPlayer();
        p.translate(card);
        // use the player instance of the current turn and use it to place the card: translate from card to index -> give to uno
        // translate(String card) -> translate np. -> set up NP variable -> getter for that.
        //this.networking.server.getUno().getTable().getCurrentPlayer();
    }

    /**
     * This method handles the response from a networking.Client regarding the fact that they chose to draw a card (DC).
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
     * This method handles the command from the networking.client to leave the game (LG).
     */
    @Override
    public void handleLeaveGame() {
        // put the cards back into the deck.
        for (Card c: this.correspondingPlayer.getHand()) {
            this.server.getUno().getTable().getDeck().getPlayingCards().add(c);
        }
        Collections.shuffle(this.server.getUno().getTable().getDeck().getPlayingCards());
        // update all table variables --> do we have a players array somewhere else?
        if (this.server.getPlayers().size()>2) {
            // is there anything else we need to do?
            removePlayer(correspondingPlayer);
            this.server.getHandlers().remove(this);
            // stop this thread.
        }else if (this.server.getPlayers().size()==2) {
            // player who is left won the game, modifications in gameOver allow to call it here (it also checks if players arr .size()==1) and handles informing other clients
            removePlayer(correspondingPlayer);
            this.server.getUno().gameOver();
            this.server.getHandlers().remove(this);
            // stop this thread.
        }
        else {
            System.exit(0);
        }
    }

    public void removePlayer(Player p) {
        this.server.getPlayers().remove(p);
        this.server.getUno().getPlayers().remove(p);
        this.server.getUno().getTable().getPlayers().remove(p);
    }

    /**
     * This method handles the networking.client-side request for the creation of a lobby, and responds in an appropriate manner (CL).
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void handleCreateLobby(String lobbyName) {
        // should be global, a list of current lobbies that should be stored in the server.
        Lobby lobby = new Lobby(lobbyName);
        lobby.addPlayer(correspondingPlayer);
        this.server.addLobby(lobby);
        doInformAdmin();
    }

    /**
     * This method handles the networking.client-side request for joining a lobby, and responds in an appropriate manner (JL).
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void handleJoinLobby(String lobbyName) {
        // the corresponding player is the player instance that is connected to this serverhandler
        this.server.getLobby(lobbyName).addPlayer(correspondingPlayer);
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
     * The method processes the networking.client saying Uno, which then needs to be processed (UNO).
     */
    @Override
    public void handleSayUno() {
        if (correspondingPlayer instanceof NetworkPlayer) {
            ((NetworkPlayer)correspondingPlayer).setAddUno(true);
        }
        sendMessageToAll("BUNO|"+correspondingPlayer.getNickname());
    }

    /**
     * This method is intended to handle the client's choice whether to play the drawn card or not.
     *
     * @param choice of type String, representing false if they do not want to play, true if they want to play it.
     */
    @Override
    public void handleRetainCard(String choice) {
        if (choice.equals("true")){
            if (correspondingPlayer instanceof NetworkPlayer){
                // automatically last card. --> we still receive BGI for input.
                // Broadcast the card that is played?
                //String ca = np.getHand().get(np.getHand().size()-1).getColor() + " " + np.getHand().get(np.getHand().size()-1).getValue().toString();
                ((NetworkPlayer) correspondingPlayer).translate("proceed");
            }
        }
        else {
            if (correspondingPlayer instanceof NetworkPlayer) {
                ((NetworkPlayer) correspondingPlayer).translate("skip");
            }
        }
    }

    /**
     * This method is intended to handle the client's choice in changing the color (ONLY IN THE INSTANCE THAT THE FIRST CARD DRAWN FROM THE DECK TO THE PLAYING SPACE IS A WILD).
     *
     * @param color of type String, representing the color.
     */
    @Override
    public synchronized void handleColorChoice(String color) {
        NetworkPlayer p = (NetworkPlayer) correspondingPlayer;
        p.pickColor(color);
        doBroadcastColourChange(color);
        notifyAll();
    }

    /**
     * This method is intended to handle the client's choice for the player they want to swap hands with.
     *
     * @param playerName of type String, representing the name of the player.
     * @param card       of type String, representing the SEVEN that was played.
     */
    @Override
    public void handleMakeChoiceSeven(String playerName, String card) {

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
        // when is someone informed that he is an admin? a message sent to all should probs be done in networking.server.
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
     * @param playerHand  of type String, representing this particular network networking.client player's hand.
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
     * This method creates the appropriate tag and message corresponding to a player drawing a card that can be played directly (DPC).
     * Once the data packet is produced, it is sent.
     *
     * @param card of type String, representing the card that the player drew.
     */
    @Override
    public void doDrewPlayableCard(String card) {
        String msg = "DPC|" + card;
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to a player playing a wild card (AC).
     * Once the data packet is produced, it is sent.
     */
    @Override
    public synchronized void doAskColour() {
        String msg = "AC";
        sendMessage(msg);
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method creates the appropriate tag and message corresponding to a player changing the colour that is current played (BCC).
     * Once the data packet is produced, it is sent.
     *
     * @param colour of type String, representing the colour that the player chose to switch to.
     */
    @Override
    public void doBroadcastColourChange(String colour) {
        String msg = "BCC|" + colour;
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to the server sending messages relating to the game (BGM).
     * Once the data packet is produced, it is sent.
     *
     * @param message of type String, representing the message that needs to be sent.
     */
    @Override
    public void doBroadcastGameMessage(String... message) {

    }

    /**
     * This method exists so that the networking.server can implement a mechanism to handle an inactive player (RP can be used).
     */
    @Override
    public void doHandleInactivePlayer() {

    }

    /**
     * This method exists so that the networking.server can handle a networking.client that disconnected (by terminating the socket and adjusting the game).
     */
    @Override
    public void doHandleClientDisconnected() {
        String msg = "ERR|E007:" ;
        // to all?
        sendMessage(msg);
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
        String msg = "LOL|";
        for (Lobby l: this.server.getLobbies()) {
            msg+=l.getName()+":"+l.getPlayers().size()+";";
        }
        sendMessageToAll(msg);
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
        String msg = "BCL|"+lobbyName;
        sendMessageToAll(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to player joining a lobby (BJL).
     * Once the data packet is produced, it is sent.
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    @Override
    public void doBroadcastPlayerJoinedLobby(String playerName) {
        String msg = "BJL|" + playerName;
        sendMessageToAll(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to player sending a message (BM).
     * The method broadcasts a message sent my a networking.client to the other clients.
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
