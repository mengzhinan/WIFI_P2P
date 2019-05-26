package com.duke.wifip2p.p2phelper;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:26
 * description: https://developer.android.com/guide/topics/connectivity/wifip2p.html#java
 */
public class WifiP2PHelper {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2PBroadCastReceiver mReceiver;

    private WifiP2PListener mWifiP2PListener;

    private boolean isReceiverRegistered;
    private Context applicationContext;
    private static WifiP2PHelper instance;


    private WifiP2PHelper(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null exception.");
        }
        applicationContext = context.getApplicationContext();

        mManager = (WifiP2pManager) applicationContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(applicationContext, Looper.getMainLooper(), null);
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

    public WifiP2PHelper setListener(WifiP2PListener wifiP2PListener) {
        this.mWifiP2PListener = wifiP2PListener;
        if (mReceiver != null) {
            mReceiver.setListener(this.mWifiP2PListener);
        }
        return this;
    }

    public boolean isSupportWifiP2P() {
        return applicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT);
    }

    public void createGroup(final WifiP2PListener wifiP2PListener) {
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onCreateGroup(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onCreateGroup(false);
                }
            }
        });
    }

    public void removeGroup(final WifiP2PListener wifiP2PListener) {
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onRemoveGroup(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onRemoveGroup(false);
                }
            }
        });
    }

    public void connectPeer(WifiP2pDevice wifiP2pDevice, final WifiP2PListener wifiP2PListener) {
        if (wifiP2pDevice == null || TextUtils.isEmpty(wifiP2pDevice.deviceAddress)) {
            return;
        }
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onConnectCallChanged(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onConnectCallChanged(false);
                }
            }
        });
    }

    public void discover(final WifiP2PListener wifiP2PListener) {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onDiscoverPeers(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (wifiP2PListener != null) {
                    wifiP2PListener.onDiscoverPeers(false);
                }
            }
        });
    }

    public String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "可用的";
            case WifiP2pDevice.INVITED:
                return "邀请中";
            case WifiP2pDevice.CONNECTED:
                return "已连接";
            case WifiP2pDevice.FAILED:
                return "失败的";
            case WifiP2pDevice.UNAVAILABLE:
                return "不可用的";
            default:
                return "未知";
        }
    }

    public void registerReceiver() {
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

    public void unRegisterReceiver() {
        if (!isReceiverRegistered) {
            return;
        }
        applicationContext.unregisterReceiver(mReceiver);
        isReceiverRegistered = false;
    }
}
