package chess.view;

import chess.engine.GameSnapshot;
import chess.engine.MotionSnapshot;
import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

public class Renderer
{
    private static final int CELL_SIZE = 100;
    private static final int TOTAL_REST_TIME = 10_000;

    private static final String BOARD_PATH =
            "src/main/images/board.png";

    private static final String PIECES_PATH =
            "src/main/images/pieces2/";

    private final PieceImageLoader imageLoader;

    /*
     * משמש להחלפת תמונות האנימציה.
     */
    private long animationTime;

    public Renderer()
    {
        imageLoader = new PieceImageLoader(
                PIECES_PATH,
                CELL_SIZE
        );

        animationTime = 0;
    }

    public Img render(
            GameSnapshot snapshot,
            Position selected,
            List<Position> legalMoves
    )
    {
        Board board = snapshot.getBoard();

        int boardWidth =
                board.getWidth() * CELL_SIZE;

        int boardHeight =
                board.getHeight() * CELL_SIZE;

        Img canvas = new Img().read(
                BOARD_PATH,
                new Dimension(
                        boardWidth,
                        boardHeight
                ),
                false,
                null
        );

        /*
         * החלון מצייר בערך כל 16 מילישניות.
         */
        animationTime += 16;

        /*
         * קודם מציירים את המשבצות החוקיות,
         * כדי שהכלים יופיעו מעל הצבע הירוק.
         */
        drawLegalMoves(
                canvas,
                legalMoves
        );

        drawPieces(
                canvas,
                board,
                snapshot.getActiveMotion()
        );

        drawSelection(
                canvas,
                selected
        );

        if (snapshot.isGameOver())
        {
            drawGameOver(canvas);
        }

        return canvas;
    }

    private void drawLegalMoves(
            Img canvas,
            List<Position> legalMoves
    )
    {
        if (legalMoves == null
                || legalMoves.isEmpty())
        {
            return;
        }

        Graphics2D graphics =
                canvas.get().createGraphics();

        graphics.setColor(
                new Color(
                        80,
                        180,
                        70,
                        130
                )
        );

        for (Position position : legalMoves)
        {
            int x =
                    position.getCol()
                            * CELL_SIZE;

            int y =
                    position.getRow()
                            * CELL_SIZE;

            graphics.fillRect(
                    x,
                    y,
                    CELL_SIZE,
                    CELL_SIZE
            );
        }

        graphics.dispose();
    }

    private void drawPieces(
            Img canvas,
            Board board,
            MotionSnapshot activeMotion
    )
    {
        for (int row = 0;
             row < board.getHeight();
             row++)
        {
            for (int col = 0;
                 col < board.getWidth();
                 col++)
            {
                Position position =
                        new Position(row, col);

                Piece piece =
                        board.getPiece(position);

                if (piece == null
                        || piece.getState()
                        == Piece.State.CAPTURED)
                {
                    continue;
                }

                /*
                 * קודם מציירים את הכלי.
                 */
                drawPiece(
                        canvas,
                        piece,
                        row,
                        col,
                        activeMotion
                );

                /*
                 * אחר כך מציירים מעליו את שכבת זמן המנוחה.
                 */
                drawRestOverlay(
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
            int col,
            MotionSnapshot activeMotion
    )
    {
        double pixelX =
                col * CELL_SIZE;

        double pixelY =
                row * CELL_SIZE;

        /*
         * רק הכלי השייך לתנועה הפעילה
         * מקבל מיקום בין שתי משבצות.
         */
        if (activeMotion != null
                && activeMotion.getPieceId()
                == piece.getId())
        {
            double progress =
                    calculateProgress(activeMotion);

            double startX =
                    activeMotion
                            .getSource()
                            .getCol()
                            * CELL_SIZE;

            double startY =
                    activeMotion
                            .getSource()
                            .getRow()
                            * CELL_SIZE;

            double endX =
                    activeMotion
                            .getDestination()
                            .getCol()
                            * CELL_SIZE;

            double endY =
                    activeMotion
                            .getDestination()
                            .getRow()
                            * CELL_SIZE;

            pixelX =
                    startX
                            + (endX - startX)
                            * progress;

            pixelY =
                    startY
                            + (endY - startY)
                            * progress;
        }

        Img pieceImage =
                imageLoader.getFrame(
                        piece,
                        animationTime
                );

        pieceImage.drawOn(
                canvas,
                (int) Math.round(pixelX),
                (int) Math.round(pixelY)
        );
    }

    private void drawRestOverlay(
            Img canvas,
            Piece piece,
            int row,
            int col
    )
    {
        if (piece.getState() != Piece.State.LONG_REST
                && piece.getState() != Piece.State.SHORT_REST)
        {
            return;
        }

        int timeLeft =
                piece.getRestTimeLeft();

        double progress =
                timeLeft
                        / (double) TOTAL_REST_TIME;

        progress = Math.max(
                0.0,
                Math.min(1.0, progress)
        );

        int overlayHeight =
                (int) Math.round(
                        CELL_SIZE * progress
                );

        int x =
                col * CELL_SIZE;

        /*
         * השכבה מתחילה בתחתית התא
         * וקטנה ככל שהזמן עובר.
         */
        int y =
                row * CELL_SIZE
                        + CELL_SIZE
                        - overlayHeight;

        Graphics2D graphics =
                canvas.get().createGraphics();

        graphics.setColor(
                new Color(
                        255,
                        255,
                        120,
                        100
                )
        );

        graphics.fillRect(
                x,
                y,
                CELL_SIZE,
                overlayHeight
        );

        graphics.dispose();
    }

    private double calculateProgress(
            MotionSnapshot motion
    )
    {
        if (motion.getDuration() <= 0)
        {
            return 1.0;
        }

        double progress =
                motion.getElapsedTime()
                        / (double)
                        motion.getDuration();

        return Math.min(
                1.0,
                Math.max(0.0, progress)
        );
    }

    private void drawSelection(
            Img canvas,
            Position selected
    )
    {
        if (selected == null)
        {
            return;
        }

        int x =
                selected.getCol()
                        * CELL_SIZE;

        int y =
                selected.getRow()
                        * CELL_SIZE;

        canvas.drawRect(
                x,
                y,
                CELL_SIZE,
                CELL_SIZE,
                Color.YELLOW,
                6
        );
    }

    private void drawGameOver(
            Img canvas
    )
    {
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