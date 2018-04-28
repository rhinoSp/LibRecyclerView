package com.rhino.librecyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.rhino.librecyclerview.data.SingleTextData;
import com.rhino.rv.SimpleGridSpan;
import com.rhino.rv.SimpleRecyclerAdapter;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.librecyclerview.data.SingleButtonData;
import com.rhino.librecyclerview.data.SingleEditData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class MixItemActivity extends AppCompatActivity {

    private static final int GRID_SPAN_ITEM = 15;
    private static final int GRID_SPAN_ITEM_1IN3 = 5;
    private static final int GRID_SPAN_ITEM_1IN5 = 3;
    private List<BaseHolderData> mBaseHolderDataList = new ArrayList<>();
    private SimpleRecyclerAdapter mSimpleRecyclerAdapter;


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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_ITEM);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new SimpleGridSpan(mSimpleRecyclerAdapter, GRID_SPAN_ITEM));
        mPullRecyclerView.setLayoutManager(gridLayoutManager);
        mPullRecyclerView.addItemDecoration(new SimpleItemDecoration(getApplicationContext()));

        refreshList();
    }

    private void refreshList() {

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

        mBaseHolderDataList.add(buildEditData());
        mBaseHolderDataList.add(buildEditData());
        mBaseHolderDataList.add(buildEditData());

        mBaseHolderDataList.add(buildButtonData());

        mSimpleRecyclerAdapter.updateData(mBaseHolderDataList);
        mSimpleRecyclerAdapter.notifyDataSetChanged();
    }

    private SingleTextData buildTextData() {
        SingleTextData data = new SingleTextData();
        data.mDesc = "铺满";
        data.mItemSpanSize = GRID_SPAN_ITEM;
        return data;
    }

    private SingleTextData buildTextData1IN3() {
        SingleTextData data = new SingleTextData();
        data.mDesc = "三分之一";
        data.mItemSpanSize = GRID_SPAN_ITEM_1IN3;
        return data;
    }

    private SingleTextData buildTextData1IN5() {
        SingleTextData data = new SingleTextData();
        data.mDesc = "五分之一";
        data.mItemSpanSize = GRID_SPAN_ITEM_1IN5;
        return data;
    }

    private SingleEditData buildEditData() {
        SingleEditData data = new SingleEditData();
        data.mItemSpanSize = GRID_SPAN_ITEM;
        return data;
    }

    private SingleButtonData buildButtonData() {
        SingleButtonData data = new SingleButtonData();
        data.mItemSpanSize = GRID_SPAN_ITEM;
        return data;
    }



}
