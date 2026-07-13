package chess.rules;

import chess.model.Board;
import chess.model.Piece;
import chess.model.Position;

public class RuleEngine {

    private final PieceRules pieceRules;

    public RuleEngine() {
        this.pieceRules = new PieceRules();
    }

    public MoveValidation validateMove(
            Board board,
            Position source,
            Position destination
    ) {
        if (!board.isInside(source)
                || !board.isInside(destination)) {
            return MoveValidation.invalid("outside_board");
        }

        Piece selectedPiece = board.getPiece(source);

        if (selectedPiece == null) {
            return MoveValidation.invalid("empty_source");
        }

        Piece destinationPiece =
                board.getPiece(destination);

        if (destinationPiece != null
                && destinationPiece.getColor()
                == selectedPiece.getColor()) {
            return MoveValidation.invalid(
                    "friendly_destination"
            );
        }

        boolean legalPieceMove =
                pieceRules.isValidMove(
                        board,
                        selectedPiece,
                        source,
                        destination
                );

        if (!legalPieceMove) {
            return MoveValidation.invalid(
                    "illegal_piece_move"
            );
        }

        return MoveValidation.valid();
    }
}