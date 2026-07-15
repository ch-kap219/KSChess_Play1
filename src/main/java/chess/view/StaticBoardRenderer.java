package chess.view;

import java.awt.Dimension;

public class StaticBoardRenderer {

    private static final int CELL_SIZE = 100;
    private static final int BOARD_SIZE = 8;

    private static final String IMAGES_PATH =
            "src/main/images/";

    private static final String BOARD_PATH =
            IMAGES_PATH + "board.png";

    private static final String PIECES_PATH =
            IMAGES_PATH + "pieces2/";

    public Img createBoard() {

        // טוענים את הלוח ומכריחים אותו להיות בגודל 800x800
        Img board = new Img().read(
                BOARD_PATH,
                new Dimension(
                        BOARD_SIZE * CELL_SIZE,
                        BOARD_SIZE * CELL_SIZE
                ),
                false,
                null
        );

        drawBlackPieces(board);
        drawWhitePieces(board);

        return board;
    }

    private void drawBlackPieces(Img board) {

        // השורה האחורית של השחורים
        String[] pieces = {
                "RB", "NB", "BB", "QB",
                "KB", "BB", "NB", "RB"
        };

        for (int col = 0; col < BOARD_SIZE; col++) {
            drawPiece(board, pieces[col], 0, col);
        }

        // שמונת הרגלים השחורים
        for (int col = 0; col < BOARD_SIZE; col++) {
            drawPiece(board, "PB", 1, col);
        }
    }

    private void drawWhitePieces(Img board) {

        // שמונת הרגלים הלבנים
        for (int col = 0; col < BOARD_SIZE; col++) {
            drawPiece(board, "PW", 6, col);
        }

        // השורה האחורית של הלבנים
        String[] pieces = {
                "RW", "NW", "BW", "QW",
                "KW", "BW", "NW", "RW"
        };

        for (int col = 0; col < BOARD_SIZE; col++) {
            drawPiece(board, pieces[col], 7, col);
        }
    }

    private void drawPiece(
            Img board,
            String pieceFolder,
            int row,
            int col
    ) {

        String imagePath =
                PIECES_PATH
                        + pieceFolder
                        + "/states/idle/sprites/1.png";

        Img piece = new Img().read(
                imagePath,
                new Dimension(CELL_SIZE, CELL_SIZE),
                true,
                null
        );

        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;

        piece.drawOn(board, x, y);
    }
}