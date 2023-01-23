package client.contract;
/**
 * This interface includes all relevant protocol codes, and methods that the client of Uno Game will need to use in order to ensure
 * secure integration with the network protocol that was designed.
 *
 * Relevant JavaDocs are added.
 */
public interface ClientProtocol {

    /**
     * The following list contains the client commands (commands that are sent from the client to the server).
     *
     * The access modifier is public because the server will need access to these in order to determine what appropriate course of action needs to be
     * taken with respect to each particular command sent by the client. Further documentation for each command can be found in the protocol description table.
     */
    enum ClientCommand {
        MH("Make Handshake"),
        ACP("Add Computer Player"),
        SG("Start Game"),
        PC("Play Card"),
        DC("Draw Card"),
        LG("Leave Game"),
        /* Extra features */
        CL("Create Lobby"),
        JL("Join Lobby"),
        SM("Send Chat Message"),
        UNO("Say Uno");

        private final String action;

        ClientCommand(String action) {
            this.action = action;
        }
        public String getAction() {
            return this.action;
        }
    }


    /* Handlers - those handlers are used to handle messages received from the server. */

    /**
     * This method informs the client that the handshake was accepted (AH).
     */
    void handleAcceptHandshake();

    /**
     * This method handles the message being sent by the server regarding informing the client that they are the admin (IAD).
     */
    void handleInformAdmin();

    /**
     * This method handles the message being sent by the broadcast player joined (BPJ).
     * @param playerName of type {@code String} representing the unique name of the player
     */
    void handleBroadcastPlayerJoined(String playerName);

    /**
     * This method handles the message being sent by the game started (GST).
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    void handleGameStarted(String gameMode);

    /**
     * This method handles the message being sent by the round started (RST).
     */
    void handleRoundStarted();

    /**
     * This method handles the message being sent by the broadcast game information method (BGI).
     * @param topCard of type {@code String} representing the top card on the pile visible to players
     * @param playerHand of type {@code String} representing the corresponding playre's hand
     * @param playersList of type {@code String} representing the list of players of the game sorted by the order of turn
     * @param isYourTurn of type {@code String} indicates if it is the player’s turn
     */
    void handleBroadcastGameInformation(String topCard, String playerHand, String playersList, String isYourTurn);

    /**
     * This method handles the message being sent by the broadcast card played (BCP).
     * @param playerName of type {@code String} representing the unique name of the player
     * @param playedCard of type {@code String} representing the card played
     */
    void handleBroadcastCardPlayed(String playerName, String playedCard);

    /**
     * This method handles the message being set by the broadcast drew card (BDC).
     * @param playerName of type {@code String} representing the unique name of the player
     */
    void handleBroadcastDrewCard(String playerName);

    /**
     * This method handles the message being sent by the broadcast turn skipped(BTS).
     * @param playerName of type {@code String} representing the unique name of the player
     */
    void handleBroadcastTurnSkipped(String playerName);

    /**
     * This method handles the message being sent by the broadcast reverse (BRS).
     * @param direction of type {@code String} representing the direction of the game
     */
    void handleBroadcastReverse(String direction);

    /**
     * This method handles the message being sent by the broadcast left game (BLG).
     * @param playerName of type {@code String} representing the unique name of the player
     */
    void handleBroadcastLeftGame(String playerName);

    /**
     * This method handles the message being sent by the server, reminding the client to play (RP).
     * @param timeLeft of type {@code String} representing the time left to play
     */
    void handleRemindPlay(String timeLeft);

    /**
     * This method handles the message being sent by the round ended (RE).
     * @param playerName of type {@code String} representing the unique name of the winner of the round
     */
    void handleRoundEnded(String playerName);

    /**
     * This method handles the message being sent by the game ended (GE).
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    void handleGameEnded(String playerName);

    /**
     * This method handles the message being sent by send error code (E***).
     * @param errorCode of type {@code String} containing the error code
     */
    void handleSendErrorCode(String errorCode);


    /* Handlers for additional features */

    /**
     * This method handles the message being sent by the broadcast list of lobbies (LOL).
     * @param lobbiesList of type String, representing the list of existing lobbies.
     */
    void handleBroadcastListOfLobbies(String lobbiesList);

