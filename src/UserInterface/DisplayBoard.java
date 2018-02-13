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

    public DisplayBoard(int numOfTanks, boolean cheat) {
        this.board = new Board(numOfTanks,cheat);
    }

    public void startGame() {
        Fortress gameFortress = board.getFortress();
        int fortressHealth;
        ArrayList<Tank> listOfTanks;
        do {
            System.out.print("Please enter the coordinate to fire upon: ");
            Scanner reader = new Scanner(System.in);
            String userInput = reader.next();
            int[] convertedCoordinate = board.convertCoordinateToInt(userInput);
            AttackCell currentPlayerAttack = new AttackCell(convertedCoordinate);
            board.userAttack(currentPlayerAttack);
            gameFortress = board.getFortress();
            fortressHealth = gameFortress.getFortressHealth();
            listOfTanks = board.getTankPlacement().getListOfTanks();
        } while(fortressHealth > MIN_FORTRESS_HEALTH || listOfTanks.size() > MIN_AMOUNT_OF_TANK);
    }
}
