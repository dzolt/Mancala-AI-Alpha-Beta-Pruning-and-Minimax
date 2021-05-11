package pl.zoltowski.damian.mancala.tools;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zoltowski.damian.mancala.GameEngine;
import pl.zoltowski.damian.mancala.GameState;
import pl.zoltowski.damian.mancala.Mancala;
import pl.zoltowski.damian.utils.dataType.MoveStatus;
import pl.zoltowski.damian.utils.dataType.Player;
import pl.zoltowski.damian.utils.dataType.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@NoArgsConstructor
public class MinMaxAlgorithm {

    /**
     * Returns index of the best move and its rate value within depth moves
     *
     * @param gameState
     * @param depth
     * @param maximizingPlayer
     * @return Tuple of index and it's rate value for the best move
     */
    public Tuple<Integer, Double> minMax(GameState gameState, int depth, Player maximizingPlayer) {
        if (depth == 0 || gameState.countStonesLeft(gameState.getCurrentPlayer()) == 0 || gameState.countStonesLeft(gameState.getOtherPlayer()) == 0) {
            return new Tuple<>(null, gameState.rateMove(maximizingPlayer));
        }
        List<Integer> possibleMoves = gameState.getPossibleMovesForPlayer(gameState.getCurrentPlayer());
        List<Tuple<Integer, Double>> valuesForMoves = new ArrayList<>();

        for (Integer move : possibleMoves) {
            Mancala tempGame = new Mancala(gameState).copyGameState();
            MoveStatus status = tempGame.move(move);
            int nextDepth = status == MoveStatus.REQUIRES_ANOTHER_MOVE ? depth : depth - 1;

            valuesForMoves.add(new Tuple<>(move, minMax(tempGame.getGameState(), nextDepth, maximizingPlayer).getSecond()));
        }

        if (gameState.getCurrentPlayer() == maximizingPlayer) {
            return valuesForMoves.stream().max(Comparator.comparing(Tuple::getSecond)).orElseThrow(NoSuchElementException::new);
        } else {
            return valuesForMoves.stream().min(Comparator.comparing(Tuple::getSecond)).orElseThrow(NoSuchElementException::new);
        }
    }
}
