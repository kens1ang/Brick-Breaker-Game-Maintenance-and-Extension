package brickGame.Model.GameElements;
/**
 * Represents the game paddle in this game.
 * This class has coordinates, movement direction, and methods to update its position.
 */
public class GamePaddle {
    private double centerBreakX;
    private double xBreak = (500 - 130) / 2.0;
    private double yBreak = 640.0f;
    private final int breakWidth = 130;
    /**
     * Gets the x-coordinate of the center of the paddle.
     *
     * @return The x-coordinate of the center of the paddle.
     */
    public double getCenterBreakX() {
        return centerBreakX;
    }
    /**
     * Gets the y-coordinate of the paddle.
     *
     * @return The y-coordinate of the paddle.
     */
    public double getyBreak() {
        return yBreak;
    }
    /**
     * Gets the x-coordinate of the paddle.
     *
     * @return The x-coordinate of the paddle.
     */
    public double getxBreak() {
        return xBreak;
    }
    /**
     * Increases the x-coordinate of the paddle by 1 unit.
     */
    public void addxBreak() {
        xBreak++;
    }
    /**
     * Decreases the x-coordinate of the paddle by 1 unit.
     */
    public void minusxBreak() {
        xBreak--;
    }
    /**
     * Gets half of the width of the paddle.
     *
     * @return Half of the width of the paddle.
     */
    public int getHalfBreakWidth() {
        return breakWidth / 2;
    }
    /**
     * Gets the width of the paddle.
     *
     * @return The width of the paddle.
     */
    public int getBreakWidth() {
        return breakWidth;
    }
    /**
     * Gets the height of the paddle.
     *
     * @return The height of the paddle.
     */
    public int getBreakHeight() {
        return 30;
    }
    /**
     * Sets the x-coordinate of the paddle.
     *
     * @param xBreak The new x-coordinate of the paddle.
     */
    public void setxBreak(double xBreak) {
        this.xBreak = xBreak;
    }
    /**
     * Sets the x-coordinate of the center of the paddle.
     *
     * @param centerBreakX The new x-coordinate of the center of the paddle.
     */
    public void setCenterBreakX(double centerBreakX) {
        this.centerBreakX = centerBreakX;
    }
    /**
     * Sets the y-coordinate of the paddle.
     *
     * @param yBreak The new y-coordinate of the paddle.
     */
    public void setyBreak(double yBreak) {
        this.yBreak = yBreak;
    }
}