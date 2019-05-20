package com.duke.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:25
 * description:
 */
public class WifiP2PBroadCastReceiver extends BroadcastReceiver {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2PListener.WifiP2PPeerListListener wifiP2PPeerListListener;

    public WifiP2PBroadCastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.mManager = manager;
        this.mChannel = channel;
        wifiP2PPeerListListener = new WifiP2PListener.WifiP2PPeerListListener();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null ||
                intent == null ||
                TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            // 检查 Wi-Fi P2P 是否已启用
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Log.v("dkp2ptest", "P2PReceiver.onReceive.Wifi P2P is enabled");
                Toast.makeText(context, "P2PReceiver onReceive Wifi P2P is enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Wi-Fi P2P is not enabled
                Log.v("dkp2ptest", "P2PReceiver.onReceive.Wifi P2P is not enabled");
                Toast.makeText(context, "P2PReceiver onReceive Wifi P2P is not enabled", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // 调用 wifip2pmanager.requestpeers() 以获取当前对等方的列表
            WifiP2pDeviceList wifiP2pDeviceList = intent.getParcelableExtra(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            int size = -1;
            if (wifiP2pDeviceList != null && wifiP2pDeviceList.getDeviceList() != null) {
                size = wifiP2pDeviceList.getDeviceList().size();
            }
            Log.v("dkp2ptest", "P2PReceiver.onReceive.peers changed size = " + size);
            Toast.makeText(context, "P2PReceiver onReceive peers changed size = " + size, Toast.LENGTH_SHORT).show();
            if (mManager != null) {
                // 异步方法
                mManager.requestPeers(mChannel, wifiP2PPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            // 链接状态变化回调
            Log.v("dkp2ptest", "P2PReceiver.onReceive.链接状态变化回调");
            Toast.makeText(context, "P2PReceiver onReceive 链接状态变化回调", Toast.LENGTH_SHORT).show();
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            // 此设备的WiFi状态更改回调
            Log.v("dkp2ptest", "P2PReceiver.onReceive.此设备的WiFi状态更改回调");
            Toast.makeText(context, "P2PReceiver onReceive 此设备的WiFi状态更改回调", Toast.LENGTH_SHORT).show();
        }
    }
}
