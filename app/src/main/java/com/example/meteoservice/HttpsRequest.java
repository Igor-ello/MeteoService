package com.example.meteoservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HttpsRequest implements Runnable {
    static final String KEY = "d3c47e0625444650a9d133848231909";
    static final String API_REQUEST = "https://api.weatherapi.com/v1/current.json";
    public static String CITY = "Genoa";
    Handler handler;

    URL url;

    public HttpsRequest(Handler handler) {
        this.handler = handler;
        try {
            url = new URL(API_REQUEST+"?"+"key="+KEY+"&"+"q="+CITY);
            Log.d("MyLog", "URL: " + url);
        } catch (MalformedURLException e) {
            Log.e("MyLog", "Ошибка формирования URL: " + e.getMessage());
            CITY = "Genoa";
            try {
                url = new URL(API_REQUEST + "?" + "key=" + KEY + "&" + "q=" + CITY);
                Log.d("MyLog", "URL: " + url);
            } catch (MalformedURLException ex) {
                // Если опять ошибка, можно выполнить другие действия по умолчанию
                Log.e("MyLog", "Не удалось восстановить URL: " + ex.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner in = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while(in.hasNext()){
                    response.append(in.nextLine());
                }
                in.close();
                connection.disconnect();

                Message msg = Message.obtain();
                msg.obj = response.toString();
                handler.sendMessage(msg);
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                Log.e("MyLog", "Ошибка: файл не найден");
                // TODO: уведомление пользователя
            } else {
                Log.e("MyLog", "Неизвестная ошибка. Response Code: " + responseCode);
                // TODO: уведомление пользователя
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
