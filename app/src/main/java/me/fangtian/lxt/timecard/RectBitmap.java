package me.fangtian.lxt.timecard;

/**
 * Created by lxt on 2017/6/24.
 */

public class RectBitmap {
    private int startX = 0;
    private int startY = 0;

    private int width = 0;
    private int height = 0;

    public RectBitmap () {}

    public RectBitmap (int startX, int startY, int width, int height) {

        this.startX = startX;
        this.startY = startY;

        this.width = width;
        this.height = height;

    }

    public int getStartX () { return startX; }
    public void setStartX (int startX) { this.startX = startX; }

    public int getStartY () { return startY; }
    public void setStartY (int startY) { this.startY = startY; }

    public int getWidth () { return width; }
    public void setWidth (int width) { this.width = width; }

    public int getHeight () { return height; }
    public void setHeight (int height) { this.height = height; }

}
