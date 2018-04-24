package com.rhino.rv.impl;

import android.view.View;

import com.rhino.rv.base.BaseHolderData;

/**
 * Created by LuoLin on 2016/11/21.
 **/
public interface IOnLongClickListener<T extends BaseHolderData> {
    boolean onLongClick(View v, T data, int position);
}
