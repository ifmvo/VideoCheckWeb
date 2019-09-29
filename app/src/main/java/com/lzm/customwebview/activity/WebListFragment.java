package com.lzm.customwebview.activity;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ifmvo.quicklist.BaseRecyclerViewFragment;
import com.lzm.customwebview.R;

import java.util.ArrayList;
import java.util.List;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew Chen on 2019-09-06.
 */
public class WebListFragment extends BaseRecyclerViewFragment<UrlBean, BaseViewHolder> {

    /**
     * https://hindi.indiatvnews.com/
     * https://xhamster.com
     * https://www.hotstar.com/news/ 已调整兼容
     * https://www.instagram.com
     * https://m.facebook.com/  已调整兼容
     * https://www.liveleak.com/
     * https://www.dailymotion.com 已调整兼容
     * https://mobile.twitter.com/twittervideo 修改UA->Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/12.0 Mobile/15A372 Safari/604.1
     * https://www.360kan.com/
     * https://www.rediff.com/
     * https://www.foxsportsasia.com/videos/
     * https://www.nbcsports.com/
     * https://www.skysports.com/
     * https://www.ncaa.com/video
     * https://www.nhl.com/video
     * https://www.nba.com/news/ 修改UA->Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/12.0 Mobile/15A372 Safari/604.1
     * https://www.goal.com/
     * https://sports.yahoo.com/videos/
     * https://www.sonymax.tv/en_us/
     * https://hindi.pardaphash.com/category/video/
     * https://www.indiatoday.in/videos
     * https://www.oneindia.com/videos/
     * https://www.filmibeat.com/videos/
     * https://www.indiatimes.com/videos/
     * https://www.bhaskar.com/videos/bulletins/
     * https://www.ndtv.com/
     * https://m.cricbuzz.com/cricket-videos/
     * http://www.espncricinfo.com/ci/content/video_audio/index.html
     * https://www.saregama.com/
     * https://www.jiosaavn.com/
     * https://www.bollywoodhungama.com/music/
     * https://gaana.com/
     * https://www.indiatoday.in/
     * https://www.bloomberg.com/video/
     * https://www.wsj.com/video/
     */

    @Override
    protected void getData(int currentPage) {
        List<UrlBean> urlBeanList = new ArrayList<>();
        urlBeanList.add(new UrlBean("https://vimeo.com/stock/collection/5855140"));
        urlBeanList.add(new UrlBean("https://hindi.indiatvnews.com/"));
        urlBeanList.add(new UrlBean("https://xhamster.com", "有色网站慎入"));
        urlBeanList.add(new UrlBean("https://www.hotstar.com/news/", "必须印度Vpn才能播放"));
        urlBeanList.add(new UrlBean("https://www.instagram.com"));
        urlBeanList.add(new UrlBean("https://m.facebook.com/"));
        urlBeanList.add(new UrlBean("https://www.liveleak.com/"));
        urlBeanList.add(new UrlBean("https://www.dailymotion.com"));
        urlBeanList.add(new UrlBean("https://mobile.twitter.com/twittervideo"));
        urlBeanList.add(new UrlBean("https://www.360kan.com/"));
        urlBeanList.add(new UrlBean("https://www.rediff.com/"));
        urlBeanList.add(new UrlBean("https://www.foxsportsasia.com/videos/"));
        urlBeanList.add(new UrlBean("https://www.nbcsports.com/"));
        urlBeanList.add(new UrlBean("https://www.skysports.com/"));
        urlBeanList.add(new UrlBean("https://www.ncaa.com/video"));
        urlBeanList.add(new UrlBean("https://www.nhl.com/video"));
        urlBeanList.add(new UrlBean("https://www.nba.com/news/"));
        urlBeanList.add(new UrlBean("https://www.goal.com/"));
        urlBeanList.add(new UrlBean("https://sports.yahoo.com/videos/"));
        urlBeanList.add(new UrlBean("https://www.sonymax.tv/en_us/"));
        urlBeanList.add(new UrlBean("https://hindi.pardaphash.com/category/video/"));
        urlBeanList.add(new UrlBean("https://www.indiatoday.in/videos"));
        urlBeanList.add(new UrlBean("https://www.oneindia.com/videos/"));
        urlBeanList.add(new UrlBean("https://www.filmibeat.com/videos/"));
        urlBeanList.add(new UrlBean("https://www.indiatimes.com/videos/"));
        urlBeanList.add(new UrlBean("https://www.bhaskar.com/videos/bulletins/"));
        urlBeanList.add(new UrlBean("https://www.ndtv.com/"));
        urlBeanList.add(new UrlBean("https://m.cricbuzz.com/cricket-videos/"));
        urlBeanList.add(new UrlBean("http://www.espncricinfo.com/ci/content/video_audio/index.html"));
        urlBeanList.add(new UrlBean("https://www.saregama.com/"));
        urlBeanList.add(new UrlBean("https://www.jiosaavn.com/"));
        urlBeanList.add(new UrlBean("https://www.bollywoodhungama.com/music/"));
        urlBeanList.add(new UrlBean("https://gaana.com/"));
        urlBeanList.add(new UrlBean("https://www.indiatoday.in/"));
        urlBeanList.add(new UrlBean("https://www.bloomberg.com/video/"));
        urlBeanList.add(new UrlBean("https://www.wsj.com/video/"));
        handleListData(urlBeanList, currentPage);
    }

    @Override
    protected void initRecyclerViewAdapter() {
        setMAdapter(new BaseQuickAdapter<UrlBean, BaseViewHolder>(R.layout.list_item_web) {
            @Override
            protected void convert(BaseViewHolder helper, UrlBean item) {
                helper.setText(R.id.tv_url, item.getUrl() + (!TextUtils.isEmpty(item.getDesc()) ? "\n" + item.getDesc() : ""));
            }
        });

        getMAdapter().setOnItemClickListener((adapter, view, position) -> {
            MainActivity.action(getActivity(), ((UrlBean) adapter.getItem(position)).getUrl());
        });
    }

    @Override
    public boolean canLoadMore() {
        return false;
    }

    @Override
    public boolean canRefresh() {
        return false;
    }
}

class UrlBean {

    private String url;
    private String desc;

    public UrlBean(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public UrlBean(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
