package com.duke.wifip2p.sockethelper;

import android.os.Build;
import android.text.TextUtils;

import com.duke.wifip2p.DExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @Author: duke
 * @DateTime: 2019-05-25 17:41
 * @Description:
 */
public class ClientSendHelper extends Base {

    public ClientSendHelper(OnReceiveListener onReceiveListener) {
        super(onReceiveListener);
    }

    public void postSend(final String ip) {
        if (TextUtils.isEmpty(ip)) {
            return;
        }
        DExecutor.get().execute(new Runnable() {
            @Override
            public void run() {
                innerSend(ip);
            }
        });
    }

    private void innerSend(String ip) {
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            socket = new Socket(ip, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            //简化代码，demo
            String sendMsg = "客户端发送：" + Build.BRAND + " - " + Build.VERSION.RELEASE;
            outputStream.write(sendMsg.getBytes());
            byte[] bytes = new byte[1024];
            int length = inputStream.read(bytes);
            String content = new String(bytes, 0, length, Charset.defaultCharset());
            show(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    outputStream = null;
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream = null;
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    socket = null;
                }
            }
        }
    }

}
