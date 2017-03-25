package com.tretiakov.absframework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;
import java.util.Set;

/**
 * @author andrewtretiakov; 6/8/16.
 */
public class Preferences {

    private SharedPreferences mPreferences;

    private static Preferences mInstance;

    private Context mContext;

    private Preferences(Context context) {
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void initialize(Context context) {
        mInstance = new Preferences(context);
    }

    public static Preferences getInstance() {
        return mInstance;
    }

    public static String getPrimaryOwnerID() {
        return Preferences.getString(null, "primary_owner_id");
    }

    public static void setPrimaryOwnerID(String id) {
        Preferences.saveString(null, "primary_owner_id", id);
    }

    public static boolean has(String ownerId, String key, Class c) {
        if (c == String.class) {
            String o = getInstance().mPreferences.getString(ownerId == null ? key : ownerId + ":" + key, null);
            return o != null && !o.isEmpty();
        } else if (c == Boolean.class) {
            return getInstance().mPreferences.getBoolean(ownerId == null ? key : ownerId + ":" + key, false);
        }

        return false;
    }

    public static String getString(String ownerId, String key) {
        return getInstance().mPreferences.getString(ownerId == null ? key : ownerId + ":" + key, null);
    }

    public static String getString(String ownerId, String key, String def) {
        return getInstance().mPreferences.getString(ownerId == null ? key : ownerId + ":" + key, def);
    }

    public static Set<String> getStringSet(String ownerId, String key) {
        return getInstance().mPreferences.getStringSet(ownerId == null ? key : ownerId + ":" + key, null);
    }

    public static boolean getBoolean(String ownerId, String key) {
        return getInstance().mPreferences.getBoolean(ownerId == null ? key : ownerId + ":" + key, false);
    }

    public static boolean getBoolean(String ownerId, String key, boolean def) {
        return getInstance().mPreferences.getBoolean(ownerId == null ? key : ownerId + ":" + key, def);
    }

    public static void saveString(String ownerId, String key, String value) {
        getInstance().mPreferences.edit().putString(ownerId == null ? key : ownerId + ":" + key, value).apply();
    }

    public static void saveStringSet(String ownerId, String key, Set<String> value) {
        getInstance().mPreferences.edit().putStringSet(ownerId == null ? key : ownerId + ":" + key, value).apply();
    }

    public static void saveInteger(String ownerId, String key, int value) {
        getInstance().mPreferences.edit().putInt(ownerId == null ? key : ownerId + ":" + key, value).apply();
    }

    public static void saveLong(String ownerId, String key, long value) {
        getInstance().mPreferences.edit().putLong(ownerId == null ? key : ownerId + ":" + key, value).apply();
    }

    public static int getInteger(String ownerId, String key) {
        return getInstance().mPreferences.getInt(ownerId == null ? key : ownerId + ":" + key, 0);
    }

    public static int getInteger(String ownerId, String key, int def) {
        return getInstance().mPreferences.getInt(ownerId == null ? key : ownerId + ":" + key, def);
    }

    public static long getLong(String ownerId, String key) {
        return getInstance().mPreferences.getLong(ownerId == null ? key : ownerId + ":" + key, 0);
    }

    public static long getLong(String ownerId, String key, long def) {
        return getInstance().mPreferences.getLong(ownerId == null ? key : ownerId + ":" + key, def);
    }

    public static void saveBoolean(String ownerId, String key, Boolean value) {
        getInstance().mPreferences.edit().putBoolean(ownerId == null ? key : ownerId + ":" + key, value).apply();
    }


    public static void clear(String ownerId, String key) {
        getInstance().mPreferences.edit().remove(ownerId == null ? key : ownerId + ":" + key).apply();
    }

    public static void clear() {
        getInstance().mPreferences.edit().clear().apply();
    }

    public static Map getBundle() {
        return getInstance().mPreferences.getAll();
    }
}
