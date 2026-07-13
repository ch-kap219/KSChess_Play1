package chess.rules;

public class MoveValidation {

    private final boolean valid;
    private final String reason;

    public MoveValidation(boolean valid, String reason) {
        this.valid = valid;
        this.reason = reason;
    }

    public boolean isValid() {
        return valid;
    }

    public String getReason() {
        return reason;
    }

    public static MoveValidation valid() {
        return new MoveValidation(true, "ok");
    }

    public static MoveValidation invalid(String reason) {
        return new MoveValidation(false, reason);
    }
}