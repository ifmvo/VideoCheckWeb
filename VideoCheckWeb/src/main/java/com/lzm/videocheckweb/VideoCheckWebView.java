package com.lzm.videocheckweb;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew Chen on 2019-09-29.
 */
public class VideoCheckWebView extends WebView {

    private Map<String, String> mUserAgentMap;

    private String mDefaultUserAgent;

    public VideoCheckWebView(Context context) {
        super(context);
        init();
    }

    public VideoCheckWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoCheckWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VideoCheckWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mDefaultUserAgent = getSettings().getUserAgentString();

        setWebContentsDebuggingEnabled(true);
        getSettings().setSupportZoom(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setNeedInitialFocus(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
        getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        addJavascriptInterface(new CustomJavascriptCallback(), "androidVideoBridge");

    }

    @Override
    public void loadUrl(String url) {
        getSettings().setUserAgentString(getUserAgentOfUrl(url));
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        getSettings().setUserAgentString(getUserAgentOfUrl(url));
        super.loadUrl(url, additionalHttpHeaders);
    }

    private String getUserAgentOfUrl(String url) {
        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        if (!TextUtils.isEmpty(host) && mUserAgentMap != null && mUserAgentMap.size() > 0) {
            Set<Map.Entry<String, String>> entries = mUserAgentMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (host.contains(key)) {
                    return value;
                }
            }
        }
        return mDefaultUserAgent;
    }

    public void setUserAgentMap(Map<String, String> userAgentMap) {
        this.mUserAgentMap = userAgentMap;
    }

    private class CustomJavascriptCallback {
        @JavascriptInterface
        public void checkPlay(String message) {
            List<Map<String, String>> mapList = new GsonBuilder().create().fromJson(message, new TypeToken<List<Map<String, String>>>() {}.getType());
            if (mapList != null && !mapList.isEmpty()) {
                for (Map<String, String> map : mapList) {
                    if (!TextUtils.isEmpty(map.get("url")) && javascriptCallback != null) {
                        javascriptCallback.jsCheckVideoResult(map.get("url"), map.get("quality"));
                    }
                }
            }
        }
    }

    private JavascriptCallback javascriptCallback;

    public void setJavascriptCallback(JavascriptCallback javascriptCallback) {
        this.javascriptCallback = javascriptCallback;
    }

    public interface JavascriptCallback {
        void jsCheckVideoResult(String url, String quality);
    }
}
