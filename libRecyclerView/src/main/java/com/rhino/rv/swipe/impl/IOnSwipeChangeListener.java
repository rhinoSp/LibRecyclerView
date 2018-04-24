package com.rhino.rv.swipe.impl;

import com.rhino.rv.swipe.BaseSwipeHolderData;
import com.rhino.rv.swipe.SwipeItemLayout;

/**
 * Created by LuoLin on 2017/6/16
 */
public interface IOnSwipeChangeListener<T extends BaseSwipeHolderData> {

    void onOpened(SwipeItemLayout swipeItemLayout, T data);

    void onClosed(SwipeItemLayout swipeItemLayout, T data);

    void onStartOpen(SwipeItemLayout swipeItemLayout, T data);

}
