import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A king checker piece
 * Moves diagonally both forward and backward
 */
public class King extends Piece {

    public King(Player owner) {
        super(owner);
        this.isKing = true;
    }

    @Override
    public List<Move> getValidMoves(Board board, int r, int c) {
        List<Move> moves = new ArrayList<>();

        // Kings move in both directions
        int[] dirs = {-1, 1};

        for (int dir : dirs) {
            moves.addAll(simpleDiagMoves(board, r, c, dir));
            moves.addAll(captureDiagonalMoves(board, r, c, dir));
        }

        return moves;
    }

}
