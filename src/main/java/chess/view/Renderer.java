package chess.view;

import chess.engine.GameSnapshot;
import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

import java.awt.Color;
import java.awt.Dimension;

public class Renderer {

    private static final int CELL_SIZE = 100;

    private static final String BOARD_PATH =
            "src/main/images/board.png";

    private static final String PIECES_PATH =
            "src/main/images/pieces2/";

    public Img render(
            GameSnapshot snapshot,
            Position selected
    ) {
        Board board = snapshot.getBoard();

        int boardWidth =
                board.getWidth() * CELL_SIZE;

        int boardHeight =
                board.getHeight() * CELL_SIZE;

        // בכל ציור יוצרים מחדש תמונה של הלוח
        Img canvas = new Img().read(
                BOARD_PATH,
                new Dimension(boardWidth, boardHeight),
                false,
                null
        );

        drawPieces(canvas, board);
        drawSelection(canvas, selected);

        if (snapshot.isGameOver()) {
            drawGameOver(canvas);
        }

        return canvas;
    }

    private void drawPieces(
            Img canvas,
            Board board
    ) {
        for (int row = 0;
             row < board.getHeight();
             row++) {

            for (int col = 0;
                 col < board.getWidth();
                 col++) {

                Position position =
                        new Position(row, col);

                Piece piece =
                        board.getPiece(position);

                if (piece == null) {
                    continue;
                }

                drawPiece(
                        canvas,
                        piece,
                        row,
                        col
                );
            }
        }
    }

    private void drawPiece(
            Img canvas,
            Piece piece,
            int row,
            int col
    ) {
        String folderName =
                createPieceFolderName(piece);

        String imagePath =
                PIECES_PATH
                        + folderName
                        + "/states/idle/sprites/1.png";

        Img pieceImage = new Img().read(
                imagePath,
                new Dimension(CELL_SIZE, CELL_SIZE),
                true,
                null
        );

        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;

        pieceImage.drawOn(canvas, x, y);
    }

    private String createPieceFolderName(
            Piece piece
    ) {
        char type = piece.getType();

        char color =
                Character.toUpperCase(
                        piece.getColor()
                );

        return "" + type + color;
    }

    private void drawSelection(
            Img canvas,
            Position selected
    ) {
        if (selected == null) {
            return;
        }

        int x =
                selected.getCol() * CELL_SIZE;

        int y =
                selected.getRow() * CELL_SIZE;

        canvas.drawRect(
                x,
                y,
                CELL_SIZE,
                CELL_SIZE,
                Color.YELLOW,
                6
        );
    }

    private void drawGameOver(Img canvas) {
        canvas.putText(
                "Game Over",
                310,
                400,
                4.0f,
                Color.RED,
                2
        );
    }
}