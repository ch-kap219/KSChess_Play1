package chess;

import chess.texttests.ScriptRunner;

import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> lines = new ArrayList<>();

        while (scanner.hasNextLine())
        {
            lines.add(scanner.nextLine());
        }

        ScriptRunner runner = new ScriptRunner();
        runner.run(lines);
    }
}