package com.rhino.rv.base;

import android.view.ViewGroup;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public abstract class BaseHolderFactory {
    abstract public BaseHolder buildHolder(ViewGroup parent, int layoutResId, Class holderClass);
}
