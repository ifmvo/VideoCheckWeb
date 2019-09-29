package com.lzm.customwebview.init;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lzm.customwebview.common.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadLocalJSFile {
    public static void getInjectionJSContent(Context context) {
        if (TextUtils.isEmpty(Constant.CORE_INJECT_JAVASCRIPT_CONTENT)) {
            new Thread(() -> {
                try (InputStreamReader inputReader = new InputStreamReader(context.getAssets().open("core.js"));
                     BufferedReader bufReader = new BufferedReader(inputReader)
                ) {
                    String line;
                    while ((line = bufReader.readLine()) != null) {
                        Constant.CORE_INJECT_JAVASCRIPT_CONTENT += line;
                    }
                    if (!TextUtils.isEmpty(Constant.CORE_INJECT_JAVASCRIPT_CONTENT)) {
                        Log.e("LOG_TAG", "Core javascript load successful !");
                        // Constant.CORE_INJECT_JAVASCRIPT_CONTENT = "<script type=\"text/javascript\">" + Constant.CORE_INJECT_JAVASCRIPT_CONTENT + "</script>";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        if (TextUtils.isEmpty(Constant.PART_INJECT_JAVASCRIPT_CONTENT)) {
            new Thread(() -> {
                try (InputStreamReader inputReader = new InputStreamReader(context.getAssets().open("part.js"));
                     BufferedReader bufReader = new BufferedReader(inputReader)
                ) {
                    String line;
                    while ((line = bufReader.readLine()) != null) {
                        Constant.PART_INJECT_JAVASCRIPT_CONTENT += line;
                    }
                    if (!TextUtils.isEmpty(Constant.PART_INJECT_JAVASCRIPT_CONTENT)) {
                        Log.e("LOG_TAG", "Part javascript load successful !");
                        Constant.PART_INJECT_JAVASCRIPT_CONTENT = "<script type=\"text/javascript\">" + Constant.PART_INJECT_JAVASCRIPT_CONTENT + "</script>";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
