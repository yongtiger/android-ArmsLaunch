package cc.brainbook.armssplash.splash.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
    public static Boolean getBoolean(Context context, String key, Boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, Boolean defaultValue) {
        SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = defaultSharedPrefs.edit();
        editor.putBoolean(key, defaultValue);
        editor.apply();
    }

}
