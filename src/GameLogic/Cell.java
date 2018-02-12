package GameLogic;

import java.util.ArrayList;

public class Cell {
    private boolean isDestroyed;
    private int[] cellCoordinate;
    private final int HORIZONTAL_COORDINATE = 0;
    private final int VERTICAL_COORDINATE = 1;

    public Cell() {
    }

    public boolean getDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.isDestroyed = destroyed;
    }

    public void setCellCoordinate(int[] cellCoordinate) {
        this.cellCoordinate = cellCoordinate;
    }

    public int getHorizontalCoordinate(){
        return cellCoordinate[HORIZONTAL_COORDINATE];
    }

    public int getVerticalCoordinate(){
        return cellCoordinate[VERTICAL_COORDINATE];
    }

    public boolean isTankCell(ArrayList<Tank> listOfTank, Cell currentCell){
        for(Tank currentTank : listOfTank){
            ArrayList<Cell> currentListOfTankCell = currentTank.getListOfTankCell();
            if (currentListOfTankCell.contains(currentCell)){
                return true;
            }
        }
        return false;
    }
}
