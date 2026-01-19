import javafx.application.Application;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Checkers");

        showStartScreen();
        stage.show();
    }

    private void showStartScreen() {
        StartScreen startScreen = new StartScreen();

        stage.setScene(
                startScreen.createScene(this::startGame)
        );
    }

    private void startGame() {
        GameController controller = new GameController();
        stage.setScene(controller.createScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
