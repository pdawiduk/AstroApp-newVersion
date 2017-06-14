package com.example.shogun.astroapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.example.shogun.astroapp.Database.DaoMaster;
import com.example.shogun.astroapp.Database.DaoSession;
import com.example.shogun.astroapp.Database.DataBaseUtility;
import com.example.shogun.astroapp.Database.LocationEntity;
import com.example.shogun.astroapp.Database.WeatherEntity;
import com.example.shogun.astroapp.Database.WeatherEntityDao;
import com.example.shogun.astroapp.webservice.Forecast;
import com.example.shogun.astroapp.webservice.ForecastInstance;
import com.example.shogun.astroapp.webservice.ForecastService;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import org.greenrobot.greendao.database.Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shogun.astroapp.Utility.*;

public class MainActivity extends AppCompatActivity implements SettingsFragment.Update {

    @BindView(R.id.longitiude)
    TextView tvLongitiude;
    @BindView(R.id.latitiude)
    TextView tvLatitiude;
    @BindView(R.id.tvTime)
    TextView tvTime;


    private static final String TAG = MainActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Timer autoUpdate;
    private boolean twoPane = false;


    DaoMaster.DevOpenHelper helper;
    private static MainActivity instance;

    public static MainActivity getActivity() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.tabs) != null) {
            twoPane = false;
        } else {
            twoPane = true;
            MoonFragment moonFragment = new MoonFragment();
            SunFragment sunFragment = new SunFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment1, moonFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment2, sunFragment).commit();

        }
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (twoPane == false) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();


        if (this != null) {
            autoUpdate = new Timer();
            autoUpdate.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (this == null) return;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            tvTime.setText(getTime());
                        }
                    });
                }
            }, 0, 1000);
        }
        updateAstroCalculator(getApplicationContext());
        update();
        long cityID = DataBaseUtility.getCityId(getApplicationContext());
        if ((cityID<0) &DataBaseUtility.getWeatherEntityDao(getApplicationContext(), true).queryBuilder().where(WeatherEntityDao.Properties.CityId.eq(cityID)).list().isEmpty()) {


            downloadAndSaverData();
        }

    }

    public void update() {
        AstroCalculator.Location location = getAstroCalculator().getLocation();
        tvLatitiude.setText("Szerokość " + String.valueOf(location.getLatitude()));
        tvLongitiude.setText("Długość " + String.valueOf(location.getLongitude()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), SettingsFragment.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_refresh) {
            downloadAndSaverData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void downloadAndSaverData() {
        Intent serviceIntent = new Intent(this, ForecastIntentService.class);
        startService(serviceIntent);


        Intent alararmInten = new Intent(getActivity(), ForecastIntentService.ForecastServiceAlarm.class);

        PendingIntent pi = PendingIntent.getBroadcast(getActivity()
                , 0, alararmInten, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        am.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, pi);


    }

    @Override
    public void callbackUpdate() {
        downloadAndSaverData();
        Log.d(TAG, "callbackUpdate: wywolanie callbacka" );
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return SunFragment.newInstance();
                case 1:
                    return MoonFragment.newInstance();
                case 2:
                    return BasicInfoFragment.newInstance();
                case 3:
                    return OtherInfoFragment.newInstance();
                case 4:
                    return ForecastFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {

            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SUN";
                case 1:
                    return "MOON";
                case 2:
                    return "INFO";
                case 3:
                    return "EX INFO";
                case 4:
                    return "FORECAST";
            }
            return null;
        }
    }


}

