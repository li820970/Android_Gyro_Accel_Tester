package comli820970.httpsgithub.android_gyro_accel_tester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

import java.nio.channels.Channel;

/**
 * Created by Colin on 11/2/2015.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver{

    private WifiP2pManager m_Manager;
    private Channel m_Channel;
    private MainActivity m_Activity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MainActivity activity) {
        super();

        this.m_Manager = manager;
        this.m_Channel = channel;
        this.m_Activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }


}
