package chess.io;

import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

public class BoardPrinter {

    public String print(Board board) {
        StringBuilder result = new StringBuilder();

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {

                Position position = new Position(row, col);
                Piece piece = board.getPiece(position);

                if (piece == null) {
                    result.append(".");
                } else {
                    result.append(piece.getToken());
                }

                if (col < board.getWidth() - 1) {
                    result.append(" ");
                }
            }

            if (row < board.getHeight() - 1) {
                result.append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    public void printToConsole(Board board) {
        System.out.println(print(board));
    }
}