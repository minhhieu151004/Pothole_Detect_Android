package com.example.prj_android_detectpothole.MODEL;

import android.util.Log;

import com.example.prj_android_detectpothole.OBJECT.MyUserToken;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.net.URISyntaxException;

public class WebSocketManager {
    private static final String TAG = "WebSocketManager";
    private static final String SERVER_URL = "https://api-detect-pothole-app.onrender.com";

    private Socket socket;

    public void startSocket() {
        try {
            // Tạo OkHttpClient để thêm token vào header
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder()
                                .header("token", "Bearer " + MyUserToken.token); // Thêm token vào header
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    })
                    .build();

            // Cấu hình OkHttpClient cho Socket.IO
            IO.setDefaultOkHttpCallFactory((Call.Factory) okHttpClient);
            IO.setDefaultOkHttpWebSocketFactory((WebSocket.Factory) okHttpClient);

            // Khởi tạo socket với URL server
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"}; // Sử dụng WebSocket
            socket = IO.socket(SERVER_URL, options);

            // Lắng nghe các sự kiện
            socket.on(Socket.EVENT_CONNECT, args -> {
                Log.d(TAG, "Socket connected!");
            }).on("newPothole", args -> {
                if (args != null && args.length > 0) {
                    Log.d(TAG, "Received new pothole data: " + args[0]);
                } else {
                    Log.e(TAG, "No data received in 'newPothole' event.");
                }
            }).on(Socket.EVENT_DISCONNECT, args -> {
                Log.d(TAG, "Socket disconnected!");
            }).on(Socket.EVENT_CONNECT_ERROR, args -> {
                Log.e(TAG, "Connection error: " + (args.length > 0 ? args[0] : "Unknown error"));
            });

            // Kết nối socket
            socket.connect();

        } catch (URISyntaxException e) {
            Log.e(TAG, "URISyntaxException: " + e.getMessage());
        }
    }

    public void stopSocket() {
        if (socket != null) {
            socket.disconnect();
            socket.close();
            Log.d(TAG, "Socket stopped.");
        }
    }
}
