package GameLogic;

import java.util.ArrayList;

public class TankPlacement {
    private ArrayList<Tank> listOfTanks;
    private int numberOfTanks;
    private final int INITIALIZER = 0;

    public TankPlacement(int numberOfTanks) {
        this.numberOfTanks = numberOfTanks;
    }

    public void placeAllTanks(){
        for (int index = INITIALIZER; index < numberOfTanks; index++){
            Tank currentTank = new Tank();
        }
    }

    private void placeTank(){

    }

    public ArrayList<Tank> getListOfTanks() {
        return listOfTanks;
    }

    public int getNumberOfTanks() {
        return numberOfTanks;
    }
}
