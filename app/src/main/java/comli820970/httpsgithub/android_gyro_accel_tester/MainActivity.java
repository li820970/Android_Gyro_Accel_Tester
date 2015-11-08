package comli820970.httpsgithub.android_gyro_accel_tester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver updateAccel = new BroadcastReceiver() {//Receiver for Magnemoter only
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView accel_x = (TextView) findViewById(R.id.accel_x);
            accel_x.setText(intent.getExtras().getString("x"));

            TextView accel_y = (TextView) findViewById(R.id.accel_y);
            accel_y.setText(intent.getExtras().getString("y"));

            TextView accel_z = (TextView) findViewById(R.id.accel_z);
            accel_z.setText(intent.getExtras().getString("z"));
        }
    };

    BroadcastReceiver updateMag = new BroadcastReceiver() {//Receiver for Magnemoter only
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView gyro_x = (TextView) findViewById(R.id.mag_x);
            gyro_x.setText(intent.getExtras().getString("x"));

            TextView gyro_y = (TextView) findViewById(R.id.mag_y);
            gyro_y.setText(intent.getExtras().getString("y"));

            TextView gyro_z = (TextView) findViewById(R.id.mag_z);
            gyro_z.setText(intent.getExtras().getString("z"));
        }
    };


    BroadcastReceiver updateGyro = new BroadcastReceiver() {//Receiver for Magnemoter only
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView gyro_x = (TextView) findViewById(R.id.gyro_x);
            gyro_x.setText(intent.getExtras().getString("x"));

            TextView gyro_y = (TextView) findViewById(R.id.gyro_y);
            gyro_y.setText(intent.getExtras().getString("y"));

            TextView gyro_z = (TextView) findViewById(R.id.gyro_z);
            gyro_z.setText(intent.getExtras().getString("z"));
        }
    };

//    private WiFiDirectBroadcastReceiver wifiReceiver;

    private  IntentFilter wifip2p_intentfilter = new IntentFilter();

    private WifiP2pManager wifip2p_manager;
    private WifiP2pManager.Channel wifip2p_channel;
    private WiFiDirectBroadcastReceiver wifiP2preceiver;
    private boolean WifiP2pEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("isActive", true); // Storing string
//        editor.putBoolean("loggingEnabled", false);
//        editor.commit();

        setContentView(R.layout.activity_main);

        registerReceiver(updateMag, new IntentFilter("MAG_UPDATED"));

        registerReceiver(updateAccel, new IntentFilter("ACCEL_UPDATED"));

        registerReceiver(updateGyro, new IntentFilter("GYRO_UPDATED"));


        Accelerometer_Service.start(this.getApplicationContext());
        Accelerometer_Service.appIsNowActive();

//       WifiP2pManager wifiP2pManager = new WifiP2pManager();
//        wifiReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager, wifiP2pManager.channel, this);
        //updateGyroValues();

        //  Indicates a change in the Wi-Fi P2P status.
        wifip2p_intentfilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        wifip2p_intentfilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        wifip2p_intentfilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        wifip2p_intentfilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifip2p_manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifip2p_channel = wifip2p_manager.initialize(this, getMainLooper(), null);

        wifiP2preceiver = new WiFiDirectBroadcastReceiver(wifip2p_manager, wifip2p_channel, this);
        registerReceiver(wifiP2preceiver, wifip2p_intentfilter);

    }

    public void onPause(){
        super.onPause();
        Accelerometer_Service.appIsNowInactive();
        unregisterReceiver(updateAccel);
        unregisterReceiver(updateMag);
        unregisterReceiver(updateGyro);

        unregisterReceiver(wifiP2preceiver);
//        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putBoolean("isActive", false); // Storing string
//        editor.commit();
    }


    public void onResume(){
        super.onResume();
        registerReceiver(updateMag, new IntentFilter("MAG_UPDATED"));
        registerReceiver(updateAccel, new IntentFilter("ACCEL_UPDATED"));
        registerReceiver(updateGyro, new IntentFilter("GYRO_UPDATED"));

        wifiP2preceiver = new WiFiDirectBroadcastReceiver(wifip2p_manager, wifip2p_channel, this);
        registerReceiver(wifiP2preceiver, wifip2p_intentfilter);

        Accelerometer_Service.appIsNowActive();

//        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        ToggleButton logging_togglebutton = (ToggleButton) findViewById(R.id.toggleButton_Logging);
//
//        editor.putBoolean("isActive", true); // Storing string
//        logging_togglebutton.setChecked(prefs.getBoolean("loggingEnabled", false));//set proper state of logging button
//        editor.commit();
    }

//    public void updateGyroValues(){
//        TextView gyro_x = (TextView) findViewById(R.id.gyro_x);
//        TextView gyro_y = (TextView) findViewById(R.id.gyro_y);
//        TextView gyro_z = (TextView) findViewById(R.id.gyro_z);
//
//        gyro_x.setText(Float.toString(Accelerometer_Service.getMagX()));
//        gyro_y.setText(Float.toString(Accelerometer_Service.getMagY()));
//        gyro_z.setText(Float.toString(Accelerometer_Service.getMagZ()));
//    }


    public boolean getGravityButton(){
        ToggleButton gravity_togglebutton = (ToggleButton)  findViewById(R.id.toggleButton_Gravity);
        return gravity_togglebutton.isChecked();

    }

    public void setIsWifiP2pEnabled(boolean status){
        WifiP2pEnabled = status;
    }
    public void loggingToggle(View view) {
        ToggleButton logging_togglebutton = (ToggleButton) findViewById(R.id.toggleButton_Logging);

//        SharedPreferences prefs = getSharedPreferences("GyroTester", MODE_WORLD_READABLE);
//        SharedPreferences.Editor editor = prefs.edit();

        if (logging_togglebutton.isChecked()){

//            editor.putBoolean("loggingEnabled", true);

            try {
                Accelerometer_Service.startLogging();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{

//            editor.putBoolean("loggingEnabled", false);
            try {
                Accelerometer_Service.stopLogging();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void gravityToggle(View view) {
        ToggleButton gravity_togglebutton = (ToggleButton) findViewById(R.id.toggleButton_Gravity);
        if (gravity_togglebutton.isChecked()){

            Accelerometer_Service.setGravityFilter(true);
        }else{

            Accelerometer_Service.setGravityFilter(false);
        }

    }
}
