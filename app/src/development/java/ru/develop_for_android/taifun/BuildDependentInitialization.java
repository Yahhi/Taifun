package ru.develop_for_android.taifun;

import android.content.Context;

import com.facebook.stetho.Stetho;

public class BuildDependentInitialization {
    public static void initialize(Context context) {

        Stetho.initializeWithDefaults(context);
    }
}
