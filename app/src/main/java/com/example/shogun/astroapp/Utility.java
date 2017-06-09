package com.example.shogun.astroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.concurrent.TimeUnit;

/**
 * Created by Shogun on 15.05.2017.
 */

public class Utility {

    private static DateTime dateTime = new DateTime();
    private static DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/Warsaw");
    private static AstroDateTime astroDateTime;
    private static AstroCalculator.Location location;
    private static AstroCalculator astroCalculator;
    private static final String TAG = Utility.class.getSimpleName();


    public static int getMonth(){
        return dateTime.getMonthOfYear();
    }
    public static int getDay(){
        return dateTime.getDayOfMonth();
    }
    public static int getYear(){
        return dateTime.getYear();
    }
    public static int getHour(){
        return dateTime.getHourOfDay();
    }
    public static int getMinute(){
        return dateTime.getMinuteOfHour();
    }
    public static int getSecond(){
        return dateTime.getSecondOfMinute();
    }

    public static int getTimeOffset(){
        DateTimeZone tz = DateTimeZone.getDefault();
        Long instant = DateTime.now().getMillis();

        String name = tz.getName(instant);

        long offsetInMilliseconds = tz.getOffset(instant);
        long hours = TimeUnit.MILLISECONDS.toHours( offsetInMilliseconds );
        return (int) hours;
    }

    public static boolean isDayLightTime(){
        if(getTimeOffset() == 2) return true;
        else return false;
    }

    public static void updateAstroCalculator(Context context){
        astroDateTime = new AstroDateTime(getYear(),
                getMonth(),
                getDay(),
                getHour(),
                getMinute(),
                getSecond(),
                getTimeOffset(),
                isDayLightTime());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long latitiude=0;
        long longitiude=0;
        try {
           latitiude  = Long.parseLong(sharedPreferences.getString(context.getResources().getString(R.string.latitiude), "465"));
            longitiude = Long.parseLong(sharedPreferences.getString(context.getResources().getString(R.string.longitiude), "456"));
        }catch(Exception ex){

        }
        location = new AstroCalculator.Location(latitiude,longitiude);

        astroCalculator = new AstroCalculator(astroDateTime, location);
    }

    public static int getREfreshTIme(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
        return Integer.parseInt(sharedPreferences.getString(context.getResources().getString(R.string.refresh),"15"));}
        catch (Exception e){
            Toast.makeText(context,"mamy problem z refresh time " , Toast.LENGTH_SHORT).show();
            return 756;
        }
    }

    public static AstroCalculator getAstroCalculator(){
        return astroCalculator;
    }

    public static String getTime(){
        dateTime = new DateTime();
        return dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour() + ":"+ dateTime.getSecondOfMinute();
    }

    public static String getCityName(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getResources().getString(R.string.cityName),"Lodz");
    }

    public static String dataTimeConverter(String utcData){
        String gtcDataTime = "";


        return gtcDataTime;
    }

}
