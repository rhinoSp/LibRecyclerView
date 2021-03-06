package com.rhino.rv.demo.data;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.demo.R;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class SingleButtonData extends BaseHolderData {


    @Override
    public int getLayoutRes() {
        return R.layout.recv_list_item_single_button;
    }

    @NonNull
    @Override
    public Class getHolderClass() {
        return SingleButtonDataHolder.class;
    }

    public static class SingleButtonDataHolder extends BaseHolder<SingleButtonData> {

        private Button mButton;

        public SingleButtonDataHolder(View itemView) {
            super(itemView);
            mButton = findSubViewById(R.id.button);
        }

        @Override
        public void bindView(SingleButtonData data, int position) {

        }
    }
}