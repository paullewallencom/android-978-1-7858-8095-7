package nikhil.nikmlnkr.game;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

public class FullscreenActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set our game to full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set no title on screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(new GameView(this));
    }
}
