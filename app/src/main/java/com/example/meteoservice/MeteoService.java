package com.example.meteoservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MeteoService extends Service {

    Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String str = (String) msg.obj;
                Intent intent = new Intent("MeteoService");
                intent.putExtra("INFO", str);
                sendBroadcast(intent);
            }
        };
        Log.d("MyLog", "Сервис создан");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread weatherThread = new Thread(new HttpsRequest(handler));
        weatherThread.start();
        Log.d("MyService", "Сервис запущен");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("MyService", "Сервис уничтожен");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
