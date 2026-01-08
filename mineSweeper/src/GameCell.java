import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class GameCell extends Button {

    public final int row;
    public final int col;
    private boolean mine;

    public GameCell(int row, int col, int tileSize) {
        this.row = row;
        this.col = col;

        setMinSize(tileSize, tileSize);
        setFont(Font.font("Arial Unicode MS", 24));
        setFocusTraversable(false);
    }

    public boolean hasMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
}
