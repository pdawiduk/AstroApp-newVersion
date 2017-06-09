package com.example.shogun.astroapp.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Shogun on 01.06.2017.
 */
@Entity(nameInDb = "WEATHERS")
public class WeatherEntity {
    @Id(autoincrement = true)
   @Index(unique = true)
    private long id;


    private long dt;
    private double temp;
    private double maxTemp;
    private double minTemp;


    private double pressure;
    private double humidity;
    private double weatherId;
    private String main;
    private String description;

    private double speed;
    private int deg;
    private int clouds;
    private double rain;

    private long cityId;
    @Generated(hash = 1070036199)
    public WeatherEntity(long id, long dt, double temp, double maxTemp,
            double minTemp, double pressure, double humidity, double weatherId,
            String main, String description, double speed, int deg, int clouds,
            double rain, long cityId) {
        this.id = id;
        this.dt = dt;
        this.temp = temp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weatherId = weatherId;
        this.main = main;
        this.description = description;
        this.speed = speed;
        this.deg = deg;
        this.clouds = clouds;
        this.rain = rain;
        this.cityId = cityId;
    }
    @Generated(hash = 1598697471)
    public WeatherEntity() {
    }
    public long getDt() {
        return this.dt;
    }
    public void setDt(long dt) {
        this.dt = dt;
    }
    public double getTemp() {
        return this.temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }
    public double getMaxTemp() {
        return this.maxTemp;
    }
    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }
    public double getMinTemp() {
        return this.minTemp;
    }
    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }
    public double getPressure() {
        return this.pressure;
    }
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    public double getHumidity() {
        return this.humidity;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    public double getWeatherId() {
        return this.weatherId;
    }
    public void setWeatherId(double weatherId) {
        this.weatherId = weatherId;
    }
    public String getMain() {
        return this.main;
    }
    public void setMain(String main) {
        this.main = main;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getSpeed() {
        return this.speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public int getDeg() {
        return this.deg;
    }
    public void setDeg(int deg) {
        this.deg = deg;
    }
    public int getClouds() {
        return this.clouds;
    }
    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
    public double getRain() {
        return this.rain;
    }
    public void setRain(double rain) {
        this.rain = rain;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getCityId() {
        return this.cityId;
    }
    public void setCityId(long cityId) {
        this.cityId = cityId;
    }
    public void setId(long id) {
        this.id = id;
    }


}
