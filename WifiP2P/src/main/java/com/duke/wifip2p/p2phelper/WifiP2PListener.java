package com.duke.wifip2p.p2phelper;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:24
 * description:
 */
public interface WifiP2PListener {

    void onDiscoverPeers(boolean isSuccess);

    void onWifiP2pEnabled(boolean isEnabled);

    void onCreateGroup(boolean isSuccess);

    void onRemoveGroup(boolean isSuccess);

    void onConnectionChanged(boolean isConnected);

    void onConnectionInfoAvailable(@NonNull WifiP2pInfo wifiP2pInfo);

    void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList);

    void onSelfDeviceAvailable(@NonNull WifiP2pDevice wifiP2pDevice);
}
