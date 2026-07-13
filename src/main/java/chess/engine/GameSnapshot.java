package chess.engine;

import chess.model.Board;

public class GameSnapshot
{
    private final Board board;
    private final boolean gameOver;

    public GameSnapshot(Board board, boolean gameOver)
    {
        this.board = board;
        this.gameOver = gameOver;
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }
}