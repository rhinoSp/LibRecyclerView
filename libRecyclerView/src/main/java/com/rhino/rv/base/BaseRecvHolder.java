package com.rhino.rv.base;

import android.databinding.DataBindingUtil;
import android.view.View;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 **/
public class BaseRecvHolder extends BaseHolder<BaseRecvHolderData> {

    public BaseRecvHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindView(BaseRecvHolderData data, int position) {
        if (data != null) {
            data.bindView(DataBindingUtil.bind(itemView), position);
        }
    }

    @Override
    public void unbindView(BaseRecvHolderData data) {
        if (data != null) {
            data.unbindView();
        }
    }

}
