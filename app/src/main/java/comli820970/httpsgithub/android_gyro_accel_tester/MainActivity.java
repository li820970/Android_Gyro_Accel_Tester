package comli820970.httpsgithub.android_gyro_accel_tester;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Accelerometer_Service.start(this.getApplicationContext());

        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("isActive", true); // Storing string
        editor.putBoolean("LoggingEnabled", true);
        editor.commit();


        updateValues();


    }

    public void onPause(){
        super.onPause();
    }


    public void onResume(){
        super.onResume();

    }

    public void updateValues(){
        TextView gyro_x = (TextView) findViewById(R.id.gyro_x);
        TextView gyro_y = (TextView) findViewById(R.id.gyro_y);
        TextView gyro_z = (TextView) findViewById(R.id.gyro_z);

        gyro_x.setText(Float.toString(Accelerometer_Service.getMagX()));
        gyro_y.setText(Float.toString(Accelerometer_Service.getMagY()));
        gyro_z.setText(Float.toString(Accelerometer_Service.getMagZ()));
    }



}
