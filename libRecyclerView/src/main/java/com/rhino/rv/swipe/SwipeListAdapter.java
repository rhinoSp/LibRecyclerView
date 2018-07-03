package com.rhino.rv.swipe;

import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.base.BaseHolderFactory;
import com.rhino.rv.tree.TreeRecyclerAdapter;

import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2017/6/16.
 */
public class SwipeListAdapter extends TreeRecyclerAdapter {

    public SwipeListAdapter() {
        super();
    }

    public SwipeListAdapter(BaseHolderFactory factory) {
        super(factory);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    /**
     * Whether has at least one item swiped.
     *
     * @return True has at least one item swiped.
     */
    public boolean hasRealSwipedItem() {
        List<? extends BaseHolderData> list = getDataList();
        for (BaseHolderData d : list) {
            if (d instanceof BaseSwipeHolderData) {
                BaseSwipeHolderData swipeHolderData = (BaseSwipeHolderData) d;
                if (swipeHolderData.isSwipedFlag() && swipeHolderData.isOpened()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Close all swipe item.
     */
    public void closeAllSwipeMenu() {
        List<? extends BaseHolderData> list = getDataList();
        for (BaseHolderData d : list) {
            if (d instanceof BaseSwipeHolderData) {
                BaseSwipeHolderData swipeHolderData = (BaseSwipeHolderData) d;
                swipeHolderData.close(true);
                swipeHolderData.setSwipeEnable(true);
            }
        }
    }

    /**
     * Close other swipe item.
     *
     * @param data except this.
     */
    public void closeOtherSwipeMenu(BaseSwipeHolderData data) {
        List<? extends BaseHolderData> list = getDataList();
        for (BaseHolderData d : list) {
            if (d instanceof BaseSwipeHolderData) {
                BaseSwipeHolderData swipeHolderData = (BaseSwipeHolderData) d;
                if (null == data || swipeHolderData.getBindPosition() != data.getBindPosition()) {
                    swipeHolderData.close(true);
                }
            }
        }
    }

    /**
     * Set all item swipe enable.
     *
     * @param enable boolean.
     */
    public void setAllSwipeEnable(boolean enable) {
        List<? extends BaseHolderData> list = getDataList();
        for (BaseHolderData d : list) {
            if (d instanceof BaseSwipeHolderData) {
                BaseSwipeHolderData swipeHolderData = (BaseSwipeHolderData) d;
                swipeHolderData.setSwipeEnable(enable);
            }
        }
    }

    /**
     * Set other item swipe able.
     *
     * @param enable boolean.
     */
    public void setOtherSwipeAble(BaseSwipeHolderData data, boolean enable) {
        List<? extends BaseHolderData> list = getDataList();
        for (BaseHolderData d : list) {
            if (d instanceof BaseSwipeHolderData) {
                BaseSwipeHolderData swipeHolderData = (BaseSwipeHolderData) d;
                if (null == data || swipeHolderData.getBindPosition() != data.getBindPosition()) {
                    swipeHolderData.setSwipeEnable(enable);
                }
            }
        }
    }
}
