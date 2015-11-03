package comli820970.httpsgithub.android_gyro_accel_tester;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.*; //I'm lazy
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.opencsv.CSVWriter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * Created by INTERNET EXAMPLE CODE YAY on 10/13/2015.
 */
public class Accelerometer_Service {

    private static SensorManager sensor_manager;
    private static SensorEventListener sensor_event_listener;
    private static boolean started = false;

    private static float[] accel_reading = new float[3];
    private static float[] gravity_accel = new float[3];
    private static float[] mag_reading = new float[3];
    private static float[] gyro_reading = new float[3];

    private static float[] rotation_matrix = new float[9];
    private static float[] inclination_matrix = new float[9];
    private static float[] attitude = new float[3];

    private final static double RAD2DEG = 180/Math.PI;

    private static int initial_azimuth = 0;
    private static int initial_pitch = 0;
    private static int initial_roll = 0;

    private static int[] attitude_in_degrees = new int[3];

    private static boolean isAppActive = true;
    private static boolean loggingActive = false;
    private static boolean gravityFilter = false;

    private static CSVWriter csv_writer;

    public static void start(final Context applicationContext){
        if(started) return;
        // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);



        sensor_manager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
        sensor_event_listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                // SharedPreferences prefs = PreferenceManager.getSharedPreferences("GyroTester", MODE_WORLD_READABLE);

                if(loggingActive){
                    AddLogEntry(event.timestamp);
                }

                int type = event.sensor.getType();

                if(type == Sensor.TYPE_MAGNETIC_FIELD){
                    mag_reading = event.values.clone();

                    if(isAppActive){
                        Intent i = new Intent("MAG_UPDATED");
                        i.putExtra("x",Float.toString(mag_reading[0]));
                        i.putExtra("y",Float.toString(mag_reading[1]));
                        i.putExtra("z",Float.toString(mag_reading[2]));

                        applicationContext.sendBroadcast(i);

                    }


                }

                if(type == Sensor.TYPE_GYROSCOPE){
                    gyro_reading = event.values.clone();

                    if(isAppActive){
                        Intent i = new Intent("GYRO_UPDATED");
                        i.putExtra("x",Float.toString(gyro_reading[0]));
                        i.putExtra("y",Float.toString(gyro_reading[1]));
                        i.putExtra("z",Float.toString(gyro_reading[2]));

                        applicationContext.sendBroadcast(i);

                    }


                }

                if(type == Sensor.TYPE_ACCELEROMETER){
                    accel_reading = event.values.clone();

                    if(isAppActive){


                        if (gravityFilter) {
                            Intent i = new Intent("ACCEL_UPDATED");
                            i.putExtra("x", Float.toString(accel_reading[0] - gravity_accel[0]));
                            i.putExtra("y", Float.toString(accel_reading[1] - gravity_accel[1]));
                            i.putExtra("z", Float.toString(accel_reading[2] - gravity_accel[2]));


//                            i.putExtra("x",  gravity_accel[0]);
//                            i.putExtra("y",  gravity_accel[1]);
//                            i.putExtra("z",  gravity_accel[2]);
                            applicationContext.sendBroadcast(i);
                        }else{
                            Intent i = new Intent("ACCEL_UPDATED");
                            i.putExtra("x", Float.toString(accel_reading[0]));
                            i.putExtra("y", Float.toString(accel_reading[1]));
                            i.putExtra("z", Float.toString(accel_reading[2]));

                            applicationContext.sendBroadcast(i);
                        }
                    }

                }

                if(type == Sensor.TYPE_GRAVITY){
                    gravity_accel = event.values.clone();
                }


                sensor_manager.getRotationMatrix(rotation_matrix, inclination_matrix, accel_reading, mag_reading);
                sensor_manager.getOrientation(rotation_matrix,attitude);


                attitude_in_degrees[0] =  (int) Math.round(attitude[0] * RAD2DEG);    //azimuth
                attitude_in_degrees[1] = (int) Math.round(attitude[1] * RAD2DEG);     //pitch
                attitude_in_degrees[2] = (int) Math.round(attitude[2] * RAD2DEG);     //roll
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensor_manager.registerListener(sensor_event_listener,
                sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        sensor_manager.registerListener(sensor_event_listener,
                sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
        sensor_manager.registerListener(sensor_event_listener,
                sensor_manager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_UI);
        sensor_manager.registerListener(sensor_event_listener,
                sensor_manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_UI);

                started = true;

    }

    public static boolean isActive(){
        return started;
    }

    public static void stop(){
        if (started){
            sensor_manager.unregisterListener(sensor_event_listener);
            started = false;
        }
    }

    public static void startLogging() throws IOException {
        if (!loggingActive){
//            String baseDir = android.os.Environment.getDataDirectory().getAbsolutePath();
            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName =  DateFormat.getDateTimeInstance().format(new Date())+".csv";
            String filePath = baseDir + File.separator + fileName;
            //File f = new File(filePath );

//            try {
            csv_writer = new CSVWriter(new FileWriter(filePath.replace(":","-")));
//            } catch (IOException e) {
//                //e.printStackTrace();
//
//            }

            String[] data = {"Time", "TimeStamp",
                    "Mag x", "Mag y", "Mag z",
                    "Accel x", "Accel y", "Accel z",
                    "Gyro x", "Gyro y", "Gyro z",
                    "Gravity x", "Gravity y", "Gravity z"
            };
            csv_writer.writeNext(data);
            loggingActive = true;

        }
    }
    public static void stopLogging() throws IOException {
        if (loggingActive){
//            try {
            csv_writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            loggingActive = false;

        }
    }

    public static void setGravityFilter(boolean val){
        gravityFilter = val;
    }

    public static boolean getLogging(){
        return loggingActive;
    }
    public static void AddLogEntry(long time_stamp){
        if (loggingActive){
            String[] data = {DateFormat.getTimeInstance().format(new Date()),String.valueOf(time_stamp),
                    String.valueOf(mag_reading[0]),  String.valueOf(mag_reading[1]),  String.valueOf(mag_reading[2]),
                    String.valueOf(accel_reading[0]), String.valueOf(accel_reading[1]), String.valueOf(accel_reading[2]),
                    String.valueOf(gyro_reading[0]), String.valueOf(gyro_reading[1]), String.valueOf(gyro_reading[2]),
                    String.valueOf(gravity_accel[0]), String.valueOf(gravity_accel[1]), String.valueOf(gravity_accel[2])

            };

            csv_writer.writeNext(data );
        }


    }
    public static void appIsNowInactive(){isAppActive = false;}
    public static void appIsNowActive(){isAppActive = true;}

//    public static float getMagX(){
//        return mag_reading[0];
//    }
//    public static float getMagY(){ return mag_reading[1]; }
//    public static float getMagZ(){  return mag_reading[2]; }


}
