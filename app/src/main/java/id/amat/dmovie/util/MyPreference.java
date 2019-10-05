package id.amat.dmovie.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreference {
    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String KEY_RELEASE_REMINDER = "RELEASE_REMINDER";
    private static final String KEY_DAILY_REMINDER = "KEY_DAILY_REMINDER";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyPreference(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
    }

    public void setReleaseReminder(Boolean status){
        editor.putBoolean(KEY_RELEASE_REMINDER, status);
        editor.apply();
    }

    public void setDailyReminder(Boolean status){
        editor.putBoolean(KEY_DAILY_REMINDER, status);
        editor.apply();
    }

    public boolean getReleaseReminder(){
        return sharedPreferences.getBoolean(KEY_RELEASE_REMINDER, false);
    }

    public boolean getDailyReminder(){
        return sharedPreferences.getBoolean(KEY_DAILY_REMINDER, false);
    }
}
