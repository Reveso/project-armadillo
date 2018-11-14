package testpackage;

public class Game {

    private Player player1;
    private Player player2;
    private Board board;
    private Move move;

    public Game(Player player1, Player player2, Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void playGame() {
        while(endOfGame()) {
            move = player1.askForMove();
            board.setNewMove(move);
            board.updateFreeCells();
            move = player2.askForMove();
            board.setNewMove(move);
            board.updateFreeCells();
        }
    }

    private boolean endOfGame() {
        return false;
    }
}
