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
    private ArrayList<UpperBoundary> upperBoundary;
    private ArrayList<LowerBoundary> lowerBoundary;

    private int maxBoundaryHeight;
    private int minBoundaryHeight;

    private boolean upBound = true;
    private boolean lowBound = true;

    private int progressDenom = 20;

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
        upperBoundary = new ArrayList<UpperBoundary>();
        lowerBoundary = new ArrayList<LowerBoundary>();

        mainThread = new MainGameThread(getHolder(), this);
        //we can safely start the game loop
        mainThread.setRunning(true);
        mainThread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!playerCharacter.getPlaying() && newGameCreated && reset){
                playerCharacter.setPlaying(true);
                playerCharacter.setUp(true);
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

            if(lowerBoundary.isEmpty()) {
                playerCharacter.setPlaying(false);
                return;
            }

            if(upperBoundary.isEmpty()) {
                playerCharacter.setPlaying(false);
                return;
            }

            bgImg.update();
            playerCharacter.update();

            this.updateUpperBound();
            this.updateLowerBound();

            maxBoundaryHeight = 30+playerCharacter.getScore() / progressDenom;

            if(maxBoundaryHeight > HEIGHT/4)maxBoundaryHeight = HEIGHT/4;
            minBoundaryHeight = 5 + playerCharacter.getScore()/progressDenom;

            for(int i=0; i<lowerBoundary.size();i++) {
                if(collision(lowerBoundary.get(i),playerCharacter)) {
                    playerCharacter.setPlaying(false);
                }
            }

            for(int i=0; i<upperBoundary.size();i++) {
                if(collision(upperBoundary.get(i),playerCharacter)) {
                    playerCharacter.setPlaying(false);
                }
            }

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
        } else {
            playerCharacter.resetDYC();
            if(!reset) {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
            }

            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 2500 && !newGameCreated) {
                newGame();
            }

            if(!newGameCreated) {
                newGame();
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

            for(UpperBoundary ub : upperBoundary){
                ub.draw(canvas);
            }

            for(LowerBoundary lb: lowerBoundary) {
                lb.draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }

    public void updateUpperBound () {
        if(playerCharacter.getScore()%50==0){
            upperBoundary.add(new UpperBoundary(BitmapFactory.decodeResource(getResources(), R.drawable.ground),upperBoundary.get(upperBoundary.size()-1).getXC()+20,0,(int)((rnd.nextDouble()*(maxBoundaryHeight))+1)));
        }

        for(int i=0; i<upperBoundary.size();i++) {
            upperBoundary.get(i).update();
            if(upperBoundary.get(i).getXC()<-20){
                upperBoundary.remove(i);

                if(upperBoundary.get(upperBoundary.size()-1).getHeight()>=maxBoundaryHeight) {
                    upBound = false;
                }

                if(upperBoundary.get(upperBoundary.size()-1).getHeight()<=minBoundaryHeight) {
                    upBound = true;
                }

                if(upBound){
                upperBoundary.add(new UpperBoundary(BitmapFactory.decodeResource(getResources(), R.drawable.ground),upperBoundary.get(upperBoundary.size()-1).getXC()+20,0,upperBoundary.get(upperBoundary.size()-1).getHeight()+1));
                } else {
                    upperBoundary.add(new UpperBoundary(BitmapFactory.decodeResource(getResources(), R.drawable.ground),upperBoundary.get(upperBoundary.size()-1).getXC()+20,0,upperBoundary.get(upperBoundary.size()-1).getHeight()-1));
                }
            }
        }

    }

    public void updateLowerBound () {
        if(playerCharacter.getScore()%40 == 0) {
            lowerBoundary.add(new LowerBoundary(BitmapFactory.decodeResource(getResources(),R.drawable.ground),lowerBoundary.get(lowerBoundary.size()-1).getXC()+20,(int)((rnd.nextDouble()*maxBoundaryHeight)+(HEIGHT-maxBoundaryHeight))));
        }

        for(int i=0;i<lowerBoundary.size();i++) {
            lowerBoundary.get(i).update();

            if(lowerBoundary.get(i).getXC()<-20){
                lowerBoundary.remove(i);

                if(lowerBoundary.get(lowerBoundary.size()-1).getHeight()>=maxBoundaryHeight) {
                    lowBound = false;
                }

                if(lowerBoundary.get(lowerBoundary.size()-1).getHeight()<=minBoundaryHeight) {
                    lowBound = true;
                }

                if(lowBound) {
                    lowerBoundary.add(new LowerBoundary(BitmapFactory.decodeResource(getResources(),R.drawable.ground), lowerBoundary.get(lowerBoundary.size()-1).getXC()+20,lowerBoundary.get(lowerBoundary.size()-1).getYC()+1));
                } else {
                    lowerBoundary.add(new LowerBoundary(BitmapFactory.decodeResource(getResources(),R.drawable.ground), lowerBoundary.get(lowerBoundary.size()-1).getXC()+20,lowerBoundary.get(lowerBoundary.size()-1).getYC()-1));
                }
            }
        }
    }

    public void newGame () {
        lowerBoundary.clear();
        upperBoundary.clear();
        rocks.clear();

        minBoundaryHeight = 5;
        maxBoundaryHeight = 30;

        if(playerCharacter.getScore() > bestScore) {
            bestScore = playerCharacter.getScore();
        }
        playerCharacter.resetScore();
        playerCharacter.resetDYC();
        playerCharacter.setYC(HEIGHT/2);

        for(int i=0; i*20<WIDTH+40;i++) {
            if(i==0) {
                upperBoundary.add(new UpperBoundary(BitmapFactory.decodeResource(getResources(),R.drawable.ground),i*20,0,10));
            } else {
                upperBoundary.add(new UpperBoundary(BitmapFactory.decodeResource(getResources(),R.drawable.ground),i*20,0,upperBoundary.get(i-1).getHeight()+1));
            }
        }

        for(int i = 0; i*20<WIDTH+40;i++) {
            if(i==0) {
                lowerBoundary.add(new LowerBoundary(BitmapFactory.decodeResource(getResources(),R.drawable.ground),i*20, HEIGHT-minBoundaryHeight));
            } else {
                lowerBoundary.add(new LowerBoundary(BitmapFactory.decodeResource(getResources(),R.drawable.ground),i*20, lowerBoundary.get(i-1).getYC()-1));
            }
        }
        newGameCreated = true;
    }

}