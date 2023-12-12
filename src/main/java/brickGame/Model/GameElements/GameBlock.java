package brickGame.Model.GameElements;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;
/**
 * Represents a block in the game.
 * This class contains collision detection logic
 * and also initialize visual representation of each blocks
 */
public class GameBlock implements Serializable {
    // Block attributes
    private static final GameBlock block = new GameBlock(-1, -1, Color.TRANSPARENT, 99);
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

    // Constants for hit detection
    public static double NO_HIT = -1;
    public static double HIT_RIGHT = 0;
    public static double HIT_BOTTOM = 1;
    public static double HIT_LEFT = 2;
    public static double HIT_TOP = 3;

    // Constants for block types
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_BOMB = 103;

    /**
     * Constructs a GameBlock with the specified attributes.
     *
     * @param row    The row index of the block.
     * @param column The column index of the block.
     * @param color  The color of the block.
     * @param type   The type of the block.
     */
    public GameBlock(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }
    /**
     * Initializes the visual representation of the GameBlock.
     * Sets the rectangle dimensions, stroke, and fill pattern based on the block type.
     */
    private void draw() {
        x = column * width + paddingH;
        y = row * height + paddingTop;

        rect = new Rectangle(x, y, width, height);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);

        setBlockFill();

    }
    /**
     * Sets the fill of the rectangle based on the block type.
     * Uses a specific fill pattern for special block types.
     */
    private void setBlockFill() {
        if (type == BLOCK_CHOCO || type == BLOCK_HEART || type == BLOCK_STAR || type == BLOCK_BOMB) {
            setFillPattern(type);
        } else {
            rect.setFill(color);
        }
    }
    /**
     * Sets the fill pattern for special block types.
     *
     * @param blockType The type of the block.
     * @throws IllegalArgumentException if the block type is invalid.
     */
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

    /**
     * Checks if the ball hits the block and returns the hit code.
     * Take block edges into consideration to avoid penetration of blocks
     *
     * @param xBall      The x-coordinate of the ball.
     * @param yBall      The y-coordinate of the ball.
     * @param ballRadius The radius of the ball.
     * @return The hit code or {@link #NO_HIT} if no collision.
     */
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
    /**
     * Gets the padding value at the top of the game block.
     *
     * @return The padding value at the top of the game block.
     */
    public static double getPaddingTop() {
        return block.paddingTop;
    }
    /**
     * Gets the horizontal padding value of the game block.
     *
     * @return The horizontal padding value of the game block.
     */
    public static double getPaddingH() {
        return block.paddingH;
    }
    /**
     * Gets the height of the game block.
     *
     * @return The height of the game block.
     */
    public static double getHeight() {
        return block.height;
    }
    /**
     * Gets the width of the game block.
     *
     * @return The width of the game block.
     */
    public static double getWidth() {
        return block.width;
    }
    /**
     * Gets the row index of the game block.
     *
     * @return The row index of the game block.
     */
    public int getRow() {
        return row;
    }
    /**
     * Gets the column index of the game block.
     *
     * @return The column index of the game block.
     */
    public int getColumn() {
        return column;
    }
    /**
     * Gets the x-coordinate of the game block.
     *
     * @return The x-coordinate of the game block.
     */
    public double getX() {
        return x;
    }
    /**
     * Gets the y-coordinate of the game block.
     *
     * @return The y-coordinate of the game block.
     */
    public double getY() {
        return y;
    }
    /**
     * Gets the rectangle representing the visual element of the game block.
     *
     * @return The rectangle representing the visual element of the game block.
     */
    public Rectangle getRect() {
        return rect;
    }
    public Color getColor() {
        return color;
    }
}
