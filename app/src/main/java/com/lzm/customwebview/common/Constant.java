package com.lzm.customwebview.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Constant {

    public static final String userAgentMapJson = "{\"twitter.com\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/12.0 Mobile/15A372 Safari/604.1\",\"erosnow.com\":\"Mozilla/5.0 (X11; Linux x86_64:68.0) Gecko/20100101 Firefox/68.0\",\"indiatimes.com\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/12.0 Mobile/15A372 Safari/604.1\",\"nba.com\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/12.0 Mobile/15A372 Safari/604.1\",\"ncaa.com\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G36 Safari/601.1\"}";

    public static final CopyOnWriteArraySet<String> CAPTURED_MEDIA_URL_SET = new CopyOnWriteArraySet<>();

    public static final ConcurrentHashMap<String, Map<String, String>> REQUEST_HEADER_CACHE = new ConcurrentHashMap<>();

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
            .addObject("xhamster");

    public static String CORE_INJECT_JAVASCRIPT_CONTENT = "";
    public static String PART_INJECT_JAVASCRIPT_CONTENT = "";


}
