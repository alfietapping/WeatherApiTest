package com.example.weatherapitest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView locationName;
    TextView dateTv;
    TextView temperature;
    TextView conditionDescription;
    TextView timeStamp;
    ConstraintLayout screenBackground;
    ImageView conditionImage;

    JsonApi jsonApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);

        getWeather();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getWeather();
    }

    private void getWeather(){

        final Call<JsonWeatherString> weather = jsonApi.getJsonWeatherString("Moscow", "1a8c01971f9b312ba492d2ec6d430738");

        weather.enqueue(new Callback<JsonWeatherString>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<JsonWeatherString> call, Response<JsonWeatherString> response) {
                Log.d("Log", "onResponse: called");
                if (!response.isSuccessful()){
                    Log.d("log", "onResponse: " + response.body());
                    return;

                } else {
                    Log.d("Log", "onResponse: Successful");
                    Log.d("log", "onResponse: Successful " + response.body());
                    JsonWeatherString jsonWeatherString = response.body();

                    //temperature = jsonWeatherString.getMain().getTemp() - 273.15 (kelvin to celsius formula)

                    String name = jsonWeatherString.getName();
                    String temp = jsonWeatherString.getMain().getTemp();

                    temperature.setText(getTempCelsius(temp));
                    locationName.setText(name);

                    Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(name));
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                    String currentTime = timeFormat.format(calendar.getTime());
                    timeStamp.setText(currentTime);
                    Log.d("log", "time is " + currentTime);

                    String check = currentTime.substring(0,2);
                    Integer checkInt = Integer.parseInt(check);

                    if (checkInt < 6 || checkInt > 20) {
                        screenBackground.setBackgroundResource(R.drawable.nightbackground);
                    } else {
                        screenBackground.setBackgroundResource(R.drawable.daybackground);
                    }

                    String dayOfTheWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                    String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d");
                    dateFormat.setTimeZone(TimeZone.getTimeZone(name));

                    String dayOfTheMonth = dateFormat.format(date);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(dayOfTheWeek);
                    stringBuilder.append(", ");
                    stringBuilder.append(monthName);
                    stringBuilder.append(" ");
                    stringBuilder.append(dayOfTheMonth);

                    dateTv.setText(stringBuilder);

                    Weather[] weathersArray = jsonWeatherString.getWeather();
                    String main = weathersArray[0].getMain();
                    //conditions ("Clear", "Clouds", "Rain")
                    Log.d("log", "main weather is " + main);

                    switch (main) {
                        case "Clear":
                            conditionImage.setImageResource(R.drawable.ic_sun_1);
                            conditionDescription.setText("Sunny");
                            break;
                        case "Clouds":
                            conditionImage.setImageResource(R.drawable.ic_clouds);
                            conditionDescription.setText("Cloudy");
                            break;
                        case "Rain":
                            conditionImage.setImageResource(R.drawable.ic_rain);
                            conditionDescription.setText("Rainy");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonWeatherString> call, Throwable t) {
                Log.d("Log", "onFailure: " + t.toString());
            }
        });
    }

    private void initVars(){
        locationName = findViewById(R.id.locationTv);
        dateTv = findViewById(R.id.dateTv);
        temperature = findViewById(R.id.temperatureTv);
        conditionDescription = findViewById(R.id.conditionTv);
        timeStamp = findViewById(R.id.timeStampTv);
        screenBackground = findViewById(R.id.screenBackground);
        conditionImage = findViewById(R.id.conditionImageView);
    }

    private String getTempCelsius(String tempKelvin){
        double tempKelvin2 = Double.parseDouble(tempKelvin);
        double tempCelsiusDouble = tempKelvin2 - 273.15;
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        String tempCelsius = nf.format(tempCelsiusDouble) + "°";

        Log.d("log", "getTempCelsius: " + tempCelsius);

        return tempCelsius;
    }
}
