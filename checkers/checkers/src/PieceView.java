import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PieceView extends StackPane {

    public PieceView(Piece piece) {

        Circle body = new Circle(28);
        body.setFill(piece.getOwner() == Player.WHITE
                ? Color.WHITE
                : Color.BLACK);
        body.setStroke(Color.GRAY);
        body.setStrokeWidth(2);

        getChildren().add(body);

        if (piece instanceof King) {
            Circle ring = new Circle(22);
            ring.setFill(Color.TRANSPARENT);
            ring.setStroke(Color.GOLD);
            ring.setStrokeWidth(4);

            Text crown = new Text("♛");
            crown.setFont(Font.font("Arial Unicode MS", 20));
            crown.setFill(Color.GOLD);

            getChildren().addAll(ring, crown);
        }
    }
}
