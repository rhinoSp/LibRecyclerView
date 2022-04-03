package com.rhino.rv.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.rhino.rv.SimpleGridSpan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseHolder> {

    private BaseHolderFactory mHolderFactory;
    private List<BaseHolderData> mDataList;
    private RecyclerView mRecyclerView;

    public BaseRecyclerAdapter(@NonNull BaseHolderFactory factory) {
        mDataList = new ArrayList<>();
        mHolderFactory = factory;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int layoutResId) {
        Class holderClass = null;
        for (BaseHolderData data : mDataList) {
            if (data.getLayoutRes() == layoutResId) {
                holderClass = data.getHolderClass();
                break;
            }
        }
        if (holderClass == null) {
            throw new RuntimeException("The holder class is null!");
        }
        BaseHolder holder = mHolderFactory.buildHolder(parent, layoutResId, holderClass);
        holder.setAdapter(this);
        holder.setParentView(parent);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getLayoutRes();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.onBindView(mDataList.get(position), position);
    }

    @Override
    public void onViewRecycled(BaseHolder holder) {
        super.onViewRecycled(holder);
        holder.onUnbindView();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Nullable
    public BaseHolderData getItem(int position) {
        return 0 <= position && position < mDataList.size() ? mDataList.get(position) : null;
    }

    public int getItemPosition(BaseHolderData item) {
        return item != null && mDataList != null && !mDataList.isEmpty() ? mDataList.indexOf(item) : -1;
    }

    /**
     * Get the RecyclerView instance which started observing this adapter.
     *
     * @return The RecyclerView instance which started observing this adapter.
     */
    public RecyclerView getAttachedRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Get the item span size.
     *
     * @param spanCount The number of columns in the grid.
     * @param position  The position.
     * @return the item span size.
     */
    public int getItemSpanSize(int spanCount, int position) {
        if (position < 0 || position >= mDataList.size()) {
            return BaseHolderData.DEF_ITEM_SPAN;
        }
        BaseHolderData data = mDataList.get(position);
        if (data != null) {
            return data.getItemSpanSize(spanCount);
        }
        return BaseHolderData.DEF_ITEM_SPAN;
    }

    /**
     * Get the max item span size.
     *
     * @return the max item span size.
     */
    public int getMaxItemSpanSize(List<BaseHolderData> mDataList) {
        int spanSize = 1;
        Set<Integer> set = new HashSet<>();
        for (BaseHolderData data : mDataList) {
            set.add(data.mItemSpanSizeScaleDenominator);
        }
        for (Integer integer : set) {
            spanSize *= integer;
        }
        return spanSize;
    }

    /**
     * Set GridLayoutManager space count.
     *
     * @param gridLayoutManager GridLayoutManager
     */
    public void setGridLayoutManagerSpanCount(List<BaseHolderData> mDataList, GridLayoutManager gridLayoutManager) {
        int maxSpanSize = getMaxItemSpanSize(mDataList);
        if (gridLayoutManager.getSpanCount() != maxSpanSize) {
            gridLayoutManager.setSpanCount(maxSpanSize);
            gridLayoutManager.setSpanSizeLookup(new SimpleGridSpan(this, maxSpanSize));
        }
    }

    /**
     * Set GridLayoutManager space count.
     *
     * @param gridLayoutManager GridLayoutManager
     */
    public void setGridLayoutManagerSpanCount(GridLayoutManager gridLayoutManager) {
        int maxSpanSize = getMaxItemSpanSize(mDataList);
        if (gridLayoutManager.getSpanCount() != maxSpanSize) {
            gridLayoutManager.setSpanCount(maxSpanSize);
            gridLayoutManager.setSpanSizeLookup(new SimpleGridSpan(this, maxSpanSize));
        }
    }

    /**
     * Get the data list.
     *
     * @return list
     */
    @NonNull
    public List<? extends BaseHolderData> getDataList() {
        return mDataList;
    }

    /**
     * Clear data list.
     */
    public void clearData() {
        mDataList.clear();
    }

    /**
     * Clear data list.
     */
    public void clearDataAndNotify() {
        clearData();
        notifyDataSetChanged();
    }

    /**
     * Add data.
     *
     * @param dataList list
     */
    public void addData(@NonNull List<? extends BaseHolderData> dataList) {
        mDataList.addAll(dataList);
    }

    /**
     * Add data and Notify data changed.
     *
     * @param dataList list
     */
    public void addDataAndNotify(@NonNull List<? extends BaseHolderData> dataList) {
        int count = getItemCount();
        addData(dataList);
        notifyItemRangeInserted(count, dataList.size());
    }

    /**
     * Update the data.
     *
     * @param dataList list
     */
    public void updateData(@NonNull List<? extends BaseHolderData> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
    }

    /**
     * Update the data and Notify data changed.
     *
     * @param dataList list
     */
    public void updateDataAndNotify(@NonNull List<? extends BaseHolderData> dataList) {
        updateData(dataList);
        notifyDataSetChanged();
    }
}
