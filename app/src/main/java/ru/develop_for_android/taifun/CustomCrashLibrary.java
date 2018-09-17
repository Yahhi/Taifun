package ru.develop_for_android.taifun;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

public class CustomCrashLibrary {
    public static void log(int priority, String tag, String message) {
        Crashlytics.log(priority, tag, message);
    }

    public static void logWarning(Throwable t) {
        Crashlytics.log(Log.WARN, "catched exception", t.getMessage());
    }

    public static void logError(Throwable t) {
        Crashlytics.log(Log.ERROR, "catched exception", t.getMessage());
    }

    private CustomCrashLibrary() {
        throw new AssertionError("No instances.");
    }
}
