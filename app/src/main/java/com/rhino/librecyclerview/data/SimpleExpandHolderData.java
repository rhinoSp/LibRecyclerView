package com.rhino.librecyclerview.data;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rhino.rv.swipe.BaseSwipeHolder;
import com.rhino.rv.swipe.BaseSwipeHolderData;
import com.rhino.librecyclerview.R;


/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class SimpleExpandHolderData extends BaseSwipeHolderData {

    public String mDesc;
    public boolean mShowArrow;

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

        private TextView mTvDesc;
        private ImageView mIvArrow;

        public SimpleExpandHolder(View itemView) {
            super(itemView);
            mTvDesc = findSubViewById(R.id.text);
            mIvArrow = findSubViewById(R.id.arrow);
            mIvArrow.setOnClickListener(new View.OnClickListener() {
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
            mTvDesc.setText(data.mDesc);

            if(data.isLeaf()){
                mIvArrow.setVisibility(View.GONE);
            } else {
                mIvArrow.setVisibility(View.VISIBLE);
                if(data.isExpand()){
                    mIvArrow.setColorFilter(getBaseResources().getColor(R.color.black));
                } else {
                    mIvArrow.setColorFilter(getBaseResources().getColor(R.color.black_30), PorterDuff.Mode.SRC_IN);
                }
            }

            int depth = data.getDepth();
            int margin = (depth-1) * 14;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTvDesc.getLayoutParams();
            params.leftMargin = margin;

            float alpha = 1.0f - (depth-1) * 0.4f;
            mTvDesc.setTextColor(alphaColor(alpha, Color.BLACK));

            if (data.mShowArrow) {
                mIvArrow.setVisibility(View.VISIBLE);
            }
        }

        @ColorInt
        public static int alphaColor(float alpha, @ColorInt int color) {
            return (Math.round(alpha * (color >>> 24)) << 24 ) | (color & 0x00FFFFFF);
        }
    }

}
