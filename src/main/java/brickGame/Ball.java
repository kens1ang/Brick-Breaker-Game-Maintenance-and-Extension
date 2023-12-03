package brickGame;

public class Ball {
    private double xBall;
    private double yBall;
    private boolean goDownBall = true;
    private boolean goRightBall = true;

    public double getxBall() {
        return xBall;
    }
    public void setxBall(double xBall) {
        this.xBall = xBall;
    }
    public void incxBall(double inc) {this.xBall += inc;}
    public void drexBall(double dre) {this.xBall -= dre;}
    public double getyBall() {
        return yBall;
    }
    public void setyBall(double yBall) {
        this.yBall = yBall;
    }
    public void incyBall(double inc) {this.yBall += inc;}
    public void dreyBall(double dre) {this.yBall -= dre;}
    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }
    public boolean isGoRightBall() {
        return goRightBall;
    }
    public boolean isGoDownBall() {
        return goDownBall;
    }
    public int getBallRadius() {
        return 10;
    }
}