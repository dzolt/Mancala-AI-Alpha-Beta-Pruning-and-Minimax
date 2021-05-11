package pl.zoltowski.damian.mancala.logic;

import lombok.Data;
import pl.zoltowski.damian.mancala.board.Board;
import pl.zoltowski.damian.mancala.heuristic.MoveValueHeuristic;
import pl.zoltowski.damian.utils.dataType.GameModeAI;
import pl.zoltowski.damian.utils.dataType.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameState {
    private Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private Player playerA;
    private Player playerB;
    private int binsNumber;
    private int initialNumberOfStones;
    private GameModeAI gameModeAI_1;
    private GameModeAI gameModeAI_2;
    private MoveValueHeuristic moveValueHeuristic;

    public GameState(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, MoveValueHeuristic moveValueHeuristic) {
        this.board = new Board(binsNumber, initialNumberOfStones);
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.binsNumber = binsNumber;
        this.initialNumberOfStones = initialNumberOfStones;
        this.moveValueHeuristic = moveValueHeuristic;
    }

    public GameState(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, GameModeAI gameModeAI_1, MoveValueHeuristic moveValueHeuristic) {
        this.board = new Board(binsNumber, initialNumberOfStones);
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.binsNumber = binsNumber;
        this.initialNumberOfStones = initialNumberOfStones;
        this.gameModeAI_1 = gameModeAI_1;
        this.moveValueHeuristic = moveValueHeuristic;
    }

    public GameState(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, GameModeAI gameModeAI_1, GameModeAI gameModeAI_2, MoveValueHeuristic moveValueHeuristic) {
        this.board = new Board(binsNumber, initialNumberOfStones);
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.binsNumber = binsNumber;
        this.initialNumberOfStones = initialNumberOfStones;
        this.gameModeAI_1 = gameModeAI_1;
        this.gameModeAI_2 = gameModeAI_2;
        this.moveValueHeuristic = moveValueHeuristic;
    }

    public int[][] getBoardBins() {
        return this.getBoard().getBoardBins();
    }

    /**
     * Called after {@link #isGameOver()} returns true
     * Fetches all stones left on other player's board side as the game is over and all stones
     * are supposed to go into his well
     */
    public void fetchAllStones() {
        int indexOfWell = this.board.getBoardBins()[otherPlayer.getNumber()].length - 1;
        for (int i = 0; i < indexOfWell; i++) {
            int stones = this.board.getBoardBins()[otherPlayer.getNumber()][i];
            this.board.getBoardBins()[otherPlayer.getNumber()][indexOfWell] += stones;
        }
    }

    /**
     * Checks if the sum of stones on the current player's board is equal to 0
     * if so the game ends as he cannot move
     *
     * @return sum of stones on current player's board side
     */
    public boolean isGameOver() {
        int sum = 0;
        for (int stones : this.board.getBoardBins()[currentPlayer.getNumber()]) {
            sum += stones;
        }
        sum -= getPlayerWellStones(currentPlayer);
        return sum == 0;
    }

    /**
     * Changes current player to opposing player after move has been finished
     */
    public void changePlayers() {
        if (this.currentPlayer == this.playerA) {
            this.currentPlayer = this.playerB;
            this.otherPlayer = this.playerA;
        } else {
            this.currentPlayer = this.playerA;
            this.otherPlayer = this.playerB;
        }
    }

    /**
     * Returns stones in player's well
     *
     * @param player
     * @return number of stones in player's well
     */
    public int getPlayerWellStones(Player player) {
        int indexOfWell = this.board.getBoardBins()[player.getNumber()].length - 1;
        return this.getBoard().getBoardBins()[player.getNumber()][indexOfWell];
    }

    /**
     * Returns opponent player
     *
     * @param player - current player
     * @return opponent player
     */
    public Player getOpponentPlayer(Player player) {
        if (this.currentPlayer == player) {
            return this.otherPlayer;
        } else {
            return this.currentPlayer;
        }
    }

    /**
     * Selects winner based on number of stones in their well
     *
     * @return winner Player
     */
    public Player selectWinner() {
        if (getPlayerWellStones(this.playerA) > getPlayerWellStones(this.playerB)) {
            System.out.println("PLAYER: " + playerA.name() + " WON");
            return this.playerA;
        } else {
            System.out.println("PLAYER: " + this.playerB.name() + " WON");
            return this.playerB;
        }
    }

    public boolean checkForBothAIPlayers() {
        return (playerA == Player.PLAYER_AI_1 || playerA == Player.PLAYER_AI_2) &&
               (playerB == Player.PLAYER_AI_1 || playerB == Player.PLAYER_AI_2);
    }

    public boolean checkForAIPlayers() {
        return playerA == Player.PLAYER_AI_1 || playerA == Player.PLAYER_AI_2 ||
               playerB == Player.PLAYER_AI_1 || playerB == Player.PLAYER_AI_2;
    }

    public List<Integer> getPossibleMovesForPlayer(Player player) {
        List<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i < this.board.getBoardBins()[player.getNumber()].length - 1; i++) {
            if (this.board.getBoardBins()[player.getNumber()][i] > 0) {
                possibleMoves.add(i);
            }
        }
        return possibleMoves;
    }

    /**
     * Counts stones left on the specified player's board
     *
     * @param player
     * @return Number of stones on player's board
     */
    public int countStonesLeft(Player player) {
        int sum = 0;
        for (int stones : this.board.getBoardBins()[player.getNumber()]) {
            sum += stones;
        }
        sum -= getPlayerWellStones(player);
        return sum;
    }

    /**
     * Returns rate value of the current game state for specific player
     *
     * @param player
     * @return Rate value of the current game state for specific player
     */
    public double rateMove(Player player) {
        return this.moveValueHeuristic.rateMove(this, player);
    }
}
