import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

import java.util.List;

public class GameController {

    private Board board;
    private Player currentPlayer;

    private Tile selectedTile;
    private List<Move> currentMoves;

    private Label statusLabel = new Label("White's turn");

    public GameController() {
        board = new Board();

    }

    // ---------------- Scene creation ----------------
    public Scene createScene() {
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        GamePanel gameInfo = new GamePanel();

        root.setTop(statusLabel);
        root.setCenter(grid);
        root.setRight(gameInfo);

        // Add tiles to grid
        for (int r = 0; r < Board.size; r++) {
            for (int c = 0; c < Board.size; c++) {
                Tile tile = board.getTile(r, c);
                grid.add(tile, c, r);

                tile.setOnMouseClicked(e -> handleTileClick(tile));
            }
        }

        return new Scene(root,
                Board.size * 80 + 25,
                Board.size * 80 + 40);
    }

    // ---------------- Click handling ----------------
    private void handleTileClick(Tile tile) {

        // If no tile selected yet
        if (selectedTile == null) {
            selectTile(tile);
            return;
        }

        // If clicking same tile -> deselect
        if (tile == selectedTile) {
            clearSelection();
            return;
        }

        // Try to move
        for (Move move : currentMoves) {
            if (move.toRow == tile.getRow() && move.toCol == tile.getCol()) {
                executeMove(move);
                return;
            }
        }

        // Otherwise, select a different piece
        clearSelection();
        selectTile(tile);
    }

    // ---------------- Selection logic ----------------
    private void selectTile(Tile tile) {

        if (!tile.hasPiece()) return;
        if (tile.getPiece().getOwner() != currentPlayer) return;

        selectedTile = tile;
        selectedTile.highlight();

        currentMoves = tile.getPiece()
                .getValidMoves(board, tile.getRow(), tile.getCol());

        highlightMoves();
    }

    private void clearSelection() {
        if (selectedTile != null) {
            selectedTile.clearHighlight();
        }

        if (currentMoves != null) {
            for (Move move : currentMoves) {
                board.getTile(move.toRow, move.toCol).clearHighlight();
            }
        }

        selectedTile = null;
        currentMoves = null;
    }

    private void highlightMoves() {
        for (Move move : currentMoves) {
            board.getTile(move.toRow, move.toCol).highlight();
        }
    }

    // ---------------- Move execution ----------------
    private void executeMove(Move move) {

        Tile from = board.getTile(move.fromRow, move.fromCol);
        Tile to = board.getTile(move.toRow, move.toCol);

        Piece piece = from.getPiece();

        from.removePiece();
        to.setPiece(piece);

        // Capture
        if (move.isCapture()) {
            move.capturedTile.removePiece();
        }

        // Promotion
        if (piece instanceof Man &&
                (move.toRow == 0 || move.toRow == Board.size - 1)) {
            to.setPiece(new King(piece.getOwner()));
        }

        clearSelection();
        switchTurn();
        checkWin();
    }

    // ---------------- Turn handling ----------------
    private void switchTurn() {
        currentPlayer = (currentPlayer == Player.WHITE)
                ? Player.BLACK
                : Player.WHITE;

        statusLabel.setText(
                currentPlayer == Player.WHITE
                        ? "White's turn"
                        : "Black's turn"
        );
    }

    // ---------------- Win condition ----------------
    private void checkWin() {
        boolean whiteExists = false;
        boolean blackExists = false;

        for (int r = 0; r < Board.size; r++) {
            for (int c = 0; c < Board.size; c++) {
                Tile tile = board.getTile(r, c);
                if (tile.hasPiece()) {
                    if (tile.getPiece().getOwner() == Player.WHITE)
                        whiteExists = true;
                    else
                        blackExists = true;
                }
            }
        }

        if (!whiteExists || !blackExists) {
            statusLabel.setText(
                    (!whiteExists ? "Black" : "White") + " wins!"
            );
        }
    }
}
