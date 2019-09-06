package com.lzm.customwebview;

import android.app.Application;

import com.lzm.customwebview.init.LoadLocalJSFile;

public class BaseApplication extends Application {

    private static Application app;

    public static Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        LoadLocalJSFile.getInjectionJSContent(this);
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    }
}
