package com.rhino.rv.drag;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhino.rv.drag.impl.IOnItemDragCallbackListener;


/**
 * @author LuoLin
 * @since Create on 2018/1/10.
 */
public class DragItemTouchCallback extends ItemTouchHelper.Callback {

    private IOnItemDragCallbackListener mItemDragCallbackListener;
    private boolean mIsLongPressDragEnabled = false;
    private boolean mIsItemViewSwipeEnabled = false;

    public DragItemTouchCallback(IOnItemDragCallbackListener itemDragCallbackListener) {
        this.mItemDragCallbackListener = itemDragCallbackListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return mIsLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mIsItemViewSwipeEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int dragFlag = 0, swipeFlag = 0;
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        } else {
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            return makeMovementFlags(dragFlag, swipeFlag);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder srcViewHolder, RecyclerView.ViewHolder targetViewHolder) {
        if (null != mItemDragCallbackListener) {
            return mItemDragCallbackListener.onMove(srcViewHolder.getAdapterPosition(), targetViewHolder.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (null != mItemDragCallbackListener) {
            mItemDragCallbackListener.onSwiped(viewHolder.getAdapterPosition(), direction);
        }
    }

    public void setLongPressDragEnabled(boolean enable) {
        mIsLongPressDragEnabled = enable;
    }

    public void setItemSwipeEnable(boolean enable) {
        mIsItemViewSwipeEnabled = enable;
    }

    public void setOnItemDragCallbackListener(IOnItemDragCallbackListener itemTouchCallbackListener) {
        this.mItemDragCallbackListener = itemTouchCallbackListener;
    }


}