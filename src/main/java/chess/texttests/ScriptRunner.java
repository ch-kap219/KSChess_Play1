package chess.texttests;

import chess.engine.GameEngine;
import chess.input.Controller;
import chess.io.BoardParser;
import chess.io.BoardPrinter;
import chess.model.Board;

import java.util.ArrayList;
import java.util.List;

public class ScriptRunner
{
    private final BoardParser parser;
    private final BoardPrinter printer;

    public ScriptRunner()//בנאי
    {
        parser = new BoardParser();//הופך שורות טקסט ללוח אמיתי.
        printer = new BoardPrinter();//הופך את הלוח בחזרה לטקסט לצורך הדפסה.
    }

    public void run(List<String> lines)// בודק אלו שורות שייכות ללוח
    {
        ArrayList<String> boardRows = new ArrayList<>();

        boolean readingBoard = false;
        GameEngine engine = null;
        Controller controller = null;

        for (String originalLine : lines)
        {
            String line = originalLine.trim();

            if (line.isEmpty())
            {
                continue;
            }

            if (line.equals("Board")
                    || line.equals("Board:"))
            {
                readingBoard = true;
                continue;
            }

            if (line.equals("Commands:"))
            {
                if (engine == null)
                {
                    Board board = parser.parse(boardRows);
                    engine = new GameEngine(board);
                    controller = new Controller(engine);
                }

                readingBoard = false;
                continue;
            }

            if (line.startsWith("click")
                    || line.startsWith("jump")
                    || line.startsWith("wait")
                    || line.equals("print board"))
            {
                if (engine == null)
                {
                    Board board = parser.parse(boardRows);
                    engine = new GameEngine(board);
                    controller = new Controller(engine);
                }

                readingBoard = false;
            }

            if (readingBoard)
            {
                boardRows.add(line);
                continue;
            }

            if (line.startsWith("jump"))
            {
                String[] parts = line.split("\\s+");

                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                controller.jump(x, y);
            }
            else if (line.startsWith("click"))
            {
                String[] parts = line.split("\\s+");

                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                controller.click(x, y);
            }
            else if (line.startsWith("wait"))
            {
                String[] parts = line.split("\\s+");

                int time = Integer.parseInt(parts[1]);

                engine.waitTime(time);
            }
            else if (line.equals("print board"))
            {
                System.out.println(
                        printer.print(engine.getBoard())
                );
            }
        }
    }
}