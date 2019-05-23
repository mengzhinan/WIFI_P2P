package com.duke.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:26
 * description: https://developer.android.com/guide/topics/connectivity/wifip2p.html#java
 */
public class WifiP2PHelper {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;

    private WifiP2PListener.WifiP2PActionListener wifiP2PActionListener;

    private boolean isReceiverRegistered;
    private Context applicationContext;
    private static WifiP2PHelper instance;

    private WifiP2PHelper(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null exception.");
        }
        applicationContext = context.getApplicationContext();

        WifiP2PListener.WifiP2PChannelListener wifiP2PChannelListener = new WifiP2PListener.WifiP2PChannelListener();
        wifiP2PActionListener = new WifiP2PListener.WifiP2PActionListener();

        mManager = (WifiP2pManager) applicationContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(applicationContext, Looper.getMainLooper(), wifiP2PChannelListener);
        mReceiver = new WifiP2PBroadCastReceiver(mManager, mChannel);
    }

    public synchronized static WifiP2PHelper getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null exception.");
        }
        if (instance == null) {
            instance = new WifiP2PHelper(context);
        }
        return instance;
    }

    public boolean isSupportWifiP2P() {
        return applicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT);
    }

    public void createGroup() {
        mManager.createGroup(mChannel, wifiP2PActionListener);
    }

    public void removeGroup() {
        mManager.removeGroup(mChannel, wifiP2PActionListener);
    }

    public void connect(WifiP2pDevice wifiP2pDevice) {
        WifiP2pConfig config = new WifiP2pConfig();
        if (config.deviceAddress != null && wifiP2pDevice != null) {
            config.deviceAddress = wifiP2pDevice.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int reason) {
                }
            });
        }
    }

    public void discover() {
        mManager.discoverPeers(mChannel, wifiP2PActionListener);
    }

    public void onResume() {
        registerReceiver();
    }

    public void onPause() {
        unRegisterReceiver();
    }

    private void registerReceiver() {
        if (isReceiverRegistered) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intentFilter.addAction(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
        }
        applicationContext.registerReceiver(mReceiver, intentFilter);
        isReceiverRegistered = true;
    }

    private void unRegisterReceiver() {
        if (!isReceiverRegistered) {
            return;
        }
        applicationContext.unregisterReceiver(mReceiver);
        isReceiverRegistered = false;
    }
}
