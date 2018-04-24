package com.rhino.rv.impl;

import com.rhino.rv.base.BaseHolderData;

import android.view.View;

/**
 * Created by LuoLin on 2016/11/21.
 **/
public interface IOnClickListener<T extends BaseHolderData> {
    void onClick(View v, T data, int position);
}
