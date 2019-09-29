package com.lzm.videocheckweb;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew Chen on 2019-09-29.
 */
public abstract class VideoCheckWebChromeClient extends WebChromeClient {

    private CustomViewCallback mCustomViewCallback;
    //  横屏时，显示视频的view
    private View mCustomView;

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);

        ViewGroup fullScreenVideoContainer = getFullScreenVideoContainer();

        //如果view 已经存在，则隐藏
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }
        mCustomView = view;
        mCustomView.setVisibility(View.VISIBLE);
        mCustomViewCallback = callback;
        fullScreenVideoContainer.addView(mCustomView);
        fullScreenVideoContainer.setVisibility(View.VISIBLE);
        fullScreenVideoContainer.bringToFront();

        requestOrientationLandscape();
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();

        ViewGroup fullScreenVideoContainer = getFullScreenVideoContainer();

        if (mCustomView == null) {
            return;
        }
        mCustomView.setVisibility(View.GONE);
        fullScreenVideoContainer.removeView(mCustomView);
        mCustomView = null;
        fullScreenVideoContainer.setVisibility(View.GONE);
        mCustomViewCallback.onCustomViewHidden();
        requestOrientationPortrait();

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress > 50) {
            view.loadUrl("javascript:" + getCoreInjectJSContent());
        }
        super.onProgressChanged(view, newProgress);
    }

    public abstract ViewGroup getFullScreenVideoContainer();

    public abstract String getCoreInjectJSContent();

    public abstract void requestOrientationPortrait();

    public abstract void requestOrientationLandscape();
}
