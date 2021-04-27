package pl.zoltowski.damian;

import pl.zoltowski.damian.mancala.Mancala;
import pl.zoltowski.damian.utils.dataType.Player;

public class Program {

    public static void main(String[] args) {
        Player playerA = Player.PLAYER_A;
        Player playerB = Player.PLAYER_B;

        Mancala mancala = new Mancala(playerB, playerA, 6);
        mancala.getBoard().getBoardBins()[1][3] = 0;
        mancala.getBoard().getBoardBins()[1][2] = 1;
        mancala.getBoard().printBoard();
        mancala.move(2);
        mancala.getBoard().printBoard();
//

    }
}
