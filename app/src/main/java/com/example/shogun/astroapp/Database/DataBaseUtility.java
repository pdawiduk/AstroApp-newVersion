package com.example.shogun.astroapp.Database;

import android.content.Context;
import android.provider.ContactsContract;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Shogun on 09.06.2017.
 */

public class DataBaseUtility {
    public static WeatherEntityDao getWeatherEntityDao(Context context, boolean readable) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "newAstro.db");

        Database db;
        if (readable)
            db = helper.getReadableDb();
        else
            db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        return daoSession.getWeatherEntityDao();
    }

    public static LocationEntityDao getLocationEntityDao(Context context, boolean readable) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "newAstro.db");

        Database db;
        if (readable)
            db = helper.getReadableDb();
        else
            db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        return daoSession.getLocationEntityDao();
    }
}
