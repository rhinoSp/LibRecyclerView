package com.rhino.rv.recvitem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rhino.rv.R;
import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderData;

/**
 * @author rhino
 * @since Create on 2020/4/25.
 **/
public class RecvItemEmptyContent extends BaseHolderData {

    public int iconResId = R.mipmap.ic_empty_icon;
    public String text = "没有相关内容";
    public View recyclerViewContainer;

    public static RecvItemEmptyContent buildEmptyContent(View recyclerViewContainer) {
        RecvItemEmptyContent recvItemEmptyContent = new RecvItemEmptyContent();
        recvItemEmptyContent.mItemSpanSize = -1;
        recvItemEmptyContent.recyclerViewContainer = recyclerViewContainer;
        return recvItemEmptyContent;
    }

    public static RecvItemEmptyContent buildEmptyContent(int iconResId, String text, View recyclerViewContainer) {
        RecvItemEmptyContent recvItemEmptyContent = buildEmptyContent(recyclerViewContainer);
        recvItemEmptyContent.iconResId = iconResId;
        recvItemEmptyContent.text = text;
        return recvItemEmptyContent;
    }

    public RecvItemEmptyContent() {
        this.mItemSpanSize = -1;
        this.mDecorationEnable = false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.recv_item_empty_content;
    }

    @NonNull
    @Override
    public Class getHolderClass() {
        return RecvItemEmptyContentHolder.class;
    }

    public static class RecvItemEmptyContentHolder extends BaseHolder<RecvItemEmptyContent> {

        private View llContainer;
        private ImageView ivIcon;
        private TextView tvText;

        public RecvItemEmptyContentHolder(View itemView) {
            super(itemView);
            llContainer = findSubViewById(R.id.ll_container);
            ivIcon = findSubViewById(R.id.iv_icon);
            tvText = findSubViewById(R.id.tv_text);
        }

        @Override
        public void bindView(RecvItemEmptyContent data, int position) {
            ivIcon.setAlpha(0.2f);
            tvText.setAlpha(0.2f);
            ivIcon.setImageResource(data.iconResId);
            tvText.setText(data.text);
            llContainer.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = llContainer.getLayoutParams();
                    params.height = data.recyclerViewContainer.getHeight();
                    llContainer.setLayoutParams(params);
                }
            });
        }
    }
}