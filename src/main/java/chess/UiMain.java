package chess;

import chess.view.Img;
import chess.view.StaticBoardRenderer;

public class UiMain {

    public static void main(String[] args) {

        StaticBoardRenderer renderer =
                new StaticBoardRenderer();

        Img boardWithPieces =
                renderer.createBoard();

        boardWithPieces.show();
    }
}