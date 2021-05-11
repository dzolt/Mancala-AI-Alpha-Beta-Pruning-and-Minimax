package pl.zoltowski.damian.mancala;

import lombok.Data;
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
    private Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private Player playerA;
    private Player playerB;
    private int binsNumber;
    private int initialNumberOfStones;
    private GameModeAI gameModeAI_1;
    private GameModeAI gameModeAI_2;

    public Mancala(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones) {
        this.board = new Board(binsNumber, initialNumberOfStones);
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.binsNumber = binsNumber;
        this.initialNumberOfStones = initialNumberOfStones;
    }

    public Mancala(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, GameModeAI gameModeAI_1) {
        this.board = new Board(binsNumber, initialNumberOfStones);
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.binsNumber = binsNumber;
        this.initialNumberOfStones = initialNumberOfStones;
        this.gameModeAI_1 = gameModeAI_1;
    }

    public Mancala(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones, GameModeAI gameModeAI_1, GameModeAI gameModeAI_2) {
        this.board = new Board(binsNumber, initialNumberOfStones);
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.binsNumber = binsNumber;
        this.initialNumberOfStones = initialNumberOfStones;
        this.gameModeAI_1 = gameModeAI_1;
        this.gameModeAI_2 = gameModeAI_2;
    }

    /**
     * Handles the move logic for current player after selecting bin to play
     *
     * @param bin
     * @return move status of the selected bin
     */
    public MoveStatus move(int bin) {
        if (isGameOver()) {
            fetchAllStones();
            return MoveStatus.END_GAME;
        }
        if (bin >= this.board.getBoardBins()[this.currentPlayer.getNumber()].length - 1) {
            System.out.println("PLEASE CHOSE CORRECT NUMBER: 1 - 6");
            return MoveStatus.INCORRECT;
        }
        int[][] bins = this.board.getBoardBins();
        int sideIndex = currentPlayer.getNumber();
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
            if ((i == 1) && (sideIndex == currentPlayer.getNumber())) {
                //and it appears to be placed in our well we place it there and allow the second move
                if (index == bins[sideIndex].length - 1) {
                    bins[sideIndex][index]++;
                    return MoveStatus.REQUIRES_ANOTHER_MOVE;
                } else if (bins[sideIndex][index] == 0) { //else if it appears to be placed in our empty field
                    stealFromPlayer(index);
                    changePlayers();
                    return MoveStatus.COMPLETED;
                }
            }
            //check if other player board is on board right now and if its his well if it is increment index to switch player
            if (sideIndex == otherPlayer.getNumber()) {
                if (index == bins[sideIndex].length - 1) {
                    sideIndex = sideIndex == 0 ? sideIndex + 1 : sideIndex - 1;
                    index = 0;
                }
            }
            bins[sideIndex][index]++;
        }
        changePlayers();
        return MoveStatus.COMPLETED;
    }

    /**
     * Called after {@link #isGameOver()} returns true
     * Fetches all stones left on other player's board side as the game is over and all stones
     * are supposed to go into his well
     */
    private void fetchAllStones() {
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
    private boolean isGameOver() {
        int sum = 0;
        for (int stones : this.board.getBoardBins()[currentPlayer.getNumber()]) {
            sum += stones;
        }
        sum -= getPlayerWellStones(currentPlayer);
        return sum == 0;
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
        int indexOfWell = this.board.getBoardBins()[currentPlayer.getNumber()].length - 1;
        int opposingIndex = binsNumber - 1 - index;
        if (this.board.getBoardBins()[otherPlayer.getNumber()][opposingIndex] != 0) {
            this.board.getBoardBins()[currentPlayer.getNumber()][indexOfWell] += this.board.getBoardBins()[otherPlayer.getNumber()][opposingIndex] + 1;
            this.board.getBoardBins()[otherPlayer.getNumber()][opposingIndex] = 0;
        }
    }

    /**
     * Changes current player to opposing player after move has been finished
     */
    private void changePlayers() {
        if (this.currentPlayer == this.playerA) {
            this.currentPlayer = this.playerB;
            this.otherPlayer = this.playerA;
        } else {
            this.currentPlayer = this.playerA;
            this.otherPlayer = this.playerB;
        }
    }

    /**
     * Selects winner based on number of stones in their well
     *
     * @return winner Player
     */
    private Player selectWinner() {
        if (getPlayerWellStones(this.playerA) > getPlayerWellStones(this.playerB)) {
            System.out.println("PLAYER: " + playerA.name() + " WON");
            return this.playerA;
        } else {
            System.out.println("PLAYER: " + this.playerB.name() + " WON");
            return this.playerB;
        }
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
            playerVsPlayer();
        } else if (checkForBothAIPlayers()) {
            AIvsAI();
        } else {
            playerVsAI();
        }
        return selectWinner();
    }

    private void playerVsAI() {
        Scanner in = new Scanner(System.in);
        MoveStatus status = MoveStatus.START_GAME;
        while (status != MoveStatus.END_GAME) {
            this.getBoard().printBoard();
            if (this.currentPlayer != Player.PLAYER_AI_1 && this.currentPlayer != Player.PLAYER_AI_2) {
                System.out.println(currentPlayer.name() + " please pick a bin 1-6");
                int bin = in.nextInt();
                status = move(bin - 1);
            } else {
                Tuple<Integer, Double> move =
                  this.gameModeAI_1 == GameModeAI.MIN_MAX ?
                    minMax(this, 3, this.currentPlayer) :
                    alphaBeta(this, 3, this.currentPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                if (move.getFirst() != null) {
                    System.out.println(currentPlayer.name() + " chose move " + (move.getFirst() + 1) + " with rate of " + move.getSecond() + " and " + this.gameModeAI_1.name());
                    status = move(move.getFirst());
                } else {
                    status = move(0);
                }
            }

        }
    }

    private void AIvsAI() {
        MoveStatus status = MoveStatus.START_GAME;
        int mo = 0;
        int bin;
        while (status != MoveStatus.END_GAME) {
            System.out.println("MOVE NO: " + ++mo);
            this.getBoard().printBoard();
            System.out.println(currentPlayer.name() + " please pick a bin 1-6");
            Tuple<Integer, Double> move = determineMove();
            if (move.getFirst() != null) {
                bin = move.getFirst();

                String moveAlg = "";
                moveAlg = this.currentPlayer == Player.PLAYER_AI_1 ? this.gameModeAI_1.name() : this.gameModeAI_2.name();
                System.out.println(currentPlayer.name() + " chose move " + move.getFirst() + " with rate of " + move.getSecond() + " and " + moveAlg);
            } else {
                return;
            }

            status = move(bin);
        }
    }

    private Tuple<Integer, Double> determineMove() {
        if (this.currentPlayer == Player.PLAYER_AI_1) {
            if (this.gameModeAI_1 == GameModeAI.MIN_MAX) {
                return minMax(this, 3, this.currentPlayer);
            } else {
                return alphaBeta(this, 3, this.currentPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
        } else {
            if (this.gameModeAI_2 == GameModeAI.MIN_MAX) {
                return minMax(this, 3, this.currentPlayer);
            } else {
                return alphaBeta(this, 3, this.currentPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
        }
    }

    private boolean checkForBothAIPlayers() {
        return (playerA == Player.PLAYER_AI_1 || playerA == Player.PLAYER_AI_2) &&
               (playerB == Player.PLAYER_AI_1 || playerB == Player.PLAYER_AI_2);
    }

    private boolean checkForAIPlayers() {
        return playerA == Player.PLAYER_AI_1 || playerA == Player.PLAYER_AI_2 ||
               playerB == Player.PLAYER_AI_1 || playerB == Player.PLAYER_AI_2;
    }

    private void playerVsPlayer() {
        Scanner in = new Scanner(System.in);
        MoveStatus status = MoveStatus.START_GAME;
        while (status != MoveStatus.END_GAME) {
            this.getBoard().printBoard();
            System.out.println(currentPlayer.name() + " please pick a bin 1-6");
            int bin = in.nextInt();
            status = move(bin - 1);
        }
    }

    /**
     * Returns rate value of the current game state for specific player
     *
     * @param player
     * @return Rate value of the current game state for specific player
     */
    private double rateMove(Player player) {
        return getPlayerWellStones(player) - getPlayerWellStones(getOpponentPlayer(player));
    }

    /**
     * Returns index of the best move and its rate value within depth moves
     *
     * @param gameState
     * @param depth
     * @param maximizingPlayer
     * @return Tuple of index and it's rate value for the best move
     */
    public Tuple<Integer, Double> minMax(Mancala gameState, int depth, Player maximizingPlayer) {
        if (depth == 0 || gameState.countStonesLeft(gameState.currentPlayer) == 0 || gameState.countStonesLeft(gameState.otherPlayer) == 0) {
            return new Tuple<>(null, gameState.rateMove(maximizingPlayer));
        }
        List<Integer> possibleMoves = gameState.getPossibleMovesForPlayer(gameState.currentPlayer);
        List<Tuple<Integer, Double>> valuesForMoves = new ArrayList<>();

        for (Integer move : possibleMoves) {
            Mancala tempGame = gameState.copyGameState();
            MoveStatus status = tempGame.move(move);
            int nextDepth = status == MoveStatus.REQUIRES_ANOTHER_MOVE ? depth : depth - 1;

            valuesForMoves.add(new Tuple<>(move, minMax(tempGame, nextDepth, maximizingPlayer).getSecond()));
        }

        if (gameState.currentPlayer == maximizingPlayer) {
            return valuesForMoves.stream().max(Comparator.comparing(Tuple::getSecond)).orElseThrow(NoSuchElementException::new);
        } else {
            return valuesForMoves.stream().min(Comparator.comparing(Tuple::getSecond)).orElseThrow(NoSuchElementException::new);
        }
    }

    public Tuple<Integer, Double> alphaBeta(Mancala gameState, int depth, Player maximizingPlayer, double alpha, double beta) {
        if (depth == 0 || gameState.countStonesLeft(gameState.currentPlayer) == 0 || gameState.countStonesLeft(gameState.otherPlayer) == 0) {
            return new Tuple<>(null, gameState.rateMove(maximizingPlayer));
        }
        List<Integer> possibleMoves = gameState.getPossibleMovesForPlayer(gameState.currentPlayer);

        Tuple<Integer, Double> bestMove;
        if (gameState.currentPlayer == maximizingPlayer) {
            bestMove = new Tuple<>(null, Double.NEGATIVE_INFINITY);
            for (Integer move : possibleMoves) {
                Mancala tempGame = gameState.copyGameState();

                MoveStatus status = tempGame.move(move);
                //get move value, value of the sub-tree
                int nextDepth = status == MoveStatus.REQUIRES_ANOTHER_MOVE ? depth : depth - 1;
                Double moveValue = alphaBeta(tempGame, nextDepth, maximizingPlayer, alpha, beta).getSecond();
                if (moveValue > bestMove.getSecond()) {
                    bestMove.setSecond(moveValue);
                    bestMove.setFirst(move);
                }
                alpha = Math.max(alpha, bestMove.getSecond()); //alpha is the maximum value we have found so far
                if (alpha >= beta) {
                    break;
                }
            }
        } else {
            bestMove = new Tuple<>(null, Double.POSITIVE_INFINITY);
            for (Integer move : possibleMoves) {
                Mancala tempGame = gameState.copyGameState();
                MoveStatus status = tempGame.move(move);
                int nextDepth = status == MoveStatus.REQUIRES_ANOTHER_MOVE ? depth : depth - 1;
                Double moveValue = alphaBeta(tempGame, nextDepth, maximizingPlayer, alpha, beta).getSecond();
                if (moveValue < bestMove.getSecond()) {
                    bestMove.setFirst(move);
                    bestMove.setSecond(moveValue);
                }
                beta = Math.min(beta, bestMove.getSecond()); //beta is the minimum value we have found so far
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestMove;
    }

    /**
     * Returns all indexes of possible moves for specified player
     *
     * @param player
     * @return List of indexes of all possible moves that player can make
     */
    private List<Integer> getPossibleMovesForPlayer(Player player) {
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
    private int countStonesLeft(Player player) {
        int sum = 0;
        for (int stones : this.board.getBoardBins()[player.getNumber()]) {
            sum += stones;
        }
        sum -= getPlayerWellStones(player);
        return sum;
    }

    public void alphaBeta() {
        //TODO: implement alpha beta
    }

    /**
     * Returns opponent player
     *
     * @param player - current player
     * @return opponent player
     */
    private Player getOpponentPlayer(Player player) {
        if (this.currentPlayer == player) {
            return this.otherPlayer;
        } else {
            return this.currentPlayer;
        }
    }

    /**
     * Returns stones in player's well
     *
     * @param player
     * @return number of stones in player's well
     */
    private int getPlayerWellStones(Player player) {
        int indexOfWell = this.board.getBoardBins()[player.getNumber()].length - 1;
        return this.getBoard().getBoardBins()[player.getNumber()][indexOfWell];
    }

    /**
     * Deep copies current game state
     *
     * @return new exact copy of current game state
     */
    private Mancala copyGameState() {
        Mancala newGame = new Mancala(this.playerA, this.playerB, this.binsNumber, this.initialNumberOfStones);
        newGame.board = this.board.copyBoard();
        newGame.currentPlayer = this.currentPlayer;
        newGame.otherPlayer = this.otherPlayer;
        newGame.gameModeAI_1 = this.gameModeAI_1;
        newGame.gameModeAI_2 = this.gameModeAI_2;
        return newGame;
    }
}
