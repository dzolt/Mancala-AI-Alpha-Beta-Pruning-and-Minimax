package pl.zoltowski.damian.mancala.logic;

import lombok.Data;
import pl.zoltowski.damian.mancala.tools.AlphaBetaAlgorithm;
import pl.zoltowski.damian.mancala.tools.MinMaxAlgorithm;
import pl.zoltowski.damian.utils.dataType.GameModeAI;
import pl.zoltowski.damian.utils.dataType.MoveStatus;
import pl.zoltowski.damian.utils.dataType.Player;
import pl.zoltowski.damian.utils.dataType.Tuple;

import java.util.Scanner;

@Data
public class GameEngine {
    private GameState gameState;
    private MinMaxAlgorithm minMax;
    private AlphaBetaAlgorithm alphaBeta;

    public GameEngine(GameState gameState) {
        this.gameState = gameState;
        this.minMax = new MinMaxAlgorithm();
        this.alphaBeta = new AlphaBetaAlgorithm();
    }

    /**
     * Handles the move logic for current player after selecting bin to play
     *
     * @param bin
     * @return move status of the selected bin
     */
    public MoveStatus move(int bin) {
        if (this.gameState.isGameOver()) {
            this.gameState.fetchAllStones();
            return MoveStatus.END_GAME;
        }
        if (bin >= this.gameState.getBoardBins()[this.gameState.getCurrentPlayer().getNumber()].length - 1) {
            System.out.println("PLEASE CHOSE CORRECT NUMBER: 1 - 6");
            return MoveStatus.INCORRECT;
        }
        int[][] bins = this.gameState.getBoardBins();
        int sideIndex = this.gameState.getCurrentPlayer().getNumber();
        int stonesToMove = bins[sideIndex][bin];
        if (stonesToMove == 0) {
            System.out.println("CANNOT CHOSE EMPTY HOLE");
            return MoveStatus.INCORRECT;
        }
        bins[sideIndex][bin] = 0;
        int index = bin;

        for (int i = stonesToMove; i > 0; i--) {
            //calculate next index to move
            index++;
            //switch player board if met the end of the holes
            if (index == bins[sideIndex].length) {
                sideIndex = sideIndex == 0 ? sideIndex + 1 : sideIndex - 1;
                index = 0;
            }
            //if we have our last stone to deal with and are on our side
            if ((i == 1) && (sideIndex == this.gameState.getCurrentPlayer().getNumber())) {
                //and it appears to be placed in our well we place it there and allow the second move
                if (index == bins[sideIndex].length - 1) {
                    bins[sideIndex][index]++;
                    return MoveStatus.REQUIRES_ANOTHER_MOVE;
                } else if (bins[sideIndex][index] == 0) { //else if it appears to be placed in our empty field
                    stealFromPlayer(index);
                    this.gameState.changePlayers();
                    return MoveStatus.COMPLETED;
                }
            }
            //check if other player board is on board right now and if its his well if it is increment index to switch player
            if (sideIndex == this.gameState.getOtherPlayer().getNumber()) {
                if (index == bins[sideIndex].length - 1) {
                    sideIndex = sideIndex == 0 ? sideIndex + 1 : sideIndex - 1;
                    index = 0;
                }
            }
            bins[sideIndex][index]++;
        }
        this.gameState.changePlayers();
        return MoveStatus.COMPLETED;
    }

    /**
     * Steals all stones from other player's opposite bin if it's not empty
     * and puts them inside current player's well along with extra one
     * that was about tobe added
     *
     * @param index
     */
    private void stealFromPlayer(int index) {
        //change to get players well
        int indexOfWell = this.gameState.getBoardBins()[this.gameState.getCurrentPlayer().getNumber()].length - 1;
        int opposingIndex = this.gameState.getBinsNumber() - 1 - index;
        if (this.gameState.getBoardBins()[this.gameState.getOtherPlayer().getNumber()][opposingIndex] != 0) {
            this.gameState.getBoardBins()[this.gameState.getCurrentPlayer().getNumber()][indexOfWell] += this.gameState.getBoardBins()[this.gameState.getOtherPlayer().getNumber()][opposingIndex] + 1;
            this.gameState.getBoardBins()[this.gameState.getOtherPlayer().getNumber()][opposingIndex] = 0;
        }
    }


