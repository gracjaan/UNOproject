package networking.server;

import controller.UNO;
import model.card.Card;
import model.player.ComputerPlayer;
import model.player.NetworkPlayer;
import model.player.factory.Player;
import model.table.gameModes.Normal;
import model.table.gameModes.Progressive;
import model.table.gameModes.SevenZero;
import model.table.gameModes.factory.PlayingMode;
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
    private Lobby lobby;
    private boolean flag;

    // create method to send mesg to all players.
    public ServerHandler(Socket connection, Server server) throws IOException {
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        this.server = server;
        this.flag = true;
    }

    // seperate commands and call appropriate functions
    // todo validation if the game is currently running. just make a boolean that is true once start is called.
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
            case "LOL":
                doBroadcastListOfLobbies("");
                break;
            case "MC7":
                handleMakeChoiceSeven(splitted[1], "");
                break;
            case "SM":
                handleSendMessage(splitted[1]);
                break;
            default:
                sendMessage(Errors.E001.getMessage()+Arrays.toString(splitted));
                System.out.println(input);
                break;
        }
        }catch (IndexOutOfBoundsException e) {
            sendMessage(Errors.E001.getMessage());
        }
    }


//    public void doHandshake() throws IOException {
//        //send HS to networking.client
//        out.println("Tocjan");
//        out.flush();
//        String messageIn = in.readLine();
//        if (!messageIn.equals("Tocjan")) {
//            System.out.println("Wrong networking.client connected");
//        }
//        System.out.println("Connection successful.");
//    }
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
    public void sendMessageToLobby(String message) {
        for (Player p: this.lobby.getPlayers()) {
            ((NetworkPlayer)p).getSh().sendMessage(message);
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
                return;
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
        // todo ENTIRE METHOD: just settting, not reading correspondingplayer

        // we do not allow the same names at all on the entire servers not just lobbies.
        for (Lobby l: server.getLobbies()) {
            for (Player p : l.getPlayers()) {
                if (p.getNickname().equals(playerName)) {
                    sendMessage("ERR|E002");
                    break;
                }
            }
        }

        if (playerType.equals("human_player")) {
            Player p = new NetworkPlayer(playerName, this);
            this.correspondingPlayer = p;
            // todo NetworkedCP
        } else if (playerType.equals("computer_player")){
            // make a networked computer player! --> especially regarding the tournament.
            Player p = new ComputerPlayer(playerName);
            this.correspondingPlayer = p;
        }else {
            sendMessage("ERR|E003");
            System.out.println(Errors.E003.getMessage());
        }
        out.println("AH");
        out.flush();
        System.out.println(playerName + " connected successfully.");
        // todo is now called in create Lobby
//        if (server.getPlayersInLobby(correspondingPlayer).size()==1) {
//            doInformAdmin();
//        }
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
        // todo he is just added to players -> where is players called and should he automatically be assigned to a lobby? (e.g main?)
        // todo should be networkedCP or not?
        Player c = new ComputerPlayer(playerName);
        server.getMainLobby().addPlayer(c);
    }

    /**
     * This method handles the command from the networking.client (admin) to start the game (SG).
     * It relates heavily with the game-logic.
     *
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    @Override
    public void handleStartGame(String gameMode) {
        PlayingMode playingMode;
        if (gameMode.equals("normal")) {
            playingMode = new Normal();
        }else if (gameMode.equals("progressive")) {
            playingMode = new Progressive();
        } else if (gameMode.equals("sevenZero")) {
            playingMode = new SevenZero();
        }
        else {
            doSendErrorCode(Errors.E006);
            return;
        }
        doGameStarted(gameMode);
        server.getUno(correspondingPlayer).setup(this.server.getPlayersInLobby(correspondingPlayer), playingMode);
        server.getCurrentGames().add(server.getUno(correspondingPlayer));
        Thread myUno = new Thread(server.getUno(correspondingPlayer));
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
        ((NetworkPlayer)correspondingPlayer).translate(card);
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
        ((NetworkPlayer)correspondingPlayer).translate("draw");
        doBroadcastDrewCard(correspondingPlayer.getNickname());
    }

    /**
     * This method handles the command from the networking.client to leave the game (LG).
     */
    @Override
    public void handleLeaveGame() {
        if (this.server.getLobbyIndex(correspondingPlayer)==-1) {
            System.out.println("The player has not yet joined a lobby.");
        }

        else if (this.lobby.isGameInProgress()) {
            // put the cards back into the deck.
            for (Card c : this.correspondingPlayer.getHand()) {
                this.server.getUno(correspondingPlayer).getTable().getDeck().getPlayingCards().add(c);
            }
            Collections.shuffle(this.server.getUno(correspondingPlayer).getTable().getDeck().getPlayingCards());
            if (this.lobby.getPlayers().size() >= 2) {
                doBroadcastLeftGame(correspondingPlayer.getNickname());
                if (correspondingPlayer.getNickname().equals(this.lobby.getGame().getTable().getCurrentPlayer().getNickname())) {
                    ((NetworkPlayer) correspondingPlayer).translate("skip");
                }
                removePlayer(correspondingPlayer);
                this.server.getHandlers().remove(this);
                // stop the current thread.
            } else {
                //System.exit(0);
            }
        } else {
            this.server.getLobby(correspondingPlayer).getPlayers().remove(correspondingPlayer);
            // stop the current thread

        }
        doBroadcastLeftGame(correspondingPlayer.getNickname());
    }

    public void removePlayer(Player p) {
        UNO u = this.lobby.getGame();
        u.getTable().getPlayers().remove(p);
        u.getPlayers().remove(p);
        lobby.getPlayers().remove(p);
    }

    /**
     * This method handles the networking.client-side request for the creation of a lobby, and responds in an appropriate manner (CL).
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void handleCreateLobby(String lobbyName) {
        Lobby lobby = new Lobby(lobbyName);
        lobby.addPlayer(correspondingPlayer);
        this.server.addLobby(lobby);
        this.lobby = lobby;
        doInformAdmin();
        doBroadcastCreatedLobby(lobbyName);
    }

    /**
     * This method handles the networking.client-side request for joining a lobby, and responds in an appropriate manner (JL).
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void handleJoinLobby(String lobbyName) {
        if (lobbyName.equals("main")&&this.server.getMainLobby().getPlayers().isEmpty()) {
            doInformAdmin();
        }
        // todo validation if lobbyName valid, send ERR otherwise.
        this.server.getLobby(lobbyName).addPlayer(correspondingPlayer);
        this.lobby = this.server.getLobby(lobbyName);
        doBroadcastPlayerJoinedLobby(correspondingPlayer.getNickname());
    }

    /**
     * The method processes the message and forwards it to all other clients within the chat (SM).
     *
     * @param message of type String, representing the message.
     */
    @Override
    public void handleSendMessage(String message) {
        sendMessageToLobby("BM|" + correspondingPlayer.getNickname() + ":" + message);
    }

    /**
     * The method processes the networking.client saying Uno, which then needs to be processed (UNO).
     */
    @Override
    public void handleSayUno() {
        if (correspondingPlayer instanceof NetworkPlayer) {
            ((NetworkPlayer)correspondingPlayer).setAddUno(true);
        }
        sendMessageToLobby("BUNO|"+correspondingPlayer.getNickname());
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
    public void handleColorChoice(String color) {
        NetworkPlayer p = (NetworkPlayer) correspondingPlayer;
        p.pickColor(color);
        doBroadcastColourChange(color);
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * This method is intended to handle the client's choice for the player they want to swap hands with.
     *
     * @param playerName of type String, representing the name of the player.
     * @param card       of type String, representing the SEVEN that was played.
     */
    @Override
    public void handleMakeChoiceSeven(String playerName, String card) {
        boolean flag = false;
        for (Player p: server.getPlayersInLobby(correspondingPlayer)) {
            if (p.getNickname().equals(playerName)) {
                correspondingPlayer.swapHands(p);
                flag = true;
                break;
            }
        }
        if (!flag) {
            doAskChoiceSeven();
        }
        synchronized (this) {
            notifyAll();
        }
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
        this.server.getLobby(correspondingPlayer).setGameInProgress(true);
        String msg = "GST|" + gameMode;
        sendMessageToLobby(msg);
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
        sendMessageToLobby(result);
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
        this.server.getLobby(correspondingPlayer).setGameInProgress(false);
        String result = "GE|" + winnerName;
        sendMessage(result);
        this.server.getCurrentGames().remove(this.server.getLobbies().get(this.server.getLobbyIndex(correspondingPlayer)).getGame());
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
    public void doAskColour() {
        String msg = "AC";
        sendMessage(msg);
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
        String msg = "BGM|" + Arrays.toString(message);
        sendMessage(msg);
    }

    /**
     * This method exists so that the networking.server can implement a mechanism to handle an inactive player (RP can be used).
     */
    @Override
    public void doHandleInactivePlayer() {
        // the client automatically sends a leave game message to the server, if he is inactive for more than 45s and it is his turn.
    }

    /**
     * This method exists so that the networking.server can handle a networking.client that disconnected (by terminating the socket and adjusting the game).
     */
    @Override
    public void doHandleClientDisconnected() {
        try {
            this.connection.close();
            this.flag = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        sendMessage(msg);
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
        sendMessageToLobby(msg);
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
        sendMessage("BM|" + message);
    }

    public void doAskChoiceSeven() {
        String msg = "AC7";
        sendMessage(msg);
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
        while(flag) {
            this.receiveMessage();
        }
}
}
