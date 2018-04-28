package com.rhino.librecyclerview.data;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.librecyclerview.R;

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
    public String getHolderClassName() {
        return SingleButtonDataHolder.class.getName();
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