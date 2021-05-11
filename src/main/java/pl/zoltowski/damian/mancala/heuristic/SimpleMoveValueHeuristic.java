package pl.zoltowski.damian.mancala.heuristic;

import pl.zoltowski.damian.mancala.logic.GameState;
import pl.zoltowski.damian.utils.dataType.Player;

public class SimpleMoveValueHeuristic extends MoveValueHeuristic{

    public SimpleMoveValueHeuristic() { }

    @Override
    public double rateMove(GameState gameState, Player player) {
        return gameState.getPlayerWellStones(player) - gameState.getPlayerWellStones(gameState.getOpponentPlayer(player));
    }
}
