package com.lzm.customwebview.activity;

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

    @Override
    protected void getData(int currentPage) {
        List<UrlBean> urlBeanList = new ArrayList<>();
        urlBeanList.add(new UrlBean("https://www.filmibeat.com/videos/"));
        urlBeanList.add(new UrlBean("https://hindi.indiatvnews.com/"));
        urlBeanList.add(new UrlBean("https://xhamster.com"));
        urlBeanList.add(new UrlBean("https://www.hotstar.com/news/"));
        urlBeanList.add(new UrlBean("https://www.instagram.com"));
        urlBeanList.add(new UrlBean("https://m.facebook.com/"));
        urlBeanList.add(new UrlBean("https://www.liveleak.com/"));
        urlBeanList.add(new UrlBean("https://www.dailymotion.com"));
        urlBeanList.add(new UrlBean("https://mobile.twitter.com/twittervideo"));
        handleListData(urlBeanList, currentPage);
    }

    @Override
    protected void initRecyclerViewAdapter() {
        setMAdapter(new BaseQuickAdapter<UrlBean, BaseViewHolder>(R.layout.list_item_web) {
            @Override
            protected void convert(BaseViewHolder helper, UrlBean item) {
                helper.setText(R.id.tv_url, item.getUrl());
            }
        });

        getMAdapter().setOnItemClickListener((adapter, view, position) -> {
            MainActivity.action(getActivity(), ((UrlBean)adapter.getItem(position)).getUrl());
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

    public UrlBean(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
