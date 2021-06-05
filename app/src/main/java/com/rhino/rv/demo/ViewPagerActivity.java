package com.rhino.rv.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rhino.rv.SimpleRecyclerAdapter;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.decoration.GridItemDecoration;
import com.rhino.rv.demo.data.SingleTextData;
import com.rhino.rv.demo.view.CircleIndicator;
import com.rhino.rv.manager.GridLayoutManager;
import com.rhino.rv.pager.PagerScrollHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class ViewPagerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CircleIndicator mCircleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout mRelativeLayout = new RelativeLayout(this);
        mRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRelativeLayout.setBackgroundColor(Color.WHITE);
        mRelativeLayout.setPadding(30, 30, 30, 30);

        mRecyclerView = new RecyclerView(this);
        mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRelativeLayout.addView(mRecyclerView);

        mCircleIndicator = new CircleIndicator(this);
        mCircleIndicator.setCount(5);
        mCircleIndicator.setMode(CircleIndicator.Mode.OUTSIDE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mRelativeLayout.addView(mCircleIndicator, lp);

        setContentView(mRelativeLayout);

        init();

    }

    private void init() {
        PagerScrollHelper mPagerScrollHelper = new PagerScrollHelper(mRecyclerView);
        mPagerScrollHelper.setOnPageChangeListener(new PagerScrollHelper.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCircleIndicator.setPosition(position);
            }
        });

        SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter();
        mRecyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(4, 3);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        GridItemDecoration decoration = new GridItemDecoration(getApplicationContext());
//        decoration.setPathEffect(new DashPathEffect(new float[]{15,15,15,15},5));
//        decoration.setHorizontalLineLengthScale(0.6f);
//        decoration.setVerticalLineLengthScale(0.6f);
        decoration.setLineWidth(1);
        decoration.setRowCount(4);
        decoration.setColumnCount(3);
        mRecyclerView.addItemDecoration(decoration);

        List<BaseHolderData> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            SingleTextData d = new SingleTextData();
            d.mDesc = "item " + i;
            list.add(d);
        }
        adapter.updateData(list);
    }


}
