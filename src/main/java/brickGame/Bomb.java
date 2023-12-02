package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Bomb implements Serializable{

    public Rectangle bomb;
    public double x;
    public double y;
    public boolean taken = false;

    public Bomb(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }

    private void draw() {
        bomb = new Rectangle();
        bomb.setWidth(30);
        bomb.setHeight(30);
        bomb.setX(x);
        bomb.setY(y);

        String url = "bomb.png";
        bomb.setFill(new ImagePattern(new Image(url)));
    }

    public void updatePosition() {
        final double fallingSpeed = 2.0;
        y += fallingSpeed;
        bomb.setY(y);
    }

    public Rectangle getBomb() {
        return bomb;
    }
    public double getY() {
        return y;
    }
    public double getX() {
        return x;
    }
    public boolean isTaken() {
        return taken;
    }
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
