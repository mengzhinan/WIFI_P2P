package com.duke.wifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;

import java.util.Collection;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:24
 * description:
 */
public abstract class WifiP2PListener {

    void onWifiP2pEnabled(boolean enabled) {
    }

    void onCreateGroup(boolean isSuccess) {
    }

    void onRemoveGroup(boolean isSuccess) {
    }

    void onConnectionChanged(boolean isConnected) {
    }

    void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
    }

    void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
    }

    void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {
    }
}
