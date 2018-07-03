package com.rhino.rv.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * @author LuoLin
 * @since Create on 2017/8/26.
 */
public class GridItemDecoration extends SimpleItemDecoration {

    private int mColumnCount;
    private int mRowCount;

    public GridItemDecoration(Context context) {
        super(context);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int relWidth = getContainerUsableWidth(parent);
        int relHeight = getContainerUsableHeight(parent);
        float rowGrid = (float)relHeight / mRowCount;
        float columnGrid = (float)relWidth / mColumnCount;
        for (int i = 0, L = mRowCount * mColumnCount; i < L; i++) {
            float childLeft = i % mColumnCount * columnGrid;
            float childRight = childLeft + columnGrid;
            float childTop = i / mColumnCount * rowGrid;
            float childBottom = childTop + rowGrid;

            if(DEVIATION < relWidth - childRight){
                float margin = (1 - mVerticalLineLengthScale) * rowGrid / 2;
                mPath.reset();
                mPath.moveTo(childRight, childTop + margin);
                mPath.lineTo(childRight, childBottom - margin);
                c.drawPath(mPath, mPaint);
            }

            if(DEVIATION < relHeight - childBottom){
                float margin = (1 - mHorizontalLineLengthScale) * columnGrid / 2;
                margin = 0 == margin ? mLineWidth / 2 : margin;
                mPath.reset();
                mPath.moveTo(childLeft + margin, childBottom);
                mPath.lineTo(childRight - margin, childBottom);
                c.drawPath(mPath, mPaint);
            }
        }
    }

    /**
     * Set the number of column count.
     * @param columnCount the number of column count.
     */
    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
    }

    /**
     * Set the number of row count.
     * @param rowCount the number of row count.
     */
    public void setRowCount(int rowCount) {
        this.mRowCount = rowCount;
    }
}