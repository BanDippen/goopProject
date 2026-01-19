import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected Player owner;
    protected boolean isKing;

    public Piece(Player owner){
        this.owner = owner;
        this.isKing = false;

    }

    public Player getOwner(){
        return owner;
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing(){
        this.isKing = true;

    }

    public abstract List<Move> getValidMoves(Board board, int row, int col);

    protected List<Move> simpleDiagMoves(Board board, int row, int col, int dir) {
        List<Move> moves = new ArrayList<>();
        int[] cols = {-1,+1};
        for (int dc : cols){
            int nr = row + dir;
            int nc = col + dc;

            if (board.inBounds(nr, nc) && board.isEmpty(nr, nc)){
                moves.add(new Move(row,col,nr,nc));
            }
        }
        return moves;

    }

    protected List<Move> captureDiagonalMoves(Board board, int r, int c, int dir) {
        List<Move> moves = new ArrayList<>();

        int[] cols = {-1, 1};
        for (int dc : cols) {
            int midR = r + dir;
            int midC = c + dc;
            int landR = r + 2 * dir;
            int landC = c + 2 * dc;

            if (!board.inBounds(landR, landC)) continue;

            Tile midTile = board.getTile(midR, midC);
            Tile landTile = board.getTile(landR, landC);

            if (midTile.hasPiece()
                    && midTile.getPiece().getOwner() != owner
                    && landTile.getPiece() == null) {

                moves.add(new Move(r, c, landR, landC, midTile));
            }
        }
        return moves;
    }


}
