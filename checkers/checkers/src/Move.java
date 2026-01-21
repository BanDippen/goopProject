public class Move {
    public final int fromRow, fromCol;
    public final int toRow, toCol;
    public final Tile capturedTile; // null if no capture

    public Move(int fr, int fc, int tr, int tc) {
        this(fr, fc, tr, tc, null);
    }

    public Move(int fr, int fc, int tr, int tc, Tile captured) {
        this.fromRow = fr;
        this.fromCol = fc;
        this.toRow = tr;
        this.toCol = tc;
        this.capturedTile = captured;
    }

    public boolean isCapture() {
        return capturedTile != null;
    }
}
