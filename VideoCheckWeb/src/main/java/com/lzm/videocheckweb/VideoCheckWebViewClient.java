package com.lzm.videocheckweb;

import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzm.videocheckweb.common.Base64;
import com.lzm.videocheckweb.common.VideoCheckConstant;

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

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew Chen on 2019-09-29.
 */
public abstract class VideoCheckWebViewClient extends WebViewClient {

    private final static int MESSAGE_CODE_LOAD_JS = 101;

    private Handler.Callback mHandler = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == MESSAGE_CODE_LOAD_JS) {
                webViewLoadJs((String) message.obj);
            }
            return true;
        }
    };

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //某些网站会重定向至非有效网址，return true 取消加载该网址
        if (url == null || !url.startsWith("http") || url.contains("ucweb.com")) {
            return true;
        }
        final WebView.HitTestResult hitTestResult = view.getHitTestResult();
        if (!TextUtils.isEmpty(url) && hitTestResult == null) {
            view.loadUrl(url);
            return true;
        }
        return false;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        try {
            String accept = "*/*",
                    url = request.getUrl().toString(),
                    host = request.getUrl().getHost(),
                    shame = url.substring(0, 5).toUpperCase();
            if ("GET".equals(request.getMethod()) && shame.startsWith("HTTP")) {
                Headers.Builder builder = new Headers.Builder();
                Map<String, String> requestHeaders = request.getRequestHeaders();
                if (requestHeaders != null && !requestHeaders.isEmpty()) {
                    for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                        String key = entry.getKey().toLowerCase(), value = entry.getValue();
                        if ("accept".equals(key)) accept = value;
                        builder.add(key, value);
                    }
//                Constant.REQUEST_HEADER_CACHE.put(url, requestHeaders);
                }
                if (accept.contains("html") || isNeedInspectUrl(host, url)) {
                    url = ("HTTPS".equals(shame) && host.contains("_")) ? "http" + url.substring(5) : url;
                    return this.handleWebViewRequest(new Request.Builder().headers(builder.build()).url(url).build(), request.isForMainFrame());
                }
            }

        } catch (Exception e) {
            return super.shouldInterceptRequest(view, request);
        }
        return super.shouldInterceptRequest(view, request);
    }

    private WebResourceResponse handleWebViewRequest(Request request, boolean isMainFrame) {
        Uri uri = Uri.parse(getCurrentUrl());

        Response response = null;
        try {
            response = VideoCheckConstant.HTTP_CLIENT.newCall(request).execute();
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
                    headerMap.remove("content-security-policy");
                }
                MediaType mediaType = response.body().contentType();
                if (mediaType != null) {
                    boolean isNeedBlock = this.notJavascriptWhiteUrl(uri.getHost());
                    mimeType = (mediaType.type() + "/" + mediaType.subtype()).toLowerCase();
                    encoding = mediaType.charset() != null ? mediaType.charset() : encoding;
                    if (this.isNeedReadBody(mimeType)) {
                        byte[] bytes = response.body().bytes();
                        String body = new String(bytes, encoding);
                        if (!TextUtils.isEmpty(body)) {
                            if (mimeType.contains("javascript")) {
                                Message msg = new Message();
                                msg.what = MESSAGE_CODE_LOAD_JS;
                                msg.obj = "javascript:(function(){window.AUXILIARY_PARSE_JAVASCRIPT=window.atob('" + Base64.encodeBytes(bytes) + "')})();";
                                mHandler.handleMessage(msg);
                            } else if (!isMainFrame && mimeType.equals("text/html") && bytes.length > 300) {
                                String prefix = body.substring(0, 50).toLowerCase();
                                int prefixIndex = prefix.indexOf("<html"), endIndex = body.indexOf(">", prefixIndex + 1);
                                if (-1 < prefixIndex && prefixIndex < 20 && prefixIndex < endIndex && endIndex < 200) {
                                    bytes = (body.substring(0, endIndex + 1) + getPartInjectJSContent() + body.substring(endIndex + 1)).getBytes(encoding);
                                }
                            } else if (isNeedBlock) {
                                body = body.toUpperCase();
                                if (body.startsWith("#EXTM3U") && !body.contains("#EXT-X-STREAM-INF") && body.contains("#EXT-X-ENDLIST")) {
                                    localCheckVideoResult(url);
                                }
                            }
                        }
                        return okHttpResponseToWebResourceResponse(headerMap, mimeType, encoding.name(), response.code(), bytes);
                    } else if (isNeedBlock) {
                        if (this.checkVideoTypeSupported(mimeType)) {
                            if (!url.contains(".m4s")) {
                                localCheckVideoResult(url);
                            }
                            if (headerMap.get("accept-ranges") != null && headerMap.get("accept-ranges").toLowerCase().equals("bytes")) {
                                return okHttpResponseToWebResourceResponse(headerMap, mimeType, encoding.name(), response.code(), response.body().bytes());
                            }
                        } else if (mimeType.equals("application/octet-stream") && url.contains(".mp4") && !url.contains(".m4s") && !url.contains(".key") && !url.contains(".m3u8") && !url.contains(".ts")) {
                            localCheckVideoResult(url);
                        }
                    }
                }
                return okHttpResponseToWebResourceResponse(headerMap, mimeType, encoding.name(), response.code(), response.body().byteStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    private WebResourceResponse okHttpResponseToWebResourceResponse(Map<String, String> headerMap, String mimeType, String encoding, Integer statusCode, Object body) {
        if (body instanceof InputStream) {
            return new WebResourceResponse(mimeType, encoding, statusCode, "OK", headerMap, (InputStream) body);
        } else {
            return new WebResourceResponse(mimeType, encoding, statusCode, "OK", headerMap, new ByteArrayInputStream((byte[]) body));
        }
    }

    /**
     * CHECKPOINT
     * JAVASCRIPT_HANDLER_WHITE_LIST优先使用后端配置，后端配置获取失败则使用本地配置
     */
    private boolean notJavascriptWhiteUrl(String domain) {
        for (String charSeq : getJsHandlerWhiteList()) {
            if (domain.contains(charSeq)) {
                return false;
            }
        }
        return true;
    }

    /**
     * CHECKPOINT
     * INSPECT_URL_CHARACTER优先使用后端配置，后端配置获取失败则使用本地配置
     */
    private boolean isNeedInspectUrl(String host, String url) {
        for (Pair<String, String> pair : VideoCheckConstant.INSPECT_URL_CHARACTER) {
            if (url.contains(pair.first)) {
                if (!pair.second.equals("[]")) {
                    List<Map<String, String>> filterMapList = new GsonBuilder().create().fromJson(pair.second, new TypeToken<List<Map<String, String>>>() {
                    }.getType());
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
        for (String mime : VideoCheckConstant.SUPPORTED_MIME_TYPE) {
            if (type.contains(mime)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNeedReadBody(String mimeType) {
        for (String mime : VideoCheckConstant.READ_BODY_MIME_TYPE) {
            if (mimeType.contains(mime)) {
                return true;
            }
        }
        return false;
    }

    public abstract void webViewLoadJs(@NonNull String jsCode);

    @NonNull
    public abstract List<String> getJsHandlerWhiteList();

    @NonNull
    public abstract String getPartInjectJSContent();

    public abstract void localCheckVideoResult(@NonNull String url);

    @NonNull
    public abstract String getCurrentUrl();

}