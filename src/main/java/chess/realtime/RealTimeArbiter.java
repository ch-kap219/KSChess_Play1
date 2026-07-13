package chess.realtime;

import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

public class RealTimeArbiter {

    private final Board board;
    private Motion activeMotion;

    public RealTimeArbiter(Board board) {
        this.board = board;
        this.activeMotion = null;
    }

    public boolean hasActiveMotion() {
        return activeMotion != null;
    }

    public void startMotion(
            Piece piece,
            Position source,
            Position destination
    ) {
        if (hasActiveMotion()) {
            throw new IllegalStateException(
                    "motion_in_progress"
            );
        }

        activeMotion = new Motion(
                piece,
                source,
                destination
        );

        piece.setState(Piece.State.MOVING);
    }

    public boolean advanceTime(int milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException(
                    "Time cannot be negative"
            );
        }

        if (activeMotion == null) {
            return false;
        }

        activeMotion.advanceTime(milliseconds);

        if (!activeMotion.hasArrived()) {
            return false;
        }

        Position source =
                activeMotion.getSource();

        Position destination =
                activeMotion.getDestination();

        Piece capturedPiece =
                board.getPiece(destination);

        boolean kingCaptured =
                capturedPiece != null
                        && capturedPiece.getType() == 'K';

        board.movePiece(source, destination);

        activeMotion = null;

        return kingCaptured;
    }

    public Motion getActiveMotion() {
        return activeMotion;
    }
}