package com.rhino.rv.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rhino.rv.demo.data.SimpleExpandHolderData;
import com.rhino.rv.SimpleGridSpan;
import com.rhino.rv.SimpleRecyclerAdapter;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.impl.IOnClickListener;
import com.rhino.rv.pull.DefaultOnPullDownStatusChangeListener;
import com.rhino.rv.pull.DefaultOnPullUpStatusChangeListener;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.rv.pull.impl.IOnPullStatusChangeListener;
import com.rhino.rv.swipe.BaseSwipeHolderData;
import com.rhino.rv.swipe.SwipeListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class PullRefreshActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private static final int GRID_SPAN_ITEM = 2;
    private List<BaseSwipeHolderData> mBaseHolderDataList = new ArrayList<>();
    private SimpleRecyclerAdapter mSimpleRecyclerAdapter;
    private PullRefreshLayout mPullRefreshLayout;

    private int mRootItemCount = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mPullRefreshLayout = findViewById(R.id.PullRefreshLayout);
        mPullRefreshLayout.setStyle(PullRefreshLayout.Style.PULL_REFRESH);
        mPullRefreshLayout.setOnPullDownStatusChangeListener(new DefaultOnPullDownStatusChangeListener(this) {

            @Override
            public void onPullChanged(PullStatus status) {
                super.onPullChanged(status);
                Log.d(TAG, "status = " + status);
                switch (status) {
                    case START_REFRESHING:
                        requestData();
                        break;
                    default:
                        break;
                }
            }
        });
        mPullRefreshLayout.setOnPullUpStatusChangeListener(new DefaultOnPullUpStatusChangeListener(this) {

            @Override
            public void onPullChanged(PullStatus status) {
                super.onPullChanged(status);
                Log.d(TAG, "status = " + status);
                switch (status) {
                    case START_REFRESHING:
                        requestData();
                        break;
                    default:
                        break;
                }
            }
        });
        mPullRefreshLayout.setAutoLoad(true);

        PullRecyclerView mPullRecyclerView = findViewById(R.id.PullRecyclerView);
        mSimpleRecyclerAdapter = new SwipeListAdapter();
        mPullRecyclerView.setAdapter(mSimpleRecyclerAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_ITEM);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new SimpleGridSpan(mSimpleRecyclerAdapter, GRID_SPAN_ITEM));
        mPullRecyclerView.setLayoutManager(gridLayoutManager);
        mPullRecyclerView.setBackgroundColor(Color.WHITE);

        mPullRecyclerView.addItemDecoration(new SimpleItemDecoration(getApplicationContext()));

        refreshList();
    }

    private void refreshList() {
        mBaseHolderDataList.clear();
        SimpleExpandHolderData root;
        for (int i = 0; i < mRootItemCount; i++) {
            root = new SimpleExpandHolderData();
            root.mItemSpanSize = GRID_SPAN_ITEM;
            root.mDesc = "item " + i;
            root.mItemClickListener = buildItemClickListener();
            mBaseHolderDataList.add(root);
        }
        mSimpleRecyclerAdapter.updateData(mBaseHolderDataList);
        mSimpleRecyclerAdapter.notifyDataSetChanged();
    }

    private IOnClickListener<SimpleExpandHolderData> mItemClickListener = null;
    private IOnClickListener<SimpleExpandHolderData> buildItemClickListener() {
        if (null == mItemClickListener) {
            mItemClickListener = new IOnClickListener<SimpleExpandHolderData>() {
                @Override
                public void onClick(View v, SimpleExpandHolderData data, int position) {
                    showToast(data.mDesc);
                }
            };
        }
        return mItemClickListener;
    }

    private boolean requestData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRootItemCount += 3;
                refreshList();
                mPullRefreshLayout.stopRefreshAndLoad();
            }
        }, 2000);
        return true;
    }

    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}