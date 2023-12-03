package brickGame;

public class Paddle {
    private double centerBreakX;
    private double xBreak = (500 - 130) / 2.0;
    private double yBreak = 640.0f;
    private final int breakWidth = 130;

    public double getCenterBreakX() {
        return centerBreakX;
    }
    public double getyBreak() {
        return yBreak;
    }
    public double getxBreak() {
        return xBreak;
    }
    public void addxBreak() {
        xBreak++;
    }
    public void minusxBreak() {
        xBreak--;
    }
    public int getHalfBreakWidth() {
        return breakWidth / 2;
    }
    public int getBreakWidth() {
        return breakWidth;
    }
    public int getBreakHeight() {
        return 30;
    }
    public void setxBreak(double xBreak) {
        this.xBreak = xBreak;
    }
    public void setCenterBreakX(double centerBreakX) {
        this.centerBreakX = centerBreakX;
    }
    public void setyBreak(double yBreak) {
        this.yBreak = yBreak;
    }
}