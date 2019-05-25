package com.duke.wifip2p.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.duke.dfileselector.activity.DefaultSelectorActivity;
import com.duke.wifip2p.R;
import com.duke.wifip2p.p2phelper.WifiP2PHelper;
import com.duke.wifip2p.p2phelper.WifiP2PListener;

import java.util.Collection;

public class ClientSendActivity extends BaseActivity {
    private Button btnScanDevice;
    private Button btnSendFile;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_send);

        btnScanDevice = findViewById(R.id.btn_scan_device);
        btnSendFile = findViewById(R.id.btn_send_file);
        recyclerView = findViewById(R.id.recycler_view);

        btnScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiP2PHelper.getInstance(ClientSendActivity.this).discover(mWifiP2PListener);
            }
        });

        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultSelectorActivity.startActivityForResult(ClientSendActivity.this, false, false, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == DefaultSelectorActivity.FILE_SELECT_REQUEST_CODE) {

        }
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
        public void onConnectionChanged(boolean isConnected) {
            toast(isConnected ? "连接成功" : "连接失败");
        }

        @Override
        public void onConnectionInfoAvailable(@NonNull WifiP2pInfo wifiP2pInfo) {
            toast(wifiP2pInfo.groupOwnerAddress.getHostAddress());
        }

        @Override
        public void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList) {
            toast("发现设备数量 " + wifiP2pDeviceList.size());
        }

        @Override
        public void onSelfDeviceAvailable(@NonNull WifiP2pDevice wifiP2pDevice) {
            toast("onSelfDeviceAvailable " + wifiP2pDevice.deviceAddress);
        }
    };
}
