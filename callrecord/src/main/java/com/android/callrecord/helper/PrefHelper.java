package com.android.callrecord.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.TextView;
import com.android.callrecord.CallRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 6/27/17.
 */

public class PrefHelper {
    private static final String PREFS_NAME = "record-pref";
    private static PrefHelper _self;
    private SharedPreferences mSharedPreferences;
    private Gson mGson;

    public static PrefHelper self(Context context) {
        return _self == null ? _self = new PrefHelper(context.getApplicationContext()) : _self;
    }

    public PrefHelper(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public <T> T get(String key, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) mSharedPreferences.getString(key, "");
        } else if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
        } else if (clazz == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
        } else if (clazz == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
        } else if (clazz == Serializable.class) {
            String json = mSharedPreferences.getString(key, null);
            if (!TextUtils.isEmpty(json)) return mGson.fromJson(json, clazz);
        }
        return null;
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    public void setSaveFile(boolean canSave) {
        put(CallRecord.PREF_SAVE_FILE, canSave);
    }

    public boolean canSaveFile() {
        return get(CallRecord.PREF_SAVE_FILE, Boolean.class);
    }

    public void setSavePath(String path) {
        put(CallRecord.PREF_DIR_PATH, path);
    }

    public String getSavePath() {
        return get(CallRecord.PREF_DIR_NAME, String.class);
    }

    public void setShowSeed(boolean showSeed) {
        put(CallRecord.PREF_SHOW_SEED, showSeed);
    }

    public boolean isShowSeed() {
        return get(CallRecord.PREF_SHOW_SEED, Boolean.class);
    }

    public void setShowPhoneNumber(boolean showPhoneNumber) {
        put(CallRecord.PREF_SHOW_PHONE_NUMBER, showPhoneNumber);
    }

    public boolean isShowPhoneNumber() {
        return get(CallRecord.PREF_SHOW_PHONE_NUMBER, Boolean.class);
    }

    public void setShowNotification(boolean showNotification) {
        put(CallRecord.SHOW_NOTIFICATION, showNotification);
    }

    public boolean isShowNotification() {
        return get(CallRecord.SHOW_NOTIFICATION, Boolean.class);
    }

    public void setOutGoingOnly(boolean outGoingOnly) {
        put(CallRecord.OUT_GOING_ONLY, outGoingOnly);
    }

    public boolean isOutGoingOnly() {
        return get(CallRecord.OUT_GOING_ONLY, Boolean.class);
    }

    public void setInComingOnly(boolean inComingOnly) {
        put(CallRecord.IN_COMING_ONLY, inComingOnly);
    }

    public boolean isInComingOnly() {
        return get(CallRecord.IN_COMING_ONLY, Boolean.class);
    }

    public void setSyncOnline(boolean syncOnline) {
        put(CallRecord.SYNC_ONLINE, syncOnline);
    }

    public boolean isSyncOnline() {
        return get(CallRecord.SYNC_ONLINE, Boolean.class);
    }

    public void setBlackList(String blackList) {
        put(CallRecord.BLACK_LIST, blackList);
    }

    public String getBlackList() {
        return get(CallRecord.BLACK_LIST, String.class);
    }

    public List<String> getBlackListNumber() {
        String json = getBlackList();
        if (TextUtils.isEmpty(json)) return new ArrayList<>();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public boolean isInBlackList(String number) {
        return getBlackListNumber().contains(number);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }
}
