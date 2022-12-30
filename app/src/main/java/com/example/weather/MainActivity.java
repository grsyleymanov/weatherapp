package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button button;
    private TextView result_inform;
    private TextView result_inform2;
    private TextView result_inform3;
    private TextView result_inform4;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        button = findViewById(R.id.button);
        result_inform = findViewById(R.id.result_inform);
        result_inform2 = findViewById(R.id.result_inform2);
        result_inform3 = findViewById(R.id.result_inform3);
        result_inform4 = findViewById(R.id.result_inform4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_field.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                else {
                    String city = user_field.getText().toString();
                    String key = "acbd8e07006317589c1cf3fa4ba22cdf";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";

                    new GetURLData().execute(url);
                }
            }
        });
    }

    private class GetURLData extends AsyncTask <String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();
            result_inform.setText("Ожидайте...");

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (connection != null)
                    connection.disconnect();

                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject object = new JSONObject(result);
                result_inform.setText("Температура: " + object.getJSONObject("main").getDouble("temp"));
                result_inform2.setText("Влажность: " + object.getJSONObject("main").getDouble("humidity"));
                result_inform3.setText("Скорость ветра: " + object.getJSONObject("wind").getDouble("speed"));
                result_inform4.setText("Направление ветра: " + object.getJSONObject("wind").getDouble("deg" + "(в градусах :) )"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}