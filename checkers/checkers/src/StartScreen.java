import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class StartScreen {

    public Scene createScene(Runnable onStartGame) {

        Label title = new Label("Checkers");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        TextField whiteName = new TextField();
        whiteName.setPromptText("White player name");

        TextField blackName = new TextField();
        blackName.setPromptText("Black player name");

        ToggleButton toggleButton = new ToggleButton("Player 1 starts");
        toggleButton.setSelected(false); // optional, starts unpressed

        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("White starts");
            } else {
                toggleButton.setText("Black starts");
            }
        });

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> onStartGame.run());

        VBox layout = new VBox(15,
                title,
                whiteName,
                blackName,
                toggleButton,
                startButton
        );

        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30");

        return new Scene(layout, 400, 300);
    }
}
