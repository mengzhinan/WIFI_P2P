package com.duke.wifip2p.sockethelper;

import android.os.Build;

import com.duke.wifip2p.DExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @Author: duke
 * @DateTime: 2019-05-25 17:40
 * @Description:
 */
public class ServerReceiveHelper extends Base {

    private boolean isQuit;

    public void setQuit() {
        isQuit = true;
    }

    public ServerReceiveHelper(OnReceiveListener onReceiveListener) {
        super(onReceiveListener);
        postReceive();
    }

    private void postReceive() {
        DExecutor.get().execute(new Runnable() {
            @Override
            public void run() {
                innerReceive();
            }
        });
    }

    private void innerReceive() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(10000);
            while (!isQuit) {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                //简化代码，demo
                byte[] bytes = new byte[1024];
                int length = inputStream.read(bytes);
                String text = new String(bytes, 0, length, Charset.defaultCharset());
                String response = "服务端回复：" + Build.BRAND + " - " + Build.VERSION.RELEASE;
                outputStream.write(response.getBytes());
                show(text);
            }
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
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    serverSocket = null;
                }
            }
        }
    }


}
