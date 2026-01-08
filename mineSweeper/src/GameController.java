import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class GameController {

    private final int tileSize = 70;

    private int gridSize;
    private int numBombs;
    private boolean cheats;

    private GameCell[][] board;
    private final ArrayList<GameCell> mines = new ArrayList<>();

    private boolean gameOver = false;
    private int tilesRevealed = 0;

    private final Label statusLabel = new Label("Minesweeper");
    private final Label timerLabel = new Label("Time: 0");

    private Timeline timer;
    private int seconds = 0;

    private static final int[] adjR = {-1,-1,-1,0,0,1,1,1};
    private static final int[] adjC = {-1,0,1,-1,1,-1,0,1};

    public Scene createGame(GameConfig config) {

        this.gridSize = config.gridSize;
        this.numBombs = config.bombCount;
        this.cheats = config.cheats;

        board = new GameCell[gridSize][gridSize];
        mines.clear();
        gameOver = false;
        tilesRevealed = 0;

        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();

        root.setTop(statusLabel);
        root.setCenter(grid);

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                GameCell cell = new GameCell(r, c, tileSize);
                board[r][c] = cell;
                grid.add(cell, c, r);

                cell.setOnMouseClicked(e -> {
                    if (gameOver) return;

                    if (e.getButton() == MouseButton.PRIMARY) {
                        if (cell.hasMine()) {
                            revealMines();
                        } else {
                            reveal(cell.row, cell.col);
                        }
                    }

                    if (e.getButton() == MouseButton.SECONDARY) {
                        if (!cell.isDisabled()) {
                            cell.setText(cell.getText().equals("🚩") ? "" : "🚩");
                        }
                    }
                });
            }
        }

        placeMines();
        startTimer();

        return new Scene(root,
                gridSize * tileSize,
                gridSize * tileSize + 18);
    }

    private void placeMines() {
        Random rand = new Random();

        while (mines.size() < numBombs) {
            int r = rand.nextInt(gridSize);
            int c = rand.nextInt(gridSize);

            GameCell cell = board[r][c];
            if (!cell.hasMine()) {
                cell.setMine(true);
                mines.add(cell);
            }
        }
    }

    private void reveal(int r, int c) {
        if (r < 0 || r >= gridSize || c < 0 || c >= gridSize) return;

        GameCell cell = board[r][c];
        if (cell.isDisabled()) return;

        cell.setDisable(true);
        tilesRevealed++;

        AtomicInteger count = new AtomicInteger();
        forAdj(r, c, (nr, nc) -> {
            if (inBounds(nr, nc) && board[nr][nc].hasMine()) count.getAndIncrement();
        });

        if (count.get() > 0) {
            cell.setText(String.valueOf(count.get()));
        } else {
            forAdj(r, c, this::reveal);
        }

        if (tilesRevealed == gridSize * gridSize - numBombs) {
            statusLabel.setText("You win!");
            stopTimer();
            gameOver = true;
        }
    }

    private void revealMines() {
        for (GameCell mine : mines) {
            mine.setText("💣");
            mine.setDisable(true);
        }
        statusLabel.setText("Game Over");
        stopTimer();
        gameOver = true;
    }

    private void forAdj(int r, int c, BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < 8; i++) {
            action.accept(r + adjR[i], c + adjC[i]);
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < gridSize && c >= 0 && c < gridSize;
    }

    private void startTimer() {
        seconds = 0;
        timerLabel.setText("Time: 0");

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            timerLabel.setText("Time: " + seconds);
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) timer.stop();
    }
}

