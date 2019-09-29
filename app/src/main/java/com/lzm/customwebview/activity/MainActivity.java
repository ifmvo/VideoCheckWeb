package com.lzm.customwebview.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzm.customwebview.R;
import com.lzm.customwebview.common.Constant;
import com.lzm.videocheckweb.VideoCheckWebChromeClient;
import com.lzm.videocheckweb.VideoCheckWebView;
import com.lzm.videocheckweb.VideoCheckWebViewClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /**
     * WebView 横屏播放时，播放器的容器
     */
    private FrameLayout mFlViewContainer;
    /**
     * 自定义 WebView
     */
    private VideoCheckWebView webView;
    /**
     * 当前正在访问的页面 URL
     */
    private String mUrl;

    /**
     * 入口
     */
    public static void action(@NonNull Context context, @NonNull String url) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取 URL
        Intent intent = getIntent();
        if (intent.hasExtra("url")) {
            String intentUrl = intent.getStringExtra("url");
            if (!TextUtils.isEmpty(intentUrl)) {
                Constant.CAPTURED_MEDIA_URL_SET.clear();
                mUrl = intentUrl.trim();
            }
        }

        // 权限检查
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }


        webView = findViewById(R.id.webView);
        mFlViewContainer = findViewById(R.id.fl_view_container);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());

        /*
         * 设置 UserAgent Map json
         */
        Map<String, String> map = new GsonBuilder().create().fromJson(Constant.userAgentMapJson, new TypeToken<Map<String, String>>() {}.getType());
        webView.setUserAgentMap(map);

        /*
         * 设置js 回调监听
         */
        webView.setJavascriptCallback(this::whetherOnlyUrl);

        webView.loadUrl(mUrl);

        Button btnRefresh = findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(view -> {
            Constant.CAPTURED_MEDIA_URL_SET.clear();
            webView.reload();
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    class CustomWebViewClient extends VideoCheckWebViewClient {

        @Override
        public void webViewLoadJs(String jsCode) {
            //WebView 执行 js 代码
            webView.loadUrl(jsCode);
        }

        @Override
        public List<String> getJsHandlerWhiteList() {
            //返回 js 白名单 str
            return Constant.JAVASCRIPT_HANDLER_WHITE_LIST;
        }

        @Override
        public String getPartInjectJSContent() {
            //返回 part js code
            return Constant.PART_INJECT_JAVASCRIPT_CONTENT;
        }

        @Override
        public void localCheckVideoResult(String url) {
            //本地拦截的回调
            whetherOnlyUrl(url, "");
        }

        @Override
        public String getCurrentUrl() {
            //返回当前正在浏览的 URL
            return mUrl;
        }
    }

    class CustomWebChromeClient extends VideoCheckWebChromeClient {

        @Override
        public ViewGroup getFullScreenVideoContainer() {
            //WebView 横屏播放时，播放器的容器
            return mFlViewContainer;
        }

        @Override
        public String getCoreInjectJSContent() {
            //返回 core js code
            return Constant.CORE_INJECT_JAVASCRIPT_CONTENT;
        }

        @Override
        public void requestOrientationPortrait() {
            //请求竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        @Override
        public void requestOrientationLandscape() {
            //请求横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 横竖屏切换监听，设置状态栏显示与隐藏
     */
    @Override
    public void onConfigurationChanged(@NotNull Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }

    private void whetherOnlyUrl(String url, String quality) {
        if (Constant.CAPTURED_MEDIA_URL_SET.add(url)) {
            MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "quality: " + quality + "\nurl ====> " + url, Toast.LENGTH_LONG).show());
            Log.e("LOG_TAG", "---------------------------------------------");
            Log.e("LOG_TAG", "media url ====> " + url);
            Log.e("LOG_TAG", "quality   ====> " + quality);
            Log.e("LOG_TAG", "---------------------------------------------");
        }
    }
}