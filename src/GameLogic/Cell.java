package GameLogic;

import java.util.ArrayList;

public class Cell {
    private String name;
    private String status;

    public Cell() {
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
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
