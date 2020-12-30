package nikhil.nikmlnkr.game;

import android.graphics.Rect;

/**
 * Created by Nikhil on 13-01-2017.
 */

public abstract class GameObj {
    protected int xc, yc, dxc, dyc; //Our x and y coordinates along with their displacement variables
    protected int width, height;    //width and height of our objects

    public int getXC() {
        return xc;
    }

    public int getYC() {
        return yc;
    }

    public void setXC(int xc) {
        this.xc = xc;
    }

    public void setYC(int yc) {
        this.yc = yc;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rect getRectangle() {
        return new Rect(xc,yc,xc+width,yc+height);
    }
}
