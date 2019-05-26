package com.duke.wifip2p.sockethelper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @Author: duke
 * @DateTime: 2019-05-26 10:53
 * @Description:
 */
public abstract class Base {
    protected static final int PORT = 65432;

    public interface OnReceiveListener {
        void onReceived(String text);
    }

    private OnReceiveListener onReceiveListener;

    public Base(OnReceiveListener onReceiveListener) {
        this.onReceiveListener = onReceiveListener;
    }

    protected void show(String text) {
        Message message = Message.obtain(handler);
        message.obj = text;
        message.sendToTarget();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null || msg.obj == null || onReceiveListener == null) {
                return;
            }
            onReceiveListener.onReceived(msg.obj.toString());
        }
    };
}
