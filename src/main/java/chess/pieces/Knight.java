package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.enums.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.BLACK) {
            return " ♘ ";
        } else {
            return " ♞ ";
        }
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) this.getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];

        Position p = new Position(0, 0);

        p.setValues(this.position.getRow() - 1, this.position.getColumn() - 2);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(this.position.getRow() - 2, this.position.getColumn() - 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(this.position.getRow() - 2, this.position.getColumn() + 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(this.position.getRow() - 1, this.position.getColumn() + 2);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(this.position.getRow() + 1, this.position.getColumn() + 2);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(this.position.getRow() + 2, this.position.getColumn() + 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(this.position.getRow() + 2, this.position.getColumn() - 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(this.position.getRow() + 1, this.position.getColumn() - 2);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }
}
