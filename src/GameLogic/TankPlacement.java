package GameLogic;

import java.util.ArrayList;

/**
 * Class TankPlacement is responsible for placing all Tanks and return
 * a list of tank
 */

public class TankPlacement {
    private ArrayList<Tank> listOfTanks;
    private boolean cheat;
    private int numberOfTanks;
    private final int INITIALIZER = 0;
    private final int GRID_DIMENSION = 10;
    private final int MAX_CELL_VALUE = 9;
    private final int MIN_CELL_VALUE = 0;
    private final int MAX_NUMBER_OF_TANK_CELL = 4;
    private final int CELL_OFFSET = 1;
    private final int NUM_DIFF_SHAPES = 7;
    private final int STARTING_SHAPE = 1;
    private final int T_SHAPE = 7;
    private final int TANK_NUMBERING_OFFSET = 1;

    public TankPlacement(int numberOfTanks, boolean cheat) {
        this.numberOfTanks = numberOfTanks;
        this.cheat = cheat;
    }

    public int randomNum(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    // place all tanks into the game board
    public void placeAllTanks(Cell[][] gameBoard){
        for (int index = INITIALIZER; index < numberOfTanks; index++) {
            Tank currentTank = new Tank(index + TANK_NUMBERING_OFFSET);
            listOfTanks.add(currentTank);
            Cell firstTankCell = generateRandomTankCell(gameBoard);
            currentTank.addTankCell(firstTankCell);
            growTankCell(firstTankCell, gameBoard, currentTank);
        }
    }

    private void growTankCell(Cell firstTankCell, Cell[][] gameBoard, Tank currentTank) {
        Cell currentTankCell = firstTankCell;
        ArrayList<Cell> adjacentCells;
        int tankShape = randomNum(STARTING_SHAPE,NUM_DIFF_SHAPES);
        for (int cellPosition = INITIALIZER; cellPosition < MAX_NUMBER_OF_TANK_CELL; cellPosition++) {
            adjacentCells = getAdjacentCells(currentTankCell, gameBoard);
            if (tankShape == T_SHAPE){
                if (!placeTShape(adjacentCells, currentTankCell, gameBoard, currentTank)){
                    break;
                }
            }
            int nextCell = (int) Math.ceil(Math.random() * (adjacentCells.size() - CELL_OFFSET));
            currentTankCell = adjacentCells.get(nextCell);
            currentTank.addTankCell(currentTankCell);
        }
    }

    private boolean placeTShape(ArrayList<Cell> adjacentCells, Cell currentCell, Cell[][] gameBoard, Tank currentTank){
        int[] currentPosition = currentCell.getCellCoordinate();

        //arrays of coordinates that surrounds the target cell that can make a tShape
//        int[][] tShapeLeft = new int[3][2];
//        int[][] tShapeTop = new int[3][2];
//        int[][] tShapeRight = new int[3][2];
//        int[][] tShapeBottom = new int[3][2];

        //coordinates of all adjacent Cells
        int [][] adjCellCoords = new int[4][2];
        int index = INITIALIZER;
        //populate adjCellCoords array
        for(Cell currentAdjacentCell : adjacentCells) {
            adjCellCoords[index][0] = currentAdjacentCell.getVerticalCoordinate();
            adjCellCoords[index][1] = currentAdjacentCell.getHorizontalCoordinate();
            index++;
        }

        int[][] tShapeLeft ={  { currentPosition[0] - 1, currentPosition[1]}, //bottom cell
                            { currentPosition[0], currentPosition[1] - 1}, //left cell
                            { currentPosition[0] + 1, currentPosition[1]}//top cell
        };

        int[][] tShapeTop = {   { currentPosition[0], currentPosition[1] - 1}, //left cell
                            { currentPosition[0] + 1, currentPosition[1] },//top cell
                            { currentPosition[0], currentPosition[1] + 1 } //right cell
        };

        int[][] tShapeRight = { { currentPosition[0] - 1, currentPosition[1]}, //bottom celll
                            { currentPosition[0], currentPosition[1] + 1}, //right cell
                            { currentPosition[0] + 1, currentPosition[1] }//top cell
        };

        int[][] tShapeBottom = { { currentPosition[0], currentPosition[1] - 1}, //left cell
                            { currentPosition[0] - 1, currentPosition[1] },//bottom cell
                            { currentPosition[0], currentPosition[1] + 1 } //right cell
        };


        // check to see if the tshape is compatible with our validated adj cells
        // if true, then place tshape in that formation and return true
        // else return false if all cases do not work

        if(isSubset(adjCellCoords, tShapeLeft)) {
            return placeTshapeHelper(tShapeLeft, gameBoard, currentTank);
        }
        else if(isSubset(adjCellCoords,tShapeTop)) {
            return placeTshapeHelper(tShapeTop, gameBoard, currentTank);
        }
        else if(isSubset(adjCellCoords, tShapeRight)) {
            return placeTshapeHelper(tShapeRight, gameBoard, currentTank);
        }
        else if(isSubset(adjCellCoords, tShapeBottom)) {
            return placeTshapeHelper(tShapeBottom, gameBoard, currentTank);
        }
        else {
            return false;
        }

    }

    //checks if tShape is a subset of adjCellCoords
    private boolean isSubset(int adjCellCoords[][], int tShape[][])
    {
        int i = 0;
        int j = 0;
        int counter = 0;

        //loop through tshape, for each tshape, check the possible coords and see if there is a match
        for (i=0; i< tShape.length; i++)
        {
            for (j = 0; j<adjCellCoords.length; j++)
            {
                if(tShape[i][0] == adjCellCoords[j][0] && tShape[i][1] == adjCellCoords[j][1]) {
                    counter++;
                }
            }
        }

        //if all the outer cells of the tShape are confirmed to be valid adjacent cells, return true,
            //else return false
        if(counter == tShape.length) {
            return true;
        }
        else {
            return false;
        }
    }

    // places tanks in the specified tshape formation, onto the gameboard
    private boolean placeTshapeHelper(int[][] tShape, Cell[][] gameBoard, Tank currentTank) {
        //loop through each coordinate in tShape
        //place new tank onto the gameboard with the given coordinates
        //insert the tank cell into our current tank
        for(int i = 0; i < tShape.length; i++) {
            //set the cell coordinates of this cell
            int element1 = tShape[i][0];
            int element2 = tShape[i][1];
            int[] thisCellCoord = {element1,element2};
            gameBoard[tShape[i][0]][tShape[i][1]].setCellCoordinate(thisCellCoord);
            currentTank.addTankCell(gameBoard[tShape[i][0]][tShape[i][1]]);
        }

        boolean flag = true;
        //check that the tshape coordinates is not null;
        for(int i = 0; i < tShape.length; i++) {
            if(gameBoard[tShape[i][0]][tShape[i][1]].getCellCoordinate() == null
                    || currentTank.getListOfTankCell() == null) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    //Get the first cell of a tank
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

    //get all adjacent cells
    //then return all cells that are in range and not occupied by other Tanks
    private ArrayList<Cell> getAdjacentCells(Cell currentTankCell, Cell[][] gameBoard) {
        ArrayList<Cell> validAdjacentCell = new ArrayList<>();
        ArrayList<Cell> allAdjacentCell = new ArrayList<>();
        int currentTankCellHorizontalCoordinate = currentTankCell.getHorizontalCoordinate();
        int currentTankCellVerticalCoordinate = currentTankCell.getVerticalCoordinate();

        if (isValidCell(currentTankCellHorizontalCoordinate + CELL_OFFSET, currentTankCellVerticalCoordinate)){
            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate + CELL_OFFSET][currentTankCellVerticalCoordinate]);
        }
        if (isValidCell(currentTankCellHorizontalCoordinate - CELL_OFFSET, currentTankCellVerticalCoordinate)){
            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate - CELL_OFFSET][currentTankCellVerticalCoordinate]);
        }
        if (isValidCell(currentTankCellHorizontalCoordinate, currentTankCellVerticalCoordinate + CELL_OFFSET)){
            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate][currentTankCellVerticalCoordinate + CELL_OFFSET]);
        }
        if (isValidCell(currentTankCellHorizontalCoordinate, currentTankCellVerticalCoordinate - CELL_OFFSET)){
            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate][currentTankCellVerticalCoordinate - CELL_OFFSET]);
        }
        for (Cell currentCell : allAdjacentCell){
            if (!currentCell.isTankCell(listOfTanks,currentTankCell)){
                validAdjacentCell.add(currentCell);
            }
        }
        return validAdjacentCell;
    }

    private boolean isValidCell(int horizontalCoordinate, int verticalCoordinate){
//        //check top Cell
//        if (verticalCoordinate + CELL_OFFSET > MAX_CELL_VALUE){
//            return false;
//        }
//        //check right cell
//        if (horizontalCoordinate + CELL_OFFSET > MAX_CELL_VALUE){
//            return false;
//        }
//        //check bottom cell
//        if (verticalCoordinate - CELL_OFFSET < MIN_CELL_VALUE){
//            return false;
//        }
//        //check left cell
//        if (horizontalCoordinate - CELL_OFFSET < MIN_CELL_VALUE){
//            return false;
//        }

        if (horizontalCoordinate < MIN_CELL_VALUE || horizontalCoordinate > MAX_CELL_VALUE){
            return false;
        }
        if (verticalCoordinate < MIN_CELL_VALUE || verticalCoordinate > MAX_CELL_VALUE){
            return false;
        }
        return true;
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
