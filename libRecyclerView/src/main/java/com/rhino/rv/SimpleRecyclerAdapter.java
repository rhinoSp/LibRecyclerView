package com.rhino.rv;

import android.support.annotation.NonNull;

import com.rhino.rv.base.BaseHolderFactory;
import com.rhino.rv.base.BaseRecyclerAdapter;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public class SimpleRecyclerAdapter extends BaseRecyclerAdapter {

    public SimpleRecyclerAdapter() {
        this(new SimpleHolderFactory());
    }

    public SimpleRecyclerAdapter(@NonNull BaseHolderFactory factory) {
        super(factory);
    }
}
