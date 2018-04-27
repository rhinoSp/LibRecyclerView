package com.rhino.librecyclerview.data;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.rhino.librecyclerview.R;
import com.rhino.librecyclerview.databinding.RecvListItemSimpleExpandBinding;
import com.rhino.rv.swipe.BaseSwipeHolder;
import com.rhino.rv.swipe.BaseSwipeHolderData;

public class SimpleExpandHolderData extends BaseSwipeHolderData {

    public String mDesc;

    @Override
    public int getLayoutRes() {
        return R.layout.recv_list_item_simple_expand;
    }

    @NonNull
    @Override
    public String getHolderClassName() {
        return SimpleExpandHolder.class.getName();
    }

    public static class SimpleExpandHolder extends BaseSwipeHolder<SimpleExpandHolderData> {

        private RecvListItemSimpleExpandBinding mBinding;

        public SimpleExpandHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleExpandHolderData d = getBindData();
                    if(null != d){
                        if(!d.isOpened()){
                            d.open(true);
                        } else {
                            d.close(true);
                        }
                    }
                }
            });
        }

        @Override
        public void bindView(SimpleExpandHolderData data, int position) {
            mBinding.text.setText(data.mDesc);

            if(data.isLeaf()){
                mBinding.arrow.setVisibility(View.GONE);
            } else {
                mBinding.arrow.setVisibility(View.VISIBLE);
                if(data.isExpand()){
                    mBinding.arrow.setColorFilter(getBaseResources().getColor(R.color.black));
                } else {
                    mBinding.arrow.setColorFilter(getBaseResources().getColor(R.color.black_30), PorterDuff.Mode.SRC_IN);
                }
            }

            int depth = data.getDepth();
            int margin = (depth-1) * 14;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBinding.text.getLayoutParams();
            params.leftMargin = margin;

            float alpha = 1.0f - (depth-1) * 0.3f;
            mBinding.text.setTextColor(alphaColor(alpha, Color.BLACK));
        }

        @ColorInt
        public static int alphaColor(float alpha, @ColorInt int color) {
            return (Math.round(alpha * (color >>> 24)) << 24 ) | (color & 0x00FFFFFF);
        }
    }

}
