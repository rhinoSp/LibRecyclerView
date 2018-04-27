package com.rhino.rv.base;

import android.view.ViewGroup;

/**
 * Created by LuoLin on 2016/11/21.
 **/
public abstract class BaseHolderFactory {
    abstract public BaseHolder buildHolder(ViewGroup parent, int layoutResId, String holderClassName);
}
