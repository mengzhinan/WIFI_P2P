package com.duke.wifip2p;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.duke.dpermission.DPermission;

import java.util.ArrayList;

public class WifiP2PActivity extends AppCompatActivity {

    private String[] permissionArray = {Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2p);

        scanBtn = findViewById(R.id.button_scan);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiP2PHelper.getInstance(WifiP2PActivity.this).discover();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DPermission.newInstance(this).setCallback(new DPermission.DCallback() {
            @Override
            public void onResult(ArrayList<DPermission.PermissionInfo> permissionInfoList) {
                WifiP2PHelper.getInstance(WifiP2PActivity.this).onResume();
            }
        }).startRequest(permissionArray);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WifiP2PHelper.getInstance(this).onPause();
    }
}
