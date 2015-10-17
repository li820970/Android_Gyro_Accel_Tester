package comli820970.httpsgithub.android_gyro_accel_tester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = prefs.edit();

        setContentView(R.layout.activity_main);
        Accelerometer_Service.start(this.getApplicationContext());



        editor.putBoolean("isActive", true); // Storing string
        editor.putBoolean("loggingEnabled", false);
        editor.commit();


        updateGyroValues();
        BroadcastReceiver update = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TextView gyro_x = (TextView) findViewById(R.id.gyro_x);
                gyro_x.setText(intent.getExtras().getString("x"));

                TextView gyro_y = (TextView) findViewById(R.id.gyro_y);
                gyro_x.setText(intent.getExtras().getString("y"));

                TextView gyro_z = (TextView) findViewById(R.id.gyro_z);
                gyro_x.setText(intent.getExtras().getString("z"));
            }
        };
        registerReceiver( update, new IntentFilter("MAG_UPDATED"));

    }

    public void onPause(){
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("isActive", false); // Storing string
        editor.commit();
    }


    public void onResume(){
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = prefs.edit();

        ToggleButton logging_togglebutton = (ToggleButton) findViewById(R.id.toggleButton_Logging);

        editor.putBoolean("isActive", true); // Storing string
        logging_togglebutton.setChecked(prefs.getBoolean("loggingEnabled", false));//set proper state of logging button

        editor.commit();
    }

    public void updateGyroValues(){
        TextView gyro_x = (TextView) findViewById(R.id.gyro_x);
        TextView gyro_y = (TextView) findViewById(R.id.gyro_y);
        TextView gyro_z = (TextView) findViewById(R.id.gyro_z);

        gyro_x.setText(Float.toString(Accelerometer_Service.getMagX()));
        gyro_y.setText(Float.toString(Accelerometer_Service.getMagY()));
        gyro_z.setText(Float.toString(Accelerometer_Service.getMagZ()));
    }



    public void loggingToggle(View view) {
        ToggleButton logging_togglebutton = (ToggleButton) findViewById(R.id.toggleButton_Logging);

        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = prefs.edit();

        if (logging_togglebutton.isChecked()){



            editor.putBoolean("loggingEnabled", true);
            Accelerometer_Service.startLogging();
        }else{



            editor.putBoolean("loggingEnabled", false);
            Accelerometer_Service.stopLogging();
        }
    }
}
