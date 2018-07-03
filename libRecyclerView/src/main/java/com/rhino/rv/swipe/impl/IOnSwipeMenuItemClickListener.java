package com.rhino.rv.swipe.impl;

import com.rhino.rv.swipe.BaseSwipeHolderData;
import com.rhino.rv.swipe.SwipeMenuItem;

/**
 * @author LuoLin
 * @since Create on 2017/6/16.
 */
public interface IOnSwipeMenuItemClickListener<T extends BaseSwipeHolderData> {
    void onItemClick(T data, int position, SwipeMenuItem item);
}
