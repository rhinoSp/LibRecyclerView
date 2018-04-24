package com.rhino.rv.pull.impl;

import android.view.View;

/**
 * Created by LuoLin on 2017/4/13.
 **/
public interface IOnPullStatusChangeListener {

    enum PullStatus {
        /**
         * Cancel refresh.
         */
        CANCEL_REFRESH,
        /**
         * Pull to start refresh.
         */
        PULL_REFRESH,
        /**
         * Release to start refresh.
         */
        RELEASE_TO_REFRESH,
        /**
         * Start refresh.
         */
        START_REFRESHING,
        /**
         * Stop refresh.
         */
        STOP_REFRESH
    }

    View getView();

    void onPullChanged(PullStatus status);

}