package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.enums.Color;

public class King extends ChessPiece {

    private final ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) this.getBoard().piece(position);
        return p instanceof Rook && p.getColor() == this.getColor() && p.getMoveCount() == 0;
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.BLACK) {
            return " ♔ ";
        } else {
            return " ♚ ";
        }
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) this.getBoard().piece(position);
        return p == null || p.getColor() != this.getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];

        Position p = new Position(0, 0);

        // ABOVE
        p.setValues(this.position.getRow() - 1, this.position.getColumn());
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // BELOW
        p.setValues(this.position.getRow() + 1, this.position.getColumn());
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // LEFT
        p.setValues(this.position.getRow(), this.position.getColumn() - 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // RIGHT
        p.setValues(this.position.getRow(), this.position.getColumn() + 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // NW
        p.setValues(this.position.getRow() - 1, this.position.getColumn() - 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // NE
        p.setValues(this.position.getRow() - 1, this.position.getColumn() + 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // SW
        p.setValues(this.position.getRow() + 1, this.position.getColumn() - 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // SE
        p.setValues(this.position.getRow() + 1, this.position.getColumn() + 1);
        if (this.getBoard().positionExists(p) && this.canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // # SpecialMove Castling
        if (this.getMoveCount() == 0 && !this.chessMatch.getCheck()) {
            // # SpecialMove Castling KingSide Rook
            Position positionRookOne = new Position(this.position.getRow(), this.position.getColumn() + 3);
            if (this.testRookCastling(positionRookOne)) {
                Position p1 = new Position(this.position.getRow(), this.position.getColumn() + 1);
                Position p2 = new Position(this.position.getRow(), this.position.getColumn() + 2);
                if (this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null) {
                    mat[this.position.getRow()][this.position.getColumn() + 2] = true;
                }
            }
            // # SpecialMove Castling QueenSide Rook
            Position positionRookTwo = new Position(this.position.getRow(), this.position.getColumn() - 4);
            if (this.testRookCastling(positionRookTwo)) {
                Position p1 = new Position(this.position.getRow(), this.position.getColumn() - 1);
                Position p2 = new Position(this.position.getRow(), this.position.getColumn() - 2);
                Position p3 = new Position(this.position.getRow(), this.position.getColumn() - 3);
                if (this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null && this.getBoard().piece(p3) == null) {
                    mat[this.position.getRow()][this.position.getColumn() - 2] = true;
                }
            }
        }

        return mat;
    }
}
