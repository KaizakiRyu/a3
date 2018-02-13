package GameLogic;


//This class checks if the targetted cell is a tank, changes the targetted cell to be destroyed,
//and determines the result, whether it was a hit or miss.

import java.util.ArrayList;

public class AttackCell {
    private int[] targetingCell;
    private boolean result;

    public AttackCell(int[] targetingCell) {
        this.targetingCell = targetingCell;
    }

    //move this function to userinput class
    //convert string to arrraylist (position on gameboard)


    //take in gameboard, and check if the targetingCell exists in our gameboard
    public Cell searchCell(Cell[][] gameBoard) {
        //check if the cell exists in our gameboard
        return gameBoard[targetingCell[0]][targetingCell[1]];
    }


    //checks if the targetted cell is a tank and whether it was a hit or miss
    public boolean isHit(Cell currentCell, ArrayList<Tank> listOfTank) {
        boolean hit = currentCell.isTankCell(listOfTank,currentCell);
        if(hit == true) {
            return true;
        } else {
                return false;
        }
    }


    //changes isDestroyed to true for the indicated cell since it is targetted.
    public void updateCell(Cell currentCell) {
        //change the attacked cell to
        currentCell.setDestroyed(true);
        result = true;
    }

    //getters and setters
    public boolean getResult() {
        return this.result;
    }

    public void setResult(boolean newResult) {
        this.result = newResult;
    }

    public int[] getTargetingCell() {
        return this.targetingCell;
    }

    public void setTargetingCell(int[] newTargetingCell) {
        this.targetingCell =  newTargetingCell;
    }






}
