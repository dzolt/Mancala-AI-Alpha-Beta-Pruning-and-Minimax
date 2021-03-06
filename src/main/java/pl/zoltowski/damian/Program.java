package pl.zoltowski.damian;

import pl.zoltowski.damian.mancala.Mancala;
import pl.zoltowski.damian.mancala.heuristic.MoveValueHeuristic;
import pl.zoltowski.damian.mancala.heuristic.SimpleMoveValueHeuristic;
import pl.zoltowski.damian.utils.dataType.GameModeAI;
import pl.zoltowski.damian.utils.dataType.Player;

import java.util.Scanner;

public class Program {
    static final int BINS_NUMBER = 6;
    static final int INITIAL_NUMBER_OF_STONES = 4;
    static final MoveValueHeuristic MOVE_VALUE_HEURISTIC = new SimpleMoveValueHeuristic();

    public static void main(String[] args) {
        Mancala mancala = getGameMode();
        long timeSt = System.currentTimeMillis();
        Player p = mancala.start();
        long elapsedTime = System.currentTimeMillis() - timeSt;
        System.out.println("ELAPSED TIME: " + elapsedTime);
    }

    private static Mancala getGameMode() {
        Scanner in = new Scanner(System.in);
        System.out.println("CHOSE GAME MODE: ");
        System.out.println("1. Player vs Player");
        System.out.println("2. Player vs AI");
        System.out.println("3. AI vs AI");
        int input = in.nextInt();
        if(input < 1 || input > 3) {
            System.out.println("PLEASE CHOSE CORRECT OPTION");
            return getGameMode();
        } else {
            if(input == 1) {
                return new Mancala(Player.PLAYER_A, Player.PLAYER_B, BINS_NUMBER, INITIAL_NUMBER_OF_STONES, MOVE_VALUE_HEURISTIC);
            } else if (input == 2) {
                return chooseGameModeAIvsHuman(in);
            } else {
                return chooseGameModeAIvsAI(in);
            }
        }
    }
    private static Mancala chooseGameModeAIvsAI(Scanner in) {
        GameModeAI gameModeAI_1 = chooseGameModeAIvsAI(in, 1);
        GameModeAI gameModeAI_2 = chooseGameModeAIvsAI(in, 2);

        return new Mancala(Player.PLAYER_AI_1, Player.PLAYER_AI_2, BINS_NUMBER, INITIAL_NUMBER_OF_STONES , gameModeAI_1, gameModeAI_2, MOVE_VALUE_HEURISTIC);
    }

    private static GameModeAI chooseGameModeAIvsAI(Scanner in, int playerNumber) {
        System.out.println("CHOSE GAME MODE FOR AI " + playerNumber + ": ");
        System.out.println("1. MIN MAX");
        System.out.println("2. ALPHA BETA");
        int input = in.nextInt();
        if(input < 1 || input > 2) {
            System.out.println("PLEASE CHOSE CORRECT OPTION:");
            return chooseGameModeAIvsAI(in, playerNumber);
        } else {
            if(input == 1) {
                return GameModeAI.MIN_MAX;
            } else {
                return GameModeAI.ALPHA_BETA;
            }
        }
    }

    private static Mancala chooseGameModeAIvsHuman(Scanner in) {
        GameModeAI gameModeAI = chooseGameModeAIvsAI(in, 1);
        return new Mancala(Player.PLAYER_A, Player.PLAYER_AI_2, BINS_NUMBER, INITIAL_NUMBER_OF_STONES, gameModeAI, MOVE_VALUE_HEURISTIC);
    }
}
