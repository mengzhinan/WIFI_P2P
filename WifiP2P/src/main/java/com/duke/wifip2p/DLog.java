package com.duke.wifip2p;

import android.text.TextUtils;
import android.util.Log;

/**
 * @Author: duke
 * @DateTime: 2019-05-26 08:44
 * @Description:
 */
public class DLog {

    public static final String TAG = "DLogtest";

    public static final boolean ID_DEBUG = true;

    public static void logD(String msg) {
        if (!ID_DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.d(TAG, msg);
    }

    public static void logV(String msg) {
        if (!ID_DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.v(TAG, msg);
    }

    public static void logE(String msg) {
        if (!ID_DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.e(TAG, msg);
    }

}
