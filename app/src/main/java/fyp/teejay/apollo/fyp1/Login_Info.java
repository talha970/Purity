package fyp.teejay.apollo.fyp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

/**
 * Created by teejay on 4/16/2016.
 */
public class Login_Info {
    public static Boolean loggedin=false;
    public static String username="Guest";
    public static String dev_name;
    public static float lat;
    public static float lon;
    public static String dev_id;
    public static Boolean isPaired;
    public static Boolean isfirst;
    private static final String MY_PREFERENCES = "my_preferences";
    public static Boolean getIsPaired(Context con){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        isPaired=settings.getBoolean("paired", false);
        return isPaired;
    }

    public static void  setisPaired(Context con,Boolean val){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        settings.edit().putBoolean("paired", val).commit();
    }

    public static void  setloggedin(Context con,Boolean val){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        settings.edit().putBoolean("login", val).commit();
    }
    public static Boolean getloggedin(Context con){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        loggedin=settings.getBoolean("login", true);
        return loggedin;
    }
    public static Boolean getIsfirst(Context con){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        isfirst=settings.getBoolean("first_time", true);
        return isfirst;
    }
    public static Location getLoc(Context con){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        float lat=settings.getFloat("Latitude", 33);
        float lon=settings.getFloat("Longitude", 73);
        Location loc=new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        return loc;
    }
    public static void  setloc(Context con,Location loc){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        settings.edit().putFloat("lat", (float)(loc.getLatitude())).commit();
        settings.edit().putFloat("lat", (float)(loc.getLongitude())).commit();
    }
    public static void  setisfirst(Context con,Boolean val){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        settings.edit().putBoolean("first_time", val).commit();
    }
    public static void  setUsername(Context con,String val){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        settings.edit().putString("username", val).commit();
    }
    public static String getusername(Context con){
        SharedPreferences settings = con.getSharedPreferences(MY_PREFERENCES, 0);
        username=settings.getString("username", "Guest");
        return username;
    }
}

