package chess;

import boardgame.Position;
import chess.exceptions.ChessException;

public class ChessPosition {

    private final Character column;
    private final Integer row;

    public ChessPosition(Character column, Integer row) {
        if (column < 'A' || column > 'H' || row < 1 || row > 8) {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from A1 to H8.");
        }

        this.column = column;
        this.row = row;
    }

    public Character getColumn() {
        return this.column;
    }

    public Integer getRow() {
        return this.row;
    }

    protected Position toPosition() {
        return new Position(8 - this.row, this.column - 'A');
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char) ('A' + position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return "" + this.column + this.row;
    }
}
