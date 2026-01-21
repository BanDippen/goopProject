public class Board {
    static final int size = 8;
    private Tile[][] tiles = new Tile[8][8];

    public Board(){
        tiles = new Tile[size][size];
        createTiles();
        placeInitialPieces();
    }

    private void createTiles(){
        for (int r = 0; r < size; r++){
            for (int c = 0; c < size; c ++){
                tiles[r][c] = new Tile(r,c);
            }
        }
    }

    private void placeInitialPieces() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                if ((r + c) % 2 == 1) { // dark squares only

                    if (r < 3) {
                        tiles[r][c].setPiece(new Man(Player.BLACK));
                    } else if (r > 4) {
                        tiles[r][c].setPiece(new Man(Player.WHITE));
                    }
                }
            }
        }
    }

    public Tile getTile(int r,int c){
        if (!inBounds(r,c)) return null;
        return tiles[r][c];
    }

    public boolean inBounds(int r, int c){
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    public boolean isEmpty(int r, int c){
        return inBounds(r,c) && tiles[r][c].getPiece() == null;
    }

    public void removePiece(int r, int c) {
        if (inBounds(r, c)) {
            tiles[r][c].setPiece(null);
        }
    }

}

