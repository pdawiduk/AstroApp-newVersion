package com.example.shogun.astroapp.webservice;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Shogun on 17.05.2017.
 */

public interface ForecastService {
    @GET
    Call<Forecast> forecast(@Url String url);
}
//"https://api.openweathermap.org/data/2.5/forecast/daily?q=Lodz&mode=json&units=metric&cnt=7&appid=b98ea5dfa950bfd8ebbb34cc0276459c"