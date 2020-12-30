package nikhil.nikmlnkr.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Nikhil on 13-01-2017.
 */

public class PlayerCharacter extends GameObj{
    private Bitmap spriteSheet;
    private int score;
    private boolean up, playing;
    private AnimationClass ac = new AnimationClass();
    private long startTime;

    public PlayerCharacter(Bitmap res, int w, int h, int noOfFrames) {
        xc = 100;
        yc = GameView.HEIGHT/2;
        dyc = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] img = new Bitmap[noOfFrames];
        spriteSheet = res;

        for(int i=0; i<img.length;i++){
            img[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }
        ac.setFrames(img);
        ac.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b){
        up = b;
    }

    public void update() {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
        ac.update();

        if(up){
            dyc -=1;
        }
        else {
            dyc +=1;
        }

        if(dyc > 10) {
            dyc = 10;
        }
        if(dyc < -10) {
            dyc = -10;
        }

        yc += dyc*2;
        //removing the dya variable
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(ac.getImage(), xc, yc, null);
    }

    public int getScore() {
        return score;
    }

    public boolean getPlaying(){
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDYC() {
        dyc = 0;
    }

    public void resetScore () {
        score = 0;
    }
}