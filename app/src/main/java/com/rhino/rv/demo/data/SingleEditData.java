package com.rhino.rv.demo.data;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.demo.R;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class SingleEditData extends BaseHolderData {


    @Override
    public int getLayoutRes() {
        return R.layout.recv_list_item_single_edit;
    }

    @NonNull
    @Override
    public Class getHolderClass() {
        return SingleEditDataHolder.class;
    }

    public static class SingleEditDataHolder extends BaseHolder<SingleEditData> {

        private EditText mEtText;

        public SingleEditDataHolder(View itemView) {
            super(itemView);
            mEtText = findSubViewById(R.id.edit);
        }

        @Override
        public void bindView(SingleEditData data, int position) {

        }
    }
}