    /**
     * Chooses move algorithm depending on the AI's algorithm chosen during game init
     * @return best possible move in current situation
     */
    public Tuple<Integer, Double> determineMove() {
        if (this.gameState.getCurrentPlayer() == Player.PLAYER_AI_1) {
            if (this.gameState.getGameModeAI_1() == GameModeAI.MIN_MAX) {
                return minMax.minMax(this.gameState, 3, this.gameState.getCurrentPlayer());
            } else {
                return alphaBeta.alphaBeta(this.gameState, 3, this.gameState.getCurrentPlayer(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
        } else {
            if (this.gameState.getGameModeAI_2() == GameModeAI.MIN_MAX) {
                return minMax.minMax(this.gameState, 3, this.gameState.getCurrentPlayer());
            } else {
                return alphaBeta.alphaBeta(this.gameState, 3, this.gameState.getCurrentPlayer(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
        }
    }

    public void AIvsAI() {
        MoveStatus status = MoveStatus.START_GAME;
        int mo = 0;
        int bin;
        while (status != MoveStatus.END_GAME) {
            System.out.println("MOVE NO: " + ++mo);
            this.gameState.getBoard().printBoard();
            System.out.println(this.gameState.getCurrentPlayer().name() + " please pick a bin 1-6");
            Tuple<Integer, Double> move = determineMove();
            if (move.getFirst() != null) {
                bin = move.getFirst();

                String moveAlg = "";
                moveAlg = this.gameState.getCurrentPlayer() == Player.PLAYER_AI_1 ? this.gameState.getGameModeAI_1().name() : this.gameState.getGameModeAI_2().name();
                System.out.println(this.gameState.getCurrentPlayer().name() + " chose move " + move.getFirst() + " with rate of " + move.getSecond() + " and " + moveAlg);
            } else {
                return;
            }

            status = move(bin);
        }
    }

    public void playerVsAI() {
        Scanner in = new Scanner(System.in);
        MoveStatus status = MoveStatus.START_GAME;
        while (status != MoveStatus.END_GAME) {
            this.gameState.getBoard().printBoard();
            if (this.gameState.getCurrentPlayer() != Player.PLAYER_AI_1 && this.gameState.getCurrentPlayer() != Player.PLAYER_AI_2) {
                System.out.println(this.gameState.getCurrentPlayer().name() + " please pick a bin 1-6");
                int bin = in.nextInt();
                status = move(bin - 1);
            } else {
                Tuple<Integer, Double> move =
                  this.gameState.getGameModeAI_1() == GameModeAI.MIN_MAX ?
                    this.minMax.minMax(this.gameState, 3, this.gameState.getCurrentPlayer()) :
                    this.alphaBeta.alphaBeta(this.gameState, 3, this.gameState.getCurrentPlayer(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                if (move.getFirst() != null) {
                    System.out.println(this.gameState.getCurrentPlayer().name() + " chose move " + (move.getFirst() + 1) + " with rate of " + move.getSecond() + " and " + this.gameState.getGameModeAI_1().name());
                    status = move(move.getFirst());
                } else {
                    status = move(0);
                }
            }

        }
    }

    public void playerVsPlayer() {
        Scanner in = new Scanner(System.in);
        MoveStatus status = MoveStatus.START_GAME;
        while (status != MoveStatus.END_GAME) {
            this.gameState.getBoard().printBoard();
            System.out.println(this.gameState.getCurrentPlayer().name() + " please pick a bin 1-6");
            int bin = in.nextInt();
            status = move(bin - 1);
        }
    }
}
