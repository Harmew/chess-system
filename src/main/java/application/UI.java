package application;

import application.enums.ANSI;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.enums.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < 50; i++) { // For "clear" terminal in IntelliJ
            System.out.println();
        }
    }

    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine().toUpperCase();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from A1 to H8.");
        }
    }

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turn: " + chessMatch.getTurn());
        if (!chessMatch.getCheckMate()) {
            System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println("CHECK!");
            }

        } else {
            System.out.println("CHECKMATE!");
            System.out.println("Winner: " + chessMatch.getCurrentPlayer());
        }
    }

    public static void printBoard(ChessPiece[][] pieces) {

        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");

            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], false);
            }
            System.out.println();
        }

        System.out.println("   A  B  C  D  E  F  G  H");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {

        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");

            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }

        System.out.println("   A  B  C  D  E  F  G  H");
    }

    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI.BLACK_BACKGROUND.getCode());
        }

        if (piece == null) {
            System.out.print(" - " + ANSI.RESET.getCode());
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(piece);
            } else {
                System.out.print(ANSI.WHITE.getCode() + piece + ANSI.RESET.getCode());
            }
        }

        System.out.print("");
    }

    private static void printCapturedPieces(List<ChessPiece> captured) {
        System.out.println("Captured pieces:");
        System.out.print(" - White: ");
        System.out.print(Arrays.toString(captured.stream().filter(piece -> piece.getColor() == Color.WHITE).toArray()));
        System.out.println(ANSI.WHITE.getCode());
        System.out.print(" - Black: ");
        System.out.print(Arrays.toString(captured.stream().filter(piece -> piece.getColor() == Color.BLACK).toArray()));
        System.out.println(ANSI.RESET.getCode());
    }
}
