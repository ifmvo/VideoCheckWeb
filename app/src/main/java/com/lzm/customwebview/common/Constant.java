package com.lzm.customwebview.common;

import android.util.Log;
import android.util.Pair;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Constant {

    public static String LOAD_URL = "https://twitter.com/TwitterVideo/media";

    private static final CopyOnWriteArraySet<String> CAPTURED_MEDIA_URL_SET = new CopyOnWriteArraySet<>();

    public static final CustomArrayList<String> JAVASCRIPT_HANDLER_WHITE_LIST = new CustomArrayList<>()
            .addObject("viu.tv")
            .addObject("facebook.com")
            .addObject("bilibili.com")
            .addObject("altbalaji.com")
            .addObject("erosnow.com")
            .addObject("dailymotion.com")
            .addObject("xvideos")
            .addObject("tiktok.com")
            .addObject("pornhub.com")
            .addObject("xnxx")
            .addObject("openloadmovies.bz")
            .addObject("timesnownews.com")
            .addObject("tubidy.mobi")
            .addObject("vimeo.com")
            .addObject("news18.com")
            .addObject("soundcloud.com")
            .addObject("steampowered.com")
            .addObject("liveleak.com")
            .addObject("instagram.com")
            .addObject("twitter.com")
            .addObject("xhamster");

    public static String CORE_INJECT_JAVASCRIPT_CONTENT = "";
    public static String PART_INJECT_JAVASCRIPT_CONTENT = "";

    public static final CustomArrayList<String> READ_BODY_MIME_TYPE = new CustomArrayList<String>()
            .addObject("text")
            .addObject("mpegurl")
            .addObject("video/webm")
            .addObject("video/webp")
            .addObject("application/javascript");

    public static final CustomArrayList<Pair<String, String>> INSPECT_URL_CHARACTER = new CustomArrayList<Pair<String, String>>()
            .addObject(new Pair<>(".js", "[{\"host\":\"youtube.com\",\"contains\":\".com/yts/jsbin/\",\"endsWith\":\"/base.js\"}]"))
            .addObject(new Pair<>("video%2Fmp4", "[]"))
            .addObject(new Pair<>(".mp", "[]"))
            .addObject(new Pair<>(".webp", "[]"))
            .addObject(new Pair<>(".m3u8", "[]"))
            .addObject(new Pair<>(".3gp", "[]"))
            .addObject(new Pair<>(".flv", "[]"))
            .addObject(new Pair<>(".f4v", "[]"))
            .addObject(new Pair<>(".m4v", "[]"))
            .addObject(new Pair<>(".wmv", "[]"))
            .addObject(new Pair<>(".aac", "[]"))
            .addObject(new Pair<>(".ogg", "[]"))
            .addObject(new Pair<>(".webm", "[]"))
            .addObject(new Pair<>(".mid", "[]"));

    public static final CustomArrayList<String> SUPPORTED_MIME_TYPE = new CustomArrayList<String>()
            .addObject("video/x-flv")
            .addObject("video/x-f4v")
            .addObject("video/mp4")
            .addObject("video/3gpp")
            .addObject("video/quicktime")
            .addObject("video/x-msvideo")
            .addObject("video/x-ms-wmv")
            .addObject("video/x-m4v")
            .addObject("video/webm")
            .addObject("video/webp")
            .addObject("video/mpeg")
            .addObject("audio/aac")
            .addObject("audio/mp")
            .addObject("audio/ogg")
            .addObject("audio/wav")
            .addObject("audio/webm")
            .addObject("audio/mid");

    public static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cookieJar(new WebViewCookieHandler())
            .retryOnConnectionFailure(true)
            .build();

    public static void whetherOnlyUrl(String url, String mimeType) {
        if (CAPTURED_MEDIA_URL_SET.add(url)) {
            Log.e("LOG_TAG", "---------------------------------------------");
            Log.e("LOG_TAG", "mimeType ==== " + mimeType);
            Log.e("LOG_TAG", "url ==== " + url);
            Log.e("LOG_TAG", "---------------------------------------------");
        }
    }
}
