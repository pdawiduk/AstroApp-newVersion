package com.example.shogun.astroapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shogun.astroapp.Database.DataBaseUtility;
import com.example.shogun.astroapp.Database.LocationEntityDao;
import com.example.shogun.astroapp.Database.WeatherEntity;
import com.example.shogun.astroapp.Database.WeatherEntityDao;
import com.example.shogun.astroapp.R;
import com.example.shogun.astroapp.webservice.Temp;

import org.greenrobot.greendao.database.Database;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherInfoFragment extends Fragment {
    @BindView(R.id.tvWindCourse)
    TextView tvWindCourse;

    @BindView(R.id.tvWindStrong)
    TextView tvWindStrong;

    @BindView(R.id.tvWet)
    TextView tvWet;

    public OtherInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        long actualCityID =
                DataBaseUtility.
                        getLocationEntityDao(getContext(),true).
                        queryBuilder().
                        where(LocationEntityDao.Properties.Name.eq(Utility.getCityName(getContext()))).list().get(0).getId();
        WeatherEntity weatherEntity =DataBaseUtility.getWeatherEntityDao(getContext(),true).queryBuilder().where(WeatherEntityDao.Properties.CityId.eq(actualCityID)).list().get(0);

        tvWindCourse.setText(Utility.getFormattedWind(weatherEntity.getDeg()));
        tvWindStrong.setText(Double.toString(weatherEntity.getSpeed()));
        tvWet.setText(Double.toString(weatherEntity.getHumidity()));
    }
}
