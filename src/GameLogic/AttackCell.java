package GameLogic;


//This class checks if the targetted cell is a tank, changes the targetted cell to be destroyed,
//and determines the result, whether it was a hit or miss.

public class AttackCell {
    private int[] targetingCell;
    private boolean result;

    public AttackCell(int[] targetingCell) {
        this.targetingCell = targetingCell;
        this.result = NULL;
    }

    //move this function to userinput class
    //convert string to arrraylist (position on gameboard)
    private int[] toCellPosition(String targetingCell) {
        int[] position;

        //get the first character from targetingCell
        char firstChar = targetingCell.charAt(0);

        switch (firstChar) {
            case "a":
            case "A":
                position[0] = 0;
                break;

            case "b":
            case "B":
                position[0] = 1;
                break;

            case "c":
            case "C":
                position[0] = 2;
                break;

            case "d":
            case "D":
                position[0] = 3;
                break;

            case "e":
            case "E":
                position[0] = 4;
                break;

            case "f":
            case "F":
                position[0] = 5;
                break;

            case "g":
            case "G":
                position[0] = 6;
                break;

            case "h":
            case "H":
                position[0] = 7;
                break;

            case "i":
            case "I":
                position[0] = 8;
                break;

            case "j":
            case "J":
                position[0] = 9;
                break;
        }

        position[1] = Character.getNumericValue(targetingCell.charAt(1));

        return position;
    }

    //take in gameboard, and check if the targetingCell exists in our gameboard
    public Cell searchCell(Cell[][] gameBoard) {
        int[] cellPosition = toCellPosition(this.targetingCell);
        //check if the cell exists in our gameboard
        return gameBoard[cellPosition[0]][cellPosition[1]];
    }

    //checks if the targetted cell is a tank and whether it was a hit or miss
    public boolean isHit(Cell currentCell) {
        boolean hit = currentCell.isTankCell();
        if(hit == true) {
            return true;
        else {
                return false;
        }
    }


    //changes isDestroyed to true for the indicated cell since it is targetted.
    public void updateCell(Cell currentCell) {
        //change the attacked cell to
        currentCell.setDestroyed(true);
    }

    //getters and setters
    public boolean getResult() {
        return this.result;
    }

    public void setResult(boolean newResult) {
        this.result = newResult;
    }

    public String getTargetingCell() {
        return this.targetingCell;
    }

    public void setTargetingCell(String newTargetingCell) {
        this.targetingCell =  newTargetingCell;
    }






}
