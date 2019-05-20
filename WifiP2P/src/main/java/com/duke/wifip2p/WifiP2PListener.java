package com.duke.wifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:24
 * description:
 */
public class WifiP2PListener {

    public static class WifiP2PPeerListListener implements WifiP2pManager.PeerListListener {

        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            Log.v("dkp2ptest", "WifiP2PPeerListListener.onPeersAvailable.start");
            Collection<WifiP2pDevice> collection = peers.getDeviceList();
            Log.v("dkp2ptest", "WifiP2PPeerListListener.onPeersAvailable. size = " + collection.size());
            Iterator<WifiP2pDevice> iterator = collection.iterator();
            while (iterator.hasNext()) {
                WifiP2pDevice device = iterator.next();
                String deviceAddress = device.deviceAddress;
                String deviceName = device.deviceName;
                String primaryDeviceType = device.primaryDeviceType;
                String secondaryDeviceType = device.secondaryDeviceType;
                int status = device.status;
                String s = deviceAddress + "\n"
                        + deviceName + "\n"
                        + primaryDeviceType + "\n"
                        + secondaryDeviceType + "\n"
                        + "status\n"
                        + "===================================";
                Log.v("dkp2ptest", s);
            }
            Log.v("dkp2ptest", "WifiP2PPeerListListener.onPeersAvailable.end");
        }
    }

    public static class WifiP2PChannelListener implements WifiP2pManager.ChannelListener {

        @Override
        public void onChannelDisconnected() {
            Log.v("dkp2ptest", "WifiP2PChannelListener.onChannelDisconnected");
        }
    }


    public static class WifiP2PActionListener implements WifiP2pManager.ActionListener {

        @Override
        public void onSuccess() {
            Log.v("dkp2ptest", "WifiP2PActionListener.discover.onSuccess");
        }

        @Override
        public void onFailure(int reason) {
            Log.v("dkp2ptest", "WifiP2PActionListener.discover.onFailure");
        }
    }
}
