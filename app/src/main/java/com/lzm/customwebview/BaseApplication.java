package com.lzm.customwebview;

import android.app.Application;

import com.lzm.customwebview.init.LoadLocalJSFile;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoadLocalJSFile.getInjectionJSContent(this);
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    }
}
