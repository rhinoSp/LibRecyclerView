package com.rhino.rv.base;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 **/
public abstract class BaseRecvHolderData<T extends ViewDataBinding> extends BaseHolderData {

    public abstract void bindView(T dataBinding, int position);

    @NonNull
    @Override
    public String getHolderClassName() {
        return BaseRecvHolder.class.getName();
    }

    public void unbindView() {

    }

}
