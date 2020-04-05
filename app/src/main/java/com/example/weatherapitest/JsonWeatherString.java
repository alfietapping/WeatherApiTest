package com.example.weatherapitest;

public class JsonWeatherString {

    private Main main;
    private Weather[] weathers;

    public JsonWeatherString(Main main, Weather[] weathers){
        this.main = main;
        this.weathers = weathers;
    }

    public Main getMain() {
        return main;
    }

    public Weather[] getWeathers() {
        return weathers;
    }
}
