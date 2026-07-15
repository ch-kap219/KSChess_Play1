package chess.view;

import chess.model.Piece;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceImageLoader {

    private static final int FRAME_DURATION_MS = 120;

    private final String piecesPath;
    private final int cellSize;

    private final Map<String, List<Img>> framesCache;

    public PieceImageLoader(
            String piecesPath,
            int cellSize
    ) {
        this.piecesPath = piecesPath;
        this.cellSize = cellSize;
        this.framesCache = new HashMap<>();
    }

    public Img getFrame(
            Piece piece,
            long animationTime
    ) {
        String pieceFolder =
                createPieceFolderName(piece);

        String stateFolder =
                getStateFolder(piece.getState());

        String key =
                pieceFolder + "/" + stateFolder;

        List<Img> frames =
                framesCache.get(key);

        if (frames == null) {
            frames = loadFrames(
                    pieceFolder,
                    stateFolder
            );

            framesCache.put(key, frames);
        }

        int frameIndex =
                (int) (
                        animationTime
                                / FRAME_DURATION_MS
                                % frames.size()
                );

        return frames.get(frameIndex);
    }

    private List<Img> loadFrames(
            String pieceFolder,
            String stateFolder
    ) {
        String folderPath =
                piecesPath
                        + pieceFolder
                        + "/states/"
                        + stateFolder
                        + "/sprites";

        List<Img> frames =
                new ArrayList<>();

        for (int number = 1; ; number++) {
            String imagePath =
                    folderPath
                            + "/"
                            + number
                            + ".png";

            File file =
                    new File(imagePath);

            if (!file.exists()) {
                break;
            }

            Img frame = new Img().read(
                    imagePath,
                    new Dimension(
                            cellSize,
                            cellSize
                    ),
                    true,
                    null
            );

            frames.add(frame);
        }

        /*
         * במקרה שאין תמונות למצב מסוים,
         * משתמשים בתמונת idle הראשונה.
         */
        if (frames.isEmpty()
                && !stateFolder.equals("idle")) {
            return loadFrames(
                    pieceFolder,
                    "idle"
            );
        }

        if (frames.isEmpty()) {
            throw new IllegalStateException(
                    "No animation frames found in: "
                            + folderPath
            );
        }

        return frames;
    }

    private String createPieceFolderName(
            Piece piece
    ) {
        char type =
                Character.toUpperCase(
                        piece.getType()
                );

        char color =
                Character.toUpperCase(
                        piece.getColor()
                );

        return "" + type + color;
    }

    private String getStateFolder(
            Piece.State state
    )
    {
        return switch (state)
        {
            case IDLE -> "idle";
            case MOVING -> "move";
            case JUMPING -> "jump";
            case SHORT_REST -> "short_rest";
            case LONG_REST -> "long_rest";
            case CAPTURED -> "idle";
        };
    }
}