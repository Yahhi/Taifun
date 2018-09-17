package ru.develop_for_android.taifun;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

import static android.util.Log.INFO;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private static final class CrashReportingTree extends Timber.Tree {

        @Override
        protected boolean isLoggable(@org.jetbrains.annotations.Nullable String tag, int priority) {
            return priority >= INFO;
        }

        @Override
        protected void log(int priority, @org.jetbrains.annotations.Nullable String tag,
                           @NotNull String message, @org.jetbrains.annotations.Nullable Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            CustomCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    CustomCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    CustomCrashLibrary.logWarning(t);
                }
            }
        }
    }
}
