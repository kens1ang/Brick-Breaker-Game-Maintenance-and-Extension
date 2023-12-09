package GameElements;

import Model.GameModel;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;
import java.util.Random;

public class BonusBlock implements Serializable {
    public Rectangle choco;
    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;

    public BonusBlock(int row, int column) {
        x = (column * (GameBlock.getWidth())) + GameBlock.getPaddingH() + (GameBlock.getWidth() / 2) - 15;
        y = (row * (GameBlock.getHeight())) + GameBlock.getPaddingTop() + (GameBlock.getHeight() / 2) - 15;
        draw();
    }

    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png";
        } else {
            url = "bonus2.png";
        }

        choco.setFill(new ImagePattern(new Image(url)));
    }

    public void updateChocoPosition(BonusBlock choco, GameModel model) {
        choco.addtoY(((model.getTime() - choco.getTimeCreated()) / 1000.000) + 1.000);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void addtoY(double add) {
        y += add;
    }
    public long getTimeCreated() {
        return timeCreated;
    }
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
    public boolean isTaken() {
        return taken;
    }
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
    public Rectangle getChoco() {
        return choco;
    }
}
