package chess.input;

import chess.model.Board;
import chess.model.Position;

public class BoardMapper
{
    private static final int CELL_SIZE = 100;

    public Position getPosition(int x, int y, Board board)
    {
        if (x < 0 || y < 0)
        {
            return null;
        }

        int row = y / CELL_SIZE;
        int col = x / CELL_SIZE;

        Position position = new Position(row, col);

        if (!board.isInside(position))
        {
            return null;
        }

        return position;
    }
}