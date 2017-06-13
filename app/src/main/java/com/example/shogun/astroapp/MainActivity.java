package com.example.shogun.astroapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.example.shogun.astroapp.Database.DaoMaster;
import com.example.shogun.astroapp.Database.DaoSession;
import com.example.shogun.astroapp.Database.LocationEntity;
import com.example.shogun.astroapp.Database.WeatherEntity;
import com.example.shogun.astroapp.Database.WeatherEntityDao;
import com.example.shogun.astroapp.webservice.Forecast;
import com.example.shogun.astroapp.webservice.ForecastInstance;
import com.example.shogun.astroapp.webservice.ForecastService;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class MainActivity extends AppCompatActivity {

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
    private static final String keyApi = "b98ea5dfa950bfd8ebbb34cc0276459c";
    private static final String apiAddr = "http://api.openweathermap.org/data/2.5/forecast/";
    private static final String FILE_NAME = "forecast.json";


    DaoMaster.DevOpenHelper helper;


    private void downloadData(final Context context) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiAddr)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForecastService forecastService = retrofit.create(ForecastService.class);

        final Call<Forecast> forecastCall = forecastService.forecast(buildURL(getCityName(this), keyApi));


        Callback<Forecast> callback = new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                saveToDatabase(response.body());
                convertObjectToJSON(response.body(), context);
                Log.d(TAG, "onResponse:  odpowiedz ");

            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        };
        forecastCall.enqueue(callback);


    }

    private String buildURL(
            String city,
            String appid) {
        return "daily?q=city=" + city + "&mode=json&units=metric&cnt=7&appid=" + appid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (isConnected()) {
            downloadData(getApplicationContext());
        } else {
            Gson gson = new Gson();
            try {
                Forecast forecast = gson.fromJson(JSONFromFile(), Forecast.class);
                saveToDatabase(forecast);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onResume: nie polaczono");

        }

    }

    private long getWeatherRaws(DaoMaster.DevOpenHelper helper) {

        Database dbRead = helper.getReadableDb();
        DaoSession daoSession = new DaoMaster(dbRead).newSession();
        return daoSession.getWeatherEntityDao().queryBuilder().list().size();


    }

    private void saveToDatabase(Forecast forecast) {


        helper = new DaoMaster.DevOpenHelper(this, "newAstro.db");

        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        daoSession.getWeatherEntityDao().deleteAll();

        LocationEntity instance = new LocationEntity(
                forecast.getCity().getId(),
                forecast.getCity().getName(),
                forecast.getCity().getCoord().getLat(),
                forecast.getCity().getCoord().getLon());


        daoSession.insertOrReplace(instance);


        List<WeatherEntity> weatherEntities = new ArrayList<>();

        long countOfWeather = getWeatherRaws(helper);

        for (int i = 0; i < forecast.getForecastInstances().size(); i++) {

            ForecastInstance instance1 = forecast.getForecastInstances().get(i);

            WeatherEntity weather = new WeatherEntity();
            weather.setId(countOfWeather + i);
            weather.setDt(instance1.getDt());
            weather.setTemp(instance1.getTemp().getDay());
            weather.setMaxTemp(instance1.getTemp().getMax());
            weather.setMinTemp(instance1.getTemp().getMin());
            weather.setPressure(instance1.getPressure());
            weather.setHumidity(instance1.getHumidity());
            weather.setWeatherId(instance1.getWeather().get(0).getId());
            weather.setMain(instance1.getWeather().get(0).getMain());
            weather.setDescription(instance1.getWeather().get(0).getDescription());
            weather.setSpeed(instance1.getSpeed());
            weather.setDeg(instance1.getDeg());
            weather.setClouds(instance1.getClouds());
            weather.setRain(instance1.getRain());
            weather.setCityId(instance.getId());

            weatherEntities.add(weather);
        }

        for (WeatherEntity entity : weatherEntities) {
            daoSession.insertOrReplace(entity);
            Log.d(TAG, "onResume: insert id :" + entity.getId());
        }
    }


    private void convertObjectToJSON(Forecast forecast,Context context) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(forecast);


        FileOutputStream stream;

        try{
            stream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            stream.write(jsonString.getBytes());
            Log.d(TAG, "convertObjectToJSON: ");
            stream.close();
            File file = new File(context.getFilesDir(),FILE_NAME);
            Log.d(TAG, "convertObjectToJSON: file path = " + file.getAbsolutePath());
        }catch(Exception e){
            Log.d(TAG, "convertObjectToJSON: " + "nie moge zapisać pliku ");
        }



    }

    private String JSONFromFile() throws IOException {
        String content = null;
        File file = new File("forecast.json"); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content;
    }


    private boolean isConnected() {

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
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

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new SunFragment();
                case 1:
                    return new MoonFragment();
                case 2:
                    return new BasicInfoFragment();
                case 3:
                    return new OtherInfoFragment();
                case 4:
                    return new ForecastFragment();
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
