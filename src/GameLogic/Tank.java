package GameLogic;

import java.util.ArrayList;

public class Tank {
    private ArrayList<Cell> listOfTankCell;
    private int tankHealth;
    private int tankDamage;
    private int tankDamagePointer = 4;
    private final int MAX_TANK_HEALTH = 4;
    private final int MIN_TANK_HEALTH = 0;
    private final int DECREMENT_OF_TANK_HEALTH = 1;
    private final int DECREMENT_OF_TANK_DAMAGE = 1;
    private final int[] LIST_OF_TANK_DAMAGE= {0, 1, 2, 5, 20};

    public Tank() {
        this.tankHealth = MAX_TANK_HEALTH;
        this.tankDamage = LIST_OF_TANK_DAMAGE[tankDamagePointer];
    }

    public void setTankHealth(boolean damaged){
        if (damaged){
            this.tankHealth -= DECREMENT_OF_TANK_HEALTH;
        }
    }

    public void setTankDamage(boolean damaged){
        if (damaged){
            this.tankDamage = LIST_OF_TANK_DAMAGE[tankDamagePointer - DECREMENT_OF_TANK_DAMAGE];
        }
    }

    public ArrayList<Cell> getListOfTankCell() {
        return listOfTankCell;
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
            return true;
        }
        return false;
    }

}
