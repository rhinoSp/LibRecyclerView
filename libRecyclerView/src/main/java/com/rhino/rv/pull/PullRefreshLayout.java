package com.rhino.rv.pull;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.rhino.rv.pull.impl.IOnPullStatusChangeListener;
import com.rhino.rv.pull.impl.IPull;

/**
 * Subclass of {@link RelativeLayout} that supports swipe.
 * Follow this example:
 * <pre class="prettyprint">
 * &lt;?xml version="1.0" encoding="utf-8"?&gt</br>
 * &lt;com.rhino.rl.pr.PullRefreshLayout
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     android:id="@+id/PullRefreshLayout"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"&gt
 *
 *     &lt;com.rhino.rl.pr.PullRecyclerView
 *         android:id="@+id/PullRecyclerView"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent"/&gt
 *
 * &lt;/com.rhino.rl.pr.PullRefreshLayout&gt
 * </pre>
 *
 * @author LuoLin
 * @since Create on 2017/4/13.
 */
public class PullRefreshLayout extends RelativeLayout {

    public enum Style {
        NONE,
        FLEXIBLE,
        PULL_REFRESH
    }

    private Style mStyle = Style.FLEXIBLE;

    /**
     * The none pull status.
     */
    private static final int STATUS_PULL_NONE = 0;
    /**
     * Pull to start refresh.
     */
    private static final int STATUS_PULL_DOWN_TO_REFRESH = 1;
    /**
     * Release to start refresh.
     */
    private static final int STATUS_PULL_DOWN_RELEASE_TO_REFRESH = 2;
    /**
     * Refreshing.
     */
    private static final int STATUS_PULL_DOWN_REFRESHING = 3;

    /**
     * Pull to start loading.
     */
    private static final int STATUS_PULL_UP_TO_LOAD = 4;
    /**
     * Release to start loading.
     */
    private static final int STATUS_PULL_UP_RELEASE_TO_LOAD = 5;
    /**
     * Loading.
     */
    private static final int STATUS_PULL_UP_LOADING = 6;
    /**
     * The pull status.
     */
    private int mPullStatus;

    /**
     * The default min pull distance.
     */
    private static final int DF_MIN_PULL_DISTANCE = 60;
    /**
     * The min pull distance.
     */
    private int mPullMinDistance = dip2px(DF_MIN_PULL_DISTANCE);
    /**
     * The default max pull distance.
     */
    private static final int DF_MAX_PULL_DISTANCE = 140;
    /**
     * The max pull distance,0 >= pullMaxDistance unlimited
     */
    private int mPullMaxDistance = dip2px(DF_MAX_PULL_DISTANCE);

    /**
     * The pull distance.
     */
    private int mPullDistance;
    /**
     * The last move x.
     */
    private float mLastX;
    /**
     * The last move y.
     */
    private float mLastY;

    /**
     * Whether pull down or up.
     */
    private boolean mIsPulled;
    /**
     * Whether auto load.
     */
    private boolean mIsAutoLoad;

    /**
     * The pull down status change listener.
     */
    private IOnPullStatusChangeListener mPullDownListener;
    /**
     * The pull down header view.
     */
    private View mPullDownView;

    /**
     * The pull up status change listener.
     */
    private IOnPullStatusChangeListener mPullUpListener;
    /**
     * The pull up footer view.
     */
    private View mPullUpView;

    /**
     * The pull interface to judge pull up or down.
     */
    private IPull mIPull;
    /**
     * Whether support pull down refresh.
     */
    private boolean isSupportPullDownRefresh = true;
    /**
     * Whether support pull up load.
     */
    private boolean isSupportPullUpLoad = true;
    /**
     * Down start x
     */
    private float downStartX = 0f;
    /**
     * Down start y
     */
    private float downStartY = 0f;


