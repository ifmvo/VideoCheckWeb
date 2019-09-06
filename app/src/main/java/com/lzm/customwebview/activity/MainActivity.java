package com.lzm.customwebview.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzm.customwebview.R;
import com.lzm.customwebview.common.Base64;
import com.lzm.customwebview.common.Constant;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    private String mUrl = "https://www.google.com/";//默认的url

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
                mUrl = intentUrl.trim();
            }
        }

        WebView.setWebContentsDebuggingEnabled(true);

        // 权限检查
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        webView = findViewById(R.id.webView);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setNeedInitialFocus(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android;) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/38.0.0.0 Mobile Safari/537.36");

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.addJavascriptInterface(new CustomJavascriptCallback(), "androidVideoBridge");

        webView.loadUrl(mUrl);

        Button btnRefresh = findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(view -> webView.reload());
    }

    private class CustomJavascriptCallback {
        @JavascriptInterface
        public void checkPlay(String message) {
            List<Map<String, String>> mapList = JSONObject.parseObject(message, new TypeReference<List<Map<String, String>>>() {
            });
            if (mapList != null && !mapList.isEmpty()) {
                for (Map<String, String> map : mapList) {
                    if (!TextUtils.isEmpty(map.get("url"))) {
                        Log.e("LOG_TAG", "video definition ====> " + map.get("quality"));
                        Constant.whetherOnlyUrl(map.get("url"), "checkPlay/JSCallback");
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webView);
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((WebView) findViewById(R.id.webView)).onPause();
    }

    @Override
    protected void onDestroy() {
        ((WebView) findViewById(R.id.webView)).destroy();
        super.onDestroy();
    }

    class CustomWebViewClient extends WebViewClient {

        private Uri uri = Uri.parse(Constant.LOAD_URL);
        private boolean isNeedBlock = this.notJavascriptWhiteUrl(uri.getHost());

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http")) {
                view.loadUrl(url);
                return true;
            }
            return false;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString(), accept = "*/*", shame = url.substring(0, 5).toUpperCase(), host = request.getUrl().getHost();
            if ("GET".equals(request.getMethod()) && shame.startsWith("HTTP")) {
                Headers.Builder builder = new Headers.Builder();
                Map<String, String> requestHeaders = request.getRequestHeaders();
                if (requestHeaders != null && !requestHeaders.isEmpty()) {
                    for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                        String key = entry.getKey().toLowerCase(), value = entry.getValue();
                        if ("accept".equals(key)) accept = value;
                        builder.add(key, value);
                    }
                }
                if (accept.contains("html") || isNeedInspectUrl(host, url)) {
                    url = ("HTTPS".equals(shame) && host.contains("_")) ? "http" + url.substring(5) : url;
                    return this.handleWebViewRequest(new Request.Builder().headers(builder.build()).url(url).build());
                }
            }
            return super.shouldInterceptRequest(view, request);
        }

        private WebResourceResponse handleWebViewRequest(Request request) {
            Response response = null;
            try {
                response = Constant.HTTP_CLIENT.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String url = request.url().url().toString(), mimeType = "application/octet-stream";
                    Charset encoding = StandardCharsets.UTF_8;
                    Map<String, String> headerMap = new HashMap<>();
                    if (!response.headers().names().isEmpty()) {
                        Set<String> headerSet = response.headers().names();
                        for (String key : headerSet) {
                            headerMap.put(key.toLowerCase(), response.header(key));
                        }
                        if ("true".equalsIgnoreCase(headerMap.get("access-control-allow-credentials"))) {
                            String origin = TextUtils.isEmpty(request.header("origin")) ? request.header("referer") : request.header("origin");
                            if (!TextUtils.isEmpty(origin)) {
                                Uri originUri = Uri.parse(origin);
                                headerMap.put("access-control-allow-origin", originUri.getScheme() + "://" + originUri.getHost());
                            } else {
                                if (!TextUtils.isEmpty(headerMap.get("access-control-allow-origin"))) {
                                    if ("*".equals(headerMap.get("access-control-allow-origin"))) {
                                        headerMap.put("access-control-allow-origin", uri.getScheme() + "://" + uri.getHost());
                                    }
                                } else {
                                    headerMap.remove("access-control-allow-credentials");
                                }
                            }
                        }
                    }
                    MediaType mediaType = response.body().contentType();
                    if (mediaType != null) {
                        mimeType = (mediaType.type() + "/" + mediaType.subtype()).toLowerCase();
                        encoding = mediaType.charset() != null ? mediaType.charset() : encoding;
                        if (this.isNeedReadBody(mimeType)) {
                            byte[] bytes = response.body().bytes();
                            String body = new String(bytes, encoding);
                            if (!TextUtils.isEmpty(body)) {
                                if (mimeType.equals("application/javascript")) {
                                    final byte[] tempBytes = bytes;
                                    MainActivity.this.runOnUiThread(() -> webView.loadUrl("javascript:(function(){window.DEFAULT_YOUTUBE_BASE_JS=window.atob('" + Base64.encodeBytes(tempBytes) + "')})();"));
                                } else if (mimeType.equals("text/html") && bytes.length > 500) {
                                    String bodyCopy = body.toLowerCase();
                                    int prefixIndex = bodyCopy.indexOf("<html"), endIndex = body.indexOf("</html>", prefixIndex + 1);
                                    if (-1 < prefixIndex && prefixIndex < 20 && prefixIndex < endIndex && endIndex < 200) {
                                        bytes = (body + Constant.PART_INJECT_JAVASCRIPT_CONTENT).getBytes(encoding);
                                    }

                                } else if (isNeedBlock) {
                                    body = body.toUpperCase();
                                    if (body.startsWith("#EXTM3U") && !body.contains("#EXT-X-STREAM-INF") && body.contains("#EXT-X-ENDLIST")) {
                                        Constant.whetherOnlyUrl(url, mimeType);
                                    }
                                }
                            }
                            return okHttpResponseToWebResourceResponse(request.header("range"), headerMap, mimeType, encoding.name(), response.code(), bytes);
                        } else if (isNeedBlock) {
                            if (this.checkVideoTypeSupported(mimeType)) {
                                if (!url.contains(".m4s")) {
                                    Constant.whetherOnlyUrl(url, mimeType);
                                }
                                String acceptRange = headerMap.get("accept-ranges");
                                if (acceptRange != null && acceptRange.toLowerCase().equals("bytes")) {
                                    return okHttpResponseToWebResourceResponse(request.header("range"), headerMap, mimeType, encoding.name(), response.code(), response.body().bytes());
                                }
                            } else if (mimeType.equals("application/octet-stream") && url.contains(".mp4") && !url.contains(".m4s") && !url.contains(".key") && !url.contains(".m3u8") && !url.contains(".ts")) {
                                Constant.whetherOnlyUrl(url, mimeType);
                            }
                        }
                    }
                    return okHttpResponseToWebResourceResponse(request.header("range"), headerMap, mimeType, encoding.name(), response.code(), response.body().byteStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (response != null) {
                    response.close();
                }
            }
            return null;
        }

        private WebResourceResponse okHttpResponseToWebResourceResponse(String requestRange, Map<String, String> headerMap, String mimeType, String encoding, Integer statusCode, Object body) {
            if (TextUtils.isEmpty(requestRange)) {
                if (body instanceof InputStream) {
                    return new WebResourceResponse(mimeType, encoding, statusCode, "OK", headerMap, (InputStream) body);
                } else {
                    return new WebResourceResponse(mimeType, encoding, statusCode, "OK", headerMap, new ByteArrayInputStream((byte[]) body));
                }
            }
            return null;
        }

        private boolean notJavascriptWhiteUrl(String domain) {
            for (String charSeq : Constant.JAVASCRIPT_HANDLER_WHITE_LIST) {
                if (domain.contains(charSeq)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isNeedInspectUrl(String host, String url) {
            for (Pair<String, String> pair : Constant.INSPECT_URL_CHARACTER) {
                if (url.contains(pair.first)) {
                    if (!pair.second.equals("[]")) {
                        List<Map<String, String>> filterMapList = JSONObject.parseObject(pair.second, new TypeReference<List<Map<String, String>>>() {
                        });
                        for (Map<String, String> filterMap : filterMapList) {
                            if (host.contains(filterMap.get("host"))) {
                                boolean startsWith = !filterMap.containsKey("startsWith") || url.startsWith(filterMap.get("startsWith"));
                                boolean contains = !filterMap.containsKey("contains") || url.contains(filterMap.get("contains"));
                                boolean endsWith = !filterMap.containsKey("endsWith") || url.endsWith(filterMap.get("endsWith"));
                                return startsWith && contains && endsWith;
                            }
                        }
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }

        private boolean checkVideoTypeSupported(String type) {
            for (String mime : Constant.SUPPORTED_MIME_TYPE) {
                if (type.contains(mime)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isNeedReadBody(String mimeType) {
            for (String mime : Constant.READ_BODY_MIME_TYPE) {
                if (mimeType.contains(mime)) {
                    return true;
                }
            }
            return false;
        }
    }
}

class CustomWebChromeClient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress > 50) {
            view.loadUrl("javascript:" + Constant.CORE_INJECT_JAVASCRIPT_CONTENT);
        }
        super.onProgressChanged(view, newProgress);
    }
}
