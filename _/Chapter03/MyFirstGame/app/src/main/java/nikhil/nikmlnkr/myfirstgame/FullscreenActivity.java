package nikhil.nikmlnkr.myfirstgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        final TextView tv =
                (TextView) findViewById(R.id.myTextView);

        final Button button =
                (Button) findViewById(R.id.myFirstButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                tv.setText("Button Clicked");
            }
        });

        final RelativeLayout parent =
                (RelativeLayout) findViewById(R.id.parent);

        final TextView text =
                (TextView) findViewById(R.id.coords);

        parent.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                text.setText
                        ("Touch at " + ev.getX() + ", " + ev.getY());
                return true;
            }
        });
        SensorManager sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                TextView acc = (TextView) findViewById(R.id.accel);
                acc.setText("x: "+x+", y: "+y+", z: "+z);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor,
                                          int accuracy){
            }
        }, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onBackPressed() {
        // your code.
        Toast.makeText(FullscreenActivity.this,
                "Back button pressed", Toast.LENGTH_SHORT).show();
    }
}
