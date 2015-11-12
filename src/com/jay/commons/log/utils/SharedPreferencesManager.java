
package com.jay.commons.log.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static SharedPreferencesManager sInstance = new SharedPreferencesManager();
    private static Context sContext;

    public static final String ARK_LOCK = "ark_lock";
    public static final String KEY_LOCK_BROWSER = "browser_lock";

    public static final String ARK_PATHCONFIG = "pathConfig";
    public static final String KEY_WEBSERVICE_PATH = "webServicePath";
    public static final String KEY_WEBSERVICE_ADDR = "webServiceAddr";
    public static final String KEY_SOCKET_HOST = "socketHost";
    public static final String KEY_SOCKET_PORT = "socketPort";
    public static final String KEY_WISDOMCAMPUS_WEBPATH = "webpath";
    public static final String KEY_SERVERCONFIG_PATH = "serverConfigPath";
    
    public static final String CONFIG_USER_INFO = "userInfoConfig";
    public static final String SERVER_IP_RUL = "ServerIPUrl";
    public static final String CLINT_UPGRADE = "clientUpgrade";
    public static final String KEY_USER_CODE = "userCode";
    public static final String KEY_BOOK_ID = "bookId";
    public static final String SERVER_IP_UP = "serverIPup";

    /** 安装包配置 **/
    public static final String ARK_Apk = "apkConfig";
    /** 升级安装包存放路径 **/
    public static final String KEY_ApkFilePath = "apkFilePath";
    /** 升级安装包版本 **/
    public static final String KEY_ApkFileVersion = "apkFileVersion";
    /**笔记本提交**/
    public static final String KEY_ApkNote = "apkNote";
    public static final String NOTE_UP_SUM="note_sum";
    public static final String NOTE_UP_OK="note_ok";
    public static final String NOTE_UP_ERROR="note_error";
    /**自我梳理**/
    public static final String KEY_CONTENT = "content";
    public static final String KEY_WEATHER="weather";
    public static final String KEY_MOOD="mood";
    public static final String KEY_ISPUBLIC="isPublic";
    public static final String KEY_CREATETIME="createTime";
    
    public static void init(Context ctx) {
        sContext = ctx;
    }

    public static SharedPreferencesManager getInstance() {
        return sInstance;
    }

    public boolean setString(String name, String key, String value) {
        SharedPreferences sp = sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.edit().putString(key, value).commit();
    }

    public String getStringValue(String name, String key, String defaultValue) {
        SharedPreferences sp = sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public boolean setBoolean(String name, String key, boolean value) {
        SharedPreferences sp = sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.edit().putBoolean(key, value).commit();
    }

    public boolean getBooleanValue(String name, String key, boolean defaultValue) {
        SharedPreferences sp = sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }
    
    public boolean setInt(String name, String key, int value) {
        SharedPreferences sp = sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.edit().putInt(key, value).commit();
    }

    public int getIntValue(String name, String key, int defaultValue) {
        SharedPreferences sp = sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

}
