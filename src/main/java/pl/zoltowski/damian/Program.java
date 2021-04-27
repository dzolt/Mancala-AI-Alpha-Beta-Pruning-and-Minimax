package pl.zoltowski.damian;

import pl.zoltowski.damian.mancala.Mancala;
import pl.zoltowski.damian.utils.dataType.Player;

public class Program {

    public static void main(String[] args) {
        Player playerA = Player.PLAYER_A;
        Player playerB = Player.PLAYER_B;

        Mancala mancala = new Mancala(playerA, playerB, 6);
        mancala.getBoard().getBoardBins()[0][5] = 8;
       Player p = mancala.start();
        System.out.println("WINNER " + p.name());
//

    }
}
