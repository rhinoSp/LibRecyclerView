package com.rhino.rv.demo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.rhino.rv.demo.data.SingleTextData;
import com.rhino.rv.SimpleGridSpan;
import com.rhino.rv.SimpleRecyclerAdapter;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.rv.demo.data.SingleButtonData;
import com.rhino.rv.demo.data.SingleEditData;
import com.rhino.rv.recvitem.RecvItemEmptyContent;
import com.rhino.rv.recvitem.RecvItemEmptySpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class MixItemActivity extends AppCompatActivity {

    private List<BaseHolderData> mBaseHolderDataList = new ArrayList<>();
    private SimpleRecyclerAdapter mSimpleRecyclerAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PullRefreshLayout mPullRefreshLayout = findViewById(R.id.PullRefreshLayout);
        mPullRefreshLayout.setStyle(PullRefreshLayout.Style.FLEXIBLE);
        mPullRefreshLayout.setPullMaxDistance(-1);

        PullRecyclerView mPullRecyclerView = findViewById(R.id.PullRecyclerView);
        mPullRecyclerView.setBackgroundColor(Color.WHITE);

        mSimpleRecyclerAdapter = new SimpleRecyclerAdapter();
        mPullRecyclerView.setAdapter(mSimpleRecyclerAdapter);

        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), -1);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mGridLayoutManager.setSpanSizeLookup(new SimpleGridSpan(mSimpleRecyclerAdapter, -1));
        mPullRecyclerView.setLayoutManager(mGridLayoutManager);
        mPullRecyclerView.addItemDecoration(new SimpleItemDecoration(getApplicationContext()));

        refreshList();
    }

    private void refreshList() {

        mBaseHolderDataList.add(buildTextData());

        mBaseHolderDataList.add(buildTextData1IN2());
        mBaseHolderDataList.add(buildTextData1IN2());

        mBaseHolderDataList.add(buildTextData());

        mBaseHolderDataList.add(buildTextData1IN3());
        mBaseHolderDataList.add(buildTextData1IN3());
        mBaseHolderDataList.add(buildTextData1IN3());
        mBaseHolderDataList.add(buildTextData1IN3());

        mBaseHolderDataList.add(buildTextData());

        mBaseHolderDataList.add(buildEditData());
        mBaseHolderDataList.add(buildEditData());

        mBaseHolderDataList.add(buildTextData());
        mBaseHolderDataList.add(buildTextData());

        mBaseHolderDataList.add(buildTextData1IN5());
        mBaseHolderDataList.add(buildTextData1IN5());
        mBaseHolderDataList.add(buildTextData1IN5());
        mBaseHolderDataList.add(buildTextData1IN5());
        mBaseHolderDataList.add(buildTextData1IN5());
        mBaseHolderDataList.add(buildTextData1IN5());
        mBaseHolderDataList.add(buildTextData1IN5());
        mBaseHolderDataList.add(buildTextData1IN5());

        mBaseHolderDataList.add(buildTextData());

        mBaseHolderDataList.add(buildTextData3IN5());
        mBaseHolderDataList.add(buildTextData3IN5());

        mBaseHolderDataList.add(buildTextData());

        mBaseHolderDataList.add(buildTextData1IN7());
        mBaseHolderDataList.add(buildTextData1IN7());
        mBaseHolderDataList.add(buildTextData1IN7());
        mBaseHolderDataList.add(buildTextData1IN7());
        mBaseHolderDataList.add(buildTextData1IN7());
        mBaseHolderDataList.add(buildTextData1IN7());
        mBaseHolderDataList.add(buildTextData1IN7());

        mBaseHolderDataList.add(RecvItemEmptySpace.buildEmptySpace(40, new ColorDrawable(0xff333333)));
        mBaseHolderDataList.add(RecvItemEmptyContent.buildEmptyContent(findViewById(R.id.PullRecyclerView)));
        mBaseHolderDataList.add(RecvItemEmptySpace.buildEmptySpace(40, new ColorDrawable(0xffff3333)));

        mBaseHolderDataList.add(buildEditData());
        mBaseHolderDataList.add(buildEditData());
        mBaseHolderDataList.add(buildEditData());

        mBaseHolderDataList.add(buildButtonData());

        mSimpleRecyclerAdapter.setGridLayoutManagerSpanCount(mBaseHolderDataList, mGridLayoutManager);
        mSimpleRecyclerAdapter.updateDataAndNotify(mBaseHolderDataList);
    }

    private SingleTextData buildTextData() {
        SingleTextData data = new SingleTextData();
        data.mItemSpanSize = -1;
        return data;
    }

    private SingleTextData buildTextData1IN2() {
        SingleTextData data = new SingleTextData();
        data.mItemSpanSizeScaleDenominator = 2;
        return data;
    }

    private SingleTextData buildTextData1IN3() {
        SingleTextData data = new SingleTextData();
        data.mItemSpanSizeScaleDenominator = 3;
        return data;
    }

    private SingleTextData buildTextData1IN5() {
        SingleTextData data = new SingleTextData();
        data.mItemSpanSizeScaleDenominator = 5;
        return data;
    }

    private SingleTextData buildTextData3IN5() {
        SingleTextData data = new SingleTextData();
        data.mItemSpanSizeScaleNumerator = 3;
        data.mItemSpanSizeScaleDenominator = 5;
        return data;
    }

    private SingleTextData buildTextData1IN7() {
        SingleTextData data = new SingleTextData();
        data.mItemSpanSizeScaleNumerator = 1;
        data.mItemSpanSizeScaleDenominator = 7;
        return data;
    }

    private SingleEditData buildEditData() {
        SingleEditData data = new SingleEditData();
        data.mItemSpanSize = -1;
        return data;
    }

    private SingleButtonData buildButtonData() {
        SingleButtonData data = new SingleButtonData();
        data.mItemSpanSize = -1;
        return data;
    }


}
