package pl.zoltowski.damian.mancala;

import lombok.Data;
import pl.zoltowski.damian.mancala.heuristic.MoveValueHeuristic;
import pl.zoltowski.damian.utils.dataType.GameModeAI;
import pl.zoltowski.damian.utils.dataType.MoveStatus;
import pl.zoltowski.damian.utils.dataType.Player;
import pl.zoltowski.damian.utils.dataType.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

@Data
public class Mancala {
    private GameState gameState;
    private GameEngine gameEngine;

    public Mancala(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, MoveValueHeuristic moveValueHeuristic) {
        this.gameState = new GameState(playerA, playerB, binsNumber, initialNumberOfStones, moveValueHeuristic);
        this.gameEngine = new GameEngine(this.gameState);
    }

    public Mancala(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, GameModeAI gameModeAI_1, MoveValueHeuristic moveValueHeuristic) {
        this.gameState = new GameState(playerA, playerB, binsNumber, initialNumberOfStones, gameModeAI_1, moveValueHeuristic);
        this.gameEngine = new GameEngine(this.gameState);
    }

    public Mancala(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, GameModeAI gameModeAI_1, GameModeAI gameModeAI_2, MoveValueHeuristic moveValueHeuristic) {
        this.gameState = new GameState(playerA, playerB, binsNumber, initialNumberOfStones, gameModeAI_1, gameModeAI_2, moveValueHeuristic);
        this.gameEngine = new GameEngine(this.gameState);
    }

    public Mancala(GameState gameState) {
        this.gameState = gameState;
        this.gameEngine = new GameEngine(this.gameState);
    }

    /**
     * Handles the move logic for current player after selecting bin to play
     *
     * @param bin
     * @return move status of the selected bin
     */
    public MoveStatus move(int bin) {
        return this.gameEngine.move(bin);
    }

    /**
     * Selects winner based on number of stones in their well
     *
     * @return winner Player
     */
    private Player selectWinner() {
       return this.gameState.selectWinner();
    }

    /**
     * Game loop that handles the game state.
     * Waits for player move and handles the move
     * accordingly to game rules.
     *
     * @return Player - player who won the game
     */
    public Player start() {
        if (!checkForAIPlayers()) {
            this.gameEngine.playerVsPlayer();
        } else if (checkForBothAIPlayers()) {
            this.gameEngine.AIvsAI();
        } else {
            this.gameEngine.playerVsAI();
        }
        return selectWinner();
    }

    /**
     * Checks if both players laying are AI
     */
    private boolean checkForBothAIPlayers() {
        return this.gameState.checkForBothAIPlayers();
    }

    /**
     * Checks if one of the players are AI
     */
    private boolean checkForAIPlayers() {
        return this.gameState.checkForAIPlayers();
    }

    /**
     * Deep copies current game state
     *
     * @return new exact copy of current game state
     */
    public Mancala copyGameState() {
        Mancala newGame = new Mancala(this.gameState.getPlayerA(), this.gameState.getPlayerB(), this.gameState.getBinsNumber(), this.gameState.getInitialNumberOfStones(), this.gameState.getMoveValueHeuristic());
        newGame.gameState.setBoard(this.gameState.getBoard().copyBoard());
        newGame.gameState.setCurrentPlayer(this.gameState.getCurrentPlayer());
        newGame.gameState.setOtherPlayer(this.gameState.getOtherPlayer());
        newGame.gameState.setGameModeAI_1(this.gameState.getGameModeAI_1());
        newGame.gameState.setGameModeAI_2(this.gameState.getGameModeAI_2());
        return newGame;
    }
}
