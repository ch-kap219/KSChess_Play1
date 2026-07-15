package chess.input;

import chess.engine.GameEngine;
import chess.model.Board;
import chess.model.Piece;
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
            selected = null;
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

        /*
         * לחיצה שנייה על אותה משבצת:
         * הכלי קופץ במקום.
         */
        if (selected.equals(clicked))
        {
            engine.jump(selected);
            selected = null;
            return;
        }

        Piece selectedPiece =
                board.getPiece(selected);

        Piece clickedPiece =
                board.getPiece(clicked);

        /*
         * לחיצה על כלי אחר מאותו צבע:
         * מחליפים את הבחירה.
         */
        if (clickedPiece != null && selectedPiece != null
                && clickedPiece.getColor() == selectedPiece.getColor())
        {
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