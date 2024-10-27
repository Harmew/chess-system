package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.enums.Color;

public abstract class ChessPiece extends Piece {

    private final Color color;
    private Integer moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
        this.moveCount = 0;
    }

    public Color getColor() {
        return this.color;
    }

    public Integer getMoveCount() {
        return this.moveCount;
    }

    public void increaseMoveCount() {
        this.moveCount++;
    }

    public void decreaseMoveCount() {
        this.moveCount--;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(this.position);
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) this.getBoard().piece(position);
        return p != null && p.getColor() != this.color;
    }

}