    /**
     * This method handles the message being sent by the broadcast created lobby (BCL).
     * @param lobbyName of type {@code String} representing the unique name of the lobby
     */
    void handleBroadcastCreatedLobby(String lobbyName);

    /**
     * This method handles the message being sent by the server about a player joining the lobby (BJL).
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    void handleBroadcastPlayerJoinedLobby(String playerName);

    /**
     * This method handles the message being sent by the broadcast message (BM).
     * @param message of type String, representing the chat message.
     */
    void handleBroadcastMessage(String message);

    /**
     * This method handles the message being sent by the server after a player says UNO (BUNO).
     */
    void handleBroadcastSayUNO();


    /* Methods implementing protocols from Client to Server */

    /**
     * This method creates the appropriate tag and message corresponding to the make handshake (MH)..
     * The method initializes the handshake of the client and the server with the parameters provided.
     * Once the data packet is produced, the sender() method is invoked.
     * @param playerName of type {@code String} representing the unique name of the player
     * @param playerType of type {@code String} representing computer_player ot human_player
     */
    void doMakeHandshake(String playerName, String playerType);


    /* Admin methods - in order to be able to use those methods the client, connected to the server,
     * needs to be the one that created the game, so is chosen as an admin.
     */

    /**
     * This method creates the appropriate tag and message corresponding to the add computer player (ACP).
     * The method adds a computer player to the created game with the provided name and strategy
     * Once the data packet is produced, the sender() method is invoked.
     * @param playerName of type {@code String} representing the name of the computer player
     * @param strategy of type {@code String} representing the strategy for the computer player
     */
    void doAddComputerPlayer(String playerName, String strategy);

    /**
     * This method creates the appropriate tag and message corresponding to the start game (SG).
     * The method initializes the game
     * Once the data packet is produced, the sender() method is invoked.
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    void doStartGame(String gameMode);


    /* In-game methods - these methods are used when the game is already initialized and is being played. */

    /**
     * This method creates the appropriate tag and message corresponding to a card being played (PC).
     * The method is being used when it is the client's turn, and he needs to play a card. The chosen card is passed as a parameter to the method.
     * Once the data packet is produced, the sender() method is invoked.
     * @param card of type {@code String} representing the card that the client wants to play
     */
    void doPlayCard(String card); // this should have no parameter because client does not have card on client-side. Look at the table, also he knows his cards in the hand

    /**
     * This method creates the appropriate tag and message corresponding to a card being drawn (DC).
     * The method is being used when it is the client's turn, and he wants to draw a card.
     * Once the data packet is produced, the sender() method is invoked.
     */
    void doDrawCard();

    /**
     * This method creates the appropriate tag and message corresponding to a client leaving the game (LG).
     * The method is being used when the client wants to leave the game.
     * Once the data packet is produced, the sender() method is invoked.
     */
    void doLeaveGame();


    /* Handlers for additional features */

    /**
     * This method creates the appropriate tag and message corresponding to a client creating a lobby (CL).
     * The method is being used when the client wants to create a lobby.
     * Once the data packet is produced, the sender() method is invoked.
     * @param lobbyName of type String, representing the name of the lobby.
     */
    void doCreateLobby(String lobbyName);

    /**
     * This method creates the appropriate tag and message corresponding to a client joining a lobby (JL).
     * The method is being used when the client wants to join a lobby.
     * Once the data packet is produced, the sender() method is invoked.
     * @param lobbyName of type String, representing the name of the lobby.
     */
    void doJoinLobby(String lobbyName);

    /**
     * This method creates the appropriate tag and message corresponding to a client sending a message in the chat (SM).
     * The method is being used when the client wants to send a message in the chat.
     * Once the data packet is produced, the sender() method is invoked.
     * @param message of type String, representing the message.
     */
    void doSendMessage(String message);

    /**
     * This method creates the appropriate tag and message corresponding to a client saying UNO to avoid punishment(UNO).
     * The method is being used when the client wants to say UNO.
     * Once the data packet is produced, the sender() method is invoked.
     */
    void doSayUno();
}

