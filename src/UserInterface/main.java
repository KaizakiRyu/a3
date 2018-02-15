package UserInterface;

import GameLogic.Board;

public class main {
    private static final int MIN_NUM_ARGS = 0;
    private static final int MAX_NUM_ARGS = 2;
    private static final int DEFAULT_NUM_TANK = 5;
    private static final int FIRST_ARGUMENT = 0;
    private static final int SECOND_ARGUMENT = 1;
    private static final int MIN_NUMBER_OF_TANK = 0;
    private static final String CHEAT_CODE = "--cheat";
    public static void main(String args[]){
        //set the number of tanks in this game.
        int numOfTanks;
        boolean cheat;
        if (args.length == MIN_NUM_ARGS){
            numOfTanks = DEFAULT_NUM_TANK;
            cheat = false;
        } else {
            numOfTanks = Integer.parseInt(args[FIRST_ARGUMENT]);
            if (args.length == MAX_NUM_ARGS && (args[SECOND_ARGUMENT].toLowerCase().equals(CHEAT_CODE))){
                cheat = true;
            } else {
                cheat = false;
            }
        }
        DisplayBoard displayBoard = new DisplayBoard(numOfTanks,cheat);
        displayBoard.startGame();

    }
}
