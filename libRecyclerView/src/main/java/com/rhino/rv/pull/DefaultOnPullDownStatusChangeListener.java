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
public class DefaultOnPullDownStatusChangeListener implements IOnPullStatusChangeListener {

    public View mContentView;
    public LinearLayout mLlHeaderContainer;
    public ImageView mIvHeaderProgress;
    public TextView mTvPullHeaderTips;
    public Animation mAnimationRefreshing;

    public DefaultOnPullDownStatusChangeListener(Context context) {
        mContentView = LayoutInflater.from(context).inflate(R.layout.layout_pull_down_refresh_header, null, false);
        mLlHeaderContainer = mContentView.findViewById(R.id.ll_header_container);
        mIvHeaderProgress = mContentView.findViewById(R.id.iv_header_progress);
        mTvPullHeaderTips = mContentView.findViewById(R.id.tv_header_tips);
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
                mIvHeaderProgress.clearAnimation();
                break;
            case PULL_REFRESH:
                mIvHeaderProgress.setVisibility(View.GONE);
                mTvPullHeaderTips.setText("下拉刷新");
                break;
            case RELEASE_TO_REFRESH:
                mIvHeaderProgress.setVisibility(View.GONE);
                mTvPullHeaderTips.setText("释放立即刷新");
                break;
            case START_REFRESHING:
                mIvHeaderProgress.startAnimation(mAnimationRefreshing);
                mIvHeaderProgress.setVisibility(View.VISIBLE);
                mTvPullHeaderTips.setText("正在刷新");
                break;
            case STOP_REFRESH:
                mIvHeaderProgress.clearAnimation();
                mIvHeaderProgress.setVisibility(View.GONE);
                mTvPullHeaderTips.setText("刷新完成");
                break;
            default:
                break;
        }
    }

    @Override
    public void releaseAll() {
        mIvHeaderProgress.clearAnimation();
    }
}
