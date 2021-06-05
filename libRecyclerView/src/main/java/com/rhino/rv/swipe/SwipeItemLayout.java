package com.rhino.rv.swipe;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import androidx.annotation.FloatRange;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.rhino.rv.R;

import java.lang.reflect.Method;

/**
 * Subclass of {@link FrameLayout} that supports swipe.
 * Follow this example:
 * <pre class="prettyprint">
 * &lt;com.rhino.rl.swipe.SwipeItemLayout
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:app="http://schemas.android.com/apk/res-auto"
 *     android:layout_width="match_parent"
 *     android:layout_height="70dp"
 *     android:tag="com.rhino.mr.holder.SimpleTextHolder"
 *     app:sil_bottomMode="layDown"
 *     app:sil_springDistance="0dp"
 *     app:sil_swipeAlphaEnable="false"
 *     app:sil_swipeDirection="left"
 *     app:sil_swipeEnable="true"/&gt
 *
 *     &lt;--The swipe menu view(bottom)--&gt</br>
 *     &lt;LinearLayout
 *         android:layout_width="wrap_content"
 *         android:layout_height="match_parent"/&gt
 *
 *     &lt;--The content view(top)--&gt</br>
 *     &lt;RelativeLayout
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent"
 *         android:background="@color/white"/&gt
 *
 *         &lt;TextView
 *             android:id="@+id/text"
 *             android:layout_width="wrap_content"
 *             android:layout_height="wrap_content"
 *             android:layout_centerVertical="true"
 *             android:gravity="center"
 *             android:text="text"
 *             android:textColor="@color/theme_color"
 *             android:textSize="20sp"/&gt
 *
 *     &lt;RelativeLayout>
 * &lt;/androidx.percent.PercentFrameLayout/&gt
 * </pre>
 * <p>The attributes that you can use are:</p>
 * <ul>
 * <li>{@code sil_swipeDirection}
 * <li>{@code sil_bottomMode}
 * <li>{@code sil_springDistance}
 * <li>{@code sil_swipeEnable}
 * <li>{@code sil_swipeAlphaEnable}
 * </ul>
 * <p>
 * Notice: Only two child view.
 * </p>
 *
 * @author LuoLin
 * @since Create on 2017/6/16.
 */
public class SwipeItemLayout extends FrameLayout {

    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_OPEN_CLOSE = "status_open_close";
    private static final int VEL_THRESHOLD = 600;
    private static final float DF_SWIPE_ENABLE_SCALE = 0.3f;
    /**
     * The drag helper.
     */
    private ViewDragHelper mDragHelper;
    /**
     * The top view, is item view.
     */
    private View mTopView;
    /**
     * The bottom view, is swipe menu view.
     */
    private View mBottomView;
    /**
     * The drag spring distance.
     */
    private int mSpringDistance = 0;
    /**
     * The drag distance.
     */
    private int mDragDistance;
    /**
     * The swipe direction.
     */
    private SwipeDirection mSwipeDirection = SwipeDirection.Left;
    /**
     * The bottom view show style.
     */
    private SwipeShowStyle mSwipeShowStyle = SwipeShowStyle.PullOut;
    /**
     * The swipe status.
     */
    private SwipeStatus mCurrentStatus = SwipeStatus.Closed;
    /**
     * The swipe status before.
     */
    private SwipeStatus mPreStatus = mCurrentStatus;
    /**
     * The top view final left.
     */
    private int mTopFinalLeft;
    /**
     * The MarginLayoutParams of top view.
     */
    private MarginLayoutParams mTopLp;
    /**
     * The MarginLayoutParams of bottom view.
     */
    private MarginLayoutParams mBottomLp;
    /**
     * The drag ratio.
     */
    private float mDragRatio;
    /**
     * The swipe change listener.
     */
    private IOnSwipeChangeListener mSwipeChangeListener;

