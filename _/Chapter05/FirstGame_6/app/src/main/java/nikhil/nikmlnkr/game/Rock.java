package nikhil.nikmlnkr.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Nikhil on 30-01-2017.
 */

public class Rock extends GameObj{

    private int score;
    private int speed;
    private Random rnd = new Random ();
    private AnimationClass animationClass = new AnimationClass();
    private Bitmap spriteSheet;

    public Rock (Bitmap res, int xc, int yc, int w, int h, int s, int noOfFrames) {
        super.xc = xc;
        super.yc = yc;
        width = w;
        height = h;
        score = s;

        speed = 7  + (int) (rnd.nextDouble()*score/30);

        if(speed > 35)
            speed = 35;

        Bitmap[] img = new Bitmap[noOfFrames];

        spriteSheet = res;

        for(int i=0; i<img.length; i++) {
            img[i] = Bitmap.createBitmap(spriteSheet, 0, i*height, width, height);
        }

        animationClass.setFrames(img);
        animationClass.setDelay(100);

    }

    public void update() {
        xc -= speed;
        animationClass.update();
    }

    public void draw(Canvas canvas) {
        try{
            canvas.drawBitmap(animationClass.getImage(), xc, yc, null);
        } catch (Exception e) {}
    }

    @Override
    public int getWidth () {
        return width-10;
    }
}
