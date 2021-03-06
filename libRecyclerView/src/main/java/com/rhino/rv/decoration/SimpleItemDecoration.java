package com.rhino.rv.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.rhino.rv.base.BaseHolder;

/**
 * @author LuoLin
 * @since Create on 2017/6/16.
 */
public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    protected static final int DEVIATION = 5;
    protected static final int DF_LINE_COLOR = 0x22000000;
    protected static final int DF_LINE_WIDTH = 1;
    protected int mLineWidth;
    protected int mHorizontalLineWidth;
    protected int mVerticalLineWidth;
    protected float mHorizontalLineLengthScale;
    protected float mVerticalLineLengthScale;
    protected Paint mPaint;
    protected Path mPath;

    public SimpleItemDecoration(Context context) {
        this(context, DF_LINE_COLOR, (int) (context.getResources().getDisplayMetrics().density * DF_LINE_WIDTH));
    }

    public SimpleItemDecoration(Context context, int lineColor, int lineWidth) {
        this.mHorizontalLineLengthScale = 1.0f;
        this.mVerticalLineLengthScale = 1.0f;
        this.mLineWidth = lineWidth;
        this.mHorizontalLineWidth = mLineWidth;
        this.mVerticalLineWidth = mLineWidth;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(lineColor);
        this.mPath = new Path();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if (viewHolder instanceof BaseHolder && !((BaseHolder) viewHolder).getBindData().mDecorationEnable) {
            return;
        }
        outRect.set(mVerticalLineWidth / 2, mHorizontalLineWidth / 2, mVerticalLineWidth / 2, mHorizontalLineWidth / 2);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int relParentWidth = getContainerUsableWidth(parent);
        int relParentHeight = getContainerUsableHeight(parent);
        for (int i = 0, L = parent.getChildCount(); i < L; i++) {
            View child = parent.getChildAt(i);
            if (child == null) {
                continue;
            }
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(child);
            if (viewHolder instanceof BaseHolder && !((BaseHolder) viewHolder).getBindData().mDecorationEnable) {
                continue;
            }
            int childLeft = child.getLeft();
            int childRight = child.getRight();
            int childTop = child.getTop();
            int childBottom = child.getBottom();
            if (DEVIATION < relParentWidth - childRight - mLineWidth / 2) {
                float margin = (1 - mVerticalLineLengthScale) * child.getHeight() / 2;
                mPath.reset();
                mPath.moveTo(childRight + mLineWidth / 2, childTop + margin);
                mPath.lineTo(childRight + mLineWidth / 2, childBottom - margin);
                mPaint.setStrokeWidth(mVerticalLineWidth);
                c.drawPath(mPath, mPaint);
            }
            if (DEVIATION < relParentHeight - childBottom - mLineWidth / 2) {
                float margin = (1 - mHorizontalLineLengthScale) * child.getWidth() / 2;
                margin = 0 == margin ? -mLineWidth / 2 : margin;
                mPath.reset();
                mPath.moveTo(childLeft + margin, childBottom + mLineWidth / 2);
                mPath.lineTo(childRight - margin, childBottom + mLineWidth / 2);
                mPaint.setStrokeWidth(mHorizontalLineWidth);
                c.drawPath(mPath, mPaint);
            }
        }
    }

    /**
     * Get the usable width of container.
     *
     * @param v View
     * @return width
     */
    public int getContainerUsableWidth(View v) {
        return v.getWidth() - v.getPaddingLeft() - v.getPaddingRight();
    }

    /**
     * Get the usable height of container.
     *
     * @param v View
     * @return height
     */
    public int getContainerUsableHeight(View v) {
        return v.getHeight() - v.getPaddingTop() - v.getPaddingBottom();
    }

    /**
     * Set the line color.
     *
     * @param color the line color.
     */
    public void setLineColor(@ColorInt int color) {
        this.mPaint.setColor(color);
    }

    /**
     * Set the line width.
     *
     * @param width the line width.(px)
     */
    public void setLineWidth(int width) {
        this.mLineWidth = width;
        this.mHorizontalLineWidth = width;
        this.mVerticalLineWidth = width;
    }

    /**
     * Set the horizontal line width.
     *
     * @param width the horizontal line width.(px)
     */
    public void setHorizontalLineWidth(int width) {
        this.mHorizontalLineWidth = width;
    }

    /**
     * Set the vertical line width.
     *
     * @param width the vertical line width.(px)
     */
    public void setVerticalLineWidth(int width) {
        this.mVerticalLineWidth = width;
    }

    /**
     * Set the horizontal line length scale.
     *
     * @param scale the horizontal line length scale.
     */
    public void setHorizontalLineLengthScale(float scale) {
        mHorizontalLineLengthScale = scale;
    }

    /**
     * Set the vertical line length scale.
     *
     * @param scale the vertical line length scale.
     */
    public void setVerticalLineLengthScale(float scale) {
        mVerticalLineLengthScale = scale;
    }

    /**
     * setPathEffect(new DashPathEffect(new float[]{15,15,15,15},5));
     *
     * @param effect PathEffect
     */
    public void setPathEffect(PathEffect effect) {
        this.mPaint.setPathEffect(effect);
    }
}