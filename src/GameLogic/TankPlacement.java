package GameLogic;

import java.lang.reflect.Array;
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
                            //keep generating a random coordinate until the chosen coordinate is not a tank cell.
                            int flag1 = 0; //flag that triggers when the chosen coordinate is not a tank cell.

                            while(flag1 == 0) {
                                firstTankCoord[0] = generateRandomTankCell(gameBoard).getCellCoordinate()[0]; //designate firstTankCoord as a random valid tank cell on the board
                                firstTankCoord[1] = generateRandomTankCell(gameBoard).getCellCoordinate()[1]; //designate firstTankCoord as a random valid tank cell on the board

                                if(gameBoard[firstTankCoord[0]][firstTankCoord[1]].isTankCell() == false) {
                                    flag1 = 1;
                                    System.out.println("Successfully found a random coordinate that is not a tank cell.");
                                }
                                else {
                                    System.out.println("Error, firstTankCell found is already a tank cell, randomly selecting another coordinate");
                                }
                            }

                            growTankCell(firstTankCoord, gameBoard, currentTank); //grow the tank starting from the random cell we have initiated
                        }while (currentTank.getListOfTankCell().size() != 4); //if the size of this tank is not 4, then make a new tank
                    }
                };

                Future<?> f = service.submit(r);

                f.get(5, TimeUnit.SECONDS);     // attempt the task for 60 seconds
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

        System.out.println("Started growTankCell");

        int tankShape = randomNum(STARTING_SHAPE,NUM_DIFF_SHAPES); //determine whether to create a regular tank or a t-shape

//        int[][] listOfCoords = new int[4][2]; //2d array that stores the list of coordinates that we have iterated over
//
//        //save firstTankCell into listOfCoords
//        listOfCoords[0][0] = firstTankCell[0];
//        listOfCoords[0][1] = firstTankCell[1];

        ArrayList<int[]> listOfCoords = new ArrayList<>();
        listOfCoords.add(firstTankCell);

        int[] currentCoord = new int[2]; //2d array that stores the current coordinates that is being grown
        currentCoord = firstTankCell;

