package nikhil.nikmlnkr.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Nikhil on 20-02-2017.
 */

public class ExplosionEffect {
    private int xc;
    private int yc;
    private int height;
    private int width;
    private int row;
    private AnimationClass ac = new AnimationClass();
    private Bitmap spriteSheet;

    public ExplosionEffect(Bitmap res, int xc, int yc, int w, int h, int noOfFrames){
        this.xc = xc;
        this.yc = yc;
        this.width = w;
        this.height = h;

        Bitmap[] img = new Bitmap[noOfFrames];

        spriteSheet = res;

        for(int i=0;i<img.length;i++) {
            if(i%5==0&&i>0)row++;
            img[i] = Bitmap.createBitmap(spriteSheet, (i-(5*row))*width, row*height, width, height);
        }
        ac.setFrames(img);
        ac.setDelay(10);
    }

    public void draw(Canvas canvas) {
        if(!ac.playedOnce()){
            canvas.drawBitmap(ac.getImage(),xc,yc,null);
        }
    }

    public void update() {
        if(!ac.playedOnce()){
            ac.update();
        }
    }

    public int getHeight() {
        return height;
    }
}
