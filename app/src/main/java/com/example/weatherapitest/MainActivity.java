package com.example.weatherapitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView id;
    TextView main;
    TextView description;
    JsonApi jsonApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.locationName);
        main = findViewById(R.id.mainDescription);
        description = findViewById(R.id.furtherDetails);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);

        getWeather();


    }

    private void getWeather(){
        Call<Weather> weather = jsonApi.getWeather("London", "1a8c01971f9b312ba492d2ec6d430738");

        weather.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.d("Log", "onResponse: called");
                if (!response.isSuccessful()){
                   //TODO add unsuccessful response
                    return;

                } else {
                    Log.d("Log", "onResponse: Successful");

                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d("Log", "onFailure: " + t.toString());
                id.setText("d " + t.getMessage());
            }
        });
    }
}
