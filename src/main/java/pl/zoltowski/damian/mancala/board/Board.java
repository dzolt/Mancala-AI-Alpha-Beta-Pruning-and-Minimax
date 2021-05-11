package pl.zoltowski.damian.mancala.board;

import lombok.Data;

@Data
public class Board {
    private int[][] boardBins;
    int N;
    int initialStones;

    public Board(int N, int initialStones) {
        this.boardBins = new int[2][N+1];
        this.N = N;
        this.initialStones = initialStones;
        initBoardBins(initialStones);
    }

    private void initBoardBins(int initialBins) {
        for(int i = 0; i < this.boardBins.length ; i++) {
            for(int j = 0; j < this.boardBins[0].length ; j++) {
                this.boardBins[i][j] = initialBins;
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

    public Board copyBoard() {
        Board newBoard = new Board(this.N, this.initialStones);
        for(int i = 0; i < newBoard.getBoardBins().length; i++) {
            for(int j = 0; j< newBoard.getBoardBins()[0].length; j++) {
                newBoard.getBoardBins()[i][j] = this.getBoardBins()[i][j];
            }
        }
        return newBoard;
    }

}
