package chess.realtime;

import chess.model.Piece;
import chess.model.Position;

public class Motion {

    private final Piece piece;
    private final Position source;
    private final Position destination;
    private final int duration;

    private int elapsedTime;

    public Motion(
            Piece piece,
            Position source,
            Position destination
    ) {
        this.piece = piece;
        this.source = source;
        this.destination = destination;

        int rowDistance = Math.abs(
                destination.getRow() - source.getRow()
        );

        int colDistance = Math.abs(
                destination.getCol() - source.getCol()
        );

        int cellDistance = Math.max(
                rowDistance,
                colDistance
        );

        this.duration = cellDistance * 1000;
        this.elapsedTime = 0;
    }

    public Piece getPiece() {
        return piece;
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

    public void advanceTime(int milliseconds) {
        elapsedTime += milliseconds;
    }

    public boolean hasArrived() {
        return elapsedTime >= duration;
    }
}