//        ArrayList<int[]> validAdjacentCoords;
        Cell currentCell;
        int[] lastCoord = new int[2]; //2d array that stores the last coordinate that was travelled from when growing tank
        lastCoord[0] = -2; //set lastCoord to an impossible coordinate on gameBoard as a starting value
        lastCoord[1] = -2;
        ArrayList<Cell> validAdjacentCells;

        Cell nextCell;
        //System.out.println("blah");
        Cell lastCell; //initialize lastCall as an impossible value to start
        lastCell = null;
        //System.out.println("blah2");


        for(int i = 1; i < 4; i++) {
            System.out.println("Growing Tank... i = " + i);
            currentCell = gameBoard[currentCoord[0]][currentCoord[1]]; //find currentcell using currentcoord
            validAdjacentCells = getValidAdjacentCells(currentCell, gameBoard); //find an arraylist of adjacent cells, list of adjacenet cells that arent tank cells and are within bounds of the gameboard

            //search validAdjacentCells for the last Cell that we grew our tank from and remove it from our array of valid cells
            validAdjacentCells.remove(lastCell);

            if(validAdjacentCells.isEmpty()) { //if there are no valid adjacent cells, break out of the loop.
                break;
            }

            //set our lastCell to our currentCell for the next loop
            lastCell = currentCell;

            //set our lastCoord as our currentCoord for next loop
            lastCoord[0] = currentCoord[0];
            lastCoord[1] = currentCoord[1];

            int nextCellIndex;

            //generate random number from 0 to (validAdjacentCells.size() - 1)
            nextCellIndex = randomNum(0, validAdjacentCells.size() - 1); //a random index of validAdjacentCells



            nextCell = validAdjacentCells.get(nextCellIndex); // our chosen random cell
            int[] nextCoord = nextCell.getCellCoordinate(); //the coordinates of our chosen random cell

            //set our currentCoord as nextCoord that we have just found for next loop
            currentCoord = nextCoord;

            //append our listofcoords with our currentcoord
//            listOfCoords[i][0] = currentCoord[0];
//            listOfCoords[i][1] = currentCoord[1];

            listOfCoords.add(currentCoord);
        }

        if(listOfCoords.size() < 4) {
            System.out.println("Did not grow tank, listOfCoords.length < 0");

            return false;

        }
        else {
            //save stuff

            //loop through listOfCoords and find the cell in gameboard
            for(int i = 0; i < listOfCoords.size(); i++) {
                gameBoard[ listOfCoords.get(i)[0] ][ listOfCoords.get(i)[1] ].setId(currentTank.getTankID());
                gameBoard[ listOfCoords.get(i)[0] ][ listOfCoords.get(i)[1] ].setTankCell(true);

                //save listoftank cells for the currentTank

                currentTank.getListOfTankCell().add(gameBoard[ listOfCoords.get(i)[0] ][ listOfCoords.get(i)[1] ]);
            }

            System.out.println("Placed tank with ID: " + currentTank.getTankID() + " at");
            System.out.print("Cell coords: ");
            for(Cell tankCell: currentTank.getListOfTankCell()) {
                System.out.print("[" + tankCell.getHorizontalCoordinate() + "][" + tankCell.getVerticalCoordinate() + "], ");

            }
            System.out.println();

            return true;
        }

    }
    // pick a cell to place the t-shape
    // get a list of adjacent coordinates
    // get a list of adjacent coordinates that make up top, right, bottom, left t-shape formations
    // assess the list of adjacent coordinates and return a list of valid coordinates
    // find the corresponding cells for the validAdjCoords and store in arraylist
    // check if the validAdjCells are tanks, if true, remove from the arraylist
    // convert the validAdjCells to coordinates
    // for each of the t-shape formations' coordinates, loop through and check if all of the coordinates are in validAdjCoords,
    //  if false, then that t-shape is invalid
    // if all t-shapes are invalid, return false
    // if there is a valid t-shape, place that t-shape and return true
    // parameters: currentCell, gameBoard, currentTank
    private boolean placeTShape(Cell currentCell, Cell[][] gameBoard, Tank currentTank){
//        // pick the coordinate to place the t-shape
//        int[] currentPosition = currentCell.getCellCoordinate();
//
//        //arrays of coordinates that surrounds the target cell that can make a tShape
////        int[][] tShapeLeft = new int[3][2];
////        int[][] tShapeTop = new int[3][2];
////        int[][] tShapeRight = new int[3][2];
////        int[][] tShapeBottom = new int[3][2];
//
//        // get a list of all valid adjacent cells of the current coordinate
//        // adjacent cells that are not occupied by a tank and are within bounds
//        ArrayList<Cell> validAdjCells = getValidAdjacentCells(currentCell, gameBoard);
//
//        //if there are only two validAdjCells, then break and return false, it is impossible to create a t-shape from this spot.
//        if(validAdjCells.size() < 3) {
//            System.out.println("Error, cannot create a t-shape with only two adj cells.");
//            return false;
//        }
//
//
//        //coordinates of all adjacent cells
//        ArrayList<int[]> adjCellCoords = new ArrayList<>();
//
//        //populate adjCellCoords ArrayList
//        for(Cell currentAdjCell : validAdjCells) {
//            int[] currentCoord = new int[2];
//            currentCoord[0] = currentAdjCell.getHorizontalCoordinate();
//            currentCoord[1] = currentAdjCell.getVerticalCoordinate();
//
//            adjCellCoords.add(currentCoord);
//        }
//
//
//        ArrayList<int[]> tShapeLeft = new ArrayList<>();
//        int[] tempCoord = new int[2];
//
//        //bottom cell
//        tempCoord[0] = currentPosition[0] - 1;
//        tempCoord[1] = currentPosition[1];
//        tShapeLeft.add(tempCoord);
//
//        //left cell
//        tempCoord[0] = currentPosition[0];
//        tempCoord[1] = currentPosition[1] - 1;
//        tShapeLeft.add(tempCoord);
//
//        tempCoord[0] = currentPosition[0] + 1;
//        tempCoord[1] = currentPosition[1];
//        tShapeLeft.add(tempCoord);
//
//
//
//
//
//        tShapeLeft.add(currentPosition[0] - 1, currentPosition[1]); //bottom cell
//        tShapeLeft.add( { currentPosition[0], currentPosition[1] - 1} ); //left cell
//        tShapeLeft.add( { currentPosition[0] + 1, currentPosition[1]} ); //top cell
//
//        ArrayList<int[]> tShapeTop = new ArrayList<>();
//        tShapeTop.add( { currentPosition[0], currentPosition[1] - 1} ); //left cell
//        tShapeTop.add( { currentPosition[0] + 1, currentPosition[1] } ); //top cell
//        tShapeTop.add( { currentPosition[0], currentPosition[1] + 1 } ); //right cell
//
//
//
//        int[][] tShapeTop = {   { currentPosition[0], currentPosition[1] - 1}, //left cell
//                            { currentPosition[0] + 1, currentPosition[1] },//top cell
//                            { currentPosition[0], currentPosition[1] + 1 } //right cell
//        };
//
//        int[][] tShapeRight = { { currentPosition[0] - 1, currentPosition[1]}, //bottom celll
//                            { currentPosition[0], currentPosition[1] + 1}, //right cell
//                            { currentPosition[0] + 1, currentPosition[1] }//top cell
//        };
//
//        int[][] tShapeBottom = { { currentPosition[0], currentPosition[1] - 1}, //left cell
//                            { currentPosition[0] - 1, currentPosition[1] },//bottom cell
//                            { currentPosition[0], currentPosition[1] + 1 } //right cell
//        };
//
//
//        // check to see if the tshape is compatible with our validated adj cells
//        // if true, then place tshape in that formation and return true
//        // else return false if all cases do not work
//
//        if(isSubset(adjCellCoords, tShapeLeft)) {
//            return placeTshapeHelper(tShapeLeft, gameBoard, currentTank);
//        }
//        else if(isSubset(adjCellCoords,tShapeTop)) {
//            return placeTshapeHelper(tShapeTop, gameBoard, currentTank);
//        }
//        else if(isSubset(adjCellCoords, tShapeRight)) {
//            return placeTshapeHelper(tShapeRight, gameBoard, currentTank);
//        }
//        else if(isSubset(adjCellCoords, tShapeBottom)) {
//            return placeTshapeHelper(tShapeBottom, gameBoard, currentTank);
//        }
//        else {
//            return false;
//        }

        return true;
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
        Cell currentCell = new Cell();

        int flag = 0; //flag that activates when the cell picked is not a tank cell, then the while loop exists
        while(flag == 0) {
            firstCellRow = randomNum(0, MAX_CELL_VALUE);
            firstCellColumn = randomNum(0, MAX_CELL_VALUE);
            currentCell = gameBoard[firstCellRow][firstCellColumn];
            if(currentCell.isTankCell() == false) {
                flag = 1;
            }
        }

        return currentCell;
    }


    //then return all cells that are in range and not occupied by other Tanks
    //parameters: currentTankCell, gameBoard
    private ArrayList<Cell> getValidAdjacentCells(Cell currentTankCell, Cell[][] gameBoard) {
        ArrayList<Cell> validAdjacentCells = new ArrayList<>(); //arraylist of validadjacent cells
//        ArrayList<Cell> allAdjacentCell = new ArrayList<>(); //arraylist of alladjacent cells
//        int currentTankCellHorizontalCoordinate = currentTankCell.getHorizontalCoordinate();
//        int currentTankCellVerticalCoordinate = currentTankCell.getVerticalCoordinate();
//
//        if (isValidCell(currentTankCellHorizontalCoordinate + CELL_OFFSET, currentTankCellVerticalCoordinate)){
//            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate + CELL_OFFSET][currentTankCellVerticalCoordinate]);
//        }
//        if (isValidCell(currentTankCellHorizontalCoordinate - CELL_OFFSET, currentTankCellVerticalCoordinate)){
//            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate - CELL_OFFSET][currentTankCellVerticalCoordinate]);
//        }
//        if (isValidCell(currentTankCellHorizontalCoordinate, currentTankCellVerticalCoordinate + CELL_OFFSET)){
//            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate][currentTankCellVerticalCoordinate + CELL_OFFSET]);
//        }
//        if (isValidCell(currentTankCellHorizontalCoordinate, currentTankCellVerticalCoordinate - CELL_OFFSET)){
//            allAdjacentCell.add(gameBoard[currentTankCellHorizontalCoordinate][currentTankCellVerticalCoordinate - CELL_OFFSET]);
//        }
//        for (Cell currentCell : allAdjacentCell){
//            if (!currentCell.isTankCell()){
//                validAdjacentCell.add(currentCell);
//            }
//        }
//        return validAdjacentCell;

        ArrayList<Cell> allAdjacentCells = getAllAdjacentCells(currentTankCell, gameBoard);//arraylist of alladjacent cells

        //loop through allAdjacentCells and check which ones are valid, if valid, add to validAdjacentCell arraylist

        for(Cell adjacentCell: allAdjacentCells) {
            int flag = 0; //flag that is triggered when the cell is invalid (is out of bounds or is a tank cell)

            if(adjacentCell.isTankCell() || !isValidCell(adjacentCell.getHorizontalCoordinate(), adjacentCell.getVerticalCoordinate())) {

                if(adjacentCell.isTankCell() && !isValidCell(adjacentCell.getHorizontalCoordinate(), adjacentCell.getVerticalCoordinate())) {
                    System.out.println("This is an impossible case.");
                }
                else if(adjacentCell.isTankCell()) {
                    System.out.println("Spot [" + adjacentCell.getHorizontalCoordinate() + "][" + adjacentCell.getVerticalCoordinate() + "] was taken up by a tank cell.");
                }
                else if(!isValidCell(adjacentCell.getHorizontalCoordinate(), adjacentCell.getVerticalCoordinate())) {
                    System.out.println("Spot [" + adjacentCell.getHorizontalCoordinate() + "][" + adjacentCell.getVerticalCoordinate() + "] is invalid.");
                }
                flag = 1;
            }

            if(flag == 0) {
                validAdjacentCells.add(adjacentCell);
            }

        }
        return validAdjacentCells;
    }

    //get all adjacent cells, given a cell and the gameboard
    private ArrayList<Cell> getAllAdjacentCells(Cell currentTankCell, Cell[][] gameBoard) {
        ArrayList<Cell> allAdjacentCells = new ArrayList<>(); //arraylist of alladjacent cells
        int rowCoord = currentTankCell.getHorizontalCoordinate();
        int colCoord = currentTankCell.getVerticalCoordinate();

        //rowCoord + 1
        if(rowCoord + 1 < 10 && rowCoord + 1 >= 0) {
            allAdjacentCells.add(gameBoard[rowCoord + 1][colCoord]);
        }
        else {
            System.out.println("rowCoord = " + (rowCoord + 1) + "is out of bounds");
        }

        //rowCoord - 1
        if(rowCoord - 1 < 10 && rowCoord - 1 >= 0) {
            allAdjacentCells.add(gameBoard[rowCoord - 1][colCoord]);
        }
        else {
            System.out.println("rowCoord = " + (rowCoord - 1) + "is out of bounds");
        }

        //colCoord + 1
        if(colCoord + 1 < 10 && colCoord + 1 >= 0) {
            allAdjacentCells.add(gameBoard[rowCoord][colCoord + 1]);
        }
        else {
            System.out.println("rowCoord = " + (colCoord + 1) + "is out of bounds");
        }

        //colCoord - 1
        if(colCoord - 1 < 10 && colCoord - 1 >= 0) {
            allAdjacentCells.add(gameBoard[rowCoord][colCoord - 1]);
        }
        else {
            System.out.println("rowCoord = " + (colCoord - 1) + "is out of bounds");
        }

        System.out.println("All Adjacent Cells = " + allAdjacentCells.toString());

        return allAdjacentCells;
    }

        //returns true if the cell is within bounds, false otherwise
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
        return this.listOfAliveTanks;
    }

    public ArrayList<Tank> getListOfTanks() {
        return this.listOfTanks;
    }

    public int getNumberOfTanks() {
        return numberOfTanks;
    }
}
