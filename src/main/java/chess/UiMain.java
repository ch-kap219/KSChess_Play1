package chess;

import chess.engine.GameEngine;
import chess.input.Controller;
import chess.io.BoardParser;
import chess.model.Board;
import chess.view.GameWindow;
import chess.view.Renderer;

import java.util.List;

public class UiMain {

    public static void main(String[] args) {

        BoardParser parser =
                new BoardParser();

        Board board = parser.parse(
                List.of(
                        "bR bN bB bQ bK bB bN bR",
                        "bP bP bP bP bP bP bP bP",
                        ". . . . . . . .",
                        ". . . . . . . .",
                        ". . . . . . . .",
                        ". . . . . . . .",
                        "wP wP wP wP wP wP wP wP",
                        "wR wN wB wQ wK wB wN wR"
                )
        );

        GameEngine engine =
                new GameEngine(board);

        Controller controller =
                new Controller(engine);

        Renderer renderer =
                new Renderer();

        GameWindow window =
                new GameWindow(
                        engine,
                        controller,
                        renderer
                );

        window.start();
    }
}