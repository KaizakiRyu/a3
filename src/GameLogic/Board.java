package GameLogic;

import java.util.ArrayList;

public class Board {
    private boolean cheat;
    private Fortress fortress;
    private TankPlacement tankPlacement;
    private Cell[][] gameBoard;
    private AttackCell currentPlayerAttack;
    private final int GRID_DIMENSION = 10;
    private final int INITIALIZER = 0;
    private final int CORDINATES_OF_A_CELL = 2;
    private final int HORIZONTAL_COORDINATE = 0;
    private final int VERTICAL_COORDINATE = 1;

    public Board(int numberOfTanks, boolean cheat) {
        if (cheat){
            this.cheat = true;
        }
        this.fortress = new Fortress();
        this.tankPlacement = new TankPlacement(numberOfTanks, cheat);
        this.gameBoard = initializeBoard();
        tankPlacement.placeAllTanks(this.gameBoard);
    }

    //creates a newGameBoard and sets its cell coordinates and sets the cell display
    private Cell[][] initializeBoard(){
        Cell[][] gameBoard = new Cell[GRID_DIMENSION][GRID_DIMENSION];
        //Set coordinate for all Cell within the gameBoard
        for (int row = INITIALIZER; row < GRID_DIMENSION; row++){
            for (int column = INITIALIZER; column < GRID_DIMENSION; column++){
                int cellCoordinate[] = new int[]{row,column};
                gameBoard[row][column].setCellCoordinate(cellCoordinate);
                gameBoard[row][column].setDestroyed(false);
                if (this.cheat){
                    gameBoard[row][column].setCellDisplay(".");
                } else {
                    gameBoard[row][column].setCellDisplay("~");
                }
            }
        }
        return gameBoard;
    }

    //takes in a coordinate in the form of a string, and converts it to a numerical index
    //eg. A1 -> 0,0
    public int[] convertCoordinateToInt(String targetingCell) {
        int[] position = new int[CORDINATES_OF_A_CELL];

        //get the first character from targetingCell
        char firstChar = targetingCell.charAt(HORIZONTAL_COORDINATE);

        position[HORIZONTAL_COORDINATE] = charAlphabetConversion(firstChar);
        position[VERTICAL_COORDINATE] = Character.getNumericValue(targetingCell.charAt(VERTICAL_COORDINATE)) - 1;

        return position;
    }


    //helper function for convertCoordinateToInt
    //converts string to its numerical value for our numerical coordinates
    //eg. A || a -> 0
    private int charAlphabetConversion(char currentCharacter){
        int position;
        String convertedFirstChar = Character.toString(currentCharacter);
        convertedFirstChar = convertedFirstChar.toUpperCase();
        switch (convertedFirstChar) {
            case "A":
                position = 0;
                break;

            case "B":
                position = 1;
                break;

            case "C":
                position = 2;
                break;

            case "D":
                position = 3;
                break;

            case "E":
                position = 4;
                break;

            case "F":
                position = 5;
                break;

            case "G":
                position = 6;
                break;

            case "H":
                position = 7;
                break;

            case "I":
                position = 8;
                break;

            case "J":
                position = 9;
                break;

            default:
                position = -1;
                break;
        }
        return position;
    }

    //executes an attack on a cell, takes in an AttackCell object
    public void userAttack(AttackCell currentPlayerAttack){
        Cell currentCell = currentPlayerAttack.searchCell(gameBoard);
        ArrayList<Tank> listOfTanks = tankPlacement.getListOfTanks();
        for (Tank currentTank : listOfTanks){
            if (currentTank.getListOfTankCell().contains(currentCell)){
                if (!currentTank.isDestroyed()) {
                    currentTank.setTankHealth();
                    currentTank.setTankDamage();
                    currentPlayerAttack.updateCell(currentCell);
                    this.currentPlayerAttack = currentPlayerAttack;
                }
                return;
            }
        }
        currentCell.setCellDisplay(" ");
    }

    // getters and setters
    public Fortress getFortress() {
        return this.fortress;
    }

    public TankPlacement getTankPlacement() {
        return this.tankPlacement;
    }

    public Cell[][] getGameBoard() {
        return this.gameBoard;
    }

    public AttackCell getCurrentPlayerAttack() {
        return this.currentPlayerAttack;
    }


}
