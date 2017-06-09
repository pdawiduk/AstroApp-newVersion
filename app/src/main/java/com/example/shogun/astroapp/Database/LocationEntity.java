package com.example.shogun.astroapp.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Shogun on 01.06.2017.
 */



@Entity(nameInDb = "LOCATIONS")
public class LocationEntity  {
    @Id
    @Index(unique = true)
    private long id;
    
    private String name;
    private double lat;
    private double lot;
    @Generated(hash = 1992313127)
    public LocationEntity(long id, String name, double lat, double lot) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lot = lot;
    }
    @Generated(hash = 1723987110)
    public LocationEntity() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getLat() {
        return this.lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLot() {
        return this.lot;
    }
    public void setLot(double lot) {
        this.lot = lot;
    }


    
}
