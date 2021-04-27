package pl.zoltowski.damian.mancala;

import lombok.Data;
import pl.zoltowski.damian.utils.dataType.MoveStatus;
import pl.zoltowski.damian.utils.dataType.Player;

import java.util.Scanner;

@Data
public class Mancala {
    private Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private Player playerA;
    private Player playerB;
    private int binsNumber;

    public Mancala(Player playerA, Player playerB, int binsNumber) {
        this.board = new Board(binsNumber);
        this.currentPlayer = playerA;
        this.otherPlayer = playerB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.binsNumber = binsNumber;
    }

    /**
     * Handles the move logic for current player after selecting bin to play
     * @param bin
     * @return move status of the selected bin
     */
    public MoveStatus move(int bin) {
        if (isGameOver()) {
            fetchAllStones();
            return MoveStatus.END_GAME;
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
                if (index == bins[sideIndex].length - 1){
                    bins[sideIndex][index]++;
                    return MoveStatus.REQUIRES_ANOTHER_MOVE;
                } else if(bins[sideIndex][index] == 0) { //else if it appears to be placed in our empty field
                    stealFromPlayer(index);
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
        return MoveStatus.COMPLETED;
    }

    /**
     * Called after isGameOver() returns true
     * Fetches all stones left on other player's board side as the game is over and all stones
     * are supposed to go into his well
     */
    private void fetchAllStones() {
        int indexOfWell = this.board.getBoardBins()[otherPlayer.getNumber()].length - 1;
        for(int stones: this.board.getBoardBins()[otherPlayer.getNumber()]){
            this.board.getBoardBins()[otherPlayer.getNumber()][indexOfWell] += stones;
        }
    }

    /**
     * Checks if the sum of stones on the current player's board is equal to 0
     * if so the game ends as he cannot move
     * @return sum of stones on current player's board side
     */
    private boolean isGameOver() {
        int sum = 0;
        for(int stones: this.board.getBoardBins()[currentPlayer.getNumber()]){
            sum += stones;
        }
        return sum == 0;
    }

    /**
     * Steals all stones from other player's opposite bin if it's not empty
     * and puts them inside current player's well along with extra one
     * that was about tobe added
     * @param index
     */
    private void stealFromPlayer(int index) {
        int indexOfWell = this.board.getBoardBins()[currentPlayer.getNumber()].length - 1;
        int opposingIndex = binsNumber - 1 - index;
        if(this.board.getBoardBins()[otherPlayer.getNumber()][opposingIndex] != 0) {
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


    private Player selectWinner() {
        int indexOfWell = this.board.getBoardBins()[currentPlayer.getNumber()].length - 1;
        if(this.board.getBoardBins()[this.playerA.getNumber()][indexOfWell] > this.board.getBoardBins()[this.playerB.getNumber()][indexOfWell]) {
            System.out.println("PLAYER: " + playerA.name() + "WON");
            return this.playerA;
        } else {
            System.out.println("PLAYER: " + this.playerB.name() + "WON");
            return this.playerB;
        }
    }

    /**
     * Game loop that handles the game state.
     * Waits for player move and handles the move
     * accordingly to game rules.
     * @return Player - player who won the game
     */
    public Player start(){
        Scanner in = new Scanner(System.in);
        MoveStatus status = MoveStatus.START_GAME;
        while(status != MoveStatus.END_GAME) {
            this.getBoard().printBoard();
            System.out.println(currentPlayer.name() + " please pick a bin 1-6");
            int bin = in.nextInt();
            status = move(bin - 1);
            if(status == MoveStatus.COMPLETED) {
                changePlayers();
            }
        }
        return selectWinner();
    }

    public void minMax(){
        //TODO: implement min max
    }

    public void alphaBeta(){
        //TODO: implement alpha beta
    }
}
