package chess.engine;
import chess.realtime.Motion;
import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;
import chess.realtime.RealTimeArbiter;
import chess.rules.MoveValidation;
import chess.rules.RuleEngine;

public class GameEngine
{
    private final Board board;
    private final RuleEngine rules;
    private final RealTimeArbiter realtime;

    private boolean gameOver;

    public GameEngine(Board board)
    {
        this.board = board;
        this.rules = new RuleEngine();
        this.realtime = new RealTimeArbiter(board);
        this.gameOver = false;
    }

    public MoveResult requestMove(Position from, Position to)
    {
        if (gameOver)
        {
            return MoveResult.rejected("game_over");
        }

        if (realtime.hasActiveMotion())
        {
            return MoveResult.rejected("motion_in_progress");
        }

        MoveValidation result =
                rules.validateMove(board, from, to);

        if (!result.isValid())
        {
            return MoveResult.rejected(result.getReason());
        }

        Piece piece = board.getPiece(from);

        realtime.startMotion(piece, from, to);

        return MoveResult.accepted();
    }

    public void waitTime(int milliseconds)
    {
        boolean kingCaptured =
                realtime.advanceTime(milliseconds);

        if (kingCaptured)
        {
            gameOver = true;
        }
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }
    public GameSnapshot snapshot() {
        Motion motion =
                realtime.getActiveMotion();

        MotionSnapshot motionSnapshot = null;

        if (motion != null) {
            motionSnapshot = new MotionSnapshot(
                    motion.getPiece().getId(),
                    motion.getSource(),
                    motion.getDestination(),
                    motion.getDuration(),
                    motion.getElapsedTime()
            );
        }

        return new GameSnapshot(
                board,
                gameOver,
                motionSnapshot
        );
    }

    public void jump(Position position)
    {
        if (gameOver)
        {
            return;
        }

        Piece piece = board.getPiece(position);

        if (piece == null)
        {
            return;
        }

        realtime.startJump(piece);
    }
}