package brickGame.Model.GameElements;
/**
 * Represents the game ball in this game.
 * This class has coordinates, movement direction, and methods to update its position.
 */
public class GameBall {
    private double xBall;
    private double yBall;
    private boolean goDownBall = true;
    private boolean goRightBall = true;
    /**
     * Gets the x-coordinate of the ball.
     *
     * @return The x-coordinate of the ball.
     */
    public double getxBall() {
        return xBall;
    }
    /**
     * Sets the x-coordinate of the ball.
     *
     * @param xBall The new x-coordinate of the ball.
     */
    public void setxBall(double xBall) {
        this.xBall = xBall;
    }
    /**
     * Increases the x-coordinate of the ball by a specified amount.
     *
     * @param inc The amount by which to increase the x-coordinate.
     */
    public void incxBall(double inc) {this.xBall += inc;}
    /**
     * Decreases the x-coordinate of the ball by a specified amount.
     *
     * @param dre The amount by which to decrease the x-coordinate.
     */
    public void drexBall(double dre) {this.xBall -= dre;}
    /**
     * Gets the y-coordinate of the ball.
     *
     * @return The y-coordinate of the ball.
     */
    public double getyBall() {
        return yBall;
    }
    /**
     * Sets the y-coordinate of the ball.
     *
     * @param yBall The new y-coordinate of the ball.
     */
    public void setyBall(double yBall) {
        this.yBall = yBall;
    }
    /**
     * Increases the y-coordinate of the ball by a specified amount.
     *
     * @param inc The amount by which to increase the y-coordinate.
     */
    public void incyBall(double inc) {this.yBall += inc;}
    /**
     * Decreases the y-coordinate of the ball by a specified amount.
     *
     * @param dre The amount by which to decrease the y-coordinate.
     */
    public void dreyBall(double dre) {this.yBall -= dre;}
    /**
     * Sets whether the ball is moving downward.
     *
     * @param goDownBall The value indicating whether the ball is moving downward.
     */
    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }
    /**
     * Sets whether the ball is moving rightward.
     *
     * @param goRightBall The value indicating whether the ball is moving rightward.
     */
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }
    /**
     * Checks if the ball is moving rightward.
     *
     * @return {@code true} if the ball is moving rightward, {@code false} otherwise.
     */
    public boolean isGoRightBall() {
        return goRightBall;
    }
    /**
     * Checks if the ball is moving downward.
     *
     * @return {@code true} if the ball is moving downward, {@code false} otherwise.
     */
    public boolean isGoDownBall() {
        return goDownBall;
    }
    /**
     * Gets the radius of the ball.
     *
     * @return The radius of the ball.
     */
    public int getBallRadius() {
        return 10;
    }
}