import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MineSweeper extends Application {

    private final GameController controller = new GameController();

    @Override
    public void start(Stage stage) {

        TextField nameField = new TextField();
        Spinner<Integer> gridSpinner = new Spinner<>(5, 30, 10);
        Spinner<Integer> bombSpinner = new Spinner<>(1, 200, 15);
        CheckBox cheatBox = new CheckBox("Enable cheats");

        Button startButton = new Button("Start Game");

        startButton.setOnAction(e -> {
            GameConfig config = new GameConfig(
                    nameField.getText(),
                    gridSpinner.getValue(),
                    bombSpinner.getValue(),
                    cheatBox.isSelected()
            );

            Scene gameScene = controller.createGame(config);
            stage.setScene(gameScene);
        });

        VBox layout = new VBox(10,
                new Label("Player name"), nameField,
                new Label("Grid size"), gridSpinner,
                new Label("Bombs"), bombSpinner,
                cheatBox,
                startButton
        );

        layout.setStyle("-fx-padding: 20");
        stage.setScene(new Scene(layout, 300, 350));
        stage.setTitle("Minesweeper");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
