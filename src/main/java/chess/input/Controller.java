package chess.input;

import chess.engine.GameEngine;
import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

import java.util.ArrayList;
import java.util.List;

public class Controller
{
    private final GameEngine engine;
    private final BoardMapper mapper;

    private Position selected;
    private List<Position> legalMoves;

    public Controller(GameEngine engine)
    {
        this.engine = engine;
        this.mapper = new BoardMapper();
        this.selected = null;
        this.legalMoves = new ArrayList<>();
    }

    public void click(int x, int y)
    {
        Board board = engine.getBoard();

        Position clicked =
                mapper.getPosition(x, y, board);

        // לחיצה מחוץ ללוח
        if (clicked == null)
        {
            selected = null;
            legalMoves.clear();
            return;
        }

        // לחיצה ראשונה - בחירת כלי
        if (selected == null)
        {
            if (board.isEmpty(clicked))
            {
                return;
            }

            selected = clicked;
            legalMoves =
                    engine.getLegalMoves(selected);

            return;
        }

        // לחיצה שנייה על אותו כלי - קפיצה
        if (selected.equals(clicked))
        {
            engine.jump(selected);

            selected = null;
            legalMoves.clear();

            return;
        }

        Piece selectedPiece =
                board.getPiece(selected);

        Piece clickedPiece =
                board.getPiece(clicked);

        // לחיצה על כלי אחר מאותו צבע
        if (clickedPiece != null
                && selectedPiece != null
                && clickedPiece.getColor()
                == selectedPiece.getColor())
        {
            selected = clicked;

            legalMoves =
                    engine.getLegalMoves(selected);

            return;
        }

        // ניסיון לבצע מהלך
        Position from = selected;

        selected = null;
        legalMoves.clear();

        engine.requestMove(from, clicked);
    }

    public void jump(int x, int y)
    {
        Board board = engine.getBoard();

        Position position =
                mapper.getPosition(x, y, board);

        if (position == null)
        {
            return;
        }

        engine.jump(position);
    }

    public Position getSelected()
    {
        return selected;
    }

    public List<Position> getLegalMoves()
    {
        return List.copyOf(legalMoves);
    }
}