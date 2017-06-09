package com.example.shogun.astroapp.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shogun on 19.05.2017.
 */

public class Temp {

    @SerializedName("min")
    private double min;

    @SerializedName("max")
    private double max;

    @SerializedName("day")
    private double day;

    public Temp(double min, double max, double day) {
        this.min = min;
        this.max = max;
        this.day = day;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }
}
