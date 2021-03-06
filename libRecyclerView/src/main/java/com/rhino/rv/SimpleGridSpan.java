package com.rhino.rv;

import androidx.recyclerview.widget.GridLayoutManager;

import com.rhino.rv.base.BaseRecyclerAdapter;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public class SimpleGridSpan extends GridLayoutManager.SpanSizeLookup {

    private BaseRecyclerAdapter mAdapter;
    /**
     * The number of columns in the grid
     **/
    private int mSpanCount;

    public SimpleGridSpan(BaseRecyclerAdapter adapter, int spanCount) {
        this.mAdapter = adapter;
        this.mSpanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        return mAdapter.getItemSpanSize(mSpanCount, position);
    }
}
