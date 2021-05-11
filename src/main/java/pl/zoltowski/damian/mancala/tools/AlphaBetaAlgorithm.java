package pl.zoltowski.damian.mancala.tools;

import lombok.NoArgsConstructor;
import pl.zoltowski.damian.mancala.logic.GameState;
import pl.zoltowski.damian.mancala.Mancala;
import pl.zoltowski.damian.utils.dataType.MoveStatus;
import pl.zoltowski.damian.utils.dataType.Player;
import pl.zoltowski.damian.utils.dataType.Tuple;

import java.util.List;

@NoArgsConstructor
public class AlphaBetaAlgorithm {

    public Tuple<Integer, Double> alphaBeta(GameState gameState, int depth, Player maximizingPlayer, double alpha, double beta) {
        if (depth == 0 || gameState.countStonesLeft(gameState.getCurrentPlayer()) == 0 || gameState.countStonesLeft(gameState.getCurrentPlayer()) == 0) {
            return new Tuple<>(null, gameState.rateMove(maximizingPlayer));
        }
        List<Integer> possibleMoves = gameState.getPossibleMovesForPlayer(gameState.getCurrentPlayer());

        Tuple<Integer, Double> bestMove;
        if (gameState.getCurrentPlayer() == maximizingPlayer) {
            bestMove = new Tuple<>(null, Double.NEGATIVE_INFINITY);
            for (Integer move : possibleMoves) {
                Mancala tempGame = new Mancala(gameState).copyGameState();

                MoveStatus status = tempGame.move(move);
                //get move value, value of the sub-tree
                int nextDepth = status == MoveStatus.REQUIRES_ANOTHER_MOVE ? depth : depth - 1;
                Double moveValue = alphaBeta(tempGame.getGameState(), nextDepth, maximizingPlayer, alpha, beta).getSecond();
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
                Mancala tempGame = new Mancala(gameState).copyGameState();
                MoveStatus status = tempGame.move(move);
                int nextDepth = status == MoveStatus.REQUIRES_ANOTHER_MOVE ? depth : depth - 1;
                Double moveValue = alphaBeta(tempGame.getGameState(), nextDepth, maximizingPlayer, alpha, beta).getSecond();
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
}
