package chess.engine;

import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;
import chess.realtime.Motion;
import chess.realtime.RealTimeArbiter;
import chess.rules.MoveValidation;
import chess.rules.RuleEngine;

import java.util.ArrayList;
import java.util.List;

public class GameEngine
{
    private final Board board;
    private final RuleEngine rules;
    private final RealTimeArbiter realtime;

    private final List<String> whiteMoves = new ArrayList<>();
    private final List<String> blackMoves = new ArrayList<>();

    private int whiteScore;
    private int blackScore;
    private boolean gameOver;

    private Piece pendingPiece;
    private Piece pendingTarget;
    private Position pendingFrom;
    private Position pendingTo;

    public GameEngine(Board board)
    {
        this.board = board;
        this.rules = new RuleEngine();
        this.realtime = new RealTimeArbiter(board);
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

        pendingPiece = board.getPiece(from);
        pendingTarget = board.getPiece(to);
        pendingFrom = from;
        pendingTo = to;

        realtime.startMotion(
                pendingPiece,
                from,
                to
        );

        return MoveResult.accepted();
    }

    public void waitTime(int milliseconds)
    {
        boolean hadMotion =
                realtime.hasActiveMotion();

        boolean kingCaptured =
                realtime.advanceTime(milliseconds);

        if (hadMotion && !realtime.hasActiveMotion())
        {
            finishMove();
        }

        if (kingCaptured)
        {
            gameOver = true;
        }
    }

    private void finishMove()
    {
        if (pendingPiece == null
                || pendingFrom == null
                || pendingTo == null)
        {
            clearPending();
            return;
        }

        boolean attackerCaptured =
                pendingPiece.getState()
                        == Piece.State.CAPTURED;

        if (attackerCaptured)
        {
            if (pendingTarget != null)
            {
                addScore(
                        pendingTarget.getColor(),
                        pieceValue(pendingPiece.getType())
                );
            }

            addMove(
                    pendingPiece.getColor(),
                    moveText(" - captured")
            );
        }
        else
        {
            String ending = "";

            if (pendingTarget != null)
            {
                addScore(
                        pendingPiece.getColor(),
                        pieceValue(pendingTarget.getType())
                );

                ending =
                        " x " + pendingTarget.getType();
            }

            addMove(
                    pendingPiece.getColor(),
                    moveText(ending)
            );
        }

        clearPending();
    }

    private String moveText(String ending)
    {
        return pendingPiece.getType()
                + " "
                + positionText(pendingFrom)
                + " -> "
                + positionText(pendingTo)
                + ending;
    }

    private String positionText(Position position)
    {
        return "("
                + position.getRow()
                + ","
                + position.getCol()
                + ")";
    }

    private void addMove(char color, String move)
    {
        getMoves(color).add(move);
    }

    private void addScore(char color, int points)
    {
        if (color == 'w')
        {
            whiteScore += points;
        }
        else
        {
            blackScore += points;
        }
    }

    private List<String> getMoves(char color)
    {
        return color == 'w'
                ? whiteMoves
                : blackMoves;
    }

    private int pieceValue(char type)
    {
        return switch (type)
        {
            case 'P' -> 1;
            case 'N', 'B' -> 3;
            case 'R' -> 5;
            case 'Q' -> 9;
            case 'K' -> 100;
            default -> 0;
        };
    }

    private void clearPending()
    {
        pendingPiece = null;
        pendingTarget = null;
        pendingFrom = null;
        pendingTo = null;
    }

    public List<String> getWhiteMoves()
    {
        return List.copyOf(whiteMoves);
    }

    public List<String> getBlackMoves()
    {
        return List.copyOf(blackMoves);
    }

    public int getWhiteScore()
    {
        return whiteScore;
    }

    public int getBlackScore()
    {
        return blackScore;
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public GameSnapshot snapshot()
    {
        Motion motion =
                realtime.getActiveMotion();

        MotionSnapshot motionSnapshot =
                motion == null
                        ? null
                        : new MotionSnapshot(
                        motion.getPiece().getId(),
                        motion.getSource(),
                        motion.getDestination(),
                        motion.getDuration(),
                        motion.getElapsedTime()
                );

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

        if (piece != null)
        {
            realtime.startJump(piece);
        }
    }
}