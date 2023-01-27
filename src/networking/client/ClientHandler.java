package networking.client;

import networking.client.contract.ClientProtocol;
import networking.server.contract.ServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler implements ClientProtocol, Runnable {
    private boolean isAdmin;
    private boolean gameStarted;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    public ClientHandler(Socket connection) throws IOException {
        this.connection = connection;
        this.isAdmin = false;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
    }
    private synchronized void seperateAndCall(String input) {
        String[] splitted = input.split("[|]");
        System.out.println(Arrays.toString(splitted));
        try {
            switch (splitted[0]) {
                case "AH":
                    handleAcceptHandshake();
                    break;
                case "IAD":
                    handleInformAdmin();
                    break;
                case "BPJ":
                    handleBroadcastPlayerJoined(splitted[1]);
                    break;
                case "GST":
                    handleGameStarted(splitted[1]);
                    break;
                case "RST":
                    handleRoundStarted();
                    break;
                case "BGI":
                    System.out.println("i got here");
                    handleBroadcastGameInformation(splitted[1], splitted[2], splitted[3], splitted[4]);
                    break;
                case "BCP":
                    handleBroadcastCardPlayed(splitted[1], splitted[2]);
                    break;
                case "BDC":
                    handleBroadcastDrewCard(splitted[1]);
                    break;
                case "BTS":
                    handleBroadcastTurnSkipped(splitted[1]);
                    break;
                case "BRS":
                    handleBroadcastReverse(splitted[1]);
                    break;
                case "BLG":
                    handleBroadcastLeftGame(splitted[1]);
                    break;
                case "RP":
                    handleRemindPlay(splitted[1]);
                    break;
                case "RE":
                    handleRoundEnded(splitted[1]);
                    break;
                case "GE":
                    handleGameEnded(splitted[1]);
                    break;
                case "ERR":
                    handleSendErrorCode(splitted[1]);
                    break;
                case "LOL":
                    handleBroadcastListOfLobbies(splitted[1]);
                    break;
                case "BCL":
                    handleBroadcastCreatedLobby(splitted[1]);
                    break;
                case "BJL":
                    handleBroadcastPlayerJoinedLobby(splitted[1]);
                    break;
                case "AC":
                    handleAskColor();
                    break;
                case "DPC":
                    handleDrewPlayableCard(splitted[1]);
                    break;
                case "BCC":
                    handleBroadcastColorChange(splitted[1]);
                    break;
                case "BUNO":
                    handleBroadcastSayUNO();
                    break;
                default:
                    System.out.println(ServerProtocol.Errors.E001.getMessage());
            }
        }catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        }
    }

    public void doHandshake() throws IOException {
        //send HS to networking.client
        out.println("Tocjan");
        out.flush();
        String messageIn = in.readLine();
        if (!messageIn.equals("Tocjan")) {
            throw new IOException("Wrong networking.client connected.");
        }
        System.out.println("Connection successful.");
    }
    public void sendMessage(String messageOut) {
        System.out.print("SEND: ");
//        Scanner scannner = new Scanner(System.in);
//        String messageOut = scannner.nextLine();
        out.println(messageOut);
        System.out.println(messageOut);
        out.flush();
        if(out.checkError()) {
            System.out.println("An error occured during transmission.");
        }
    }
    public void receiveMessage() throws IOException {
        System.out.println("WAITING...");
        String messageIn = "";
        try {
            messageIn = in.readLine();
            if (messageIn==null) {
                closeConnection();
            }
        } catch (IOException e) {
            sendMessage(ServerProtocol.Errors.E001.getMessage());
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
     * This method informs the networking.client that the handshake was accepted (AH).
     */
    @Override
    public void handleAcceptHandshake() {
        System.out.println("Handshake accepted.");
    }

    /**
     * This method handles the message being sent by the networking.server regarding informing the networking.client that they are the admin (IAD).
     */
    @Override
    public void handleInformAdmin() {
        // create computer players, start game etc...
        this.isAdmin = true;
        System.out.println("You're the admin");
        // just for now adding CP and starting the game.
        //doAddComputerPlayer("tom", "advanced");
        // todo shouldnt be started automatically, but sent when client makes the input for it so we just listen for input until we start the game then its business as usual.
       // doStartGame("normal");
    }

    /**
     * This method handles the message being sent by the broadcast player joined (BPJ).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastPlayerJoined(String playerName) {
        System.out.println(playerName + " connected to the networking.server");
    }

    /**
     * This method handles the message being sent by the game started (GST).
     *
     * @param gameMode of type {@code String} representing the type/mode of the game
     */
    @Override
    public void handleGameStarted(String gameMode) {
        System.out.println("Game has started in mode: " + gameMode);
    }

    /**
     * This method handles the message being sent by the round started (RST).
     */
    @Override
    public void handleRoundStarted() {
        System.out.println("New round has started!");
    }

    public void callBGI (String str){
        String [] splitted = str.split("[|]");
        handleBroadcastGameInformation(splitted[1], splitted[2], splitted[3], splitted[4]);
    }

    private boolean isInRange(String str, String[] splittedHand) {
        if (str == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(str);
            if (i < splittedHand.length) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }
    private String askInput() {
        System.out.println(">> input please");
        Scanner scan = new Scanner(System.in);
        String ind = scan.nextLine();
        return ind;
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
        if (isYourTurn.equals("true")){
            System.out.println("========================================YOUR TURN=================================================");
            System.out.println("| " + topCard + " |");
            String [] splittedHand = playerHand.split(";");
            for (int i = 0; i < splittedHand.length; i++ ){
                System.out.print(i + "| " + splittedHand[i] + " |        ");
            }
            String [] splittedPlayers = playersList.split(";");
            for (int i = 0; i < splittedPlayers.length; i++){
                String [] split = splittedPlayers[i].split(":");
                System.out.println(split[0] + " has " + split[1] + " cards and " + split[2] + " points!");
            }
            String ind = askInput();
            String[] split = ind.split(" ");
            if (split.length>1&&split[1].equals("uno")&&splittedHand.length==2) {
                doSayUno();
                if (isInRange(split[0], splittedHand)) {
                    String card = splittedHand[Integer.parseInt(split[0])];
                    doPlayCard(card);
                }else {
                    handleBroadcastGameInformation(topCard, playerHand, playersList, isYourTurn);
                }
            } else if (ind.equals("draw")) {
                doDrawCard();
            }
            else {
                if (isInRange(ind, splittedHand)) {
                    String card = splittedHand[Integer.parseInt(ind)];
                    doPlayCard(card);
                }else {
                    handleBroadcastGameInformation(topCard, playerHand, playersList, isYourTurn);
                }
            }

        }
        else{
            System.out.println("========================================NEW TURN==================================================");
            System.out.println("| " + topCard + " |");
            String [] splittedHand = playerHand.split(";");
            for (int i = 0; i < splittedHand.length; i++ ){
                System.out.print(i + "| " + splittedHand[i] + " |        ");
            }
            String [] splittedPlayers = playersList.split(";");
            for (int i = 0; i < splittedPlayers.length; i++){
                String [] split = splittedPlayers[i].split(":");
                System.out.println(split[0] + " has " + split[1] + " cards and " + split[2] + " points!");
            }
        }
    }

    /**
     * This method handles the message being sent by the broadcast card played (BCP).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     * @param playedCard of type {@code String} representing the card played
     */
    @Override
    public void handleBroadcastCardPlayed(String playerName, String playedCard) {
        System.out.println(playerName + " played " + playedCard);
    }

    /**
     * This method handles the message being set by the broadcast drew card (BDC).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastDrewCard(String playerName) {
        System.out.println(playerName + " drew a card!");
    }

    /**
     * This method handles the message being sent by the broadcast turn skipped(BTS).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastTurnSkipped(String playerName) {
        System.out.println(playerName + "'s turn was skipped!");
    }

    /**
     * This method handles the message being sent by the broadcast reverse (BRS).
     *
     * @param direction of type {@code String} representing the direction of the game
     */
    @Override
    public void handleBroadcastReverse(String direction) {
        System.out.println("Direction was changed. Now it's " + direction);
    }

    /**
     * This method handles the message being sent by the broadcast left game (BLG).
     *
     * @param playerName of type {@code String} representing the unique name of the player
     */
    @Override
    public void handleBroadcastLeftGame(String playerName) {
        System.out.println(playerName + " left the game!");
    }

    /**
     * This method handles the message being sent by the networking.server, reminding the networking.client to play (RP).
     *
     * @param timeLeft of type {@code String} representing the time left to play
     */
    @Override
    public void handleRemindPlay(String timeLeft) {
        System.out.println("You have " + timeLeft + " to make a move");
    }

    /**
     * This method handles the message being sent by the round ended (RE).
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the round
     */
    @Override
    public void handleRoundEnded(String playerName) {
        System.out.println("The round has ended! " + playerName + " was a winner!");
    }

    /**
     * This method handles the message being sent by the game ended (GE).
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    @Override
    public void handleGameEnded(String playerName) {
        System.out.println("The game has ended! " + playerName + " was an ultimate winner!");
    }

    /**
     * This method handles the message being sent by send error code (E***).
     *
     * @param errorCode of type {@code String} containing the error code
     */
    @Override
    public void handleSendErrorCode(String errorCode) {
        switch (errorCode) {
            case "E001":
                System.out.println("Please enter a valid command.");
                break;
            case "E002":
                askStartInput();
                System.out.println(ServerProtocol.Errors.E002.getMessage());
                break;
            case "E003":
                System.out.println("Please enter a valid command!");
                break;
            case "E006":
                System.out.println("Please type a valid input.");
            case "E007":



        }
    }

    /**
     * This method handles the message being sent by the broadcast list of lobbies (LOL).
     *
     * @param lobbiesList of type String, representing the list of existing lobbies.
     */
    @Override
    public void handleBroadcastListOfLobbies(String lobbiesList) {
        String[] spl = lobbiesList.split(";");
        for (String s: spl) {
            String[] l = s.split(":");
            System.out.println(l[0]+": " + l[1] + " players waiting");
        }
    }

    /**
     * This method handles the message being sent by the broadcast created lobby (BCL).
     *
     * @param lobbyName of type {@code String} representing the unique name of the lobby
     */
    @Override
    public void handleBroadcastCreatedLobby(String lobbyName) {
        System.out.println("Lobby " + lobbyName  + " has been created.");
    }

    /**
     * This method handles the message being sent by the networking.server about a player joining the lobby (BJL).
     *
     * @param playerName of type {@code String} representing the unique name of the winner of the game
     */
    @Override
    public void handleBroadcastPlayerJoinedLobby(String playerName) {
        System.out.println(playerName + " has joined the lobby.");
        Scanner scan = new Scanner(System.in);
        // FUTURE: Consider different game modes in the input
//        if (isAdmin){
//            System.out.println("Would you like to start the game now?");
//            String choice = scan.next();
//            if (choice.equals("yes")) {
//                doStartGame("normal");
//            } else {
//                scan.close();
//            }
//    }
    }

    /**
     * This method handles the message being sent by the broadcast message (BM).
     *
     * @param message of type String, representing the chat message.
     */
    @Override
    public void handleBroadcastMessage(String message) {
        // message should contain the playername?
        System.out.println("CHAT: " + message);
    }

    /**
     * This method handles the message being sent by the networking.server after a player says UNO (BUNO).
     */
    @Override
    public void handleBroadcastSayUNO() {

    }

    /**
     * This method is intended to handle the possibility when a player picks up a playable card, and is requested whether they want to play it.
     *
     * @param playableCard of type String, representing the playable card.
     */
    @Override
    public void handleDrewPlayableCard(String playableCard) {
        System.out.println("Do you want to play this card now?");
        Scanner scan = new Scanner(System.in);
        String a = scan.next();
        // yes and no, no caps
        if (a.equals("yes")) {
            doRetainCard("true");
        }else if (a.equals("no")) {
            doRetainCard("false");
        }else {
            System.out.println("Please try again. Only 'yes' and 'no' are accepted.");
            handleDrewPlayableCard(playableCard);
        }
    }

    /**
     * This method is intended to handle the request for the player to the left of the dealer for the color of the card.
     * THIS METHOD IS ONLY HANDLED IN THE EVENT THAT THE FIRST CARD DRAWN FROM THE PILE ONTO THE PLAYING AREA (FROM THE DECK) IS A WILD!
     */
    @Override
    public void handleAskColor() {
        Scanner scan = new Scanner(System.in);
        System.out.print(">> Please pick a color ");
        String c = scan.next();
        doColorChoice(c.toUpperCase());
    }

    /**
     * This method is intended to display to the client when a color is changed.
     *
     * @param color of type String, representing the new color.
     */
    @Override
    public void handleBroadcastColorChange(String color) {
        System.out.println("The color was changed to " + color);
    }

    /**
     * This is a free method: use it to your advantage to display specific information from the server as you would like.
     * This method will handle the displaying to the client.
     *
     * @param args of type String, representing multiple arguments of your choice.
     */
    @Override
    public void handleBroadcastGameMessage(String... args) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to the make handshake (MH)..
     * The method initializes the handshake of the networking.client and the networking.server with the parameters provided.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param playerName of type {@code String} representing the unique name of the player
     * @param playerType of type {@code String} representing computer_player ot human_player
     */
    @Override
    public void doMakeHandshake(String playerName, String playerType) {
        String hs = "MH|" + playerName + "|" + playerType;
        sendMessage(hs);
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
        if (isAdmin){
            String result = "ACP|" + playerName + "|" + strategy;
            sendMessage(result);
        }
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
        if (isAdmin){
            String result = "SG|" + gameMode;
            sendMessage(result);
        }
        else{
            System.out.println("Game is being setup in mode: " + gameMode);
        }
    }

    /**
     * This method creates the appropriate tag and message corresponding to a card being played (PC).
     * The method is being used when it is the networking.client's turn, and he needs to play a card. The chosen card is passed as a parameter to the method.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param card of type {@code String} representing the card that the networking.client wants to play
     */
    @Override
    public void doPlayCard(String card) {
            String result = "PC|" + card;
            sendMessage(result);
    }

    /**
     * This method creates the appropriate tag and message corresponding to a card being drawn (DC).
     * The method is being used when it is the networking.client's turn, and he wants to draw a card.
     * Once the data packet is produced, the sender() method is invoked.
     */
    @Override
    public void doDrawCard() {
        sendMessage("DC");
    }

    /**
     * This method creates the appropriate tag and message corresponding to a networking.client leaving the game (LG).
     * The method is being used when the networking.client wants to leave the game.
     * Once the data packet is produced, the sender() method is invoked.
     */
    @Override
    public void doLeaveGame() {
        sendMessage("LG");
    }

    /**
     * This method creates the appropriate tag and message corresponding to a networking.client creating a lobby (CL).
     * The method is being used when the networking.client wants to create a lobby.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void doCreateLobby(String lobbyName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a networking.client joining a lobby (JL).
     * The method is being used when the networking.client wants to join a lobby.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param lobbyName of type String, representing the name of the lobby.
     */
    @Override
    public void doJoinLobby(String lobbyName) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a networking.client sending a message in the chat (SM).
     * The method is being used when the networking.client wants to send a message in the chat.
     * Once the data packet is produced, the sender() method is invoked.
     *
     * @param message of type String, representing the message.
     */
    @Override
    public void doSendMessage(String message) {

    }

    /**
     * This method creates the appropriate tag and message corresponding to a networking.client saying UNO to avoid punishment(UNO).
     * The method is being used when the networking.client wants to say UNO.
     * Once the data packet is produced, the sender() method is invoked.
     */
    @Override
    public void doSayUno() {
        sendMessage("UNO");
    }

    /**
     * This method creates an appropriate tag and message corresponding to the choice made by a player whether to retain the Card that they picked.
     *
     * @param choice of type String, true if they want to play, false if they do not want to play the card.
     */
    @Override
    public void doRetainCard(String choice) {
        String msg = "RC|" + choice;
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to the choice made by the player to the left of the dealer about what color to be played.
     * This happens under the event that the first card of play (pulled from the deck onto the playing space) is a WILD, meaning the player to the left of the dealer chooses the color.
     *
     * @param color of type String, representation of color.
     */
    @Override
    public void doColorChoice(String color) {
        String msg = "CC|" + color;
        sendMessage(msg);
    }

    /**
     * This method creates the appropriate tag and message corresponding to the choice regarding the player to switch cards with in the Seven-0 game-mode.
     *
     * @param playerName of type String, representing the player with whom they want to change cards with.
     * @param card       of type String, representing the seven that was played and needs to be sent to the server.
     */
    @Override
    public void doMakeChoiceSeven(String playerName, String card) {

    }

    private void askStartInput() {
        System.out.print(">> Please enter name and playerType");
        Scanner scan = new Scanner(System.in);
        String nextLn = scan.nextLine();
        String[] splitted = nextLn.split(" ");
        StringBuffer sb = new StringBuffer(nextLn);
        if (splitted[splitted.length-1].equals("c")) {
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
            doMakeHandshake(sb.toString(), "computer_player");
        }else {
            doMakeHandshake(sb.toString(), "human_player");
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
        // player name and type of player
        askStartInput();
        Scanner scan = new Scanner(System.in);
        while (!gameStarted) {
            try {
                receiveMessage();
            } catch (IOException e) {
                System.out.println("IOException");
            }
            // todo this should be in a while loop with a timer for like 10s --> so every 10s you check for new messages and print new events (Broadcasting that a player joined)
            // or just have a command "u" that will update for news?
            // or just ask every time a player joins --> would u like to start the game now?(yes/no) that would not be in here then.
            String start = scan.nextLine();
            String[] spl = start.split(" ");
            // commands CL lobbyname ; JL lobbyname should be formatted with a space in between.
            if (spl.length == 1) {
                if (start.equals("start")) {
                    // modify gameModes here and change start command
                    this.gameStarted=true;
                    try {
                        in.reset();
                    } catch (IOException e) {
                        System.out.println("couldn't reset input stream.");
                    }
                    doStartGame("normal");
                    break;
                } else if (start.equals("u")) {
                    break;
                } else if (start.equals("LOL")) {
                    sendMessage("LOL");
                }
            } else if (spl.length==2) {
                if (spl[0].equals("CL")) {
                    sendMessage("CL|"+spl[1]);
                } else if (spl[0].equals("JL")) {
                    sendMessage("JL|"+spl[1]);
                }
            }
            else {
                System.out.println("invalid input.");
            }
            // end of while
        }
        // update every 10 seconds.

        while (true) {
            try {
                // wait for the message to be sent.
                receiveMessage();
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
    }
}
