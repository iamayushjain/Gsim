package com.ayush.gsim.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by ayush on 17/1/17.
 */

public class LogWrapper {
    public LogWrapper() {

    }

    public static void d(Context context, String tag, String message) {
        boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            Log.d(tag, message);
        }

    }

    public static void i(Context context, String tag, String message) {
        boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            Log.i(tag, message);
        }

    }

    public static void e(Context context, String tag, String message) {
        boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            Log.e(tag, message);
        }

    }

    public static void out(Context context, Object object) {
        boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            System.out.println(object);
        }

    }

    public static void printStackTrace(Context context, Exception e) {
        boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            e.printStackTrace();
        } else {
            FirebaseCrash.report(e);
        }
    }

    public static void printStackTraceThrowable(Context context, Throwable e) {
        boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            e.printStackTrace();
        } else {
            FirebaseCrash.report(e);
        }
    }
}