    public PullRefreshLayout(Context context) {
        super(context);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1 || !(getChildAt(0) instanceof IPull)) {
            throw new IllegalStateException(PullRefreshLayout.class.getSimpleName() + "Must only one child view instanceof IPull");
        }
        mIPull = (IPull) getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (Style.PULL_REFRESH == mStyle) {
            checkInitPullView();
        }
        int height = b - t;
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View child = getChildAt(i);
            int top = 0;
            int bottom = top + height;
            if (child == mPullDownView) {
                top = 0 - child.getHeight();
                bottom = 0;
            } else if (child == mPullUpView) {
                top = height;
                bottom = top + child.getHeight();
            }
            child.layout(l, top + mPullDistance, r, bottom + mPullDistance);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downStartX = ev.getX();
                downStartY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true); // 阻止父控件拦截事件
                onTouchDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(ev.getX() - downStartX);
                float dy = Math.abs(ev.getY() - downStartY);
                // 如果检测到是水平滑动，让父控件处理事件
                if (dx > dy) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                onTouchMove(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onTouchUp();
                break;
            default:
                break;
        }
        return mIsPulled || super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPullDownListener != null) {
            mPullDownListener.releaseAll();
        }
        if (mPullUpListener != null) {
            mPullUpListener.releaseAll();
        }
    }

    /**
     * change dp to px
     *
     * @param dpValue the dp value
     * @return the px value
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Check to init pull view.
     */
    private void checkInitPullView() {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        if (null == mPullDownView && null != mPullDownListener) {
            mPullDownView = mPullDownListener.getView();
            addView(mPullDownView);
        }

        if (null == mPullUpView && null != mPullUpListener) {
            mPullUpView = mPullUpListener.getView();
            addView(mPullUpView);
        }
    }

    /**
     * Deal the touch down event.
     *
     * @param ev MotionEvent
     */
    private void onTouchDown(MotionEvent ev) {
        mLastX = ev.getX();
        mLastY = ev.getY();
        if (Style.PULL_REFRESH == mStyle) {
            if (mPullStatus != STATUS_PULL_DOWN_REFRESHING
                    && mPullStatus != STATUS_PULL_UP_LOADING) {
                // not refreshing
                mPullDistance = 0;
            }
        }
    }

    /**
     * Deal the touch move event.
     *
     * @param ev MotionEvent
     */
    private void onTouchMove(MotionEvent ev) {
        float moveX = ev.getX();
        float moveY = ev.getY();
        float moveDistanceY = moveY - mLastY;
        float moveDistanceX = moveX - mLastX;
        mLastX = moveX;
        mLastY = moveY;
        if (Math.abs(moveDistanceY) > Math.abs(moveDistanceX)) {
            if ((Style.PULL_REFRESH == mStyle && isSupportPullDownRefresh && null != mPullDownView
                    || Style.FLEXIBLE == mStyle)
                    && !mIsPulled && 0 < moveDistanceY && mIPull.isScrolledToTop()) {
                mIsPulled = true;
            } else if ((Style.PULL_REFRESH == mStyle && isSupportPullUpLoad && null != mPullUpView
                    || Style.FLEXIBLE == mStyle)
                    && !mIsPulled && 0 > moveDistanceY && mIPull.isScrolledToBottom()) {
                mIsPulled = true;
                if (Style.PULL_REFRESH == mStyle && mIsAutoLoad && STATUS_PULL_NONE == mPullStatus) {
                    startLoad(true);
                }
            } else {
                mIsPulled = false;
            }
        }

        if (!mIsPulled && 0 != mPullDistance) {
            // scrolling
            calculatePullDistance(moveDistanceY);
            requestLayout();
            checkPullStatus();
        } else if (mIsPulled && ((Math.abs(moveDistanceY) > 5 || mPullDistance != 0))) {
            calculatePullDistance(moveDistanceY);
            requestLayout();
            checkPullStatus();
        }
    }

    /**
     * Deal the touch up event.
     */
    private void onTouchUp() {
        if (Style.PULL_REFRESH == mStyle) {
            if (!checkPullStatus()) {
                if (mPullStatus == STATUS_PULL_DOWN_RELEASE_TO_REFRESH) {
                    startRefresh();
                } else if (mPullStatus == STATUS_PULL_UP_RELEASE_TO_LOAD) {
                    startLoad(false);
                } else {
                    stopRefreshAndLoad();
                }
            }
        } else if (Style.FLEXIBLE == mStyle) {
            stopRefreshAndLoad();
        }
    }

    /**
     * Calculate the pull distance.
     *
     * @param moveDistanceY the distance of y move.
     */
    private void calculatePullDistance(float moveDistanceY) {
        mPullDistance += mIsPulled ? moveDistanceY * 0.3f : moveDistanceY;
        if (0 < mPullMaxDistance && Math.abs(mPullDistance) > mPullMaxDistance) {
            mPullDistance = mPullDistance > 0 ? mPullMaxDistance : -mPullMaxDistance;
        }
        if (mPullStatus == STATUS_PULL_DOWN_REFRESHING || mPullStatus == STATUS_PULL_UP_LOADING) {
            // is refreshing
            if (mPullDistance > mPullMinDistance) { // pull down
                mPullDistance = mPullMinDistance;
            } else if (mPullDistance < -mPullMinDistance) { // pull up
                mPullDistance = -mPullMinDistance;
            }
        }

        if (((mPullStatus == STATUS_PULL_DOWN_TO_REFRESH || mPullStatus == STATUS_PULL_DOWN_REFRESHING) && mPullDistance < 0) ||
                ((mPullStatus == STATUS_PULL_UP_TO_LOAD || mPullStatus == STATUS_PULL_UP_LOADING) && mPullDistance > 0)) {
            mPullDistance = 0;
        }
    }

    /**
     * Check the pull status.
     *
     * @return True is refreshing.
     */
    private boolean checkPullStatus() {
        if (mPullStatus == STATUS_PULL_DOWN_REFRESHING || mPullStatus == STATUS_PULL_UP_LOADING) {
            // is refreshing
            return true;
        }

        if (mPullDistance > 0) { // pull down
            if (mPullDistance > mPullMinDistance) {
                if (STATUS_PULL_DOWN_RELEASE_TO_REFRESH != mPullStatus && null != mPullDownListener) {
                    mPullDownListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.RELEASE_TO_REFRESH);
                }
                mPullStatus = STATUS_PULL_DOWN_RELEASE_TO_REFRESH;
            } else {
                if (STATUS_PULL_DOWN_TO_REFRESH != mPullStatus && null != mPullDownListener) {
                    mPullDownListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.PULL_REFRESH);
                }
                mPullStatus = STATUS_PULL_DOWN_TO_REFRESH;
            }
        } else if (mPullDistance < 0) { // pull up
            if (mPullDistance < -mPullMinDistance) {
                if (STATUS_PULL_DOWN_RELEASE_TO_REFRESH != mPullStatus && null != mPullUpListener) {
                    mPullUpListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.RELEASE_TO_REFRESH);
                }
                mPullStatus = STATUS_PULL_UP_RELEASE_TO_LOAD;
            } else {
                if (STATUS_PULL_UP_TO_LOAD != mPullStatus && null != mPullUpListener) {
                    mPullUpListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.PULL_REFRESH);
                }
                mPullStatus = STATUS_PULL_UP_TO_LOAD;
            }
        } else {
            mPullStatus = STATUS_PULL_NONE;
            if (null != mPullDownListener) {
                mPullDownListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.CANCEL_REFRESH);
            }
            if (null != mPullUpListener) {
                mPullUpListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.CANCEL_REFRESH);
            }
        }
        return false;
    }

    /**
     * Smooth move to the distance.
     *
     * @param distance int
     */
    private void smoothScroll(float distance) {
        TranslateAnimation mRecoverAnim = new TranslateAnimation(0, 0, distance, 0);
        mRecoverAnim.setDuration(300);
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View view = getChildAt(i);
            view.clearAnimation();
            view.startAnimation(mRecoverAnim);
        }
    }

    /**
     * Whether support pull down refresh.
     *
     * @return Tre support
     */
    public boolean isSupportPullDownRefresh() {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        return isSupportPullDownRefresh;
    }

    /**
     * Set pull down refresh enable.
     *
     * @param enable True support
     */
    public void setPullDownRefreshEnable(boolean enable) {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        isSupportPullDownRefresh = enable;
    }

    /**
     * Whether support pull up load.
     *
     * @return Tre support
     */
    public boolean isSupportPullUpLoad() {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        return isSupportPullUpLoad;
    }

    /**
     * Set pull up load enable.
     *
     * @param enable True support.
     */
    public void setPullUpLoadEnable(boolean enable) {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        isSupportPullUpLoad = enable;
    }

    /**
     * Stop refresh and load.
     */
    public void stopRefreshAndLoad() {
        if (mPullDistance != 0) {
            if (Style.PULL_REFRESH == mStyle) {
                if (mPullDistance > 0) { // pull down
                    if (null != mPullDownListener) {
                        if (STATUS_PULL_DOWN_TO_REFRESH == mPullStatus) {
                            mPullDownListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.CANCEL_REFRESH);
                        } else {
                            mPullDownListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.STOP_REFRESH);
                        }
                    }
                } else { // pull up
                    if (null != mPullUpListener) {
                        if (STATUS_PULL_UP_TO_LOAD == mPullStatus) {
                            mPullUpListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.CANCEL_REFRESH);
                        } else {
                            mPullUpListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.STOP_REFRESH);
                        }
                    }
                }
            }
            smoothScroll(mPullDistance);
            mPullDistance = 0;
        }
        mPullStatus = STATUS_PULL_NONE;
        mIsPulled = false;
        requestLayout();
    }

    /**
     * Start refresh.
     */
    public void startRefresh() {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        if (mPullStatus == STATUS_PULL_DOWN_REFRESHING || mPullStatus == STATUS_PULL_UP_LOADING) {
            return;
        }
        if (mPullDownListener != null) {
            mPullStatus = STATUS_PULL_DOWN_REFRESHING;
            mPullDownListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.START_REFRESHING);

            smoothScroll(mPullDistance - mPullMinDistance);
            mPullDistance = mPullMinDistance;
            requestLayout();
        } else {
            stopRefreshAndLoad();
        }
    }

    /**
     * Start load.
     */
    public void startLoad(boolean isAuto) {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        if (mPullStatus == STATUS_PULL_DOWN_REFRESHING || mPullStatus == STATUS_PULL_UP_LOADING) {
            return;
        }
        if (mPullUpListener != null) {
            mPullStatus = STATUS_PULL_UP_LOADING;
            mPullUpListener.onPullChanged(IOnPullStatusChangeListener.PullStatus.START_REFRESHING);

            smoothScroll(isAuto ? mPullMinDistance : mPullDistance + mPullMinDistance);
            mPullDistance = -mPullMinDistance;
            requestLayout();
        } else {
            stopRefreshAndLoad();
        }
    }

    /**
     * Whether auto load.
     *
     * @return True auto load.
     */
    public boolean isAutoLoad() {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        return mIsAutoLoad;
    }

    /**
     * Set auto load
     *
     * @param enable True auto load.
     */
    public void setAutoLoad(boolean enable) {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        this.mIsAutoLoad = enable;
    }

    /**
     * Set the pull down status change listener.
     *
     * @param listener IOnPullStatusChangeListener
     */
    public void setOnPullDownStatusChangeListener(IOnPullStatusChangeListener listener) {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        this.mPullDownListener = listener;
    }

    /**
     * Set the pull up status change listener.
     *
     * @param listener IOnPullStatusChangeListener
     */
    public void setOnPullUpStatusChangeListener(IOnPullStatusChangeListener listener) {
        if (Style.PULL_REFRESH != mStyle) {
            throw new RuntimeException("Notice: not support this method.");
        }
        this.mPullUpListener = listener;
    }

    /**
     * Set the style.
     *
     * @param style {@link Style}
     */
    public void setStyle(Style style) {
        this.mStyle = style;
    }

    /**
     * Set the min distance of pull.
     *
     * @param pullMinDistance the min distance of pull
     */
    public void setPullMinDistance(int pullMinDistance) {
        this.mPullMinDistance = pullMinDistance;
    }

    /**
     * Set the max distance of pull.
     *
     * @param pullMaxDistance the max distance of pull, 0 >= pullMaxDistance unlimited.
     */
    public void setPullMaxDistance(int pullMaxDistance) {
        this.mPullMaxDistance = pullMaxDistance;
    }
}
