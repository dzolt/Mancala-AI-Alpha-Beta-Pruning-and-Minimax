package pl.zoltowski.damian.mancala;

import lombok.Data;

@Data
public class Board {
    private int[][] boardBins;

    public Board(int N) {
        this.boardBins = new int[2][N+1];
        initBoardBins();
    }

    private void initBoardBins() {
        for(int i = 0; i < this.boardBins.length ; i++) {
            for(int j = 0; j < this.boardBins[0].length ; j++) {
                this.boardBins[i][j] = 4;
            }
        }
        this.boardBins[0][6] = 0;
        this.boardBins[1][6] = 0;
    }

    public void printBoard() {
        printPlayerABoard();
        System.out.println();
        printPlayerBBoard();
    }

    private void printPlayerABoard() {
        System.out.println("PLAYER A WELL: [" + getPlayerAWell() + "]");
        for(int i = this.boardBins[0].length - 2; i >= 0; i --) {
            System.out.print((i + 1) + ".[" + this.boardBins[0][i] + "] ");

        }
    }

    private void printPlayerBBoard() {
        for(int i = 0; i < this.boardBins[1].length - 1; i ++) {
            System.out.print((i + 1) + ".[" + this.boardBins[1][i] + "] ");
        }
        System.out.println();
        System.out.println("PLAYER B WELL: [" + getPlayerBWell() + "]");
    }

    public int getPlayerAWell(){
        return this.boardBins[0][6];
    }

    public int getPlayerBWell(){
        return this.boardBins[1][6];
    }
}
