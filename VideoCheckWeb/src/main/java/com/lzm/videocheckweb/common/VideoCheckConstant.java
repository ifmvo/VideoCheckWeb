package com.lzm.videocheckweb.common;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew Chen on 2019-09-29.
 */
public class VideoCheckConstant {

//    public static final Map<String, Map<String, String>> REQUEST_HEADER_CACHE = new HashMap<>();

    public static final CustomArrayList<String> READ_BODY_MIME_TYPE = new CustomArrayList<String>()
            .addObject("text")
            .addObject("mpegurl")
            .addObject("video/webm")
            .addObject("video/webp")
            .addObject("application/javascript");

    public static final CustomArrayList<Pair<String, String>> INSPECT_URL_CHARACTER = new CustomArrayList<Pair<String, String>>()
            .addObject(new Pair<>(".js", "[{\"host\":\"youtube.com\",\"contains\":\".com/yts/jsbin/\",\"endsWith\":\"/base.js\"},{\"host\":\"ytimg.com\",\"contains\":\".com/yts/jsbin/\",\"endsWith\":\"/base.js\"}]"))
            .addObject(new Pair<>("video%2Fmp4", "[]"))
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
            .addObject(new Pair<>(".mid", "[]"))
            .addObject(new Pair<>(".mp3", "[]"))
            .addObject(new Pair<>(".mp4", "[]"));

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

    public static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36";

    public static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cookieJar(new WebViewCookieHandler())
            .retryOnConnectionFailure(true)
            .build();

}
