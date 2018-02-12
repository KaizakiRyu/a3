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
        this.gameBoard = new Cell[GRID_DIMENSION][GRID_DIMENSION];
        this.listOfTanks = new TankPlacement(numberOfTanks);
        this.gameBoard = listOfTanks.placeAllTanks(cheat);
    }
}
