package com.example.shogun.astroapp.webservice;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shogun on 17.05.2017.
 */



public class Forecast {
   @SerializedName("city")
    City city;
    @SerializedName("cod")
    long cod;

    @SerializedName("list")
    List<ForecastInstance> forecastInstances = new ArrayList<>();

    public Forecast(City city, long cod, List<ForecastInstance> forecastInstances) {
        this.city = city;
        this.cod = cod;
        this.forecastInstances = forecastInstances;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public long getCod() {
        return cod;
    }

    public void setCod(long cod) {
        this.cod = cod;
    }

    public List<ForecastInstance> getForecastInstances() {
        return forecastInstances;
    }

    public void setForecastInstances(List<ForecastInstance> forecastInstances) {
        this.forecastInstances = forecastInstances;
    }
}
