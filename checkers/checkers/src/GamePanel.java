import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GamePanel extends VBox {
    Label playerWhite = new Label();
    Label playerBlack = new Label();
    Label moveLabel = new Label("Moves:");
    Label timeLabel = new Label("Time:");

    Button resetButton = new Button("Reset");
    Button quitGame = new Button("Quit game");

        GamePanel() {
            setSpacing(10);
            setPadding(new Insets(10));

            getChildren().addAll(
                    playerWhite,
                    playerBlack,
                    moveLabel,
                    timeLabel,
                    resetButton,
                    quitGame
            );
        }

        public void setPlayerNames(String whiteP, String blackP){
            playerWhite.setText("White " + whiteP);
            playerBlack.setText("Black " + blackP);

        }

        public void setMoves(int moves) {
            moveLabel.setText("Moves " + moves);
        }

        public void setTimer(int timer){
            timeLabel.setText("Time " + timer);
        }

        public void reset(Runnable click){
            resetButton.setOnAction(e -> click.run());
        }

        public void quit(Runnable click){
            quitGame.setOnAction( e ->click.run());
        }
}
