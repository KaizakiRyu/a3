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
    private final int COORDINATES_OF_A_CELL = 2;
    private final int HORIZONTAL_COORDINATE = 0;
    private final int VERTICAL_COORDINATE = 1;
    private final int NEXT_VERTICAL_COORDINATE = 2;
    private final int MAX_NUMBER_OF_TANKS = 22;
    private final String HIT_TANK = "X";
    private final int OUT_OF_BOUND_COORDINATE = -1;
    private final int COORDINATE_OFFSET = 1;
    private final int MAX_COORDINATE_INPUT = 3;

    // constructor
    // takes in an integer for the numberOfTanks, and a boolean to determine whether the user wants to cheat
    public Board(int numberOfTanks, boolean cheat) {
        if (cheat){
            this.cheat = true;
        }
        this.fortress = new Fortress();
        this.tankPlacement = new TankPlacement(numberOfTanks, cheat); //create a new TankPlacement instance and places all the tanks
        this.gameBoard = initializeBoard();
        tankPlacement.placeAllTanks(this.gameBoard);
    }

    private Cell[][] initializeBoard(){
        Cell[][] gameBoard = new Cell[GRID_DIMENSION][GRID_DIMENSION];
        //Set coordinate for all Cell within the gameBoard

//        for (int row = INITIALIZER; row < GRID_DIMENSION; row++){
//            for (int column = INITIALIZER; column < GRID_DIMENSION; column++){
//                System.out.println(gameBoard.toString());
//            }
//        }

        //declare 100 new cells, one for each spot on the gameboard, and set the cellCoordinate and cellDisplay
        for (int row = INITIALIZER; row < GRID_DIMENSION; row++){
            for (int column = INITIALIZER; column < GRID_DIMENSION; column++){
                int cellCoordinate[] = new int[]{row,column};
                gameBoard[row][column] = new Cell();
                gameBoard[row][column].setCellCoordinate(cellCoordinate);
                gameBoard[row][column].setCellDisplay("~");
            }
        }
        return gameBoard;
    }

    //converts string coordinate to array of ints.
    //eg: A1 -> [0,0]
    public int[] convertCoordinateToInt(String targetingCell) {
        int[] position = new int[COORDINATES_OF_A_CELL];
        if (!isValidCell(targetingCell)){
            position[HORIZONTAL_COORDINATE] = OUT_OF_BOUND_COORDINATE;
            position[VERTICAL_COORDINATE] = OUT_OF_BOUND_COORDINATE;
            return position;
        } else {
            //get the first character from targetingCell
            char firstChar = targetingCell.charAt(HORIZONTAL_COORDINATE);
            position[HORIZONTAL_COORDINATE] = charAlphabetConversion(firstChar);
            if (targetingCell.length() == MAX_COORDINATE_INPUT){
                String verticalCoordinate = targetingCell.substring(VERTICAL_COORDINATE);
                position[VERTICAL_COORDINATE] = (Integer.parseInt(verticalCoordinate) - COORDINATE_OFFSET);
            } else {
                position[VERTICAL_COORDINATE] = Character.getNumericValue(targetingCell.charAt(VERTICAL_COORDINATE)) - COORDINATE_OFFSET;
            }
            return position;
        }
    }

    private boolean isValidCell(String targetingCell){
        if (targetingCell.length() > MAX_COORDINATE_INPUT){
            return false;
        }
        if (targetingCell.length() == MAX_COORDINATE_INPUT){
            if(!(Character.isDigit(targetingCell.charAt(NEXT_VERTICAL_COORDINATE)))) {
                return false;
            }
        }
        return true;
    }

    //converts a character to its appropriate integer
    //eg, A -> 0
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

    private boolean hasTooManyTanks(int numOfTanks){
        if (numOfTanks > MAX_NUMBER_OF_TANKS){
            return true;
        }
        return false;
    }

    //getter for fortress
    public Fortress getFortress() {
        return fortress;
    }

    //getter for TankPlacement
    public TankPlacement getTankPlacement() {
        return tankPlacement;
    }

    //getter for gameBoard
    public Cell[][] getGameBoard() {
        return gameBoard;
    }

    //getter for the currentPlayerAttack
    public AttackCell getCurrentPlayerAttack() {
        return currentPlayerAttack;
    }

    // executes an attack from the user
    // parameters: AttackCell currentPlayerAttack
    // if the attack is targeting a tank then:
    // check if the tank is destroyed
    // if false, decrement tank health, decrement tank damage, update currentPlayerAttack's cell to destroyed
    // else, remove this tank from the list of alive tanks
    // change the cellDisplay to " "
    public void userAttack(AttackCell currentPlayerAttack){
        Cell currentCell = currentPlayerAttack.searchCell(gameBoard);
        ArrayList<Tank> listOfTanks = tankPlacement.getListOfTanks();
        ArrayList<Tank> listOfAliveTanks = tankPlacement.getListOfAliveTanks();
        for (Tank currentTank : listOfTanks){
            if (currentTank.getListOfTankCell().contains(currentCell)){
                currentPlayerAttack.setResult(true);
                if (currentCell.isDestroyed()){
                    return;
                } else {
                    currentCell.setCellDisplay(HIT_TANK);
                    currentCell.setDestroyed(true);
                    currentTank.setTankHealth();
                    currentTank.setTankDamage();
                    if (currentTank.isDestroyed()) {
                        listOfAliveTanks.remove(currentTank);
                    }
                    return;
                }
            }
        }
        currentCell.setCellDisplay(" ");
    }

    public boolean isCheat() {
        return cheat;
    }
}
