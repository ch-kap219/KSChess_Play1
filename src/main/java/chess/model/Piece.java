package chess.model;

public class Piece
{
    public enum State
    {
        IDLE,
        MOVING,
        JUMPING,
        SHORT_REST,
        LONG_REST,
        CAPTURED
    }

    private final int id;
    private final char color;
    private char type;

    private Position position;
    private State state;
    private int restTimeLeft;

    public Piece(
            int id,
            char color,
            char type,
            Position position
    )
    {
        this.id = id;
        this.color = color;
        this.type = type;
        this.position = position;
        this.state = State.IDLE;
        this.restTimeLeft = 0;
    }

    public int getId()
    {
        return id;
    }

    public char getColor()
    {
        return color;
    }

    public char getType()
    {
        return type;
    }

    public void setType(char type)
    {
        this.type = type;
    }

    public Position getPosition()
    {
        return position;
    }

    public State getState()
    {
        return state;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public String getToken()
    {
        return "" + color + type;
    }

    public void startLongRest()
    {
        state = State.LONG_REST;
        restTimeLeft = 10_000;
    }

    public void startShortRest()
    {
        state = State.SHORT_REST;
        restTimeLeft = 3_000;
    }

    public void advanceRestTime(int milliseconds)
    {
        if (state != State.LONG_REST
                && state != State.SHORT_REST)
        {
            return;
        }

        restTimeLeft -= milliseconds;

        if (restTimeLeft <= 0)
        {
            restTimeLeft = 0;
            state = State.IDLE;
        }
    }

    public boolean canMove()
    {
        return state == State.IDLE;
    }

    public int getRestTimeLeft()
    {
        return restTimeLeft;
    }
}