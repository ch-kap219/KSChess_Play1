package chess.io;

import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

import java.util.List;

public class BoardParser {

    public Board parse(List<String> rows) {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("ERROR EMPTY_BOARD");
        }

        int height = rows.size();
        int width = splitRow(rows.get(0)).length;

        if (width == 0) {
            throw new IllegalArgumentException("ERROR EMPTY_BOARD");
        }

        Board board = new Board(height, width);
        int nextPieceId = 1;

        for (int row = 0; row < height; row++) {
            String[] tokens = splitRow(rows.get(row));

            if (tokens.length != width) {
                throw new IllegalArgumentException(
                        "ERROR ROW_WIDTH_MISMATCH"
                );
            }

            for (int col = 0; col < width; col++) {
                String token = tokens[col];

                if (!isValidToken(token)) {
                    throw new IllegalArgumentException(
                            "ERROR UNKNOWN_TOKEN"
                    );
                }

                if (token.equals(".")) {
                    continue;
                }

                char color = token.charAt(0);
                char type = token.charAt(1);
                Position position = new Position(row, col);

                Piece piece = new Piece(
                        nextPieceId,
                        color,
                        type,
                        position
                );

                board.addPiece(piece);
                nextPieceId++;
            }
        }

        return board;
    }

    private String[] splitRow(String row) {
        String trimmedRow = row.trim();

        if (trimmedRow.isEmpty()) {
            return new String[0];
        }

        return trimmedRow.split("\\s+");
    }

    private boolean isValidToken(String token) {
        if (token.equals(".")) {
            return true;
        }

        if (token.length() != 2) {
            return false;
        }

        char color = token.charAt(0);
        char type = token.charAt(1);

        boolean validColor = color == 'w' || color == 'b';
        boolean validType = "KQRBNP".indexOf(type) >= 0;

        return validColor && validType;
    }
}