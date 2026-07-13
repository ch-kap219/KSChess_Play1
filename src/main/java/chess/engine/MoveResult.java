package chess.engine;

public class MoveResult
{
    private final boolean accepted;
    private final String reason;

    public MoveResult(boolean accepted, String reason)
    {
        this.accepted = accepted;
        this.reason = reason;
    }

    public boolean isAccepted()
    {
        return accepted;
    }

    public String getReason()
    {
        return reason;
    }

    public static MoveResult accepted()
    {
        return new MoveResult(true, "ok");
    }

    public static MoveResult rejected(String reason)
    {
        return new MoveResult(false, reason);
    }
}