package com.pch.ete.helper;

import android.util.Log;

import com.pch.ete.BuildConfig;

public class Logger {
    public static void e(String s) {
        if (BuildConfig.DEBUG)
            Log.e("TAG ", "" + s);
    }
}
