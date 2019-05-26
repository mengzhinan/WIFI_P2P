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

    void onWifiP2pEnabled(boolean isEnabled);

    void onCreateGroup(boolean isSuccess);

    void onRemoveGroup(boolean isSuccess);

    void onDiscoverPeers(boolean isSuccess);

    /**
     * 客户端主动发起的连接的动作是否成功 (非 与服务器连接是否成功)
     *
     * @param isConnected
     */
    void onConnectCallChanged(boolean isConnected);

    /**
     * 是否成功建立连接(本地设备 p2p 的连接状态，连接成功、失败时会回调)
     * 此广播 会和 WIFI_P2P_THIS_DEVICE_CHANGED_ACTION 同时回调
     * 注册广播、连接成功、连接失败 三种时机都会调用
     * 注意：此回调是连接正式建立成功 或 失败的回调。但是在进入页面注册广播时，也会返回失败状态
     *
     * @param isConnected
     */
    void onConnectionChanged(boolean isConnected);

    /**
     * 此设备的WiFi状态更改回调(本地设备 p2p 的连接状态，连接成功、失败时会回调)
     * 此广播 会和 WIFI_P2P_CONNECTION_CHANGED_ACTION 同时回调
     * 注册广播、连接成功、连接失败 三种时机都会调用
     *
     * @param wifiP2pDevice
     */
//    void onSelfDeviceAvailable(@NonNull WifiP2pDevice wifiP2pDevice);

    /**
     * 是否与服务器成功建立连接
     *
     * @param wifiP2pInfo
     */
    void onConnectionInfoAvailable(@NonNull WifiP2pInfo wifiP2pInfo);

    /**
     * 扫描的设备结果
     *
     * @param wifiP2pDeviceList
     */
    void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList);

}
