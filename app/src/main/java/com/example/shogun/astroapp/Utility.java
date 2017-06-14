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

import java.util.Date;
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
        String city = sharedPreferences.getString(context.getResources().getString(R.string.cityName),"Lodz");

        return city.substring(0,1).toUpperCase() + city.substring(1);

    }

    public static String dataTimeConverter(String utcData){
        String gtcDataTime = "";


        return gtcDataTime;
    }

    public static long getUTCDateTime(){
        return new DateTime().getMillis()/1000;
    }

    public static int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    public static String getFormattedWind( float degrees) {



        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return direction;
    }

    public static String convertMiliToDate( long dateMili){
        DateTime dt= new DateTime(Long.valueOf(dateMili*1000));

        return dt.getDayOfMonth() + "-"+ dt.getMonthOfYear() + "-" + dt.getYear();
    }

    public static long getdateInMiliFromNextDay(){
        DateTime dt = new DateTime();
        dt.plusDays(1);
        return (dt.getMillis()- dt.millisOfDay().get()) / 1000;

    }
//    public static String getUnits (Context context){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//    }

}
