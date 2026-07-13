package chess;

import chess.texttests.ScriptRunner;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        List<String> lines = List.of(
                "Board:",
                ". wR .",
                ". . .",
                ". . bK",
                "Commands:",
                "click 150 50",
                "click 150 250",
                "wait 1000",
                "print board",
                "wait 1000",
                "print board"
        );

        ScriptRunner runner = new ScriptRunner();
        runner.run(lines);
    }
}