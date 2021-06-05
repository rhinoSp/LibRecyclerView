package com.rhino.rv.swipe;

import com.rhino.rv.swipe.impl.IOnSwipeChangeListener;
import com.rhino.rv.swipe.impl.IOnSwipeMenuItemClickListener;
import com.rhino.rv.tree.BaseTreeData;

import java.util.List;


/**
 * @author LuoLin
 * @since Create on 2017/6/16.
 */
public abstract class BaseSwipeHolderData extends BaseTreeData {

    /**
     * The swipe flag.
     */
    private boolean mIsSwipedFlag = false;
    /**
     * The list of swipe menu.
     */
    public List<SwipeMenuItem> mSwipeMenuList = null;

    /**
     * The swipe item click listener.
     */
    public IOnSwipeMenuItemClickListener mSwipeMenuItemClickListener = null;
    /**
     * The swipe item swipe listener.
     */
    public IOnSwipeChangeListener mSwipeChangeListener = null;

    @Override
    public BaseSwipeHolder getBindHolder() {
        if (mHolder instanceof BaseSwipeHolder) {
            return (BaseSwipeHolder) mHolder;
        }
        return null;
    }

    public boolean hasSwipeMenu() {
        return null != mSwipeMenuList && !mSwipeMenuList.isEmpty();
    }

    public void setSwipeFlag(boolean flag) {
        mIsSwipedFlag = flag;
    }

    public boolean isSwipedFlag() {
        return mIsSwipedFlag;
    }

    public void open(boolean anim) {
        BaseSwipeHolder holder = getBindHolder();
        if (null != holder) {
            holder.open(anim);
            setSwipeFlag(true);
        }
    }

    public void close(boolean anim) {
        BaseSwipeHolder holder = getBindHolder();
        if (null != holder) {
            holder.close(anim);
            setSwipeFlag(false);
        }
    }

    public boolean isOpened() {
        BaseSwipeHolder holder = getBindHolder();
        return null != holder && holder.isOpened();
    }

    public boolean isClosed() {
        BaseSwipeHolder holder = getBindHolder();
        return null != holder && holder.isClosed();
    }

    public void setSwipeEnable(boolean enable) {
        BaseSwipeHolder holder = getBindHolder();
        if (null != holder) {
            holder.setSwipeEnable(enable);
        }
    }

}
