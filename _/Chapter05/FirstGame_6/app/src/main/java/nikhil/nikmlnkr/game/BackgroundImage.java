package nikhil.nikmlnkr.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BackgroundImage {

    private int xc, yc, dxc;
    private Bitmap backgroundImage;

    public BackgroundImage(Bitmap res)
    {
        backgroundImage = res;
        dxc = GameView.MOVINGSPEED;
    }


    public void update()
    {
        xc+=dxc;
        if(xc<-GameView.WIDTH){
            xc=0;
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(backgroundImage, xc, yc,null);
        if(xc<0)
        {
            canvas.drawBitmap(backgroundImage, xc+GameView.WIDTH, yc, null);
        }
    }
}