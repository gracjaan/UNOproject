package client.controller;

import java.util.Scanner;

public class InputListener implements Runnable {
    private boolean flag = true;
    private ClientHandler ch;

    public InputListener(ClientHandler ch) {
        this.ch = ch;
    }

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (flag) {
            System.out.println(">> Enter command: ");
            String input = scanner.nextLine();
            evaluateInput(input);
        }
    }

    public void stop() {
        //this.scanner.close();
        System.out.println("Enter the same to confirm");
        this.setFlag(false);
    }

    public void evaluateInput(String input) {
        String[] spl = input.split("[|]");
        if (spl.length == 1) {
            if (input.equals("lol")) {
                ch.sendMessage("LOL");
            } else {
                System.out.println("Invalid command. Try again!");
            }
        } else if (spl.length == 2) {
            switch (spl[0]) {
                case "start":
                    ch.doStartGame(spl[1]);
                    break;
                case "cl":
                    ch.doCreateLobby(spl[1]);
                    break;
                case "jl":
                    ch.doJoinLobby(spl[1]);
                    break;
                case "acp":
                    ch.doAddComputerPlayer(spl[1], "");
                    break;
                default:
                    System.out.println("Query not recognized. Please try one of the listed methods: start, lol, cl|[lobbyname], jl|[lobbyname]");
            }
        } else {
            System.out.println("Invalid command. Try again!");
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


}
