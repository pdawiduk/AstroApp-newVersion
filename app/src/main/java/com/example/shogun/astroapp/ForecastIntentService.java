package com.example.shogun.astroapp;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.shogun.astroapp.Database.DaoMaster;
import com.example.shogun.astroapp.Database.DaoSession;
import com.example.shogun.astroapp.Database.LocationEntity;
import com.example.shogun.astroapp.Database.WeatherEntity;
import com.example.shogun.astroapp.webservice.Forecast;
import com.example.shogun.astroapp.webservice.ForecastInstance;
import com.example.shogun.astroapp.webservice.ForecastService;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shogun.astroapp.Utility.getCityName;

/**
 * Created by Shogun on 14.06.2017.
 */

public class ForecastIntentService extends IntentService {
    private static String TAG = ForecastIntentService.class.getSimpleName();
    private DaoMaster.DevOpenHelper helper;

    private static final String keyApi = "b98ea5dfa950bfd8ebbb34cc0276459c";
    private static final String apiAddr = "http://api.openweathermap.org/data/2.5/forecast/";
    private static final String FILE_NAME = "forecast.json";


    public ForecastIntentService(String name) {
        super(name);
    }

    public ForecastIntentService() {
        super("Service");
    }

    private long getWeatherRaws(DaoMaster.DevOpenHelper helper) {

        Database dbRead = helper.getReadableDb();
        DaoSession daoSession = new DaoMaster(dbRead).newSession();
        return daoSession.getWeatherEntityDao().queryBuilder().list().size();


    }


    private void saveToDatabase(Forecast forecast) {

        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

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


    private void downloadAndSaverData(final Context context) {

        helper = new DaoMaster.DevOpenHelper(this, "newAstro.db");
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        daoSession.getWeatherEntityDao().deleteAll();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiAddr)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForecastService forecastService = retrofit.create(ForecastService.class);
        try {
            String cityName = getCityName(this);
            if (cityName.isEmpty()) cityName = "Lodz";
            final Call<Forecast> forecastCall = forecastService.forecast(buildURL(cityName, keyApi));


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

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "problem z ustawieniami wez je popraw ", Toast.LENGTH_SHORT).show();
        }
    }


    private void convertObjectToJSON(Forecast forecast, Context context) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(forecast);


        FileOutputStream stream;

        try {
            stream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            stream.write(jsonString.getBytes());
            Log.d(TAG, "convertObjectToJSON: ");
            stream.close();
            File file = new File(context.getFilesDir(), FILE_NAME);
            Log.d(TAG, "convertObjectToJSON: file path = " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.d(TAG, "convertObjectToJSON: " + "nie moge zapisać pliku ");
        }


    }

    private String JSONFromFile() throws IOException {

        FileInputStream in = openFileInput(FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception ex) {
            Log.d(TAG, "JSONFromFile: problem z odczytaniem pliku");
        }
        return sb.toString();
    }


    private boolean isConnected() {

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private String buildURL(
            String city,
            String appid) {
        return "daily?q=city=" + city + "&mode=json&units=metric&cnt=7&appid=" + appid;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "onHandleIntent: handle intent");
        if (isConnected()) {
            downloadAndSaverData(getApplicationContext());
        } else {
            Toast.makeText(getApplicationContext(), "nie ma internetu dane moga byc nieaktualne ", Toast.LENGTH_SHORT).show();
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

    public static class ForecastServiceAlarm extends BroadcastReceiver {
        
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context,"broadCastReceiver",Toast.LENGTH_SHORT).show();
            Intent sendIntent = new Intent(context, ForecastIntentService.class);
            context.startService(sendIntent);
        }
    }
}
