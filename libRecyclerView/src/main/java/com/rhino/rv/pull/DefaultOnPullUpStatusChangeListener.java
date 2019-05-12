package com.rhino.rv.pull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rhino.rv.R;
import com.rhino.rv.pull.impl.IOnPullStatusChangeListener;

/**
 * @author rhino
 * @since Create on 2018/9/29.
 */
public class DefaultOnPullUpStatusChangeListener implements IOnPullStatusChangeListener {

    public View mContentView;
    public LinearLayout mLlFooterContainer;
    public ImageView mIvFooterProgress;
    public TextView mTvFooterTips;
    public Animation mAnimationRefreshing;

    public DefaultOnPullUpStatusChangeListener(Context context) {
        mContentView = LayoutInflater.from(context).inflate(R.layout.layout_pull_up_load_footer, null, false);
        mLlFooterContainer = mContentView.findViewById(R.id.ll_footer_container);
        mIvFooterProgress = mContentView.findViewById(R.id.iv_footer_progress);
        mTvFooterTips = mContentView.findViewById(R.id.tv_footer_tips);
        mAnimationRefreshing = PullUtils.buildAnimationRefreshing();
    }

    @Override
    public View getView() {
        return mContentView;
    }

    @Override
    public void onPullChanged(PullStatus status) {
        switch (status) {
            case CANCEL_REFRESH:
                break;
            case PULL_REFRESH:
                mIvFooterProgress.setVisibility(View.GONE);
                mTvFooterTips.setText("上拉加载");
                break;
            case RELEASE_TO_REFRESH:
                mIvFooterProgress.setVisibility(View.GONE);
                mTvFooterTips.setText("释放立即加载");
                break;
            case START_REFRESHING:
                mIvFooterProgress.startAnimation(mAnimationRefreshing);
                mIvFooterProgress.setVisibility(View.VISIBLE);
                mTvFooterTips.setText("正在加载");
                break;
            case STOP_REFRESH:
                mIvFooterProgress.clearAnimation();
                mIvFooterProgress.setVisibility(View.GONE);
                mTvFooterTips.setText("加载完成");
                break;
            default:
                break;
        }
    }

    @Override
    public void releaseAll() {
        mIvFooterProgress.clearAnimation();
    }
}
