package boardgame;

import boardgame.exceptions.BoardException;

public class Board {

    private final Integer rows;
    private final Integer columns;
    private final Piece[][] pieces;

    public Board(Integer rows, Integer columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }

        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public Integer getRows() {
        return this.rows;
    }

    public Integer getColumns() {
        return this.columns;
    }

    public Piece piece(Position position) {
        if (!this.positionExists(position)) throw new BoardException("Position not on the beard");
        return this.pieces[position.getRow()][position.getColumn()];
    }

    public Piece piece(Integer row, Integer column) {
        if (!this.positionExists(row, column)) throw new BoardException("Position not on the beard");
        return this.pieces[row][column];
    }

    public void placePiece(Piece piece, Position position) {
        if (this.thereIsAPiece(position)) throw new BoardException("There is already a piece on position " + position);
        this.pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public Piece removePiece(Position position) {
        if (!this.positionExists(position)) throw new BoardException("Position not on the beard");
        if (this.piece(position) == null) return null;

        Piece aux = this.piece(position);
        aux.position = null;
        this.pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    public boolean thereIsAPiece(Position position) {
        if (!this.positionExists(position)) throw new BoardException("Position not on the beard");
        return this.piece(position) != null;
    }

    public boolean positionExists(Position position) {
        return this.positionExists(position.getRow(), position.getColumn());
    }

    /**
     * Return boolean with position exists based in provided row and column
     */
    private boolean positionExists(int row, int column) {
        return row >= 0 && row < this.rows && column >= 0 && column < this.columns;
    }
}
