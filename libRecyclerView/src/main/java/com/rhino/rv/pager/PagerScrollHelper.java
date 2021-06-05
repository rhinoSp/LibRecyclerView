package com.rhino.rv.pager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>The scroll helper of RecyclerView page.</p>
 *
 * @author LuoLin
 * @since Create on 2017/8/25.
 */
public class PagerScrollHelper implements View.OnTouchListener {

    private static final String TAG = "PagingScrollHelper";

    private static final int SCROLL_ANIM_DURATION = 300; // ms

    private int mTotalScrollX = 0;
    private int mTotalScrollY = 0;

    private int mScrollStartX = 0;
    private int mScrollStartY = 0;

    private RecyclerView mRecyclerView = null;
    private MyOnFlingListener mOnFlingListener;
    private ValueAnimator mAnimator = null;

    private int mCurrentPageIndex;
    private OnPageChangeListener mOnPageChangeListener;

    public PagerScrollHelper(RecyclerView recycleView) {
        this.mOnFlingListener = new MyOnFlingListener();
        this.mRecyclerView = recycleView;
        this.mRecyclerView.setOnFlingListener(mOnFlingListener);
        this.mRecyclerView.addOnScrollListener(new MyOnScrollListener());
        this.mRecyclerView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mScrollStartY = mTotalScrollY;
            mScrollStartX = mTotalScrollX;
        }
        return false;
    }

    private class MyOnFlingListener extends RecyclerView.OnFlingListener {
        @Override
        public boolean onFling(int velocityX, int velocityY) {
            Log.d(TAG, "onFling velocityX = " + velocityX + ", velocityY = " + velocityY);
            int scrollStartPageIndex, startPoint, endPoint;
            if (getOrientation() == RecyclerView.VERTICAL) {
                scrollStartPageIndex = mScrollStartY / mRecyclerView.getHeight();
                if (velocityY < 0 && scrollStartPageIndex > 0) {
                    scrollStartPageIndex--;
                } else if (velocityY > 0) {
                    scrollStartPageIndex++;
                }
                startPoint = mTotalScrollY;
                endPoint = scrollStartPageIndex * mRecyclerView.getHeight();
            } else {
                scrollStartPageIndex = mScrollStartX / mRecyclerView.getWidth();
                Log.d(TAG, "onFling mScrollStartX = " + mScrollStartX + ", scrollStartPageIndex = " + scrollStartPageIndex);
                if (velocityX < 0 && scrollStartPageIndex > 0) {
                    scrollStartPageIndex--;
                } else if (velocityX > 0) {
                    scrollStartPageIndex++;
                }
                startPoint = mTotalScrollX;
                endPoint = scrollStartPageIndex * mRecyclerView.getWidth();
            }
            startScrollAnimator(startPoint, endPoint);
            return true;
        }
    }

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            Log.d(TAG, "onScrollStateChanged newState = " + newState);
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                int velocityX = 0, velocityY = 0;
                if (getOrientation() == RecyclerView.VERTICAL) {
                    if (Math.abs(mTotalScrollY - mScrollStartY) > recyclerView.getHeight() / 3) {
                        velocityY = mTotalScrollY - mScrollStartY < 0 ? -1000 : 1000;
                    }
                } else {
                    if (Math.abs(mTotalScrollX - mScrollStartX) > recyclerView.getWidth() / 3) {
                        velocityX = mTotalScrollX - mScrollStartX < 0 ? -1000 : 1000;
                    }
                }
                mOnFlingListener.onFling(velocityX, velocityY);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mTotalScrollY += dy;
            mTotalScrollX += dx;
            Log.d(TAG, "onScrolled dx = " + dx + ", mTotalScrollX = " + mTotalScrollX
                    + ", mTotalScrollY = " + mTotalScrollY + ", dy = " + dy);
        }
    }

    /**
     * Start scroll anim.
     *
     * @param startPoint the start point
     * @param endPoint   the end point
     */
    private void startScrollAnimator(int startPoint, int endPoint) {
        Log.d(TAG, "startScrollAnimator startPoint = " + startPoint + ", endPoint = " + endPoint);
        if (mAnimator == null) {
            mAnimator = new ValueAnimator();
            mAnimator.setIntValues(startPoint, endPoint);
            mAnimator.setDuration(SCROLL_ANIM_DURATION);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int nowPoint = (int) animation.getAnimatedValue();
                    Log.d(TAG, "onAnimationUpdate nowPoint = " + nowPoint
                            + ", mTotalScrollX = " + mTotalScrollX + ", mTotalScrollY = " + mTotalScrollY);
                    if (getOrientation() == RecyclerView.VERTICAL) {
                        int dy = nowPoint - mTotalScrollY;
                        mRecyclerView.scrollBy(0, dy);
                    } else {
                        int dx = nowPoint - mTotalScrollX;
                        mRecyclerView.scrollBy(dx, 0);
                    }
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d(TAG, "onAnimationEnd mTotalScrollX = " + mTotalScrollX + ", mTotalScrollY = " + mTotalScrollY);
                    int currentPageIndex = getCurrentPageIndex();
                    if (mCurrentPageIndex != currentPageIndex) {
                        if (null != mOnPageChangeListener) {
                            mOnPageChangeListener.onPageSelected(currentPageIndex);
                        }
                    }
                    mCurrentPageIndex = currentPageIndex;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    Log.d(TAG, "onAnimationCancel mTotalScrollX = " + mTotalScrollX + ", mTotalScrollY = " + mTotalScrollY);
                }
            });
        } else {
            mAnimator.cancel();
            mAnimator.setIntValues(startPoint, endPoint);
        }
        mAnimator.start();
    }

    /**
     * Get current index of the page.
     *
     * @return current index of the page.
     */
    public int getCurrentPageIndex() {
        int pageIndex;
        if (getOrientation() == RecyclerView.VERTICAL) {
            pageIndex = mTotalScrollY / mRecyclerView.getHeight();
        } else {
            pageIndex = mTotalScrollX / mRecyclerView.getWidth();
        }
        return pageIndex;
    }

    /**
     * Returns the current orientation of the layout.
     *
     * @return Current orientation,  either {@link RecyclerView#HORIZONTAL} or {@link RecyclerView#VERTICAL}
     */
    public int getOrientation() {
        return mRecyclerView.getLayoutManager().canScrollHorizontally()
                ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL;
    }

    /**
     * Set a listener that will be notified of any changes in scroll state or position.
     *
     * @param listener Listener to set or null to clear
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    /**
     * Callback interface for responding to changing state of the selected page.
     **/
    public interface OnPageChangeListener {
        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        void onPageSelected(int position);

    }
}
