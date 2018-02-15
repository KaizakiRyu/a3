package GameLogic;

/**
 * Created by leom on 12/02/18.
 */
public class Fortress {
    private int fortressHealth;
    private final int MAX_FORTRESS_HEALTH = 1500;

    Fortress(){
        this.fortressHealth = MAX_FORTRESS_HEALTH;
    }

    public void setFortressHealth(int tankDamage){
        this.fortressHealth -=tankDamage;
    }

    public int getFortressHealth() {
        return fortressHealth;
    }
}
