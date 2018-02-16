package UserInterface;

import GameLogic.*;

import java.util.ArrayList;
import java.util.Scanner;

public class DisplayBoard {
    private Board board;
    private final int GRID = 10;
    private final int GRID_INITIALIZER = 0;
    private final int TANK_NUMBERING_INITIALIZER = 1;
    private final int MIN_FORTRESS_HEALTH = 0;
    private final int MIN_AMOUNT_OF_TANK = 0;
    private final int HORIZONTAL_COORDINATE = 0;
    private final int VERTICAL_COORDINATE = 1;
    private final int OUT_OF_BOUND_HORIZONTAL_COORDINATE = -1;
    private final int MIN_VERTICAL_COORDINATE = 0;
    private final int MAX_VERTICAL_COORDINATE = 9;
    private final int ROW_NUMBERING_OFFSET = 65;
    private final int GRID_OFFSET = 1;


    public DisplayBoard(int numOfTanks, boolean cheat) {
        this.board = new Board(numOfTanks,cheat);
    }

    public void startGame() {
        Fortress gameFortress = board.getFortress();
        int fortressHealth = gameFortress.getFortressHealth();
        ArrayList<Tank> listOfTanks = board.getTankPlacement().getListOfAliveTanks();
        displayInitialBoard(listOfTanks);
        do {
            System.out.print("Enter your move: ");
            Scanner reader = new Scanner(System.in);
            String userInput = reader.next();
            int[] convertedCoordinate = board.convertCoordinateToInt(userInput);
            if (!isInBoardRange(convertedCoordinate)) {
                System.out.println("Invalid target. Please enter a coordinate such as D10.\n");
            } else {
                AttackCell currentUserAttack = new AttackCell(convertedCoordinate);
                board.userAttack(currentUserAttack);
                gameFortress = board.getFortress();
                fortressHealth = gameFortress.getFortressHealth();
                listOfTanks = board.getTankPlacement().getListOfTanks();
                printAttackResult(currentUserAttack,gameFortress,listOfTanks);
            }
        } while(fortressHealth > MIN_FORTRESS_HEALTH || listOfTanks.size() > MIN_AMOUNT_OF_TANK);
    }

    private void displayInitialBoard(ArrayList<Tank> listOfTanks) {
        Cell[][] gameBoard = board.getGameBoard();
        System.out.print("  ");
        for (int columnIndex = (GRID_INITIALIZER + GRID_OFFSET); columnIndex <= GRID; columnIndex++){
            System.out.print(columnIndex + " ");
        }
        System.out.println();
        for (int row = GRID_INITIALIZER; row < GRID; row++){
            char rowIndex = (char) (row + ROW_NUMBERING_OFFSET);
            System.out.print(rowIndex + " ");
            for (int column = GRID_INITIALIZER; column < GRID; column++){
                if (board.isCheat()) {
                    if (gameBoard[row][column].isTankCell()){
                        System.out.print(gameBoard[row][column].getId() + " ");
                    } else {
                        System.out.print(". ");
                    }
                } else {
                    System.out.print(gameBoard[row][column].getCellDisplay() + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isInBoardRange (int[] coordinate){
        if (coordinate[HORIZONTAL_COORDINATE] == OUT_OF_BOUND_HORIZONTAL_COORDINATE){
            return false;
        }
        if (coordinate[VERTICAL_COORDINATE] < MIN_VERTICAL_COORDINATE || coordinate[VERTICAL_COORDINATE] > MAX_VERTICAL_COORDINATE){
            return false;
        }
        return true;
    }

    private void printAttackResult(AttackCell currentUserAttack, Fortress gameFortress, ArrayList<Tank> listOfTanks){
        if (currentUserAttack.isHit()){
            System.out.println("HIT");
        } else {
            System.out.println("MISS");
        }
        int index = TANK_NUMBERING_INITIALIZER;
        for (Tank currentTank : listOfTanks){
            int tankDamage = currentTank.getTankDamage();
            System.out.println("Alive tank # " + index + " of " + listOfTanks.size() + " shot you for " + currentTank.getTankDamage() + "!");
            gameFortress.setFortressHealth(tankDamage);
            index++;
        }
        Cell[][] gameBoard = board.getGameBoard();
        System.out.print("  ");
        for (int columnIndex = (GRID_INITIALIZER + GRID_OFFSET); columnIndex <= GRID; columnIndex++){
            System.out.print(columnIndex + " ");
        }
        System.out.println();
        for (int row = GRID_INITIALIZER; row < GRID; row++){
            char rowIndex = (char) (row + ROW_NUMBERING_OFFSET);
            System.out.print(rowIndex + " ");
            for (int column = GRID_INITIALIZER; column < GRID; column++){
                System.out.print(gameBoard[row][column].getCellDisplay() + " ");
            }
            System.out.println();
        }
        System.out.println("Fortress Structure Left: " + gameFortress.getFortressHealth());
        System.out.println();
    }
}
