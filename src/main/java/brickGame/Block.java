package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);
    public int row;
    public int column;
    public boolean isDestroyed = false;
    private Color color;
    public int type;
    public double x;
    public double y;
    private double width = 100;
    private double height = 30;
    private double paddingTop = height * 2;
    private double paddingH = 50;
    public Rectangle rect;
    public static double NO_HIT = -1;
    public static double HIT_RIGHT = 0;
    public static double HIT_BOTTOM = 1;
    public static double HIT_LEFT = 2;
    public static double HIT_TOP = 3;
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_BOMB = 103;

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco2.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart3.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star3.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_BOMB) {
            Image image = new Image("bomb3.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }

    public double checkHitToBlock(double xBall, double yBall, double ballRadius) {

        if (isDestroyed) {
            return NO_HIT;
        }

        double ballLeftEdge = xBall - ballRadius;
        double ballRightEdge = xBall + ballRadius;
        double ballTopEdge = yBall - ballRadius;
        double ballBottomEdge = yBall + ballRadius;

        boolean collideTop = ballBottomEdge >= y && ballTopEdge < y;
        boolean collideBottom = ballTopEdge <= y + height && ballBottomEdge > y + height;
        boolean collideLeft = ballRightEdge >= x && ballLeftEdge < x;
        boolean collideRight = ballLeftEdge <= x + width && ballRightEdge > x + width;

        if (collideTop && xBall >= x && xBall <= x + width) {
            return HIT_TOP;
        } else if (collideBottom && xBall >= x && xBall <= x + width) {
            return HIT_BOTTOM;
        } else if (collideLeft && yBall >= y && yBall <= y + height) {
            return HIT_LEFT;
        } else if (collideRight && yBall >= y && yBall <= y + height) {
            return HIT_RIGHT;
        }

        return NO_HIT;
    }

    public static double getPaddingTop() {
        return block.paddingTop;
    }
    public static double getPaddingH() {
        return block.paddingH;
    }
    public static double getHeight() {
        return block.height;
    }
    public static double getWidth() {
        return block.width;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Rectangle getRect() {
        return rect;
    }
}
