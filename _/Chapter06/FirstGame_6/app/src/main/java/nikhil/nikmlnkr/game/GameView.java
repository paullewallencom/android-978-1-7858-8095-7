package nikhil.nikmlnkr.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final int MOVINGSPEED = -5;
    private MainGameThread mainThread;
    private BackgroundImage bgImg;
    private PlayerCharacter playerCharacter;
    private ArrayList<Rock> rocks;

    private boolean newGameCreated;
	
    private long startReset;
    private boolean reset;
    private  boolean started;

    private int bestScore;

    private Random rnd = new Random();

    int hh;
    int ww;

    public GameView(Context context)
    {
        super(context);


        //set callback to the surfaceholder to track events
        getHolder().addCallback(this);


        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter <1000)
        {
            counter++;
            try{mainThread.setRunning(false);
                mainThread.join();
                retry = false;
                mainThread = null;
            }catch(InterruptedException e){e.printStackTrace();}
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Drawable d = getResources().getDrawable(R.drawable.player_run);
        hh = d.getIntrinsicHeight();
        ww = d.getIntrinsicWidth();
        bgImg = new BackgroundImage(BitmapFactory.decodeResource(getResources(), R.drawable.background_image));
        playerCharacter = new PlayerCharacter(BitmapFactory.decodeResource(getResources(),R.drawable.player_run),ww/3,hh,3);
        //playerCharacter = new PlayerCharacter(BitmapFactory.decodeResource(getResources(),R.drawable.player_run),200,246,3);
        rocks = new ArrayList<Rock>();

        mainThread = new MainGameThread(getHolder(), this);
        //we can safely start the game loop
        mainThread.setRunning(true);
        mainThread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!playerCharacter.getPlaying()){
                playerCharacter.setPlaying(true);
            }
            if(playerCharacter.getPlaying()){
                if(!started)started = true;
                reset = false;
                playerCharacter.setUp(true);
            }
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            playerCharacter.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()
    {
        if(playerCharacter.getPlaying()) {

            bgImg.update();
            playerCharacter.update();

            //spawn rocks on screen
             if(rocks.size() < 2){
                if(rocks.size() == 0){
                    rocks.add(new Rock(BitmapFactory.decodeResource(getResources(), R.drawable.rock), WIDTH+10, HEIGHT/2, 200, 200, playerCharacter.getScore(),3));
                } else {
                    rocks.add(new Rock(BitmapFactory.decodeResource(getResources(), R.drawable.rock), WIDTH+10, (int) (rnd.nextDouble() * (HEIGHT - maxBoundaryHeight * 2))+maxBoundaryHeight, 200, 200, playerCharacter.getScore(),3));
                }
            }

            for(int i=0; i<rocks.size();i++) {
                rocks.get(i).update();
                if(collision(rocks.get(i),playerCharacter)) {
                    rocks.remove(i);
                    playerCharacter.setPlaying(false);
                    break;
                }
                //remove rocks if they go out of the screen
                if(rocks.get(i).getXC()<-100) {
                    rocks.remove(i);
                    break;
                }

            }
        }
    }

    public boolean collision(GameObj a, GameObj b) {

        if(Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/WIDTH;
        final float scaleFactorY = getHeight()/HEIGHT;
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bgImg.draw(canvas);
            playerCharacter.draw(canvas);

            for(Rock r : rocks) {
                r.draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }

}