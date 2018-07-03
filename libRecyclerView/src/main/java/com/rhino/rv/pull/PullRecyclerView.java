package com.rhino.rv.pull;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.rhino.rv.pull.impl.IPull;

/**
 * @author LuoLin
 * @since Create on 2017/4/13.
 */
public class PullRecyclerView extends RecyclerView implements IPull {

    public PullRecyclerView(Context context) {
        this(context, null);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean isScrolledToTop() {
        int firstPosition = getFirstPosition();
        int top = -1;
        if (firstPosition == 0) {
            View topView = getLayoutManager().findViewByPosition(0);
            top = topView != null ? topView.getTop() : 0;
        }
        return getLayoutManager().getItemCount() == 0 || top >= 0;
    }

    @Override
    public boolean isScrolledToBottom() {
        int lastPosition = getLastPosition();
        int bottom = -1;
        if (getLayoutManager().getItemCount() > 0 && lastPosition == getLayoutManager().getItemCount() - 1) {
            View bottomView = getLayoutManager().findViewByPosition(lastPosition);
            bottom = bottomView != null ? bottomView.getBottom() : -1;
        }
        return getLayoutManager().getItemCount() > 0 && bottom >= 0 && getMeasuredHeight() >= bottom;
    }

    /**
     * Get the first Visible item position.
     *
     * @return position
     */
    private int getFirstPosition() {
        int position = -1;
        LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] positions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPosition(positions);
        }
        return position;
    }

    /**
     * Get the last Visible item position.
     *
     * @return position
     */
    private int getLastPosition() {
        int position = -1;
        LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] positions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(positions);
        }
        return position;
    }

    /**
     * Get the min value.
     *
     * @param positions values
     * @return the min value.
     */
    private int getMinPosition(int[] positions) {
        int minPosition = Integer.MAX_VALUE;
        for (int position : positions) {
            minPosition = Math.min(minPosition, position);
        }
        return minPosition;
    }

    /**
     * Get the max value.
     *
     * @param positions values
     * @return the max value.
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }
}
