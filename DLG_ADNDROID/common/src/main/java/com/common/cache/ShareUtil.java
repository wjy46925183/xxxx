package com.common.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by wangjinya on 2017/6/20.
 */

public class ShareUtil {
    public static final String NAME = "DLG";

    /**
     * 设置值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setValue(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Set<?>) {
            edit.putStringSet(key, (Set<String>) value);
        } else {
            return;
        }
        edit.commit();
    }

    /**
     * 取值
     */
    public static String getStringValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, "");
        return string;
    }

    public static boolean getBooleanValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key, false);
        return value;
    }

    public static int getIntValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, 0);
        return value;
    }

    public static long getLongValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        long string = sharedPreferences.getLong(key, 0);
        return string;
    }

    public static float getFloatValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        float value = sharedPreferences.getFloat(key, 0.0f);
        return value;
    }

    public static Set<String> getSetValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Set<String> value = sharedPreferences.getStringSet(key, null);
        return value;
    }


}
