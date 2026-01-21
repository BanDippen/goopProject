import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Tile extends StackPane {
    private final int row;
    private final int col;

    private Piece piece;
    private final Rectangle background;

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;

        background = new Rectangle(80, 80);
        background.setFill((row + col) % 2 == 0
                ? Color.BEIGE
                : Color.BROWN);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3);
        getChildren().add(background);

    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
        getChildren().removeIf(n -> n instanceof PieceView);

        if (piece != null){
            PieceView view = new PieceView(piece);
            getChildren().add(view);
        }
    }

    public void removePiece(){
        setPiece(null);

    }

    public void highlight() {
        background.setStroke(Color.RED);
        background.setStrokeWidth(3);
    }

    public void clearHighlight() {
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3);
    }
}
