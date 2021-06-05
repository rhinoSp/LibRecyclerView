package com.rhino.rv.base;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 **/
public abstract class BaseRecvHolderData<T extends ViewDataBinding> extends BaseHolderData {

    public abstract void bindView(T dataBinding, int position);

    @NonNull
    @Override
    public Class getHolderClass() {
        return BaseRecvHolder.class;
    }

    public void unbindView() {

    }

}
