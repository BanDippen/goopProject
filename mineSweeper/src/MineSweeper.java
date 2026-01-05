import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiConsumer;

/*This is made as a GOOP project for AAU
* author BanDippen
* Feel free to use. */

public class MineSweeper extends Application {

    // ---------------- GAME CONFIG ----------------
    private final int tileSize = 70;       // Size of each tile in pixels
    private final int gridSize = 10;        // Width and height of the square grid
    private final int numRow = gridSize;    // Number of rows
    private final int numCol = gridSize;    // Number of columns
    private final int numBombs = 15;        // Number of bombs

    // ---------------- GAME STATE ----------------
    private int tilesClicked = 0;           // How many tiles have been revealed
    private boolean gameOver = false;       // True if the player hits a mine or wins

    private MineGrid[][] board = new MineGrid[numRow][numCol];  // 2D board of tiles
    private ArrayList<MineGrid> mineList = new ArrayList<>();   // List of all mines for easy revealing

    private Label textLabel = new Label("Minesweeper");         // Top label showing game messages

    // ---------------- INNER CLASS ----------------
    /**
     * Represents a single tile in the Minesweeper board
     * Extends JavaFX Button and stores its row/col and mine status
     */
    private class MineGrid extends Button {
        final int row;
        final int col;
        private boolean mine = false;   // True if this tile contains a mine

        public MineGrid(int row, int col) {
            this.row = row;
            this.col = col;
            setMinSize(tileSize, tileSize);                 // Set button size
            setFont(Font.font("Arial Unicode MS", 24));    // Font for numbers or bombs
            setFocusTraversable(false);                    // Disable focus border
        }

        public boolean mineState() {
            return mine; // Return true if tile contains a mine
        }

        public void setMine(boolean mine) {
            this.mine = mine; // Setter for assigning mine status
        }
    }

    class desConfig extends Button {
        final String playerName;
        int time;
        int gridSize;
        int desBomb;
        boolean desCheat;

        desConfig(String playerName, int desGrid, int desBomb, boolean desCheat){
            this.playerName = playerName;
            this.gridSize = desGrid;
            this.desBomb = desBomb;
            this.desCheat = desCheat;
        }

    }

    // ---------------- ADJACENCY HELPERS ----------------
    // Arrays for checking all 8 neighbors
    private static final int[] adjR = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] adjC = {-1, 0, 1, -1, 1, -1, 0, 1};

    /**
     * Loops over all adjacent tiles of a given tile and applies a BiConsumer action
     */
    private void forAdj(int r, int c, BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < adjR.length; i++) {   // Must use adjR.length, not gridSize
            action.accept(r + adjR[i], c + adjC[i]);
        }
    }

    // ---------------- HELPER METHODS ----------------
    /**
     * Returns true if the tile at (r,c) contains a mine
     */
    private boolean isMine(int r, int c) {
        if (r < 0 || r >= numRow || c < 0 || c >= numCol) return false; // Out of bounds check
        return board[r][c].mineState();
    }

    /**
     * Returns 1 if the tile contains a mine, 0 otherwise (used for counting)
     */
    private int countMine(int r, int c) {
        return isMine(r, c) ? 1 : 0;
    }

    /**
     * Place a number of mines randomly on the board
     */
    private void placeMines(int bombNumber) {
        Random random = new Random();
        int placed = 0;
        while (placed < bombNumber) {
            int r = random.nextInt(numRow);
            int c = random.nextInt(numCol);
            MineGrid cell = board[r][c];
            if (!cell.mineState()) {    // Only place mine if none exists
                cell.setMine(true);
                mineList.add(cell);
                placed++;
            }
        }
    }

    /**
     * Reveals all mines when the player hits one
     */
    private void revealMines() {
        for (MineGrid cell : mineList) {
            cell.setText("💣");   // Show bomb emoji
            cell.setDisable(true); // Disable all mine buttons
        }
        gameOver = true;           // End the game
        textLabel.setText("You hit a mine! Game over!");
    }

    /**
     * Recursive check of the tile
     * If no adjacent mines, reveals neighbors recursively
     */
    private void checkMine(int r, int c) {
        if (r < 0 || r >= numRow || c < 0 || c >= numCol) return;

        MineGrid cell = board[r][c];
        if (cell.isDisabled()) return;  // Already revealed

        cell.setDisable(true);          // Reveal this tile
        tilesClicked++;

        final int[] minesFound = {0};

        // Count adjacent mines
        forAdj(r, c, (nr, nc) -> minesFound[0] += countMine(nr, nc));

        if (minesFound[0] > 0) {
            cell.setText(String.valueOf(minesFound[0])); // Show number
        } else {
            cell.setText("");                             // Empty if no adjacent mines
            forAdj(r, c, this::checkMine);               // Recursively reveal neighbors
        }

        // Check for win condition
        if (tilesClicked == numRow * numCol - mineList.size()) {
            gameOver = true;
            textLabel.setText("Congratulations! You won!");
        }
    }

    // ---------------- JAVA FX START ----------------
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        GridPane boardPane = new GridPane();

        textLabel.setFont(Font.font("Arial", 24));
        root.setTop(textLabel);
        root.setCenter(boardPane);

        // Initialize board and buttons
        for (int r = 0; r < numRow; r++) {
            for (int c = 0; c < numCol; c++) {
                MineGrid cell = new MineGrid(r, c);
                board[r][c] = cell;

                // Mouse click handler
                cell.setOnMouseClicked(e -> {
                    if (gameOver) return;

                    if (e.getButton() == MouseButton.PRIMARY) {
                        if (cell.isDisabled()) return;
                        if (cell.mineState()) {
                            revealMines();           // Hit a mine
                        } else {
                            checkMine(cell.row, cell.col); // Safe tile
                        }
                    }

                    if (e.getButton() == MouseButton.SECONDARY) { // Right click for flag
                        if (!cell.isDisabled()) {
                            if (cell.getText().isEmpty()) {
                                cell.setText("🚩"); // Place flag
                            } else if (cell.getText().equals("🚩")) {
                                cell.setText("");   // Remove flag
                            }
                        }
                    }
                });

                boardPane.add(cell, c, r); // Add tile to grid (col,row)
            }
        }

        // Place bombs
        placeMines(numBombs);

        // Create scene and show stage
        Scene scene = new Scene(root, gridSize * tileSize, gridSize * tileSize + 50);
        stage.setScene(scene);
        stage.setTitle("JavaFX Minesweeper");
        stage.setResizable(false);
        stage.show();
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {
        launch(args); // Launches JavaFX application
    }
}
