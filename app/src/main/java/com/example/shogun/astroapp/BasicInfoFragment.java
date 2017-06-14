package com.example.shogun.astroapp;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shogun.astroapp.Database.DataBaseUtility;
import com.example.shogun.astroapp.Database.LocationEntity;
import com.example.shogun.astroapp.Database.LocationEntityDao;
import com.example.shogun.astroapp.Database.WeatherEntity;
import com.example.shogun.astroapp.Database.WeatherEntityDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.stetho.inspector.network.ResponseHandlingInputStream.TAG;

public class BasicInfoFragment extends Fragment implements SettingsFragment.Update {
    private static BasicInfoFragment instance;
    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.tvLatitiute)
    TextView tvLatitiute;
    @BindView(R.id.tvLongitiude)
    TextView tvLongitiude;
    @BindView(R.id.tvTemp)
    TextView tvTemp;
    @BindView(R.id.tvPressure)
    TextView tvPressure;
    @BindView(R.id.ivWeatherIcon)
    ImageView ivWeatherIcon;
    @BindView(R.id.tvDescription)
    TextView tvDescription;

    public BasicInfoFragment() {
        // Required empty public constructor
    }

    public static BasicInfoFragment newInstance(){
        instance = new BasicInfoFragment();
        return instance;
    }

    public static BasicInfoFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic_info, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setDisplayData();
    }

    private void setDisplayData() {
        try {
            List<LocationEntity> locationEntity = DataBaseUtility.
                    getLocationEntityDao(getContext(), true).
                    queryBuilder().
                    where(LocationEntityDao.Properties.Name.eq(Utility.getCityName(getContext())))
                    .list();

            List<WeatherEntity> weatherEntities = DataBaseUtility.getWeatherEntityDao(getContext(), true).
                    queryBuilder().
                    where(WeatherEntityDao.Properties.CityId.eq(locationEntity.get(0).getId())).list();


            if (locationEntity.size() > 0 & weatherEntities.size() > 0) {
                LocationEntity entity = locationEntity.get(0);
                tvCityName.setText(entity.getName());
                tvLatitiute.setText(Double.toString(entity.getLat()));
                tvLongitiude.setText(Double.toString(entity.getLot()));
                tvPressure.setText(Double.toString(weatherEntities.get(0).getPressure()));
                tvTemp.setText(Double.toString(weatherEntities.get(0).getTemp()));
                ivWeatherIcon.setImageResource(Utility.getIconResourceForWeatherCondition((int) weatherEntities.get(0).getWeatherId()));
                tvDescription.setText(weatherEntities.get(0).getDescription());
            }
        }catch (Exception ex )
        {
            Snackbar.make(getView(),"problem with dataBase" ,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void callbackUpdate() {

        if(this.getView() != null)
            setDisplayData();
    }
}
