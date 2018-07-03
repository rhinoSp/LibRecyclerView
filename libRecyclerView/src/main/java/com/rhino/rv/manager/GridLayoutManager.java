package com.rhino.rv.manager;

import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>A {@link android.support.v7.widget.RecyclerView.LayoutManager} implementation which provides
 * similar functionality to {@link android.widget.GridView}.</p>
 * @author LuoLin
 * @since Create on 2017/8/25.
 */
public class GridLayoutManager extends RecyclerView.LayoutManager {

    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;

    public static final int VERTICAL = OrientationHelper.VERTICAL;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    private int mOrientation = VERTICAL;

    private int mTotalScrollableHeight = 0;
    private int mTotalScrollableWidth = 0;

    private int mOffsetY = 0;
    private int mOffsetX = 0;

    private int mRowCount = 0;
    private int mColumnCount = 0;
    private int mOnePageItemCount = 0;

    private int mItemWidthUsed;
    private int mItemHeightUsed;

    private SparseArray<Rect> mAllItemsRect;

    public GridLayoutManager() {
        this(1, 1);
    }

    public GridLayoutManager(int rowCount, int columnCount) {
        this.mRowCount = rowCount;
        this.mColumnCount = columnCount;
        this.mOnePageItemCount = rowCount * columnCount;
        this.mAllItemsRect = new SparseArray<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * @return true if {@link #getOrientation()} is {@link #HORIZONTAL}
     */
    @Override
    public boolean canScrollHorizontally() {
        return HORIZONTAL == mOrientation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            return 0;
        }
        detachAndScrapAttachedViews(recycler);
        int newX = mOffsetX + dx;
        int result = dx;
        if (newX > mTotalScrollableWidth) {
            result = mTotalScrollableWidth - mOffsetX;
        } else if (newX < 0) {
            result = 0 - mOffsetX;
        }
        mOffsetX += result;
        offsetChildrenHorizontal(-result);
        recycleAndFillItems(recycler, state);
        return result;
    }

    /**
     * @return true if {@link #getOrientation()} is {@link #VERTICAL}
     */
    @Override
    public boolean canScrollVertically() {
        return VERTICAL == mOrientation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL) {
            return 0;
        }
        detachAndScrapAttachedViews(recycler);
        int newY = mOffsetY + dy;
        int result = dy;
        if (newY > mTotalScrollableHeight) {
            result = mTotalScrollableHeight - mOffsetY;
        } else if (newY < 0) {
            result = 0 - mOffsetY;
        }
        mOffsetY += result;
        offsetChildrenVertical(-result);
        recycleAndFillItems(recycler, state);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (state.isPreLayout()) {
            return;
        }
        int itemWidth = getContainerUsableWidth() / mColumnCount;
        int itemHeight = getContainerUsableHeight() / mRowCount;

        int pageCount = (int) Math.ceil((float) getItemCount() / mOnePageItemCount);
        int itemCount = getItemCount();

        mItemWidthUsed = (mColumnCount - 1) * itemWidth;
        mItemHeightUsed = (mRowCount - 1) * itemHeight;

        mTotalScrollableWidth = (pageCount - 1) * getWidth();
        mTotalScrollableHeight = (pageCount - 1) * getHeight();

        detachAndScrapAttachedViews(recycler);

        for (int p = 0; p < pageCount; p++) {
            for (int r = 0; r < mRowCount; r++) {
                for (int c = 0; c < mColumnCount; c++) {
                    int childIndex = p * mOnePageItemCount + r * mColumnCount + c;
                    if (childIndex == itemCount) {
                        c = mColumnCount;
                        r = mRowCount;
                        p = pageCount;
                        break;
                    }

                    View view = recycler.getViewForPosition(childIndex);
                    addView(view);
                    measureChildWithMargins(view, mItemWidthUsed, mItemHeightUsed);

                    Rect rect = mAllItemsRect.get(childIndex);
                    if (rect == null) {
                        rect = new Rect();
                    }
                    int offsetX = HORIZONTAL == mOrientation ? p * getContainerUsableWidth() : 0;
                    int x = offsetX + c * itemWidth;

                    int offsetY = VERTICAL == mOrientation ? p * getContainerUsableHeight() : 0;
                    int y = offsetY + r * itemHeight;

                    rect.set(x, y, getDecoratedMeasuredWidth(view) + x,
                            getDecoratedMeasuredHeight(view) + y);
                    mAllItemsRect.put(childIndex, rect);
                }
            }
            removeAndRecycleAllViews(recycler);
        }
        recycleAndFillItems(recycler, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        mOffsetX = 0;
        mOffsetY = 0;
    }

    /**
     * Get the usable width of container.
     *
     * @return width
     */
    private int getContainerUsableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * Get the usable height of container.
     *
     * @return height
     */
    private int getContainerUsableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    /**
     * Recycle view and fill items.
     *
     * @param recycler RecyclerView.Recycler
     * @param state    RecyclerView.State
     */
    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            return;
        }
        Rect displayRect = new Rect(getPaddingLeft() + mOffsetX,
                getPaddingTop() + mOffsetY,
                getWidth() - getPaddingLeft() - getPaddingRight() + mOffsetX,
                getHeight() - getPaddingTop() - getPaddingBottom() + mOffsetY);
        Rect childRect = new Rect();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            childRect.left = getDecoratedLeft(child);
            childRect.top = getDecoratedTop(child);
            childRect.right = getDecoratedRight(child);
            childRect.bottom = getDecoratedBottom(child);
            if (!Rect.intersects(displayRect, childRect)) {
                removeAndRecycleView(child, recycler);
            }
        }

        int itemCount = getItemCount();
        for (int i = 0; i < itemCount && i < mAllItemsRect.size(); i++) {
            if (Rect.intersects(displayRect, mAllItemsRect.get(i))) {
                View view = recycler.getViewForPosition(i);
                addView(view);
                measureChildWithMargins(view, mItemWidthUsed, mItemHeightUsed);
                Rect rect = mAllItemsRect.get(i);
                layoutDecorated(view, rect.left - mOffsetX,
                        rect.top - mOffsetY, rect.right - mOffsetX,
                        rect.bottom - mOffsetY);
            }
        }
    }

    /**
     * Returns the current orientation of the layout.
     *
     * @return Current orientation,  either {@link #HORIZONTAL} or {@link #VERTICAL}
     * @see #setOrientation(int)
     */
    public int getOrientation() {
        return mOrientation;
    }

    /**
     * Sets the orientation of the layout. {@link android.support.v7.widget.LinearLayoutManager}
     * will do its best to keep scroll position.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation:" + orientation);
        }
        assertNotInLayoutOrScroll(null);
        if (orientation == mOrientation) {
            return;
        }
        mOrientation = orientation;
        requestLayout();
    }

}
