package GameLogic;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Class TankPlacement is responsible for placing all Tanks and return
 * a list of tank
 */

public class TankPlacement {
    private ArrayList<Tank> listOfTanks; //list of all tanks
    private ArrayList<Tank> listOfAliveTanks; //list of currently alive tanks
    private boolean cheat; //boolean that determins whether the user cheated or not
    private int numberOfTanks;
    private final int INITIALIZER = 0;
    private final int GRID_DIMENSION = 10;
    private final int MAX_CELL_VALUE = 9;
    private final int MIN_CELL_VALUE = 0;
    private final int MAX_NUMBER_OF_TANK_CELL = 3;
    private final int CELL_OFFSET = 1;
    private final int NUM_DIFF_SHAPES = 7;
    private final int STARTING_SHAPE = 1;
    private final int T_SHAPE = 7;
    private final int TANK_NUMBERING_OFFSET = 65;

    public TankPlacement(int numberOfTanks, boolean cheat) {
        this.listOfTanks = new ArrayList<>();
        this.numberOfTanks = numberOfTanks;
        this.cheat = cheat;
    }

    // parameters: min integer, max integer
    // returns a random integer in between the min and max, inclusive of min and max
    public int randomNum(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    // place all tanks into the game board
    public void placeAllTanks(Cell[][] gameBoard){
        System.out.println("Placing Tank");

        //create tanks and update the tank cells on the game board
        for (int index = INITIALIZER; index < numberOfTanks; index++) {
            char tankId = (char) (index + TANK_NUMBERING_OFFSET); //convert the numerical ID of the tank to alpha char
            Tank currentTank = new Tank(tankId); //create a new tank with the given tankID
            listOfTanks.add(currentTank); //append listOfTanks with our newly created tank
            ExecutorService service = Executors.newSingleThreadExecutor();
            try {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        int[] firstTankCoord = new int[2];
                        do {
                            firstTankCoord[0] = generateRandomTankCell(gameBoard).getCellCoordinate()[0]; //designate firstTankCoord as a random valid tank cell on the board
                            firstTankCoord[1] = generateRandomTankCell(gameBoard).getCellCoordinate()[1]; //designate firstTankCoord as a random valid tank cell on the board


                            // currentTank.addTankCell(firstTankCoord);
                            growTankCell(firstTankCoord, gameBoard, currentTank);
                        }while (currentTank.getListOfTankCell().size() != 4);
                    }
                };

                Future<?> f = service.submit(r);

                f.get(60, TimeUnit.SECONDS);     // attempt the task for 60 seconds
            } catch (final InterruptedException e) {
                // The thread was interrupted during sleep, wait or join
            }
            catch (final TimeoutException e) {
                // Took too long!
            }
            catch (final ExecutionException e) {
                // An exception from within the Runnable task
            }
            finally {
                service.shutdown();
            }
        }
        System.out.println("Done Placing");
        this.listOfAliveTanks = this.listOfTanks;
    }


    //grows a list of tankcells from firstTankCell and saves all the coordinates in an arraylist<int>
    //if the length of the arraylist<int> is == 4, iterate through each cell and set isTank to true, and set the tankID, then add to listOfTankCells fo the tank, return true
    // else return false
    private boolean growTankCell(int[] firstTankCell, Cell[][] gameBoard, Tank currentTank) {

        int tankShape = randomNum(STARTING_SHAPE,NUM_DIFF_SHAPES);

        int[][] listOfCoords = new int[4][2]; //2d array that stores the list of coordinates that we have iterated over

        //save firstTankCell into listOfCoords
        listOfCoords[0][0] = firstTankCell[0];
        listOfCoords[0][1] = firstTankCell[1];

        int[] currentCoord = new int[2];
        currentCoord = firstTankCell;

//        ArrayList<int[]> validAdjacentCoords;

        for(int i = 1; i < 4; i++) {
            Cell currentCell = gameBoard[currentCoord[0]][currentCoord[1]]; //find currentcell using currentcoord
            ArrayList<Cell> validAdjacentCells = getValidAdjacentCells(currentCell, gameBoard); //find an arraylist of adjacent cells

            if(validAdjacentCells.isEmpty()) {
                break;
            }


            //generate random number from 0 to (validAdjacentCells.size() - 1)

            int nextCellIndex = randomNum(0, validAdjacentCells.size() - 1); //a random index of validAdjacentCells
            Cell nextCell = validAdjacentCells.get(nextCellIndex); // our chosen random cell
            int[] nextCoord = nextCell.getCellCoordinate(); //the coordinates of our chosen random cell

            currentCoord = nextCoord;

            listOfCoords[i][0] = currentCoord[0];
            listOfCoords[i][1] = currentCoord[1];


        }

        if(listOfCoords.length < 4) {
            return false;
        }
        else {
            //save stuff

            //loop through listOfCoords and find the cell in gameboard
            for(int i = 0; i < listOfCoords.length; i++) {
                gameBoard[ listOfCoords[i][0] ][ listOfCoords[i][1] ].setId(currentTank.getTankID());
                gameBoard[ listOfCoords[i][0] ][ listOfCoords[i][1] ].setTankCell(true);

                //save listoftank cells for the currentTank

                currentTank.getListOfTankCell().add(gameBoard[ listOfCoords[i][0] ][ listOfCoords[i][1] ]);
            }

            return true;
        }




// Cell currentTankCell = firstTankCell;
//        ArrayList<Cell> adjacentCells;
////        int tankShape = randomNum(STARTING_SHAPE,NUM_DIFF_SHAPES);
//        for (int cellPosition = INITIALIZER; cellPosition < MAX_NUMBER_OF_TANK_CELL; cellPosition++) {
//            adjacentCells = getValidAdjacentCells(currentTankCell, gameBoard);
////            if (tankShape == T_SHAPE){
////                System.out.println("Making T shape");
////                if (!placeTShape(adjacentCells, currentTankCell, gameBoard, currentTank)){
////                    break;
////                }
////            }
////            int nextCell = (int) Math.ceil(Math.random() * (adjacentCells.size() - CELL_OFFSET));
//            if (adjacentCells.size() == 0){
//                return;
//            } else {
//                System.out.println("Making a regular one with " + currentTankCell.getId());
//                int nextCell = (int) Math.ceil(Math.random() * (adjacentCells.size() - CELL_OFFSET));
//                currentTankCell = adjacentCells.get(nextCell);
//                currentTank.addTankCell(currentTankCell);
//            }
//        }
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
            gameBoard[tShape[i][0]][tShape[i][1]].setId(currentTank.getTankID());
            gameBoard[tShape[i][0]][tShape[i][1]].setTankCell(true);
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


    //Get the first cell of a tank by randomly choosing a spot on the gameboard that is not occupied
    private Cell generateRandomTankCell(Cell[][] gameBoard){
        int firstCellRow;
        int firstCellColumn;
        Cell currentCell;

        //generate a random coordinate that is not a current Tank Cell and is within the bounds of the gameboard
        do {
            //pick random int from 0 to MAX_CELL_VALUE = 9 for row and column index
            firstCellRow = randomNum(0, MAX_CELL_VALUE);
            firstCellColumn = randomNum(0, MAX_CELL_VALUE);
            currentCell = gameBoard[firstCellRow][firstCellColumn];
        } while (currentCell.isTankCell()); //check if the cell picked is a tank, if this case is true, repick the random first cell
        return currentCell;
    }

    //get all adjacent cells
    //then return all cells that are in range and not occupied by other Tanks
    private ArrayList<Cell> getValidAdjacentCells(Cell currentTankCell, Cell[][] gameBoard) {
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
            if (!currentCell.isTankCell()){
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

    public ArrayList<Tank> getListOfAliveTanks() {
        return listOfAliveTanks;
    }

    public ArrayList<Tank> getListOfTanks() {
        return listOfTanks;
    }

    public int getNumberOfTanks() {
        return numberOfTanks;
    }
}
