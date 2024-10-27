package boardgame;

public abstract class Piece {

    protected Position position;
    private final Board board;

    public Piece(Board board) {
        this.board = board;
        this.position = null;
    }

    protected Board getBoard() {
        return this.board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position) {
        return possibleMoves()[this.position.getRow()][this.position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (boolean[] booleans : mat) {
            for (int i = 0; i < mat.length; i++) {
                if (booleans[i]) {
                    return true;
                }
            }
        }
        return false;
    }
}
