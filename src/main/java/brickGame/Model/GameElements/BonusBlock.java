package brickGame.Model.GameElements;

import brickGame.Model.GameModel;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;
import java.util.Random;
/**
 * Represents a bonus block in the game.
 * Bonus blocks contain special items and effects.
 * This class handle the bonus falling objects.
 */
public class BonusBlock implements Serializable {
    public Rectangle choco;
    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;
    /**
     * Constructs a bonus block at the specified row and column.
     *
     * @param row    The row of the bonus block.
     * @param column The column of the bonus block.
     */
    public BonusBlock(int row, int column) {
        x = (column * (GameBlock.getWidth())) + GameBlock.getPaddingH() + (GameBlock.getWidth() / 2) - 15;
        y = (row * (GameBlock.getHeight())) + GameBlock.getPaddingTop() + (GameBlock.getHeight() / 2) - 15;
        draw();
    }
    /**
     * Initializes and configures the visual representation of the bonus block.
     * Creates a rectangular shape with specified dimensions, position, and image fill.
     * The image is randomly chosen from two options ("bonus1.png" or "bonus2.png").
     * This method sets up the appearance of the bonus block.
     */
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

    /**
     * Updates the position of the bonus block based on the elapsed time.
     *
     * @param choco The bonus block to update.
     * @param model The game model.
     */
    public void updateChocoPosition(BonusBlock choco, GameModel model) {
        choco.addtoY(((model.getTime() - choco.getTimeCreated()) / 1000.000) + 1.000);
    }
    /**
     * Gets the x-coordinate of the bonus block.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }
    /**
     * Gets the y-coordinate of the bonus block.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }
    /**
     * Adds a specified value to the y-coordinate of the bonus block.
     *
     * @param add The value to add to the y-coordinate.
     */
    public void addtoY(double add) {
        y += add;
    }
    /**
     * Gets the time when the bonus block was created.
     *
     * @return The time when the bonus block was created.
     */
    public long getTimeCreated() {
        return timeCreated;
    }
    /**
     * Sets the time when the bonus block was created.
     *
     * @param timeCreated The time when the bonus block was created.
     */
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
    /**
     * Checks whether the bonus block has been taken.
     *
     * @return {@code true} if the bonus block has been taken, {@code false} otherwise.
     */
    public boolean isTaken() {
        return taken;
    }
    /**
     * Sets whether the bonus block has been taken.
     *
     * @param taken {@code true} if the bonus block has been taken, {@code false} otherwise.
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
    /**
     * Gets the rectangle representing the bonus block.
     *
     * @return The rectangle representing the bonus block.
     */
    public Rectangle getChoco() {
        return choco;
    }
}
