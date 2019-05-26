package com.duke.wifip2p.p2phelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.text.TextUtils;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:25
 * description:
 */
public class WifiP2PBroadCastReceiver extends BroadcastReceiver {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2PListener mWifiP2PListener;

    public WifiP2PBroadCastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.mManager = manager;
        this.mChannel = channel;
    }

    public void setListener(WifiP2PListener wifiP2PListener) {
        this.mWifiP2PListener = wifiP2PListener;
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
            boolean isEnabled = (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
            if (mWifiP2PListener != null) {
                mWifiP2PListener.onWifiP2pEnabled(isEnabled);
            }
            /*if (isEnabled) {
                // Wifi P2P is enabled
            } else {
                // Wi-Fi P2P is not enabled
            }*/
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            WifiP2pDeviceList wifiP2pDeviceList = intent.getParcelableExtra(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            if (mWifiP2PListener != null &&
                    wifiP2pDeviceList != null &&
                    wifiP2pDeviceList.getDeviceList() != null &&
                    wifiP2pDeviceList.getDeviceList().size() > 0) {
                mWifiP2PListener.onPeersAvailable(wifiP2pDeviceList.getDeviceList());
            }
            if (mManager != null) {
                // 异步方法
                mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        if (mWifiP2PListener != null &&
                                peers != null &&
                                peers.getDeviceList() != null &&
                                peers.getDeviceList().size() > 0) {
                            mWifiP2PListener.onPeersAvailable(peers.getDeviceList());
                        }
                    }
                });
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            // 链接状态变化回调
            // 此广播 会和 WIFI_P2P_THIS_DEVICE_CHANGED_ACTION 同时回调
            // 注册广播、连接成功、连接失败 三种时机都会调用
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (mWifiP2PListener != null &&
                    networkInfo != null) {
                mWifiP2PListener.onConnectionChanged(networkInfo.isConnected());
            }
            mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                @Override
                public void onConnectionInfoAvailable(WifiP2pInfo info) {
                    if (mWifiP2PListener == null) {
                        return;
                    }
                    if (info != null &&
                            info.groupOwnerAddress != null) {
                        mWifiP2PListener.onConnectionInfoAvailable(info);
                    }
                }
            });
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            // 此设备的WiFi状态更改回调
            // 此广播 会和 WIFI_P2P_CONNECTION_CHANGED_ACTION 同时回调
            // 注册广播、连接成功、连接失败 三种时机都会调用
            WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            if (mWifiP2PListener != null &&
                    wifiP2pDevice != null &&
                    wifiP2pDevice.deviceAddress != null) {
//                mWifiP2PListener.onSelfDeviceAvailable(wifiP2pDevice);
            }
        }
    }
}
