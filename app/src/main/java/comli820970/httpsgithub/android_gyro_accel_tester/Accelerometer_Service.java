package comli820970.httpsgithub.android_gyro_accel_tester;
import android.hardware.*; //I'm lazy
import android.content.Context;

/**
 * Created by INTERNET EXAMPLE CODE YAY on 10/13/2015.
 */
public class Accelerometer_Service {

    private static SensorManager sensor_manager;
    private static SensorEventListener sensor_event_listener;
    private static boolean started = false;

    private static float[] accel_reading = new float[3];
    private static float[] mag_reading = new float[3];

    private static float[] rotation_matrix = new float[9];
    private static float[] inclination_matrix = new float[9];
    private static float[] attitude = new float[3];

    private final static double RAD2DEG = 180/Math.PI;

    private static int initial_azimuth = 0;
    private static int initial_pitch = 0;
    private static int initial_roll = 0;

    private static int[] attitude_in_degrees = new int[3];

    public static void start(final Context applicationContext){
        if(started) return;

        sensor_manager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
        sensor_event_listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                int type = event.sensor.getType();

                if(type == Sensor.TYPE_MAGNETIC_FIELD){
                    mag_reading = event.values.clone();

                }
                if(type == Sensor.TYPE_ACCELEROMETER){
                    accel_reading = event.values.clone();

                }

                sensor_manager.getRotationMatrix(rotation_matrix,inclination_matrix,accel_reading,mag_reading);
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

    public static float getMagX(){
        return mag_reading[0];
    }
    public static float getMagY(){
        return mag_reading[1];
    }
    public static float getMagZ(){  return mag_reading[2]; }
}
