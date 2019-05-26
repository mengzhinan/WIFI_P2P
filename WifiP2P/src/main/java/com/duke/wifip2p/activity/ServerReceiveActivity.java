package com.duke.wifip2p.activity;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duke.wifip2p.DLog;
import com.duke.wifip2p.R;
import com.duke.wifip2p.p2phelper.WifiP2PHelper;
import com.duke.wifip2p.p2phelper.WifiP2PListener;
import com.duke.wifip2p.sockethelper.Base;
import com.duke.wifip2p.sockethelper.ServerReceiveHelper;

import java.util.Collection;

public class ServerReceiveActivity extends BaseActivity {

    private Button btnCreateGroup;
    private Button btnRemoveGroup;

    private TextView textView;

    private ServerReceiveHelper serverReceiveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_receive);

        textView = findViewById(R.id.textView);
        btnCreateGroup = findViewById(R.id.btn_create_group);
        btnRemoveGroup = findViewById(R.id.btn_remove_group);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiP2PHelper.getInstance(ServerReceiveActivity.this).createGroup(mWifiP2PListener);
            }
        });
        btnRemoveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiP2PHelper.getInstance(ServerReceiveActivity.this).removeGroup(mWifiP2PListener);
            }
        });

        WifiP2PHelper.getInstance(ServerReceiveActivity.this).createGroup(mWifiP2PListener);

        serverReceiveHelper = new ServerReceiveHelper(new Base.OnReceiveListener() {
            @Override
            public void onReceived(String text) {
                textView.setText(text);
                toast(text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverReceiveHelper.setQuit();
        WifiP2PHelper.getInstance(ServerReceiveActivity.this).removeGroup(mWifiP2PListener);
    }

    @Override
    protected WifiP2PListener getListener() {
        return mWifiP2PListener;
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private WifiP2PListener mWifiP2PListener = new WifiP2PListener() {
        @Override
        public void onDiscoverPeers(boolean isSuccess) {
            toast(isSuccess ? "扫描设备成功" : "扫描设备失败");
        }

        @Override
        public void onWifiP2pEnabled(boolean isEnabled) {
            toast(isEnabled ? "wifi p2p 可用" : "wifi p2p 不可用");
        }

        @Override
        public void onCreateGroup(boolean isSuccess) {
            toast(isSuccess ? "创建群组成功" : "创建群组失败");
        }

        @Override
        public void onRemoveGroup(boolean isSuccess) {
            toast(isSuccess ? "移除群组成功" : "移除群组失败");
        }

        @Override
        public void onConnectCallChanged(boolean isConnected) {
            String msg = isConnected ? "调用连接成功" : "调用连接失败";
            toast(msg);
            DLog.logV(msg);
        }

        @Override
        public void onConnectionChanged(boolean isConnected) {
            String msg = isConnected ? "连接成功" : "连接失败";
            toast(msg);
            DLog.logV(msg);
        }

//        @Override
//        public void onSelfDeviceAvailable(@NonNull WifiP2pDevice wifiP2pDevice) {
//            String msg = "onSelfDeviceAvailable " + wifiP2pDevice.deviceAddress;
//            toast(msg);
//            DLog.logV(msg);
//        }

        @Override
        public void onConnectionInfoAvailable(@NonNull WifiP2pInfo wifiP2pInfo) {
            toast(wifiP2pInfo.groupOwnerAddress.getHostAddress());
        }

        @Override
        public void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList) {
            toast("发现设备数量 " + wifiP2pDeviceList.size());
        }
    };
}
