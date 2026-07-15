package chess.engine;

import chess.model.Board;

public class GameSnapshot {

    private final Board board;
    private final boolean gameOver;
    private final MotionSnapshot activeMotion;

    public GameSnapshot(
            Board board,
            boolean gameOver,
            MotionSnapshot activeMotion
    ) {
        this.board = board;
        this.gameOver = gameOver;
        this.activeMotion = activeMotion;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public MotionSnapshot getActiveMotion() {
        return activeMotion;
    }
}