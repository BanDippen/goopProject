import java.util.ArrayList;
import java.util.List;

/**
 * A normal (non-king) checker piece
 * Moves diagonally forward and captures by jumping
 */
public class Man extends Piece {

    public Man(Player owner) {
        super(owner);
    }

    @Override
    public List<Move> getValidMoves(Board board, int r, int c) {
        List<Move> moves = new ArrayList<>();

        // Direction depends on player
        int dir = (owner == Player.WHITE) ? -1 : 1;

        // Simple diagonal moves
        moves.addAll(simpleDiagMoves(board, r, c, dir));

        // Capture moves
        moves.addAll(captureDiagonalMoves(board, r, c, dir));

        return moves;
    }
}
