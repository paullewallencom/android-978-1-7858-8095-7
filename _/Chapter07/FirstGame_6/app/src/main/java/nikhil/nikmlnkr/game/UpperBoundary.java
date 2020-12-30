package nikhil.nikmlnkr.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Nikhil on 19-02-2017.
 */

public class UpperBoundary extends GameObj {
    private Bitmap img;

    public UpperBoundary(Bitmap res, int xc, int yc, int h) {
        height = h;
        width = 20;

        this.xc = xc;
        this.yc = yc;

        dxc = GameView.MOVINGSPEED;
        img = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update(){
        xc += dxc;
    }

    public void draw(Canvas canvas) {
        try{
            canvas.drawBitmap(img, xc, yc, null);
        } catch(Exception e) {

        };
    }
}
