package GameLogic;

import java.util.ArrayList;

public class Tank {
    private ArrayList<Cell> listOfTankCell;
    private char tankID;
    private int tankHealth;
    private int tankDamage;
    private int tankDamagePointer = 4;
    private final int MAX_TANK_HEALTH = 4;
    private final int MIN_TANK_HEALTH = 0;
    private final int DECREMENT_OF_TANK_HEALTH = 1;
    private final int DECREMENT_OF_TANK_DAMAGE = 1;
    private final int[] LIST_OF_TANK_DAMAGE= {0, 1, 2, 5, 20};

    public Tank(char id) {
        this.listOfTankCell = new ArrayList<>();
        this.tankHealth = MAX_TANK_HEALTH;
        this.tankDamage = LIST_OF_TANK_DAMAGE[tankDamagePointer];
        this.tankID = id;
    }

    public char getTankID() {
        return tankID;
    }

    public void setListOfTankCell(ArrayList<Cell> listOfTankCell) {
        this.listOfTankCell = listOfTankCell;
    }

    public void setTankHealth(){
        this.tankHealth -= DECREMENT_OF_TANK_HEALTH;
    }

    public void setTankDamage(){
        this.tankDamagePointer = tankDamagePointer - DECREMENT_OF_TANK_DAMAGE;
        this.tankDamage = LIST_OF_TANK_DAMAGE[tankDamagePointer];
    }

    public ArrayList<Cell> getListOfTankCell() {
        return listOfTankCell;
    }

    public void addTankCell(Cell currentCell){
        listOfTankCell.add(currentCell);
    }

    public int getTankHealth() {
        return tankHealth;
    }

    public int getTankDamage() {
        return tankDamage;
    }

    public boolean isDestroyed(){
        if (tankHealth == MIN_TANK_HEALTH)
        {
            System.out.println("Hi");;
            return true;
        }
        return false;
    }

}
