package com.rhino.rv.demo.data;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.rhino.rv.demo.R;
import com.rhino.rv.swipe.BaseSwipeHolder;
import com.rhino.rv.swipe.BaseSwipeHolderData;


/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class SimpleExpandHolderData1 extends BaseSwipeHolderData {

    public String mDesc;
    public boolean mShowArrow;

    @Override
    public int getLayoutRes() {
        return R.layout.recv_list_item_simple_expand1;
    }

    @NonNull
    @Override
    public Class getHolderClass() {
        return SimpleExpandHolder1.class;
    }

    public static class SimpleExpandHolder1 extends BaseSwipeHolder<SimpleExpandHolderData1> {

        private TextView mTvDesc;
        private ImageView mIvArrow;

        public SimpleExpandHolder1(View itemView) {
            super(itemView);
            mTvDesc = findSubViewById(R.id.text);
            mIvArrow = findSubViewById(R.id.arrow);
            mIvArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleExpandHolderData1 d = getBindData();
                    if (null != d) {
                        if (!d.isOpened()) {
                            d.open(true);
                        } else {
                            d.close(true);
                        }
                    }
                }
            });
        }

        @Override
        public void bindView(SimpleExpandHolderData1 data, int position) {
            mTvDesc.setText(data.mDesc);

            int depth = data.getDepth();
            int margin = (depth - 1) * 14;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTvDesc.getLayoutParams();
            params.leftMargin = margin;

            float alpha = 1.0f - (depth - 1) * 0.4f;
            mTvDesc.setTextColor(alphaColor(alpha, Color.BLACK));

            if (data.isLeaf()) {
                mIvArrow.setVisibility(View.GONE);
            } else {
                mIvArrow.setVisibility(View.VISIBLE);
                mIvArrow.setColorFilter(alphaColor(alpha, Color.BLACK), PorterDuff.Mode.SRC_IN);
            }

            if (data.mShowArrow) {
                mIvArrow.setVisibility(View.VISIBLE);
            }
        }

        @ColorInt
        public static int alphaColor(float alpha, @ColorInt int color) {
            return (Math.round(alpha * (color >>> 24)) << 24) | (color & 0x00FFFFFF);
        }
    }

}