    /**
     * The GestureDetectorCompat.
     */
    private GestureDetectorCompat mGestureDetectorCompat;
    /**
     * The long click listener.
     */
    private OnLongClickListener mOnLongClickListener;
    /**
     * The click listener.
     */
    private OnClickListener mOnClickListener;
    /**
     * Whether click enable.
     */
    private boolean mSwipeEnable = true;
    /**
     * Whether swipe alpha enable.
     */
    private boolean mSwipeAlphaEnable = true;

    private enum SwipeDirection {
        Left, Right
    }

    private enum SwipeShowStyle {
        PullOut, LayDown
    }

    private enum SwipeStatus {
        Opened, Closed, Moving
    }

    public SwipeItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initProperty();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeItemLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.SwipeItemLayout_sil_swipeDirection) {
            int leftSwipeDirection = typedArray.getInt(attr, mSwipeDirection.ordinal());
            if (leftSwipeDirection == SwipeDirection.Right.ordinal()) {
                mSwipeDirection = SwipeDirection.Right;
            }
        } else if (attr == R.styleable.SwipeItemLayout_sil_bottomMode) {
            int pullOutBottomMode = typedArray.getInt(attr, mSwipeShowStyle.ordinal());
            if (pullOutBottomMode == SwipeShowStyle.LayDown.ordinal()) {
                mSwipeShowStyle = SwipeShowStyle.LayDown;
            }
        } else if (attr == R.styleable.SwipeItemLayout_sil_springDistance) {
            mSpringDistance = typedArray.getDimensionPixelSize(attr, mSpringDistance);
            if (mSpringDistance < 0) {
                mSpringDistance = 0;
            }
        } else if (attr == R.styleable.SwipeItemLayout_sil_swipeEnable) {
            mSwipeEnable = typedArray.getBoolean(attr, mSwipeEnable);
        } else if (attr == R.styleable.SwipeItemLayout_sil_swipeAlphaEnable) {
            mSwipeAlphaEnable = typedArray.getBoolean(attr, mSwipeAlphaEnable);
        }
    }

    private void initProperty() {
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mGestureDetectorCompat = new GestureDetectorCompat(getContext(), mSimpleOnGestureListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalStateException(SwipeItemLayout.class.getSimpleName() + "Must only two child view.");
        }
        mBottomView = getChildAt(0);
        mBottomView.setVisibility(INVISIBLE);
        mTopView = getChildAt(1);

        mTopLp = (MarginLayoutParams) mTopView.getLayoutParams();
        mBottomLp = (MarginLayoutParams) mBottomView.getLayoutParams();
        mTopFinalLeft = getPaddingLeft() + mTopLp.leftMargin;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putInt(STATUS_OPEN_CLOSE, mCurrentStatus.ordinal());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (bundle.getInt(STATUS_OPEN_CLOSE) == SwipeStatus.Opened.ordinal()) {
                open();
            } else {
                close();
            }
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
        }
        return mDragHelper.shouldInterceptTouchEvent(ev) && mGestureDetectorCompat.onTouchEvent(ev);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mOnClickListener = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
        mOnLongClickListener = l;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (insideAdapterView()) {
            if (mOnClickListener == null) {
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performAdapterViewItemClick();
                    }
                });
            }
            if (mOnLongClickListener == null) {
                setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        performAdapterViewItemLongClick();
                        return true;
                    }
                });
            }
        }
    }

    private void performAdapterViewItemClick() {
        ViewParent t = getParent();
        if (t instanceof AdapterView) {
            AdapterView view = (AdapterView) t;
            int p = view.getPositionForView(SwipeItemLayout.this);
            if (p != AdapterView.INVALID_POSITION) {
                view.performItemClick(view.getChildAt(p - view.getFirstVisiblePosition()), p, view.getAdapter().getItemId(p));
            }
        }
    }

    private boolean performAdapterViewItemLongClick() {
        ViewParent t = getParent();
        if (t instanceof AdapterView) {
            AdapterView view = (AdapterView) t;
            int p = view.getPositionForView(SwipeItemLayout.this);
            if (p == AdapterView.INVALID_POSITION) {
                return false;
            }
            long vId = view.getItemIdAtPosition(p);
            boolean handled = false;
            try {
                Method m = AbsListView.class.getDeclaredMethod("performLongPress", View.class, int.class, long.class);
                m.setAccessible(true);
                handled = (boolean) m.invoke(view, SwipeItemLayout.this, p, vId);
            } catch (Exception e) {
                e.printStackTrace();
                if (view.getOnItemLongClickListener() != null) {
                    handled = view.getOnItemLongClickListener().onItemLongClick(view, SwipeItemLayout.this, p, vId);
                }
                if (handled) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                }
            }
            return handled;
        }
        return false;
    }

    private boolean insideAdapterView() {
        return getAdapterView() != null;
    }

    private AdapterView getAdapterView() {
        ViewParent t = getParent();
        if (t instanceof AdapterView) {
            return (AdapterView) t;
        }
        return null;
    }

    private void requestParentDisallowInterceptTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                requestParentDisallowInterceptTouchEvent();
                return true;
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                requestParentDisallowInterceptTouchEvent();
                return true;
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            setPressed(false);
            closeWithAnim();
            return performClick();
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isClosed()) {
                setPressed(true);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isClosed()) {
                setPressed(true);
                postDelayed(mCancelPressedTask, 300);
                performLongClick();
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (isClosed()) {
                setPressed(true);
                return true;
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (isClosed()) {
                setPressed(false);
                return true;
            }
            return false;
        }
    };

    private Runnable mCancelPressedTask = new Runnable() {
        @Override
        public void run() {
            setPressed(false);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        mGestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragDistance = mBottomView.getMeasuredWidth() + mBottomLp.leftMargin + mBottomLp.rightMargin;

        int topTop = getPaddingTop() + mTopLp.topMargin;
        int topBottom = getMeasuredHeight() - getPaddingBottom() - mTopLp.bottomMargin;
        int topRight = mTopFinalLeft + mTopView.getMeasuredWidth();

        int bottomTop = getPaddingTop() + mBottomLp.topMargin;
        int bottomBottom = getMeasuredHeight() - getPaddingBottom() - mBottomLp.bottomMargin;
        int bottomLeft;
        int bottomRight;

        if (mSwipeDirection == SwipeDirection.Left) {
            if (mSwipeShowStyle == SwipeShowStyle.LayDown) {
                bottomRight = getMeasuredWidth() - getPaddingRight() - mBottomLp.rightMargin;
                bottomLeft = bottomRight - mBottomView.getMeasuredWidth();
            } else {
                bottomLeft = mTopFinalLeft + mTopView.getMeasuredWidth() + mTopLp.rightMargin + mBottomLp.leftMargin;
                int minBottomLeft = getMeasuredWidth() - getPaddingRight() - mBottomView.getMeasuredWidth() - mBottomLp.rightMargin;
                bottomLeft = Math.max(bottomLeft, minBottomLeft);
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            }
        } else {
            if (mSwipeShowStyle == SwipeShowStyle.LayDown) {
                bottomLeft = getPaddingLeft() + mBottomLp.leftMargin;
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            } else {
                bottomLeft = mTopFinalLeft - mDragDistance;
                int maxBottomLeft = getPaddingLeft() + mBottomLp.leftMargin;
                bottomLeft = Math.min(maxBottomLeft, bottomLeft);
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            }
        }
        mBottomView.layout(bottomLeft, bottomTop, bottomRight, bottomBottom);
        mTopView.layout(mTopFinalLeft, topTop, topRight, topBottom);
    }

    /**
     * Set the swipe listener
     *
     * @param listener IOnSwipeChangeListener
     */
    public void setOnSwipeChangeListener(IOnSwipeChangeListener listener) {
        mSwipeChangeListener = listener;
    }

    /**
     * Set swipe enable
     *
     * @param enable boolean
     */
    public void setSwipeEnable(boolean enable) {
        mSwipeEnable = enable;
    }

    /**
     * Open swipe menu with animation.
     */
    public void openWithAnim() {
        if (isOpened()) {
            return;
        }
        mPreStatus = SwipeStatus.Moving;
        smoothSlideTo(true);
    }

    /**
     * Close swipe menu with animation.
     */
    public void closeWithAnim() {
        if (isClosed()) {
            return;
        }
        mPreStatus = SwipeStatus.Moving;
        smoothSlideTo(false);
    }

    /**
     * Open swipe menu.
     */
    public void open() {
        if (isOpened()) {
            return;
        }
        slideTo(true);
    }

    /**
     * Close swipe menu.
     */
    public void close() {
        if (!isOpened()) {
            return;
        }
        slideTo(false);
    }

    /**
     * Close swipe menu.
     */
    public void reset() {
        slideTo(false);
    }

    /**
     * Whether swipe menu opened.
     *
     * @return True open
     */
    public boolean isOpened() {
        return (mCurrentStatus == SwipeStatus.Opened)
                || (mCurrentStatus == SwipeStatus.Moving && mPreStatus == SwipeStatus.Opened);
    }

    /**
     * Whether swipe menu closed.
     *
     * @return True close
     */
    public boolean isClosed() {
        return mCurrentStatus == SwipeStatus.Closed
                || (mCurrentStatus == SwipeStatus.Moving && mPreStatus == SwipeStatus.Closed);
    }

    /**
     * Get the top view.
     *
     * @return the top view
     */
    public View getTopView() {
        return mTopView;
    }

    /**
     * Get the bottom view.
     *
     * @return the bottom view
     */
    public View getBottomView() {
        return mBottomView;
    }

    /**
     * Open or close the swipe menu.
     *
     * @param isOpen True - open，False - close
     */
    private void smoothSlideTo(boolean isOpen) {
        if (mDragHelper.smoothSlideViewTo(mTopView, getCloseOrOpenTopViewFinalLeft(isOpen), getPaddingTop() + mTopLp.topMargin)) {
            ViewCompat.postInvalidateOnAnimation(this);
            if (isOpen) {
                mBottomView.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Open or close the swipe menu.
     *
     * @param isOpen True - open，False - close
     */
    private void slideTo(boolean isOpen) {
        if (isOpen) {
            alphaBottomView(1.0f);
            mBottomView.setVisibility(VISIBLE);
            mCurrentStatus = SwipeStatus.Opened;
            if (mSwipeChangeListener != null) {
                mSwipeChangeListener.onOpened(this);
            }
        } else {
            alphaBottomView(0f);
            mBottomView.setVisibility(INVISIBLE);
            mCurrentStatus = SwipeStatus.Closed;
            if (mSwipeChangeListener != null) {
                mSwipeChangeListener.onClosed(this);
            }
        }
        mPreStatus = mCurrentStatus;
        mTopFinalLeft = getCloseOrOpenTopViewFinalLeft(isOpen);
        requestLayout();
    }

    private int getCloseOrOpenTopViewFinalLeft(boolean isOpen) {
        int left = getPaddingLeft() + mTopLp.leftMargin;
        if (mSwipeDirection == SwipeDirection.Left) {
            left = left - (isOpen ? mDragDistance : 0);
        } else {
            left = left + (isOpen ? mDragDistance : 0);
        }
        return left;
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mSwipeEnable && child == mTopView;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return getPaddingTop() + mTopLp.topMargin;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragDistance + mSpringDistance;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int minTopLeft;
            int maxTopLeft;

            if (mSwipeDirection == SwipeDirection.Left) {
                minTopLeft = getPaddingLeft() + mTopLp.leftMargin - (mDragDistance + mSpringDistance);
                maxTopLeft = getPaddingLeft() + mTopLp.leftMargin;
            } else {
                minTopLeft = getPaddingLeft() + mTopLp.leftMargin;
                maxTopLeft = getPaddingLeft() + mTopLp.leftMargin + (mDragDistance + mSpringDistance);
            }
            return Math.min(Math.max(minTopLeft, left), maxTopLeft);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTopFinalLeft = left;
            int topViewHorizontalOffset = Math.abs(mTopFinalLeft - (getPaddingLeft() + mTopLp.leftMargin));
            if (topViewHorizontalOffset > mDragDistance) {
                mDragRatio = 1.0f;
            } else {
                mDragRatio = 1.0f * topViewHorizontalOffset / mDragDistance;
            }

            alphaBottomView(0.1f + 0.9f * mDragRatio);
            dispatchSwipeEvent();
            invalidate();
            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalLeft = getPaddingLeft() + mTopLp.leftMargin;
            if (mSwipeDirection == SwipeDirection.Left) {
                if (xvel < -VEL_THRESHOLD || (mPreStatus == SwipeStatus.Closed && xvel < VEL_THRESHOLD && mDragRatio >= DF_SWIPE_ENABLE_SCALE)
                        || (mPreStatus == SwipeStatus.Opened && xvel < VEL_THRESHOLD && mDragRatio >= 1 - DF_SWIPE_ENABLE_SCALE)) {
                    finalLeft -= mDragDistance;
                }
            } else {
                if (xvel > VEL_THRESHOLD || (mPreStatus == SwipeStatus.Closed && xvel > -VEL_THRESHOLD && mDragRatio >= DF_SWIPE_ENABLE_SCALE)
                        || (mPreStatus == SwipeStatus.Opened && xvel > -VEL_THRESHOLD && mDragRatio >= 1 - DF_SWIPE_ENABLE_SCALE)) {
                    finalLeft += mDragDistance;
                }
            }
            mDragHelper.settleCapturedViewAt(finalLeft, getPaddingTop() + mTopLp.topMargin);
            ViewCompat.postInvalidateOnAnimation(SwipeItemLayout.this);
        }
    };

    /**
     * Dispatch the swipe event.
     */
    private void dispatchSwipeEvent() {
        SwipeStatus preStatus = mCurrentStatus;
        updateCurrentStatus();
        if (mCurrentStatus != preStatus) {
            if (mCurrentStatus == SwipeStatus.Closed) {
                mBottomView.setVisibility(INVISIBLE);
                if (mSwipeChangeListener != null && mPreStatus != mCurrentStatus) {
                    mSwipeChangeListener.onClosed(this);
                }
                mPreStatus = SwipeStatus.Closed;
            } else if (mCurrentStatus == SwipeStatus.Opened) {
                if (mSwipeChangeListener != null && mPreStatus != mCurrentStatus) {
                    mSwipeChangeListener.onOpened(this);
                }
                mPreStatus = SwipeStatus.Opened;
            } else if (mPreStatus == SwipeStatus.Closed) {
                mBottomView.setVisibility(VISIBLE);
                if (mSwipeChangeListener != null) {
                    mSwipeChangeListener.onStartOpen(this);
                }
            }
        }
    }

    /**
     * Change the bottom view alpha.
     *
     * @param alpha The opacity of the view.
     */
    private void alphaBottomView(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (mSwipeAlphaEnable) {
            ViewCompat.setAlpha(mBottomView, alpha);
        }
    }

    private void updateCurrentStatus() {
        if (mSwipeDirection == SwipeDirection.Left) {
            if (mTopFinalLeft == getPaddingLeft() + mTopLp.leftMargin - mDragDistance) {
                mCurrentStatus = SwipeStatus.Opened;
            } else if (mTopFinalLeft == getPaddingLeft() + mTopLp.leftMargin) {
                mCurrentStatus = SwipeStatus.Closed;
            } else {
                mCurrentStatus = SwipeStatus.Moving;
            }
        } else {
            if (mTopFinalLeft == getPaddingLeft() + mTopLp.leftMargin + mDragDistance) {
                mCurrentStatus = SwipeStatus.Opened;
            } else if (mTopFinalLeft == getPaddingLeft() + mTopLp.leftMargin) {
                mCurrentStatus = SwipeStatus.Closed;
            } else {
                mCurrentStatus = SwipeStatus.Moving;
            }
        }
    }

    public interface IOnSwipeChangeListener {

        void onOpened(SwipeItemLayout swipeItemLayout);

        void onClosed(SwipeItemLayout swipeItemLayout);

        void onStartOpen(SwipeItemLayout swipeItemLayout);
    }

}