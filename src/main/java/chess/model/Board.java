package chess.model;

public class Board {

    private final int height;
    private final int width;
    private final Piece[][] cells;

    public Board(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Board dimensions must be positive");
        }

        this.height = height;
        this.width = width;
        this.cells = new Piece[height][width];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isInside(Position position) {
        int row = position.getRow();
        int col = position.getCol();

        return row >= 0 && row < height
                && col >= 0 && col < width;
    }

    public Piece getPiece(Position position) {
        if (!isInside(position)) {
            return null;
        }

        return cells[position.getRow()][position.getCol()];
    }

    public boolean isEmpty(Position position) {
        return getPiece(position) == null;
    }

    public void addPiece(Piece piece) {
        Position position = piece.getPosition();

        if (!isInside(position)) {
            throw new IllegalArgumentException("Position is outside the board");
        }

        if (!isEmpty(position)) {
            throw new IllegalArgumentException("Position is already occupied");
        }

        cells[position.getRow()][position.getCol()] = piece;
    }

    public Piece removePiece(Position position) {
        if (!isInside(position)) {
            throw new IllegalArgumentException("Position is outside the board");
        }

        Piece removedPiece = cells[position.getRow()][position.getCol()];
        cells[position.getRow()][position.getCol()] = null;

        return removedPiece;
    }

    public void movePiece(Position source, Position destination) {
        if (!isInside(source) || !isInside(destination)) {
            throw new IllegalArgumentException("Position is outside the board");
        }

        Piece movingPiece = getPiece(source);

        if (movingPiece == null) {
            throw new IllegalArgumentException("Source position is empty");
        }

        Piece capturedPiece = getPiece(destination);

        if (capturedPiece != null) {
            capturedPiece.setState(Piece.State.CAPTURED);
        }

        cells[source.getRow()][source.getCol()] = null;
        cells[destination.getRow()][destination.getCol()] = movingPiece;

        movingPiece.setPosition(destination);
        movingPiece.startLongRest();
    }
}