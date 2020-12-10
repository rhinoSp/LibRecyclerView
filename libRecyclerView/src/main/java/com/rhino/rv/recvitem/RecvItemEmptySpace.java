package com.rhino.rv.recvitem;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.rhino.rv.R;
import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderData;

/**
 * @author rhino
 * @since Create on 2020/4/25.
 **/
public class RecvItemEmptySpace extends BaseHolderData {

    public int height = 20;
    public Drawable backgroundDrawable;
    public boolean focusAble;

    public static RecvItemEmptySpace buildEmptySpace(int height, Drawable backgroundDrawable) {
        RecvItemEmptySpace recvItemEmptySpace = buildEmptySpace();
        recvItemEmptySpace.height = height;
        recvItemEmptySpace.backgroundDrawable = backgroundDrawable;
        return recvItemEmptySpace;
    }

    public static RecvItemEmptySpace buildEmptySpace(int height) {
        RecvItemEmptySpace recvItemEmptySpace = buildEmptySpace();
        recvItemEmptySpace.height = height;
        return recvItemEmptySpace;
    }

    public static RecvItemEmptySpace buildEmptySpace() {
        return new RecvItemEmptySpace();
    }

    public RecvItemEmptySpace() {
        this.mItemSpanSize = -1;
        this.mDecorationEnable = false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.recv_item_empty_space;
    }

    @NonNull
    @Override
    public Class getHolderClass() {
        return RecvItemEmptySpaceHolder.class;
    }

    public static class RecvItemEmptySpaceHolder extends BaseHolder<RecvItemEmptySpace> {

        private View vEmpty;

        public RecvItemEmptySpaceHolder(View itemView) {
            super(itemView);
            vEmpty = findSubViewById(R.id.v_empty);
        }

        @Override
        public void bindView(RecvItemEmptySpace data, int position) {
            vEmpty.setFocusable(data.focusAble);
            vEmpty.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = vEmpty.getLayoutParams();
                    params.height = data.height;
                    vEmpty.setLayoutParams(params);
                }
            });
            if (data.backgroundDrawable != null) {
                vEmpty.setBackgroundDrawable(data.backgroundDrawable);
            } else {
                vEmpty.setBackgroundDrawable(new ColorDrawable(0));
            }
        }
    }
}
