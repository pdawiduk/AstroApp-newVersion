package com.example.shogun.astroapp.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shogun on 19.05.2017.
 */


public class City {
    @SerializedName("id")
    long id ;
    @SerializedName("name")
    String name ;
    @SerializedName("coord")
    Coord coord;

    public City(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }
}
