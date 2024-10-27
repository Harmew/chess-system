package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.enums.Color;
import chess.exceptions.ChessException;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChessMatch {

    private Integer turn;
    private Color currentPlayer;
    private final Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private final List<Piece> piecesOnTheBoard = new ArrayList<>();
    private final List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        this.board = new Board(8, 8);
        this.turn = 1;
        this.currentPlayer = Color.WHITE;
        this.check = false;
        this.enPassantVulnerable = null;
        this.initialSetup();
    }

    public Integer getTurn() {
        return this.turn;
    }

    public Color getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean getCheck() {
        return this.check;
    }

    public boolean getCheckMate() {
        return this.checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return this.enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return this.promoted;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[this.board.getRows()][this.board.getColumns()];
        for (int i = 0; i < this.board.getRows(); i++) {
            for (int j = 0; j < this.board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) this.board.piece(i, j);
            }
        }

        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        this.validateSourcePosition(position);
        return this.board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        this.validateSourcePosition(source);
        this.validateTargetPosition(source, target);
        Piece capturedPiece = this.makeMove(source, target);

        if (this.testCheck(this.currentPlayer)) {
            this.undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // # SpecialMove Promotion
        this.promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                this.promoted = (ChessPiece) this.board.piece(target);
                this.promoted = this.replacePromotedPiece("Q");

            }
        }

        this.check = this.testCheck(opponent(this.currentPlayer));

        if (this.testCheckMate(opponent(this.currentPlayer))) {
            this.checkMate = true;
        } else {
            this.nextTurn();
        }

        // # SpecialMove En Passant
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            this.enPassantVulnerable = movedPiece;
        } else {
            this.enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (this.promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }

        if (!type.equalsIgnoreCase("B") && !type.equalsIgnoreCase("N") && !type.equalsIgnoreCase("R") && !type.equalsIgnoreCase("Q")) {
            return promoted;
        }

        Position pos = this.promoted.getChessPosition().toPosition();
        Piece p = this.board.removePiece(pos);
        this.piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, this.promoted.getColor());
        this.board.placePiece(newPiece, pos);
        this.piecesOnTheBoard.add(newPiece);
        return newPiece;
    }

    public void initialSetup() {
        // WHITE PIECES
        this.placeNewPiece('A', 1, new Rook(this.board, Color.WHITE));
        this.placeNewPiece('B', 1, new Knight(this.board, Color.WHITE));
        this.placeNewPiece('C', 1, new Bishop(this.board, Color.WHITE));
        this.placeNewPiece('D', 1, new Queen(this.board, Color.WHITE));
        this.placeNewPiece('E', 1, new King(this.board, Color.WHITE, this));
        this.placeNewPiece('F', 1, new Bishop(this.board, Color.WHITE));
        this.placeNewPiece('G', 1, new Knight(this.board, Color.WHITE));
        this.placeNewPiece('H', 1, new Rook(this.board, Color.WHITE));
        this.placeNewPiece('A', 2, new Pawn(this.board, Color.WHITE, this));
        this.placeNewPiece('B', 2, new Pawn(this.board, Color.WHITE, this));
        this.placeNewPiece('C', 2, new Pawn(this.board, Color.WHITE, this));
        this.placeNewPiece('D', 2, new Pawn(this.board, Color.WHITE, this));
        this.placeNewPiece('E', 2, new Pawn(this.board, Color.WHITE, this));
        this.placeNewPiece('F', 2, new Pawn(this.board, Color.WHITE, this));
        this.placeNewPiece('G', 2, new Pawn(this.board, Color.WHITE, this));
        this.placeNewPiece('H', 2, new Pawn(this.board, Color.WHITE, this));

        // BLACK PIECES
        this.placeNewPiece('A', 8, new Rook(this.board, Color.BLACK));
        this.placeNewPiece('B', 8, new Knight(this.board, Color.BLACK));
        this.placeNewPiece('C', 8, new Bishop(this.board, Color.BLACK));
        this.placeNewPiece('D', 8, new Queen(this.board, Color.BLACK));
        this.placeNewPiece('E', 8, new King(this.board, Color.BLACK, this));
        this.placeNewPiece('G', 8, new Bishop(this.board, Color.BLACK));
        this.placeNewPiece('F', 8, new Knight(this.board, Color.BLACK));
        this.placeNewPiece('H', 8, new Rook(this.board, Color.BLACK));
        this.placeNewPiece('A', 7, new Pawn(this.board, Color.BLACK, this));
        this.placeNewPiece('B', 7, new Pawn(this.board, Color.BLACK, this));
        this.placeNewPiece('C', 7, new Pawn(this.board, Color.BLACK, this));
        this.placeNewPiece('D', 7, new Pawn(this.board, Color.BLACK, this));
        this.placeNewPiece('E', 7, new Pawn(this.board, Color.BLACK, this));
        this.placeNewPiece('F', 7, new Pawn(this.board, Color.BLACK, this));
        this.placeNewPiece('G', 7, new Pawn(this.board, Color.BLACK, this));
        this.placeNewPiece('H', 7, new Pawn(this.board, Color.BLACK, this));
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        this.board.placePiece(piece, new ChessPosition(column, row).toPosition());
        this.piecesOnTheBoard.add(piece);
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = this.piecesOnTheBoard.stream().filter(piece -> ((ChessPiece) piece).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private void nextTurn() {
        this.turn++;
        this.currentPlayer = (this.currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece piece = (ChessPiece) this.board.removePiece(source);
        piece.increaseMoveCount();
        Piece capturedPiece = this.board.removePiece(target);
        if (capturedPiece != null) {
            this.piecesOnTheBoard.remove(capturedPiece);
            this.capturedPieces.add(capturedPiece);
        }

        // # SpecialMove Castling KingSide Rook
        if (piece instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
            Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) this.board.removePiece(sourceRook);
            this.board.placePiece(rook, targetRook);
            rook.increaseMoveCount();
        }

        // # SpecialMove Castling QueenSide Rook
        if (piece instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
            Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) this.board.removePiece(sourceRook);
            this.board.placePiece(rook, targetRook);
            rook.increaseMoveCount();
        }

        // # SpecialMove En Passant
        if (piece instanceof Pawn) {
            if (!Objects.equals(source.getColumn(), target.getColumn()) && capturedPiece == null) {
                Position pawnPosition;
                if (piece.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }

                capturedPiece = this.board.removePiece(pawnPosition);
                this.capturedPieces.add(capturedPiece);
                this.piecesOnTheBoard.remove(capturedPiece);
            }
        }

        this.board.placePiece(piece, target);

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece piece = (ChessPiece) this.board.removePiece(target);
        piece.decreaseMoveCount();
        this.board.placePiece(piece, source);

        if (capturedPiece != null) {
            this.board.placePiece(capturedPiece, target);
            this.capturedPieces.remove(capturedPiece);
            this.piecesOnTheBoard.add(capturedPiece);
        }

        // # SpecialMove Castling KingSide Rook
        if (piece instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
            Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) this.board.removePiece(targetRook);
            this.board.placePiece(rook, sourceRook);
            rook.decreaseMoveCount();
        }

        // # SpecialMove Castling QueenSide Rook
        if (piece instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
            Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) this.board.removePiece(targetRook);
            this.board.placePiece(rook, sourceRook);
            rook.decreaseMoveCount();
        }

        // # SpecialMove En Passant
        if (piece instanceof Pawn) {
            if (!Objects.equals(source.getColumn(), target.getColumn()) && capturedPiece == this.enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) this.board.removePiece(target);
                Position pawnPosition;
                if (piece.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }

                this.board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private void validateSourcePosition(Position position) {
        if (!this.board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (this.currentPlayer != ((ChessPiece) this.board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!this.board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!this.board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private boolean testCheck(Color color) {
        Position kingPosition = this.king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = this.piecesOnTheBoard.stream().filter(piece -> ((ChessPiece) piece).getColor() == this.opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }

        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!this.testCheck(color)) {
            return false;
        }

        List<Piece> list = this.piecesOnTheBoard.stream().filter(piece -> ((ChessPiece) piece).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {

            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < this.board.getRows(); i++) {
                for (int j = 0; j < this.board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = this.makeMove(source, target);
                        boolean testCheck = this.testCheck(color);
                        this.undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equalsIgnoreCase("B")) return new Bishop(board, color);
        if (type.equalsIgnoreCase("N")) return new Knight(board, color);
        if (type.equalsIgnoreCase("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }
}
