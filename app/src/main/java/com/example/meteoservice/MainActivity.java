package com.example.meteoservice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tvName, tvRegion, tvCountry, tvTemp, tvWind, tvHumidity, tvCloud;
    ImageView ivIcon;
    TextView tvTextAboutIcon;


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("result", "Response: " + String.valueOf(intent.getStringExtra("INFO")));
            String str = intent.getStringExtra("INFO");
            try {
                JSONObject start = new JSONObject(str);
                JSONObject location = start.getJSONObject("location");
                JSONObject current = start.getJSONObject("current");

                //location
                String name = location.getString("name");
                String region = location.getString("region");
                String country = location.getString("country");

                //current
                double temp = current.getDouble("temp_c");
                double wind = current.getDouble("wind_kph");
                int humidity = current.getInt("humidity");
                int cloud = current.getInt("cloud");
                String textAboutIcon = current.getJSONObject("condition").getString("text");
                String iconUrl = "https:" + current.getJSONObject("condition").getString("icon");

                tvName.setText(String.valueOf(tvName.getText()) + name);
                tvRegion.setText(String.valueOf(tvRegion.getText()) + region);
                tvCountry.setText(String.valueOf(tvCountry.getText()) + country);
                tvTemp.setText(String.valueOf(tvTemp.getText()) + temp + " ℃");
                tvWind.setText(String.valueOf(tvWind.getText()) + wind + " kph");
                tvHumidity.setText(String.valueOf(tvHumidity.getText()) + humidity + " %");
                tvCloud.setText(String.valueOf(tvCloud.getText()) + cloud + " %");
                tvTextAboutIcon.setText(textAboutIcon);
                Picasso.get().load(iconUrl).into(ivIcon);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(receiver, new IntentFilter("MeteoService"), RECEIVER_EXPORTED);
        Intent intent = new Intent(this, MeteoService.class);
        startService(intent);

        tvName = findViewById(R.id.tvName);
        tvRegion = findViewById(R.id.tvRegion);
        tvCountry = findViewById(R.id.tvCountry);
        tvTemp = findViewById(R.id.tvTemp);
        tvWind = findViewById(R.id.tvWind);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvCloud = findViewById(R.id.tvCloud);
        tvTextAboutIcon = findViewById(R.id.tvTextAboutIcon);
        ivIcon = findViewById(R.id.ivIcon);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, MeteoService.class);
        stopService(intent);
    }
}