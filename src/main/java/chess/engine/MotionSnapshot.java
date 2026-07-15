package chess.engine;

import chess.model.Position;

public class MotionSnapshot {

    private final int pieceId;
    private final Position source;
    private final Position destination;
    private final int duration;
    private final int elapsedTime;

    public MotionSnapshot(
            int pieceId,
            Position source,
            Position destination,
            int duration,
            int elapsedTime
    ) {
        this.pieceId = pieceId;
        this.source = source;
        this.destination = destination;
        this.duration = duration;
        this.elapsedTime = elapsedTime;
    }

    public int getPieceId() {
        return pieceId;
    }

    public Position getSource() {
        return source;
    }

    public Position getDestination() {
        return destination;
    }

    public int getDuration() {
        return duration;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }
}