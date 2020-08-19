package com.rhino.rv.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.rhino.rv.SimpleRecyclerAdapter;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.impl.IOnClickListener;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.rv.demo.data.SingleTextData;

import java.util.ArrayList;
import java.util.List;


/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class MainActivity extends AppCompatActivity {

    private List<BaseHolderData> mBaseHolderDataList = new ArrayList<>();
    private SimpleRecyclerAdapter mSimpleRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PullRefreshLayout mPullRefreshLayout = findViewById(R.id.PullRefreshLayout);
        mPullRefreshLayout.setStyle(PullRefreshLayout.Style.FLEXIBLE);
        mPullRefreshLayout.setPullMaxDistance(-1);

        PullRecyclerView mPullRecyclerView = findViewById(R.id.PullRecyclerView);
        mPullRecyclerView.setBackgroundColor(Color.WHITE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mPullRecyclerView.setLayoutManager(linearLayoutManager);

        mSimpleRecyclerAdapter = new SimpleRecyclerAdapter();
        mPullRecyclerView.setAdapter(mSimpleRecyclerAdapter);

        mPullRecyclerView.addItemDecoration(new SimpleItemDecoration(getApplicationContext()));

        refreshList();
    }

    private void refreshList() {
        mBaseHolderDataList.add(buildItemData("左划菜单", SwipeDeleteActivity.class));
        mBaseHolderDataList.add(buildItemData("下拉刷新，上拉加载", PullRefreshActivity.class));
        mBaseHolderDataList.add(buildItemData("无限层级展开", SimpleExpandActivity.class));
        mBaseHolderDataList.add(buildItemData("RecyclerView实现ViewPager", ViewPagerActivity.class));
        mBaseHolderDataList.add(buildItemData("混合布局Item", MixItemActivity.class));

        mSimpleRecyclerAdapter.updateData(mBaseHolderDataList);
        mSimpleRecyclerAdapter.notifyDataSetChanged();
    }


    private SingleTextData buildItemData(String desc, Class<?> activityCls){
        return buildItemData(desc, activityCls, null);
    }

    private SingleTextData buildItemData(String desc, IOnClickListener<SingleTextData> onClickListener){
        return buildItemData(desc, null, onClickListener);
    }

    private SingleTextData buildItemData(String desc, Class<?> activityCls, IOnClickListener<SingleTextData> onClickListener){
        SingleTextData data = new SingleTextData();
        data.mRandom = false;
        data.mDesc = desc;
        data.mActivityCls = activityCls;
        data.mOnClickListener = onClickListener;
        data.mItemClickListener = buildItemNormalClickListener();
        return data;
    }

    private IOnClickListener<SingleTextData> mIOnClickListener;
    private IOnClickListener<SingleTextData> buildItemNormalClickListener(){
        if(null == mIOnClickListener){
            mIOnClickListener = new IOnClickListener<SingleTextData>() {
                @Override
                public void onClick(View v, SingleTextData data, int position) {
                    if(null != data.mOnClickListener){
                        data.mOnClickListener.onClick(v, data, position);
                    } else if(null != data.mActivityCls){
                        showActivityPage(data.mActivityCls, data.mDesc);
                    }
                }
            };
        }
        return mIOnClickListener;
    }

    private void showActivityPage(Class<?> cls, String title){
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.putExtra("title", title);
        startActivity(intent);
    }


}