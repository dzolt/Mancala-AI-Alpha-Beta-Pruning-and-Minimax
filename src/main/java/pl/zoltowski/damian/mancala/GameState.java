package pl.zoltowski.damian.mancala;

import lombok.Data;
import pl.zoltowski.damian.utils.dataType.Player;

@Data
public class GameState {
    private Player currentPlayer;
    private Player otherPlayer;
    private Player playerA;
    private Player playerB;

    public GameState(Player playerA, Player playerB, int binsNumber, int initialNumberOfStones) {
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
    }
}
