package comli820970.httpsgithub.android_gyro_accel_tester;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Colin on 11/9/2015.
 */
public class ListViewAdaptor extends ArrayAdapter{

    public ListViewAdaptor(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
