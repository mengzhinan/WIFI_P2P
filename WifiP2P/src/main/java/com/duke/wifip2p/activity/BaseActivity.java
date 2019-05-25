package com.duke.wifip2p.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.duke.dpermission.DPermission;
import com.duke.wifip2p.p2phelper.WifiP2PHelper;
import com.duke.wifip2p.p2phelper.WifiP2PListener;

import java.util.ArrayList;

/**
 * @Author: duke
 * @DateTime: 2019-05-25 18:14
 * @Description:
 */
public abstract class BaseActivity extends AppCompatActivity {

    private String[] permissionArray = {Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DPermission.newInstance(this).setCallback(new DPermission.DCallback() {
            @Override
            public void onResult(ArrayList<DPermission.PermissionInfo> permissionInfoList) {
                WifiP2PHelper.getInstance(BaseActivity.this).setListener(getListener());
                WifiP2PHelper.getInstance(BaseActivity.this).onResume();
            }
        }).startRequest(permissionArray);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WifiP2PHelper.getInstance(this).onPause();
    }

    protected abstract WifiP2PListener getListener();
}
