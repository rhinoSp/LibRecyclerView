package com.rhino.rv.drag;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.drag.impl.IOnItemDragCallbackListener;
import com.rhino.rv.swipe.SwipeListAdapter;

import java.util.Collections;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/1/10.
 */
public class DragRecyclerAdapter extends SwipeListAdapter implements IOnItemDragCallbackListener {

    private ItemTouchHelper mItemTouchHelper;
    private DragItemTouchCallback mItemTouchCallback;
    private IOnItemDragCallbackListener mItemDragCallbackListener;

    public DragRecyclerAdapter() {
        super();
        mItemTouchCallback = new DragItemTouchCallback(this);
        mItemTouchCallback.setLongPressDragEnabled(true);
        mItemTouchCallback.setItemSwipeEnable(true);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void updateData(@NonNull List<? extends BaseHolderData> dataList) {
        super.updateData(dataList);
        for (BaseHolderData data : dataList) {
            data.mItemTouchHelper = mItemTouchHelper;
        }
    }

    @Override
    public boolean onSwiped(int position, int direction) {
        if (null != mItemDragCallbackListener
                && mItemDragCallbackListener.onSwiped(position, direction)) {
            return true;
        }
        notifyItemRemoved(position);
        return true;
    }

    @Override
    public boolean onMove(int srcPosition, int targetPosition) {
        if (null != mItemDragCallbackListener
                && mItemDragCallbackListener.onMove(srcPosition, targetPosition)) {
            return true;
        }
        RecyclerView.LayoutManager layoutManager = getAttachedRecyclerView().getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (srcPosition < targetPosition) {
                for (int i = srcPosition; i < targetPosition; i++) {
                    Collections.swap(getDataList(), i, i + 1);
                }
            } else {
                for (int i = srcPosition; i > targetPosition; i--) {
                    Collections.swap(getDataList(), i, i - 1);
                }
            }
        } else {
            Collections.swap(getDataList(), srcPosition, targetPosition);
        }
        notifyItemMoved(srcPosition, targetPosition);
        return true;
    }

    public void setLongPressDragEnabled(boolean enable) {
        mItemTouchCallback.setLongPressDragEnabled(enable);
    }

    public void setItemSwipeEnable(boolean enable) {
        mItemTouchCallback.setItemSwipeEnable(enable);
    }

    public void setOnItemDragCallbackListener(IOnItemDragCallbackListener itemDragCallbackListener) {
        this.mItemDragCallbackListener = itemDragCallbackListener;
    }
}
