package com.rhino.rv.base;


import com.rhino.rv.impl.IOnClickListener;
import com.rhino.rv.impl.IOnLongClickListener;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;


/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public abstract class BaseHolderData {
    /**
     * The default item span size.
     */
    public static final int DEF_ITEM_SPAN = 1;
    /**
     * The item span size.
     */
    public int mItemSpanSize = DEF_ITEM_SPAN;
    /**
     * The extra data.
     */
    public Object mExtraData;
    /**
     * The click listener.
     */
    public IOnClickListener mItemClickListener;
    /**
     * The long click listener.
     */
    public IOnLongClickListener mItemLongClickListener;
    /**
     * The bind holder.
     */
    public BaseHolder mHolder;
    /**
     * The touch helper of item.
     */
    public ItemTouchHelper mItemTouchHelper;
    /**
     * The enable of decoration.
     */
    public boolean mDecorationEnable = true;

    /**
     * Get the layout resources id.
     */
    @LayoutRes
    public abstract int getLayoutRes();
    /**
     * Get the class name of ViewHolder.
     */
    @NonNull
    public abstract String getHolderClassName();

    /**
     * Get the span size.
     *
     * @param spanCount The number of columns in the grid.
     * @return the span size.
     */
    public int getItemSpanSize(int spanCount) {
        return mItemSpanSize > spanCount ? spanCount : mItemSpanSize;
    }

    /**
     * Get the context.
     *
     * @return the context.
     */
    public Context getContext() {
        if (null != mHolder) {
            return mHolder.getContext();
        }
        return null;
    }

    /**
     * Get the bind holder
     *
     * @return The bind holder.
     */
    public BaseHolder getBindHolder() {
        return mHolder;
    }

    /**
     * Get the bind position.
     *
     * @return The bind position.
     */
    public int getBindPosition() {
        return null != mHolder ? mHolder.getBindPosition() : 0;
    }

    /**
     * Notify data changed.
     */
    public void notifyDataChanged() {
        if (mHolder != null) {
            mHolder.notifyDataChanged();
        }
    }
}
