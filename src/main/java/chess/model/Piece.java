package chess.model;

public class Piece {

    public enum State {
        IDLE,
        MOVING,
        CAPTURED,
        JUMPING
    }

    private final int id;
    private final char color;
    private final char type;

    private Position position;
    private State state;

    public Piece(int id, char color, char type, Position position) {
        this.id = id;
        this.color = color;
        this.type = type;
        this.position = position;
        this.state = State.IDLE;
    }

    public int getId() {
        return id;
    }

    public char getColor() {
        return color;
    }

    public char getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public State getState() {
        return state;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getToken() {
        return "" + color + type;
    }
}