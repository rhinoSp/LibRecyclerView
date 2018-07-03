package com.rhino.rv.swipe;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rhino.rv.base.BaseHolder;

/**
 * @author LuoLin
 * @since Create on 2017/6/16.
 */
public abstract class BaseSwipeHolder<T extends BaseSwipeHolderData> extends BaseHolder<T>
        implements SwipeItemLayout.IOnSwipeChangeListener {

    private SwipeItemLayout mSwipeItemLayout = null;
    private LinearLayout mLlMenuContainer = null;
    private View.OnClickListener mOnSwipeItemClickListener = null;
    private View.OnTouchListener mOnItemTouchListener = null;

    public BaseSwipeHolder(View itemView) {
        super(itemView);
        if (!(itemView instanceof SwipeItemLayout)) {
            throw new IllegalArgumentException("The root view must be SwipeItemLayout.");
        }
        mSwipeItemLayout = (SwipeItemLayout) itemView;
        mSwipeItemLayout.setOnSwipeChangeListener(this);

        View view = mSwipeItemLayout.getChildAt(0);
        if (null == view || !(view instanceof LinearLayout)) {
            throw new IllegalArgumentException("The first child view must be LinearLayout.");
        }
        mLlMenuContainer = (LinearLayout) view;
    }

    @Override
    public void onBindView(T data, int position) {
        super.onBindView(data, position);
        buildSwipeMenu(data);
        itemView.setOnTouchListener(buildOnItemTouchListener());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onOpened(SwipeItemLayout swipeItemLayout) {
        BaseSwipeHolderData data = getBindData();
        if (null == data || !(getAdapter() instanceof SwipeListAdapter)) {
            return;
        }
        SwipeListAdapter adapter = (SwipeListAdapter) getAdapter();
        adapter.closeOtherSwipeMenu(data);
        adapter.setOtherSwipeAble(data, false);
        data.setSwipeFlag(true);

        if (null != data.mSwipeChangeListener) {
            data.mSwipeChangeListener.onOpened(swipeItemLayout, data);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClosed(SwipeItemLayout swipeItemLayout) {
        BaseSwipeHolderData data = getBindData();
        if (null == data || !(getAdapter() instanceof SwipeListAdapter)) {
            return;
        }
        data.setSwipeFlag(false);
        SwipeListAdapter adapter = (SwipeListAdapter) getAdapter();
        if (!adapter.hasRealSwipedItem()) {
            adapter.setAllSwipeEnable(true);
        }

        if (null != data.mSwipeChangeListener) {
            data.mSwipeChangeListener.onClosed(swipeItemLayout, data);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStartOpen(SwipeItemLayout swipeItemLayout) {
        BaseSwipeHolderData data = getBindData();
        if (null != data && null != data.mSwipeChangeListener) {
            data.mSwipeChangeListener.onStartOpen(swipeItemLayout, data);
        }
    }

    /**
     * Build the swipe menu items.
     *
     * @param data the bind data
     */
    private void buildSwipeMenu(T data) {
        mLlMenuContainer.removeAllViews();
        mSwipeItemLayout.reset();
        if (null != data && data.hasSwipeMenu()) {
            mSwipeItemLayout.setSwipeEnable(true);
            for (SwipeMenuItem item : data.mSwipeMenuList) {
                mLlMenuContainer.addView(buildMenuItem(item));
            }
        } else {
            mSwipeItemLayout.setSwipeEnable(false);
        }
    }

    /**
     * Build the swipe menu item view.
     *
     * @param item the menu data
     */
    private LinearLayout buildMenuItem(SwipeMenuItem item) {
        LinearLayout parent = new LinearLayout(getContext());
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(new LinearLayout.LayoutParams(item.getWidth(), item.getHeight()));
        parent.setTag(item);
        parent.setOnClickListener(buildSwipeItemClickListener());

        if (null != item.getBackgroundDrawable()) {
            parent.setBackgroundDrawable(item.getBackgroundDrawable());
        }

        if (null != item.getIconDrawable()) {
            parent.addView(buildIcon(item));
        }

        if (!TextUtils.isEmpty(item.getText())) {
            parent.addView(buildTitle(item));
        }
        return parent;
    }

    @SuppressWarnings("unchecked")
    private View.OnClickListener buildSwipeItemClickListener() {
        if (null == mOnSwipeItemClickListener) {
            mOnSwipeItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object o = v.getTag();
                    if (null == o) {
                        return;
                    }

                    SwipeMenuItem item = (SwipeMenuItem) o;
                    if (null != item.getClickListener()) {
                        item.getClickListener().onItemClick(getBindData(), getBindPosition(), item);
                    } else if (null != getBindData() && null != getBindData().mSwipeMenuItemClickListener) {
                        getBindData().mSwipeMenuItemClickListener.onItemClick(getBindData(), getBindPosition(), item);
                    }
                }
            };
        }
        return mOnSwipeItemClickListener;
    }

    private View.OnTouchListener buildOnItemTouchListener() {
        if (null == mOnItemTouchListener) {
            mOnItemTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_DOWN == event.getAction()) {
                        if (!(getAdapter() instanceof SwipeListAdapter)) {
                            return false;
                        }
                        SwipeListAdapter adapter = (SwipeListAdapter) getAdapter();
                        if (adapter.hasRealSwipedItem()) {
                            adapter.closeAllSwipeMenu();
                            return true;
                        }
                    }
                    return false;
                }
            };
        }
        return mOnItemTouchListener;
    }

    private ImageView buildIcon(SwipeMenuItem item) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(item.getIconDrawable());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(item.getIconWidth(), item.getIconHeight()));
        if (0 != item.getIconColor()) {
            if (null != item.getIconColorMode()) {
                imageView.setColorFilter(item.getIconColor(), item.getIconColorMode());
            } else {
                imageView.setColorFilter(item.getIconColor());
            }
        }
        return imageView;
    }

    private TextView buildTitle(SwipeMenuItem item) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(item.getText());
        textView.setTextSize(item.getTextSize());
        textView.setTextColor(item.getTextColor());
        if (null != item.getTextTypeface()) {
            textView.setTypeface(item.getTextTypeface());
        }
        return textView;
    }

    public void open(boolean anim) {
        if (mSwipeItemLayout != null) {
            if (getAdapter() instanceof SwipeListAdapter) {
                SwipeListAdapter adapter = (SwipeListAdapter) getAdapter();
                if (adapter.hasRealSwipedItem()) {
                    adapter.closeAllSwipeMenu();
                }
            }

            if (anim) {
                mSwipeItemLayout.openWithAnim();
            } else {
                mSwipeItemLayout.open();
            }
        }
    }

    public void close(boolean anim) {
        if (mSwipeItemLayout != null) {
            if (anim) {
                mSwipeItemLayout.closeWithAnim();
            } else {
                mSwipeItemLayout.close();
            }
        }
    }

    public boolean isOpened() {
        if (mSwipeItemLayout != null) {
            return mSwipeItemLayout.isOpened();
        }
        return false;
    }

    public boolean isClosed() {
        if (mSwipeItemLayout != null) {
            return mSwipeItemLayout.isClosed();
        }
        return false;
    }

    public void setSwipeEnable(boolean enable) {
        if (mSwipeItemLayout != null) {
            mSwipeItemLayout.setSwipeEnable(enable);
        }
    }

}
