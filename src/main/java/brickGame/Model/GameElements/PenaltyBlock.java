package brickGame.Model.GameElements;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
/**
 * Represents a penalty block in the game.
 * Negative effects when bomb object hit by the paddle.
 * This class handle the bomb falling objects.
 */
public class PenaltyBlock implements Serializable{

    public Rectangle bomb;
    public double x;
    public double y;
    public boolean taken = false;
    /**
     * Constructs a penalty block at the specified position.
     *
     * @param row     The row index of the penalty block.
     * @param column  The column index of the penalty block.
     */
    public PenaltyBlock(int row, int column) {
        x = (column * (GameBlock.getWidth())) + GameBlock.getPaddingH() + (GameBlock.getWidth() / 2) - 15;
        y = (row * (GameBlock.getHeight())) + GameBlock.getPaddingTop() + (GameBlock.getHeight() / 2) - 15;

        draw();
    }
    /**
     * Draws the visual representation of the penalty block.
     * The block has a bomb pattern.
     */
    private void draw() {
        bomb = new Rectangle();
        bomb.setWidth(30);
        bomb.setHeight(30);
        bomb.setX(x);
        bomb.setY(y);

        String url = "bomb.png";
        bomb.setFill(new ImagePattern(new Image(url)));
    }
    /**
     * Updates the position of the penalty block based on falling speed.
     */
    public void updatePosition() {
        final double fallingSpeed = 2.0;
        y += fallingSpeed;
        bomb.setY(y);
    }
    /**
     * Gets the rectangle representing the visual element of the penalty block.
     *
     * @return The rectangle representing the visual element of the penalty block.
     */
    public Rectangle getBomb() {
        return bomb;
    }
    /**
     * Gets the y-coordinate of the penalty block.
     *
     * @return The y-coordinate of the penalty block.
     */
    public double getY() {
        return y;
    }
    /**
     * Gets the x-coordinate of the penalty block.
     *
     * @return The x-coordinate of the penalty block.
     */
    public double getX() {
        return x;
    }
    /**
     * Sets whether the penalty block has been taken by the player.
     *
     * @param taken The value indicating whether the penalty block has been taken.
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
