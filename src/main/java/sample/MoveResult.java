package sample;

class MoveResult {

    private MoveType type;

    private Piece piece;

    MoveType getType() {
        return type;
    }

    Piece getPiece() {
        return piece;
    }

    MoveResult(MoveType type) {
        this(type, null);
    }

    MoveResult(MoveType type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }
}
