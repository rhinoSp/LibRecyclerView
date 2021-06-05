package com.rhino.rv.base;


import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.rhino.rv.impl.IOnClickListener;
import com.rhino.rv.impl.IOnLongClickListener;


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
     * The item span size scale of numerator.
     */
    public int mItemSpanSizeScaleNumerator = 1;
    /**
     * The item span size scale of denominator.
     */
    public int mItemSpanSizeScaleDenominator = 1;
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
    public abstract Class getHolderClass();

    /**
     * Get the span size.
     *
     * @param spanCount The number of columns in the grid.
     * @return the span size.
     */
    public int getItemSpanSize(int spanCount) {
        if (mItemSpanSizeScaleNumerator != 1 || mItemSpanSizeScaleDenominator != 1) {
            mItemSpanSize = spanCount / mItemSpanSizeScaleDenominator * mItemSpanSizeScaleNumerator;
        }
        if (mItemSpanSize <= 0 || mItemSpanSize >= spanCount) {
            mItemSpanSize = spanCount;
        }
        return mItemSpanSize;
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
