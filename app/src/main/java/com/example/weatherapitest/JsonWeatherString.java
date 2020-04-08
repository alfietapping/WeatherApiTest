package com.example.weatherapitest;

public class JsonWeatherString {

    private Main main;
    private Weather[] weather;
    private String name;

    public JsonWeatherString(Main main, Weather[] weather){
        this.main = main;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public Main getMain() {
        return main;
    }

    public Weather[] getWeather() {
        return weather;
    }
}
