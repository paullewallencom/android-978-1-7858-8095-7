package nikhil.nikmlnkr.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Nikhil on 19-02-2017.
 */

public class LowerBoundary extends GameObj {
    private Bitmap img;

    public LowerBoundary(Bitmap res, int xc, int yc) {
        height = 200;
        width = 20;

        this.xc = xc;
        this.yc = yc;
        dxc = GameView.MOVINGSPEED;

        img = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update() {
        xc += dxc;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(img, xc, yc, null);
    }
}
