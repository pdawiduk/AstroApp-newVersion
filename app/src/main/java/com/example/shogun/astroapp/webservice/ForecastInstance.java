package com.example.shogun.astroapp.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shogun on 17.05.2017.
 */

public class ForecastInstance {
    @SerializedName("dt")
    private long dt;

    @SerializedName("temp")
    private Temp temp;

    @SerializedName("pressure")
    private double pressure;

    @SerializedName("weather")
    private List<Weather> weather = new ArrayList<>();

    @SerializedName("speed")
    private double speed;

    @SerializedName("deg")
    private int deg;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("clouds")
    private int clouds;

    @SerializedName("rain")
    private double rain = 0;

    public ForecastInstance(long dt, Temp temp, double pressure, List<Weather> weather, double speed, int deg, int humidity, int clouds, double rain) {
        this.dt = dt;
        this.temp = temp;
        this.pressure = pressure;
        this.weather = weather;
        this.speed = speed;
        this.deg = deg;
        this.humidity = humidity;
        this.clouds = clouds;
        this.rain = rain;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
}
