package chess.input;

import chess.engine.GameEngine;
import chess.model.Board;
import chess.model.Position;

public class Controller
{
    private final GameEngine engine;
    private final BoardMapper mapper;

    private Position selected;

    public Controller(GameEngine engine)
    {
        this.engine = engine;
        this.mapper = new BoardMapper();
        this.selected = null;
    }

    public void click(int x, int y)
    {
        Board board = engine.getBoard();

        Position clicked =
                mapper.getPosition(x, y, board);

        if (clicked == null)
        {
            if (selected != null)
            {
                selected = null;
            }

            return;
        }

        if (selected == null)
        {
            if (board.isEmpty(clicked))
            {
                return;
            }

            selected = clicked;
            return;
        }

        Position from = selected;
        selected = null;

        engine.requestMove(from, clicked);
    }

    public Position getSelected()
    {
        return selected;
    }
}