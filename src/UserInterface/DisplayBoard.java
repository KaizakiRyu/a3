package UserInterface;

import GameLogic.AttackCell;
import GameLogic.Board;
import GameLogic.Fortress;
import GameLogic.Tank;

import java.util.ArrayList;
import java.util.Scanner;

public class DisplayBoard {
    private Board board;
    private final int MIN_FORTRESS_HEALTH = 0;
    private final int MIN_AMOUNT_OF_TANK = 0;
    private final int HORIZONTAL_COORDINATE = 0;
    private final int OUT_OF_BOUND_COORDINATE = -1;

    public DisplayBoard(int numOfTanks, boolean cheat) {
        this.board = new Board(numOfTanks,cheat);
    }

    public void startGame() {
        Fortress gameFortress = board.getFortress();
        int fortressHealth = gameFortress.getFortressHealth();
        ArrayList<Tank> listOfTanks = board.getTankPlacement().getListOfTanks();
        do {
            System.out.print("Please enter the coordinate to fire upon: ");
            Scanner reader = new Scanner(System.in);
            String userInput = reader.next();
            int[] convertedCoordinate = board.convertCoordinateToInt(userInput);
            if (convertedCoordinate[HORIZONTAL_COORDINATE] != OUT_OF_BOUND_COORDINATE) {
                AttackCell currentUserAttack = new AttackCell(convertedCoordinate);
                board.userAttack(currentUserAttack);
                gameFortress = board.getFortress();
                fortressHealth = gameFortress.getFortressHealth();
                listOfTanks = board.getTankPlacement().getListOfTanks();
                printBoard(currentUserAttack,gameFortress,listOfTanks);
            }
        } while(fortressHealth > MIN_FORTRESS_HEALTH || listOfTanks.size() > MIN_AMOUNT_OF_TANK);
    }

    private void printBoard(AttackCell currentUserAttack, Fortress gameFortress, ArrayList<Tank> listOfTanks){
        if (currentUserAttack.isHit()){
            System.out.println("HIT");
        } else {
            System.out.println("MISS");
        }
        for (Tank currentTank : listOfTanks){
            int tankDamage = currentTank.getTankDamage();
            System.out.println("Alive tank #" + currentTank.getTankID() + "of " + listOfTanks);
            gameFortress.setFortressHealth(currentTank.getTankDamage());
        }

    }
}
