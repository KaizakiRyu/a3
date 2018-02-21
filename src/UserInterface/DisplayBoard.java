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
    private final int ASCII_OFFSET = 32;

    // DisplayBoard Constructor
    // create a new board and designate it as this board
    public DisplayBoard(int numOfTanks, boolean cheat) {
        this.board = new Board(numOfTanks,cheat);
    }

    public void startGame() {
        System.out.println("Starting Game");
        Fortress gameFortress = board.getFortress();
        int fortressHealth = gameFortress.getFortressHealth();
        ArrayList<Tank> listOfTanks = board.getTankPlacement().getListOfAliveTanks(); //ArrayList of alive tanks
        printInitialBoard();
        do {
            System.out.print("Enter your move: ");
        } while(!isFinish(gameFortress,listOfTanks));
        printGameBoardResult(board.getGameBoard());
    }

    private boolean isFinish(Fortress gameFortress, ArrayList<Tank> listOfTanks){
        Scanner reader = new Scanner(System.in);
        String userInput = reader.next();
        if (!isValidInput(userInput)){
            System.out.println("Invalid target. Please enter a coordinate such as D10.\n");
            return false;
        } else {
            int[] convertedCoordinate = board.convertCoordinateToInt(userInput);
            AttackCell currentUserAttack = new AttackCell(convertedCoordinate);
            board.userAttack(currentUserAttack);
            gameFortress = board.getFortress();
            listOfTanks = board.getTankPlacement().getListOfAliveTanks();
            printAttackResult(currentUserAttack, gameFortress, listOfTanks);
        }
        int fortressHealth = gameFortress.getFortressHealth();
        if (fortressHealth <= MIN_FORTRESS_HEALTH || listOfTanks.size() <= MIN_AMOUNT_OF_TANK){
            if (fortressHealth <= MIN_FORTRESS_HEALTH){
                System.out.println("I'm sorry, your fortress has been smashed!");
            } else {
                System.out.println("Congratulations! You won!");
            }
            System.out.println();
            return true;
        }
        return false;
    }

    private boolean isValidInput(String userInput) {
        if(Character.isDigit(userInput.charAt(HORIZONTAL_COORDINATE))) {
            return false;
        }
        if(!(Character.isDigit(userInput.charAt(VERTICAL_COORDINATE)))) {
            return false;
        }
        int[] convertedCoordinate = board.convertCoordinateToInt(userInput);
        if (convertedCoordinate[HORIZONTAL_COORDINATE] == OUT_OF_BOUND_HORIZONTAL_COORDINATE){
            return false;
        }
        if (convertedCoordinate[VERTICAL_COORDINATE] < MIN_VERTICAL_COORDINATE || convertedCoordinate[VERTICAL_COORDINATE] > MAX_VERTICAL_COORDINATE){
            return false;
        }
        return true;
    }

    private void printInitialBoard() {
        Cell[][] gameBoard = board.getGameBoard();
        System.out.print("  ");
        for (int columnIndex = (GRID_INITIALIZER + GRID_OFFSET); columnIndex <= GRID; columnIndex++){
            System.out.print(columnIndex + " ");
        }
        System.out.println();
        if (board.isCheat()){
            printCheatBoard(gameBoard);
        } else {
            printRegularBoard(gameBoard);
        }
    }

    private void printCheatBoard(Cell[][] gameBoard){
        for (int row = GRID_INITIALIZER; row < GRID; row++){
            char rowIndex = (char) (row + ROW_NUMBERING_OFFSET);
            System.out.print(rowIndex + " ");
            for (int column = GRID_INITIALIZER; column < GRID; column++){
                if (gameBoard[row][column].isTankCell()){
                    System.out.print(gameBoard[row][column].getId() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printRegularBoard(Cell[][] gameBoard){
        for (int row = GRID_INITIALIZER; row < GRID; row++){
            char rowIndex = (char) (row + ROW_NUMBERING_OFFSET);
            System.out.print(rowIndex + " ");
            for (int column = GRID_INITIALIZER; column < GRID; column++){
                System.out.print(gameBoard[row][column].getCellDisplay() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printGameBoardResult(Cell[][] gameBoard){
        System.out.print("  ");
        for (int columnIndex = (GRID_INITIALIZER + GRID_OFFSET); columnIndex <= GRID; columnIndex++){
            System.out.print(columnIndex + " ");
        }
        System.out.println();
        for (int row = GRID_INITIALIZER; row < GRID; row++){
            char rowIndex = (char) (row + ROW_NUMBERING_OFFSET);
            System.out.print(rowIndex + " ");
            for (int column = GRID_INITIALIZER; column < GRID; column++){
                if (gameBoard[row][column].isTankCell()){
                    if (gameBoard[row][column].isDestroyed()){
                        int tankDisplayAscii = (int) gameBoard[row][column].getId();
                        char convertedDamagedTankCell = (char) (tankDisplayAscii + ASCII_OFFSET);
                        System.out.print(convertedDamagedTankCell + " ");
                    } else {
                        System.out.print(gameBoard[row][column].getId() + " ");
                    }
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("(Lower case tank letters are where you shot.)");
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
        printRegularBoard(gameBoard);
        int fortressHealth = gameFortress.getFortressHealth();
        if(fortressHealth < MIN_FORTRESS_HEALTH){
            gameFortress.setFortressHealth(MIN_FORTRESS_HEALTH);
            System.out.println("Fortress Structure Left: " + MIN_FORTRESS_HEALTH);
        } else {
            System.out.println("Fortress Structure Left: " + fortressHealth);
        }
    }
}
