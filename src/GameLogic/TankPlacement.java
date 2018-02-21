package GameLogic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Class TankPlacement is responsible for placing all Tanks and return
 * a list of tank
 */

public class TankPlacement {
    private ArrayList<Tank> listOfTanks;
    private ArrayList<Tank> listOfAliveTanks;
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
    private final int ERROR_CODE = -1;
    private final long TANK_PLACEMENT_WAIT_TIME = 5;

    public TankPlacement(int numberOfTanks) {
        this.listOfTanks = new ArrayList<>();
        this.numberOfTanks = numberOfTanks;
    }

    public int randomNum(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public void placeAllTanks(Cell[][] gameBoard){
        System.out.println("Placing Tanks");
        for (int index = INITIALIZER; index < numberOfTanks; index++) {
            char tankId = (char) (index + TANK_NUMBERING_OFFSET);
            Tank currentTank = new Tank(tankId);
            listOfTanks.add(currentTank);
            ExecutorService service = Executors.newSingleThreadExecutor();
            try {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        int[] firstTankCoord = new int[2];
                        do {
                            int flag1 = 0;
                            while(flag1 == 0) {
                                firstTankCoord[0] = generateRandomTankCell(gameBoard).getCellCoordinate()[0];
                                firstTankCoord[1] = generateRandomTankCell(gameBoard).getCellCoordinate()[1];

                                if(gameBoard[firstTankCoord[0]][firstTankCoord[1]].isTankCell() == false) {
                                    flag1 = 1;
                                }
                            }
                            growTank(firstTankCoord, gameBoard, currentTank);
                        }while (currentTank.getListOfTankCell().size() != 4);
                    }
                };
                Future<?> f = service.submit(r);
                f.get(TANK_PLACEMENT_WAIT_TIME, TimeUnit.SECONDS);
            } catch (final InterruptedException | TimeoutException | ExecutionException e) {
                System.err.println("Error: Unable to place all tanks");
                System.exit(ERROR_CODE);
            }
            finally {
                service.shutdown();
            }
        }
        this.listOfAliveTanks = this.listOfTanks;
    }

    private boolean growTank(int[] firstTankCell, Cell[][] gameBoard, Tank currentTank) {
        int tankShape = randomNum(STARTING_SHAPE,NUM_DIFF_SHAPES); //determine whether to create a regular tank or a t-shape

        //System.out.println("tankShape integer = " + tankShape);

        if(tankShape == 7) {
            //create tshape
            //System.out.println("Creating t-shape.");

            Cell currentCell = gameBoard[firstTankCell[0]][firstTankCell[1]];
            boolean createdTShape;

            // placeTShape around currentCell,
            // if it is impossible to place a tshape tank at this location, return false
            // else, return true and place the tank in its appropriate t-shape formation
            createdTShape = placeTShape(currentCell, gameBoard, currentTank);

            return createdTShape;
        }
        else {
            //System.out.print("Creating normal shape");

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
                //System.out.println("Growing Tank... i = " + i);
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
                //System.out.println("Did not grow tank, listOfCoords.length < 0");

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

                //System.out.println("Placed tank with ID: " + currentTank.getTankID() + " at");
                //System.out.print("Cell coords: ");
                for(Cell tankCell: currentTank.getListOfTankCell()) {
                    //System.out.print("[" + tankCell.getHorizontalCoordinate() + "][" + tankCell.getVerticalCoordinate() + "], ");

                }
                System.out.println();

                return true;
            }
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
        //System.out.println("Started placeTShape");

        // pick the coordinate to place the t-shape
        int[] currentPosition = new int[2];
        currentPosition[0] = currentCell.getHorizontalCoordinate();
        currentPosition[1] = currentCell.getVerticalCoordinate();


        // get a list of all valid adjacent cells of the current coordinate
        // adjacent cells that are not occupied by a tank and are within bounds
        ArrayList<Cell> validAdjCells = getValidAdjacentCells(currentCell, gameBoard);

        //if there are only two validAdjCells, then break and return false, it is impossible to create a t-shape from this spot.
        if(validAdjCells.size() < 3) {
            //System.out.println("Error, cannot create a t-shape with only two adj cells.");
            return false;
        }
        else {
            //System.out.println("There are sufficient adjacent cells to create a t-shape, continuing");
        }


        //coordinates of all adjacent cells
        ArrayList<int[]> adjCellCoords = new ArrayList<>();

        //populate adjCellCoords ArrayList
        //System.out.println("Populating adjCellCoords");
        for(Cell currentAdjCell : validAdjCells) {
            int[] currentCoord = new int[2];
            currentCoord[0] = currentAdjCell.getHorizontalCoordinate();
            currentCoord[1] = currentAdjCell.getVerticalCoordinate();

            adjCellCoords.add(currentCoord);
        }

       // System.out.println("Populated adjCellCoords");
        //System.out.print("adjCellCoords is of size: " + adjCellCoords.size());
        //System.out.println();

        int[] tempCoord1 = new int[2];
        int[] tempCoord2 = new int [2];
        int[] tempCoord3 = new int [2];
        int[] tempCoord4 = new int [2];
        int[] tempCoord5 = new int [2];
        int[] tempCoord6 = new int [2];
        int[] tempCoord7 = new int [2];
        int[] tempCoord8 = new int [2];
        int[] tempCoord9 = new int [2];
        int[] tempCoord10 = new int [2];
        int[] tempCoord11 = new int [2];
        int[] tempCoord12 = new int [2];




        ArrayList<int[]> tShapeLeft = new ArrayList<>();


            //bottom cell
            tempCoord1[0] = currentPosition[0] - 1;
            tempCoord1[1] = currentPosition[1];
            tShapeLeft.add(tempCoord1);

            //left cell
            tempCoord2[0] = currentPosition[0];
            tempCoord2[1] = currentPosition[1] - 1;
            tShapeLeft.add(tempCoord2);

            //top cell
            tempCoord3[0] = currentPosition[0] + 1;
            tempCoord3[1] = currentPosition[1];
            tShapeLeft.add(tempCoord3);

//            System.out.println("The size of tShapeLeft = " + tShapeLeft.size());
//
//            System.out.print("tShapeLeft Coords: ");
//            for(int[] this_coord : tShapeLeft) {
//                System.out.print("[" + this_coord[0] + "][" + this_coord[1] + "], ");
//            }
//            System.out.println();


        ArrayList<int[]> tShapeTop = new ArrayList<>();

            //left cell
            tempCoord4[0] = currentPosition[0];
            tempCoord4[1] = currentPosition[1] - 1;
            tShapeTop.add(tempCoord4);

            //top cell
            tempCoord5[0] = currentPosition[0] + 1;
            tempCoord5[1] = currentPosition[1];
            tShapeTop.add(tempCoord5);

            //right cell
            tempCoord6[0] = currentPosition[0];
            tempCoord6[1] = currentPosition[1] + 1;
            tShapeTop.add(tempCoord6);

//            System.out.println("The size of tShapeTop = " + tShapeTop.size());
//
//            System.out.print("tShapeTop Coords: ");
//            for(int[] this_coord : tShapeTop) {
//                System.out.print("[" + this_coord[0] + "][" + this_coord[1] + "], ");
//            }
//            System.out.println();


        ArrayList<int[]> tShapeRight = new ArrayList<>();

            //bottom cell
            tempCoord7[0] = currentPosition[0] - 1;
            tempCoord7[1] = currentPosition[1];
            tShapeRight.add(tempCoord7);

            //top cell
            tempCoord8[0] = currentPosition[0] + 1;
            tempCoord8[1] = currentPosition[1];
            tShapeRight.add(tempCoord8);

            //right cell
            tempCoord9[0] = currentPosition[0];
            tempCoord9[1] = currentPosition[1] + 1;
            tShapeRight.add(tempCoord9);

//            System.out.println("The size of tShapeRight = " + tShapeRight.size());
//
//            System.out.print("tShapeRight Coords: ");
//            for(int[] this_coord : tShapeRight) {
//                System.out.print("[" + this_coord[0] + "][" + this_coord[1] + "], ");
//            }
//            System.out.println();


        ArrayList<int[]> tShapeBottom = new ArrayList<>();

            //bottom cell
            tempCoord10[0] = currentPosition[0] - 1;
            tempCoord10[1] = currentPosition[1];
            tShapeBottom.add(tempCoord10);

            //right cell
            tempCoord11[0] = currentPosition[0];
            tempCoord11[1] = currentPosition[1] + 1;
            tShapeBottom.add(tempCoord11);

            //left cell
            tempCoord12[0] = currentPosition[0];
            tempCoord12[1] = currentPosition[1] - 1;
            tShapeBottom.add(tempCoord12);

//            System.out.println("The size of tShapeBottom = " + tShapeBottom.size());
//
//            System.out.print("tShapeBottom Coords: ");
//            for(int[] this_coord : tShapeBottom) {
//                System.out.print("[" + this_coord[0] + "][" + this_coord[1] + "], ");
//            }
//            System.out.println();



        if(isSubset(adjCellCoords, tShapeBottom)) {
            //create bottom t-shape
            //System.out.println("tShapeBottom is a subset of adjCellCoords, setting the tshape");
            setTShape(currentPosition, tShapeBottom, gameBoard, currentTank);

            return true;
        }
        else if(isSubset(adjCellCoords, tShapeLeft)) {
            //create left t-shape
            //System.out.println("tShapeLeft is a subset of adjCellCoords, setting the tshape");
            setTShape(currentPosition, tShapeLeft, gameBoard, currentTank);
            return true;
        }
        else if(isSubset(adjCellCoords, tShapeTop)) {
            //create top t-shape
            //System.out.println("tShapeTop is a subset of adjCellCoords, setting the tshape");
            setTShape(currentPosition, tShapeTop, gameBoard, currentTank);

            return true;
        }
        else if(isSubset(adjCellCoords, tShapeRight)) {
            //create right t-shape
            //System.out.println("tShapeRight is a subset of adjCellCoords, setting the tshape");
            setTShape(currentPosition, tShapeRight, gameBoard, currentTank);

            return true;
        }
        else {

            //System.out.println("None of the tshape formations is a subset of adjCellCoords, rerolling.");

            return false;
        }

    }

    //parameters: arr1, arr2, both int[] ArrayLists
    //checks if arr2 is a subset of arr1
    //return true if above is true, false otherwise
    private boolean isSubset(ArrayList<int[]> arr1, ArrayList<int[]>arr2) {
        //loop through each element of arr2
        //for each element of arr2, compare to every element in arr1 and check if there is a match

        int subSet = 0;

        for(int i = 0; i < arr2.size(); i++) {
            for(int j = 0; j < arr1.size(); j++) {
                if(arr2.get(i)[0] == arr1.get(j)[0] && arr2.get(i)[1] == arr1.get(j)[1]) {
                    subSet++;
                    break;
                }
            }
        }

        //check if the number of elements from arr2 that were found in arr1 is equal to the size of arr2

        //System.out.println("arr2.size() = " + arr2.size() );
        //System.out.println("subSet = " + subSet);

        if(subSet == arr2.size()) {
            return true;
        }
        else {
            return false;
        }
    }

    // parameters: ArrayList tShape
    // sets tanks on gameboard in the formation of a tshape with the coordinates from ArrayList tShape
    private void setTShape(int[] currentPosition, ArrayList<int[]> tShape, Cell[][] gameBoard, Tank currentTank) {
        // loop through tShape
        // find the coordinates of tShape
        // set each cell to tank
        // set the tankID of the cell

        gameBoard[currentPosition[0]][currentPosition[1]].setId(currentTank.getTankID());
        gameBoard[currentPosition[0]][currentPosition[1]].setTankCell(true);
        currentTank.getListOfTankCell().add(gameBoard[currentPosition[0]][currentPosition[1]]);


        for(int[] coord : tShape) {
            gameBoard[coord[0]][coord[1]].setId(currentTank.getTankID());
            gameBoard[coord[0]][coord[1]].setTankCell(true);

            currentTank.getListOfTankCell().add(gameBoard[coord[0]][coord[1]]);
        }

//        System.out.println("Placed t-shape tank with ID: " + currentTank.getTankID() + " at");
//        System.out.print("Cell coords: ");
//        for(Cell tankCell: currentTank.getListOfTankCell()) {
//            System.out.print("[" + tankCell.getHorizontalCoordinate() + "][" + tankCell.getVerticalCoordinate() + "], ");
//
//        }
//        System.out.println();

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
//

        ArrayList<Cell> allAdjacentCells = getAllAdjacentCells(currentTankCell, gameBoard);//arraylist of alladjacent cells

        //loop through allAdjacentCells and check which ones are valid, if valid, add to validAdjacentCell arraylist

        for(Cell adjacentCell: allAdjacentCells) {
            int flag = 0; //flag that is triggered when the cell is invalid (is out of bounds or is a tank cell)

            if(adjacentCell.isTankCell() || !isValidCell(adjacentCell.getHorizontalCoordinate(), adjacentCell.getVerticalCoordinate())) {

                if(adjacentCell.isTankCell() && !isValidCell(adjacentCell.getHorizontalCoordinate(), adjacentCell.getVerticalCoordinate())) {
                    //System.out.println("This is an impossible case.");
                }
                else if(adjacentCell.isTankCell()) {
                    //System.out.println("Spot [" + adjacentCell.getHorizontalCoordinate() + "][" + adjacentCell.getVerticalCoordinate() + "] was taken up by a tank cell.");
                }
                else if(!isValidCell(adjacentCell.getHorizontalCoordinate(), adjacentCell.getVerticalCoordinate())) {
                    //System.out.println("Spot [" + adjacentCell.getHorizontalCoordinate() + "][" + adjacentCell.getVerticalCoordinate() + "] is invalid.");
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
            //System.out.println("rowCoord = " + (rowCoord + 1) + " is out of bounds");
        }

        //rowCoord - 1
        if(rowCoord - 1 < 10 && rowCoord - 1 >= 0) {
            allAdjacentCells.add(gameBoard[rowCoord - 1][colCoord]);
        }
        else {
            //System.out.println("rowCoord = " + (rowCoord - 1) + " is out of bounds");
        }

        //colCoord + 1
        if(colCoord + 1 < 10 && colCoord + 1 >= 0) {
            allAdjacentCells.add(gameBoard[rowCoord][colCoord + 1]);
        }
        else {
            //System.out.println("rowCoord = " + (colCoord + 1) + " is out of bounds");
        }

        //colCoord - 1
        if(colCoord - 1 < 10 && colCoord - 1 >= 0) {
            allAdjacentCells.add(gameBoard[rowCoord][colCoord - 1]);
        }
        else {
            //System.out.println("rowCoord = " + (colCoord - 1) + " is out of bounds");
        }

//        System.out.print("All Adjacent Cells = ");
//        for(Cell adjCell: allAdjacentCells) {
//            System.out.print("[" + adjCell.getHorizontalCoordinate() + "][" + adjCell.getVerticalCoordinate() + "], ");
//        }
//        System.out.println();

        return allAdjacentCells;
    }

    //returns true if the cell is within bounds, false otherwise
    private boolean isValidCell(int horizontalCoordinate, int verticalCoordinate){


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
