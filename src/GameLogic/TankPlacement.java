package GameLogic;

import java.util.ArrayList;

/**
 * Class TankPlacement is responsible for placing all Tanks and return
 * a list of tank
 */

public class TankPlacement {
    private ArrayList<Tank> listOfTanks;
    private int numberOfTanks;
    private final int INITIALIZER = 0;
    private final int GRID_DIMENSION = 10;
    private final int MAX_CELL_VALUE = 9;
    private final int MAX_NUMBER_OF_TANK_CELL = 4;
    private final int CELL_OFFSET = 1;

    public TankPlacement(int numberOfTanks) {
        this.numberOfTanks = numberOfTanks;
    }

    public Cell[][] placeAllTanks(boolean cheat){
        Cell[][] gameBoard = initializeBoard();
        for (int index = INITIALIZER; index < numberOfTanks; index++) {
            Tank currentTank = new Tank();
            listOfTanks.add(currentTank);
            int numberOfCell = currentTank.getTankHealth();
            Cell firstTankCell = generateRandomTankCell(gameBoard);
            currentTank.addTankCell(firstTankCell);
            growTankCell(firstTankCell, gameBoard);
        }
        return gameBoard;
    }

    public Cell[][] initializeBoard(){
        Cell[][] gameBoard = new Cell[GRID_DIMENSION][GRID_DIMENSION];
        //Set coordinate for all Cell within the gameBoard
        for (int row = INITIALIZER; row < GRID_DIMENSION; row++){
            for (int column = INITIALIZER; column < GRID_DIMENSION; column++){
                int cellCoordinate[] = new int[]{row,column};
                gameBoard[row][column].setCellCoordinate(cellCoordinate);
            }
        }
        return gameBoard;
    }

    private void growTankCell(Cell firstTankCell, Cell[][] gameBoard) {
        Cell currentTankCell = firstTankCell;
        for (int cellPosition = INITIALIZER; cellPosition < MAX_NUMBER_OF_TANK_CELL; cellPosition++) {
            ArrayList<Cell> adjacentCells = getAdjacentCells(currentTankCell, gameBoard);
        }
    }

    private Cell generateRandomTankCell(Cell[][] gameBoard){
        int firstCellRow;
        int firstCellColumn;
        Cell currentCell;
        do {
            firstCellRow = (int) (Math.random() * MAX_CELL_VALUE);
            firstCellColumn = (int) (Math.random() * MAX_CELL_VALUE);
            currentCell = gameBoard[firstCellRow][firstCellColumn];
        } while (firstCellRow == GRID_DIMENSION || firstCellColumn == GRID_DIMENSION || currentCell.isTankCell(listOfTanks,currentCell));
        return currentCell;
    }

    private ArrayList<Cell> getAdjacentCells(Cell currentTankCell, Cell[][] gameBoard) {
        int horizontalCoordinate = currentTankCell.getHorizontalCoordinate();
        int verticalCoordinate = currentTankCell.getVerticalCoordinate();
        if (isValidCell(gameBoard[horizontalCoordinate + CELL_OFFSET][verticalCoordinate])){

        }
    }

    private boolean isValidCell(Cell gameBoard){
        if (gameBoard.isTankCell(listOfTanks,gameBoard)){
            return false;
        }
        if (gameBoard.)
    }

    private void placeTank(){

    }

    public ArrayList<Tank> getListOfTanks() {
        return listOfTanks;
    }

    public int getNumberOfTanks() {
        return numberOfTanks;
    }
}
