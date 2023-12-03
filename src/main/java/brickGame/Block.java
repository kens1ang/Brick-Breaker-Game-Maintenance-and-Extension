package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

public class Block implements Serializable {
    private static final Block block = new Block(-1, -1, Color.TRANSPARENT, 99);
    public int row;
    public int column;
    public boolean isDestroyed = false;
    private final Color color;
    public int type;
    public double x;
    public double y;
    private final double width = 100;
    private final double height = 30;
    private final double paddingTop = height * 2;
    private final double paddingH = 50;
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
        x = column * width + paddingH;
        y = row * height + paddingTop;

        rect = new Rectangle(x, y, width, height);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);

        setBlockFill();

    }

    private void setBlockFill() {
        if (type == BLOCK_CHOCO || type == BLOCK_HEART || type == BLOCK_STAR || type == BLOCK_BOMB) {
            setFillPattern(type);
        } else {
            rect.setFill(color);
        }
    }

    private void setFillPattern(int blockType) {
        String imageName;

        if (blockType == BLOCK_CHOCO) {
            imageName = "choco2.png";
        } else if (blockType == BLOCK_HEART) {
            imageName = "heart3.png";
        } else if (blockType == BLOCK_STAR) {
            imageName = "star3.png";
        } else if (blockType == BLOCK_BOMB) {
            imageName = "bomb3.png";
        } else {
            throw new IllegalArgumentException("Invalid block type: " + blockType);
        }

        Image image = new Image(imageName);
        ImagePattern pattern = new ImagePattern(image);
        rect.setFill(pattern);
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
