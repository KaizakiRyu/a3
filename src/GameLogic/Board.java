package GameLogic;

public class Board {
    private boolean cheat;
    private Fortress fortress;
    private TankPlacement listOfTanks;
    private Cell[][] gameBoard;
    private AttackCell currentPlayerAttack;
    private final int GRID_DIMENSION = 10;
    private final int INITIALIZER = 0;

    public Board(int numberOfTanks, boolean cheat) {
        if (cheat){
            this.cheat = true;
        }
        this.fortress = new Fortress();
        this.listOfTanks = new TankPlacement(numberOfTanks);
        this.gameBoard = initializeBoard();
        this.gameBoard = listOfTanks.placeAllTanks(cheat,this.gameBoard);
    }

    private Cell[][] initializeBoard(){
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
}
