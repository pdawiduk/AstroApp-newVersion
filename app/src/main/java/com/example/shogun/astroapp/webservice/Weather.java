package com.example.shogun.astroapp.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shogun on 19.05.2017.
 */
public class Weather {
    @SerializedName("id")
    private long id;

    @SerializedName("main")
    String main;

    @SerializedName("description")
    String description;

    public Weather(long id, String main, String description) {
        this.id = id;
        this.main = main;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

