package brickGame;

public class Paddle {
    private double xBreak = (500 - 130) / 2.0;
    private double centerBreakX;
    private double yBreak = 640.0f;
    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;

    public double getCenterBreakX() {
        return centerBreakX;
    }
    public double getyBreak() {
        return yBreak;
    }
    public double getxBreak() {
        return xBreak;
    }
    public double addxBreak() {return xBreak++; }
    public double minusxBreak() {return xBreak--; }
    public int getHalfBreakWidth() {
        return halfBreakWidth;
    }
    public int getBreakWidth() {
        return breakWidth;
    }
    public int getBreakHeight() {
        return breakHeight;
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