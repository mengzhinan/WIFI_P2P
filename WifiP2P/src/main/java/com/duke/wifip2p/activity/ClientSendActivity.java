package com.duke.wifip2p.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.duke.dfileselector.activity.DefaultSelectorActivity;
import com.duke.dfileselector.util.FileSelectorUtils;
import com.duke.wifip2p.DLog;
import com.duke.wifip2p.DeviceAdapter;
import com.duke.wifip2p.R;
import com.duke.wifip2p.p2phelper.WifiP2PHelper;
import com.duke.wifip2p.p2phelper.WifiP2PListener;
import com.duke.wifip2p.sockethelper.Base;
import com.duke.wifip2p.sockethelper.ClientSendHelper;

import java.util.ArrayList;
import java.util.Collection;

public class ClientSendActivity extends BaseActivity {
    public static final String TAG = ClientSendActivity.class.getSimpleName();

    private Button btnScanDevice;
    private Button btnSendFile;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DeviceAdapter adapter;

    private ClientSendHelper clientSendHelper;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_send);

        btnScanDevice = findViewById(R.id.btn_scan_device);
        btnSendFile = findViewById(R.id.btn_send_file);
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new DeviceAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WifiP2pDevice wifiP2pDevice) {
                WifiP2PHelper.getInstance(ClientSendActivity.this).connectPeer(wifiP2pDevice, mWifiP2PListener);
            }
        });

        btnScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiP2PHelper.getInstance(ClientSendActivity.this).discover(mWifiP2PListener);
            }
        });

        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DefaultSelectorActivity.startActivityForResult(ClientSendActivity.this, false, false, 1);
                clientSendHelper.postSend(ip);
            }
        });

        clientSendHelper = new ClientSendHelper(new Base.OnReceiveListener() {
            @Override
            public void onReceived(String text) {
                btnSendFile.setText(text);
                toast(text);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == DefaultSelectorActivity.FILE_SELECT_REQUEST_CODE) {
            ArrayList<String> list = DefaultSelectorActivity.getDataFromIntent(data);
            printData(list);
        }
    }

    private void printData(ArrayList<String> list) {
        if (FileSelectorUtils.isEmpty(list)) {
            return;
        }
        int size = list.size();
        Log.v(TAG, "获取到数据-开始 size = " + size);
        StringBuffer stringBuffer = new StringBuffer("选中的文件：\r\n");
        for (int i = 0; i < size; i++) {
            Log.v(TAG, (i + 1) + " = " + list.get(i));
            stringBuffer.append(list.get(i));
            stringBuffer.append("\r\n");
        }
        Toast.makeText(this, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
        Log.v(TAG, "获取到数据-结束");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 离开组
        WifiP2PHelper.getInstance(this).removeGroup(mWifiP2PListener);
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
            DLog.logV(isSuccess ? "创建群组成功" : "创建群组失败");
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

//        @Override
//        public void onSelfDeviceAvailable(@NonNull WifiP2pDevice wifiP2pDevice) {
//            String msg = "onSelfDeviceAvailable " + wifiP2pDevice.deviceAddress;
//            toast(msg);
//            DLog.logV(msg);
//        }

        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            btnSendFile.setText(R.string.send_text);
            if (wifiP2pInfo != null && !TextUtils.isEmpty(wifiP2pInfo.groupOwnerAddress.getHostAddress())) {
                btnSendFile.setEnabled(true);
                ip = wifiP2pInfo.groupOwnerAddress.getHostAddress();
                toast("连接成功 - " + ip);
            } else {
                btnSendFile.setEnabled(false);
                adapter.setWifiP2pDeviceList(null);
                ip = null;
                toast("连接失败");
            }
        }

        @Override
        public void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList) {
            adapter.setWifiP2pDeviceList(wifiP2pDeviceList);
        }
    };
}
