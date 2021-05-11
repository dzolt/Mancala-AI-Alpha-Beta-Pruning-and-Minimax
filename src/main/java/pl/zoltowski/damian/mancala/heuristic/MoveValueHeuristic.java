package pl.zoltowski.damian.mancala.heuristic;

import pl.zoltowski.damian.mancala.GameState;
import pl.zoltowski.damian.utils.dataType.Player;

public abstract class MoveValueHeuristic {
    public MoveValueHeuristic() { }
    public abstract double rateMove(GameState gameState, Player player);
}
