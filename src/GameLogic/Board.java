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

    private Cell[][] initializeBoard(){
        Cell[][] gameBoard = new Cell[GRID_DIMENSION][GRID_DIMENSION];
        //Set coordinate for all Cell within the gameBoard

//        for (int row = INITIALIZER; row < GRID_DIMENSION; row++){
//            for (int column = INITIALIZER; column < GRID_DIMENSION; column++){
//                System.out.println(gameBoard.toString());
//            }
//        }

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

    public int[] convertCoordinateToInt(String targetingCell) {
        int[] position = new int[CORDINATES_OF_A_CELL];
        if (targetingCell.length() > 3){
            position[HORIZONTAL_COORDINATE] = -1;
            position[VERTICAL_COORDINATE] = -1;
            return position;
        } else {
            //get the first character from targetingCell
            char firstChar = targetingCell.charAt(HORIZONTAL_COORDINATE);

            position[HORIZONTAL_COORDINATE] = charAlphabetConversion(firstChar);
            if (targetingCell.length() == 3){
                String verticalCoordinate = targetingCell.substring(1);
                position[VERTICAL_COORDINATE] = (Integer.parseInt(verticalCoordinate) - 1);
            } else {
                position[VERTICAL_COORDINATE] = Character.getNumericValue(targetingCell.charAt(VERTICAL_COORDINATE)) - 1;
            }
            System.out.println("Attack Coor Row " + position[HORIZONTAL_COORDINATE]);
            System.out.println("Attack Coor Column " + position[VERTICAL_COORDINATE]);
            return position;
        }
    }

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

    public Fortress getFortress() {
        return fortress;
    }

    public TankPlacement getTankPlacement() {
        return tankPlacement;
    }

    public Cell[][] getGameBoard() {
        return gameBoard;
    }

    public AttackCell getCurrentPlayerAttack() {
        return currentPlayerAttack;
    }

    public void userAttack(AttackCell currentPlayerAttack){
        Cell currentCell = currentPlayerAttack.searchCell(gameBoard);
        ArrayList<Tank> listOfAliveTanks = tankPlacement.getListOfAliveTanks();
        for (Tank currentTank : listOfAliveTanks){
            if (currentTank.getListOfTankCell().contains(currentCell)){
                if (!currentTank.isDestroyed()) {
                    currentTank.setTankHealth();
                    currentTank.setTankDamage();
                    currentPlayerAttack.updateCell(currentCell);
                    this.currentPlayerAttack = currentPlayerAttack;
                } else {
                    listOfAliveTanks.remove(currentTank);
                }
                return;
            }
        }
        currentCell.setCellDisplay(" ");
    }

    public boolean isCheat() {
        return cheat;
    }
}
