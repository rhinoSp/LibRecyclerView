package com.rhino.librecyclerview.data;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.impl.IOnClickListener;
import com.rhino.librecyclerview.R;
import com.rhino.librecyclerview.utils.ColorUtils;
import com.rhino.librecyclerview.utils.ScreenUtils;
import com.rhino.rv.tree.BaseTreeData;

import java.util.Random;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class SingleTextData extends BaseTreeData {

    public boolean mRandom;
    public String mDesc;
    public int mDescColor;
    public IOnClickListener<SingleTextData> mOnClickListener;
    public Class<?> mActivityCls;

    @Override
    public int getLayoutRes() {
        return R.layout.recv_list_item_single_text;
    }

    @NonNull
    @Override
    public String getHolderClassName() {
        return SingleTextDataHolder.class.getName();
    }

    public static class SingleTextDataHolder extends BaseHolder<SingleTextData> {

        private TextView mTvDesc;

        public SingleTextDataHolder(View itemView) {
            super(itemView);
            mTvDesc = findSubViewById(R.id.text);
        }

        @Override
        public void bindView(SingleTextData data, int position) {
            if(0 != data.mDescColor){
                mTvDesc.setTextColor(data.mDescColor);
            }
            mTvDesc.setText(data.mDesc);
            if(data.mRandom){
                mTvDesc.setBackgroundColor(ColorUtils.TRANSPARENT);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvDesc.getLayoutParams();
                params.height = ScreenUtils.dip2px(getContext(), 60) + new Random().nextInt(240);

                int r = 0x22;//new Random().nextInt(0xFF);
                int g = new Random().nextInt(0x66);
                int b = new Random().nextInt(0x66);
                itemView.setBackgroundColor(Color.rgb(r,g,b));
            }

        }
    }
}