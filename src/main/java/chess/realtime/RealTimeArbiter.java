package chess.realtime;

import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

public class RealTimeArbiter
{
    private final Board board;

    private Motion activeMotion;

    private Piece jumpingPiece;
    private int jumpTimeLeft;

    public RealTimeArbiter(Board board)
    {
        this.board = board;
        this.activeMotion = null;
        this.jumpingPiece = null;
        this.jumpTimeLeft = 0;
    }

    public boolean hasActiveMotion()
    {
        return activeMotion != null;
    }

    public void startMotion(
            Piece piece,
            Position source,
            Position destination
    )
    {
        if (hasActiveMotion())
        {
            throw new IllegalStateException("motion_in_progress");
        }

        activeMotion = new Motion(piece, source, destination);
        piece.setState(Piece.State.MOVING);
    }

    public void startJump(Piece piece)
    {
        if (piece == null)
        {
            return;
        }

        if (piece.getState() != Piece.State.IDLE)
        {
            return;
        }

        jumpingPiece = piece;
        jumpTimeLeft = 1000;
        piece.setState(Piece.State.JUMPING);
    }

    public boolean advanceTime(int milliseconds)
    {
        if (milliseconds < 0)
        {
            throw new IllegalArgumentException(
                    "Time cannot be negative"
            );
        }

        boolean jumpWasActive =
                jumpingPiece != null
                        && jumpTimeLeft > 0;

        if (activeMotion != null)
        {
            activeMotion.advanceTime(milliseconds);

            if (activeMotion.hasArrived())
            {
                Position from = activeMotion.getSource();
                Position to = activeMotion.getDestination();

                Piece movingPiece = activeMotion.getPiece();
                Piece targetPiece = board.getPiece(to);

                boolean targetIsJumping =
                        jumpWasActive
                                && targetPiece == jumpingPiece;

                if (targetIsJumping
                        && movingPiece.getColor()
                        != jumpingPiece.getColor())
                {
                    board.removePiece(from);
                    movingPiece.setState(Piece.State.CAPTURED);
                    activeMotion = null;
                }
                else
                {
                    boolean kingCaptured =
                            targetPiece != null
                                    && targetPiece.getType() == 'K';

                    board.movePiece(from, to);

                    promotePawn(movingPiece, to);

                    activeMotion = null;

                    updateJump(milliseconds);

                    return kingCaptured;
                }
            }
        }

        updateJump(milliseconds);

        return false;
    }

    private void promotePawn(Piece piece, Position position)
    {
        if (piece.getType() != 'P')
        {
            return;
        }

        boolean whiteReachedEnd =
                piece.getColor() == 'w'
                        && position.getRow() == 0;

        boolean blackReachedEnd =
                piece.getColor() == 'b'
                        && position.getRow()
                        == board.getHeight() - 1;

        if (whiteReachedEnd || blackReachedEnd)
        {
            piece.setType('Q');
        }
    }

    private void updateJump(int milliseconds)
    {
        if (jumpingPiece == null)
        {
            return;
        }

        jumpTimeLeft -= milliseconds;

        if (jumpTimeLeft <= 0)
        {
            jumpingPiece.setState(Piece.State.IDLE);
            jumpingPiece = null;
            jumpTimeLeft = 0;
        }
    }

    public Motion getActiveMotion()
    {
        return activeMotion;
    }
}