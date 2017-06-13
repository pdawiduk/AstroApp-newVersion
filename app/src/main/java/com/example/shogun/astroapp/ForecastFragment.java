package com.example.shogun.astroapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shogun.astroapp.Database.DataBaseUtility;
import com.example.shogun.astroapp.Database.LocationEntity;
import com.example.shogun.astroapp.Database.LocationEntityDao;
import com.example.shogun.astroapp.Database.WeatherEntity;
import com.example.shogun.astroapp.Database.WeatherEntityDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.shogun.astroapp.Database.WeatherEntityDao.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    @BindView(R.id.rvForecast)
    RecyclerView rvForecast;
    private ForecastAdapter forecastAdapter;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<LocationEntity> locationEntities = DataBaseUtility.getLocationEntityDao(getContext(), true).
                queryBuilder().
                where(LocationEntityDao.Properties.Name.
                        eq(Utility.getCityName(getContext()))).list();
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);


        if (locationEntities.size() > 0) {

            long actualCityId = locationEntities.get(0).getId();
            QueryBuilder<WeatherEntity> queryBuilder = DataBaseUtility.getWeatherEntityDao(getContext(), true).queryBuilder();

            queryBuilder.where(Properties.CityId.eq(actualCityId),
                    queryBuilder.and(Properties.CityId.eq(actualCityId),
                            Properties.Dt.gt(Utility.getdateInMiliFromNextDay())
                    ));
            List<WeatherEntity> list = queryBuilder.list();
            if (!list.isEmpty()) {
                forecastAdapter = new ForecastAdapter(list);
                rvForecast.setLayoutManager(llm);
                rvForecast.setAdapter(forecastAdapter);
            }
        }
    }


}
