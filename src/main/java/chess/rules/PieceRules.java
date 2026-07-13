package chess.rules;

import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

public class PieceRules {

    public boolean isValidMove(
            Board board,
            Piece piece,
            Position source,
            Position destination
    ) {
        int dRow = Math.abs(
                destination.getRow() - source.getRow()
        );

        int dCol = Math.abs(
                destination.getCol() - source.getCol()
        );

        if (dRow == 0 && dCol == 0) {
            return false;
        }

        switch (piece.getType()) {
            case 'K':
                return isKingMove(dRow, dCol);

            case 'R':
                return isRookMove(
                        board,
                        source,
                        destination,
                        dRow,
                        dCol
                );

            case 'B':
                return isBishopMove(
                        board,
                        source,
                        destination,
                        dRow,
                        dCol
                );

            case 'Q':
                return isQueenMove(
                        board,
                        source,
                        destination,
                        dRow,
                        dCol
                );

            case 'N':
                return isKnightMove(dRow, dCol);

            case 'P':
                return isPawnMove(
                        board,
                        piece,
                        source,
                        destination,
                        dCol
                );

            default:
                return false;
        }
    }

    private boolean isKingMove(int dRow, int dCol) {
        return dRow <= 1 && dCol <= 1;
    }

    private boolean isKnightMove(int dRow, int dCol) {
        return (dRow == 2 && dCol == 1)
                || (dRow == 1 && dCol == 2);
    }

    private boolean isRookMove(
            Board board,
            Position source,
            Position destination,
            int dRow,
            int dCol
    ) {
        if (dRow != 0 && dCol != 0) {
            return false;
        }

        return isPathClear(board, source, destination);
    }

    private boolean isBishopMove(
            Board board,
            Position source,
            Position destination,
            int dRow,
            int dCol
    ) {
        if (dRow != dCol) {
            return false;
        }

        return isPathClear(board, source, destination);
    }

    private boolean isQueenMove(
            Board board,
            Position source,
            Position destination,
            int dRow,
            int dCol
    ) {
        boolean straight = dRow == 0 || dCol == 0;
        boolean diagonal = dRow == dCol;

        if (!straight && !diagonal) {
            return false;
        }

        return isPathClear(board, source, destination);
    }

    private boolean isPawnMove(
            Board board,
            Piece piece,
            Position source,
            Position destination,
            int dCol
    ) {
        int direction =
                piece.getColor() == 'w' ? -1 : 1;

        int rowDirection =
                destination.getRow() - source.getRow();

        // תנועה רגילה של משבצת אחת
        if (rowDirection == direction && dCol == 0) {
            return board.isEmpty(destination);
        }

        // צעד כפול לפי הכללים הישנים שלך
        int startRow =
                piece.getColor() == 'w'
                        ? board.getHeight() - 1
                        : 0;

        if (rowDirection == 2 * direction
                && source.getRow() == startRow
                && dCol == 0) {

            Position middle = new Position(
                    source.getRow() + direction,
                    source.getCol()
            );

            return board.isEmpty(middle)
                    && board.isEmpty(destination);
        }

        // אכילה באלכסון
        if (rowDirection == direction && dCol == 1) {
            return !board.isEmpty(destination);
        }

        return false;
    }

    private boolean isPathClear(
            Board board,
            Position source,
            Position destination
    ) {
        int rowStep = Integer.compare(
                destination.getRow(),
                source.getRow()
        );

        int colStep = Integer.compare(
                destination.getCol(),
                source.getCol()
        );

        int currentRow = source.getRow() + rowStep;
        int currentCol = source.getCol() + colStep;

        while (currentRow != destination.getRow()
                || currentCol != destination.getCol()) {

            Position current =
                    new Position(currentRow, currentCol);

            if (!board.isEmpty(current)) {
                return false;
            }

            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }
}