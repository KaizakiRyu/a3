package GameLogic;

public class AttackCell {
    private String targetingCell;
    private boolean result;

    public AttackCell(String targetingCell) {
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

        position[1] = getNumericValue(targetingCell.charAt(1));

        return position;
    }

    //take in gameboard, and check if the targetingCell exists in our gameboard
    public Cell searchCell(Cell[][] gameBoard) {
        int[] cellPosition = toCellPosition(this.targetingCell);
        Cell currentCell = gameBoard[cellPosition[0]][cellPosition[1]];
        return currentCell;
    }

    public void updateCell(Cell currentCell) {

    }







}
