import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiConsumer;

/*This is made as a GOOP project for AAU
 * author BanDippen
 * Feel free to use. */

public class MineSweeper extends Application {

    // ---------------- GAME CONFIG ----------------
    private final int tileSize = 70;       // Size of each tile in pixels
    private int gridSize;        // Width and height of the square grid
    private int numRow;    // Number of rows
    private int numCol;    // Number of columns
    private int numBombs;        // Number of bombs
    private boolean cheats;

    private Timeline timer;
    private int secondsElapsed = 0;
    private final Label timerLabel = new Label("Time: 0");

    private Stage stage;

    // ---------------- GAME STATE ----------------
    private int tilesClicked = 0;           // How many tiles have been revealed
    private boolean gameOver = false;       // True if the player hits a mine or wins

    private boardCell[][] board;  // 2D board of tiles
    private final ArrayList<boardCell> mineList = new ArrayList<>();   // List of all mines for easy revealing

    private final Label textLabel = new Label("Minesweeper");         // Top label showing game messages

    /* this is a singel cell which CAN have a mine in the board. */
    private class boardCell extends Button {
        final int row;
        final int col;
        private boolean mine = false;   // True if this tile contains a mine

        public boardCell(int row, int col) {
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

    static class DesConfig{
        final String playerName;
        int gridSize;
        int desBomb;
        boolean desCheat;

        DesConfig(String playerName, int desGrid, int desBomb, boolean desCheat){
            this.playerName = playerName;
            this.gridSize = desGrid;
            this.desBomb = desBomb;
            this.desCheat = desCheat;
        }

    }

    /* This is for adjanct cells. */
    private static final int[] adjR = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] adjC = {-1, 0, 1, -1, 1, -1, 0, 1};
    private void forAdj(int r, int c, BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < adjR.length; i++) {   // Must use adjR.length, not gridSize
            action.accept(r + adjR[i], c + adjC[i]);
        }
    }

    /* If this contains a mine, it will return TRUE */
    private boolean isMine(int r, int c) {
        if (r < 0 || r >= numRow || c < 0 || c >= numCol) return false; // Out of bounds check
        return board[r][c].mineState();
    }

    /* so if it contains mine, it will return 1. Easy way to count mines if looped ;)*/
    private int countMine(int r, int c) {
        return isMine(r, c) ? 1 : 0;
    }

    /* place mines at random places across the board */
    private void placeMines(int bombNumber) {
        Random random = new Random();
        int placed = 0;
        while (placed < bombNumber) {
            int r = random.nextInt(numRow);
            int c = random.nextInt(numCol);
            boardCell cell = board[r][c];
            if (!cell.mineState()) {    // Only place mine if none exists
                cell.setMine(true);
                mineList.add(cell);
                placed++;
            }
        }
    }

    /* This reveals all the mines in case of game over */
    private void revealMines() {
        for (boardCell cell : mineList) {
            cell.setText("💣");   // Show bomb emoji
            cell.setDisable(true); // Disable all mine buttons
        }
        timer.stop();
        gameOver = true;           // End the game
        textLabel.setText("You hit a mine! Game over!");
    }

    /* Checks if its a mine, hopefully not, and if not reveals all the cells not next to it*/
    private void checkMine(int r, int c) {
        if (r < 0 || r >= numRow || c < 0 || c >= numCol) return;

        boardCell cell = board[r][c];
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
            timer.stop();
            textLabel.setText("Congratulations! You won!");
            textLabel.setText(String.valueOf(timerLabel));
        }
    }
    /*This is used to create the game board after what the player have wanted as config */
    private Scene createGameBoard() {
        BorderPane root = new BorderPane();
        GridPane boardPane = new GridPane();

        textLabel.setFont(Font.font("Arial", 24));
        root.setTop(textLabel);
        root.setCenter(boardPane);

        VBox topBar = new VBox(textLabel, timerLabel);
        root.setTop(topBar);

        for (int r = 0; r < numRow; r++) {
            for (int c = 0; c < numCol; c++) {
                boardCell cell = new boardCell(r, c);
                board[r][c] = cell;
                boardPane.add(cell, c, r);
                // Mouse click handler
                cell.setOnMouseClicked(e -> {
                    if (gameOver) return;

                    if (e.getButton() == MouseButton.PRIMARY) {
                        if (cell.isDisabled()) return;
                        if (cell.mineState()) {
                            if (!cheats){
                                revealMines();
                            }
                            else {
                                cell.setText("💣");
                            }

                            // Hit a mine
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

            }
        }

        placeMines(numBombs);

        return new Scene(root,
                gridSize * tileSize,
                gridSize * tileSize + 50);
    }

    /* This is used to create the desired config of the game for the player*/
    private void startGame(DesConfig config){
        this.gridSize = config.gridSize;
        this.numCol = config.gridSize;
        this.numRow = config.gridSize;
        this.numBombs = config.desBomb;
        this.cheats = config.desCheat;
        this.tilesClicked = 0;

        this.gameOver = false;
        this.board = new boardCell[numRow][numCol];
        this.mineList.clear();
        Scene gameBoard = createGameBoard();
        stage.setScene(gameBoard);
        startTimer();
    }
    /* a function to start the timer */
    private void startTimer() {
        secondsElapsed = 0;
        if (timer != null) timer.stop();

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    secondsElapsed++;
                    timerLabel.setText("Time: " + secondsElapsed);
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /* this is what starts when the game begins*/
    public void start(Stage stage) {
        this.stage = stage;
        showStartScreen();
        stage.setTitle("JavaFX Minesweeper");
        stage.show();
    }

    /*This is the start screen we see */
    private void showStartScreen() {
        TextField nameField = new TextField();
        nameField.setPromptText("Player name");
        TextField bomsTooMany = new TextField();

        Spinner<Integer> gridSpinner = new Spinner<>(5, 30, 10);
        Spinner<Integer> bombSpinner = new Spinner<>(1, 200, 15);

        Button startButton = new Button("Start Game");
        CheckBox cheatBox = new CheckBox("Cheats Allowed");

        startButton.setOnAction(e -> {
            DesConfig config = new DesConfig(
                    nameField.getText(),
                    gridSpinner.getValue(),
                    bombSpinner.getValue(),
                    cheatBox.isSelected()
            );

            if (bombSpinner.getValue() > gridSpinner.getValue())
            {bomsTooMany.setPromptText("You have placed to many bombs");
            }
            if (bombSpinner.getValue() < ((gridSpinner.getValue()*gridSpinner.getValue()))) {
                startGame(config);
            }
        });

        VBox layout = new VBox(10,
                new Label("Name"), nameField,
                new Label("Grid size"), gridSpinner,
                new Label("Bombs"), bombSpinner,
                cheatBox,
                new Label("Have you placed to many bombs?"), bomsTooMany,
                startButton
        );

        layout.setStyle("-fx-padding: 20");
        stage.setScene(new Scene(layout, 300, 400));
    }


    /* this launches the game*/
    public static void main(String[] args) {
        launch(args); // Launches JavaFX application
    }
}