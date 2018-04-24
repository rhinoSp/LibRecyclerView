package com.rhino.librecyclerview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.rhino.librecyclerview.data.SimpleExpandHolderData;
import com.rhino.rv.SimpleGridSpan;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.impl.IOnClickListener;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.rv.pull.impl.IOnPullStatusChangeListener;
import com.rhino.rv.swipe.BaseSwipeHolderData;
import com.rhino.rv.swipe.SwipeListAdapter;
import com.rhino.rv.swipe.SwipeMenuItem;
import com.rhino.rv.swipe.impl.IOnSwipeMenuItemClickListener;
import com.rhino.rv.tree.BaseTreeData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private static final int GRID_SPAN_ITEM = 2;
    private List<BaseSwipeHolderData> mBaseHolderDataList = new ArrayList<>();
    private SwipeListAdapter mSwipeListAdapter;
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
        mPullRefreshLayout.setOnPullDownStatusChangeListener(mOnPullDownStatusChangeListener);
        mPullRefreshLayout.setOnPullUpStatusChangeListener(mOnPullUpStatusChangeListener);
        mPullRefreshLayout.setAutoLoad(true);
        mPullRefreshLayout.setBackgroundColor(Color.BLACK);

        PullRecyclerView mPullRecyclerView = findViewById(R.id.PullRecyclerView);
        mSwipeListAdapter = new SwipeListAdapter();
        mPullRecyclerView.setAdapter(mSwipeListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_ITEM);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new SimpleGridSpan(mSwipeListAdapter, GRID_SPAN_ITEM));
        mPullRecyclerView.setLayoutManager(gridLayoutManager);
        mPullRecyclerView.setBackgroundColor(Color.WHITE);

        mPullRecyclerView.addItemDecoration(new SimpleItemDecoration(getApplicationContext()));

        refreshList();
    }

    private void refreshList() {
        mBaseHolderDataList.clear();
        SimpleExpandHolderData root;
        SimpleExpandHolderData child1;
        SimpleExpandHolderData child2;
        for (int i = 0; i < mRootItemCount; i++) {
            root = new SimpleExpandHolderData();
            root.mItemSpanSize = GRID_SPAN_ITEM;
            root.mDesc = "item " + i;
            root.mSwipeMenuItemClickListener = buildSwipeMenuItemClickListener();

            root.mSwipeMenuList = new ArrayList<>();
            SwipeMenuItem item = new SwipeMenuItem();
            item.setWidth(210);
            item.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
            item.setIconWidth(90);
            item.setIconHeight(90);
            item.setText("edit");
            root.mSwipeMenuList.add(item);

            item = new SwipeMenuItem();
            item.setWidth(210);
            item.setBackgroundDrawable(new ColorDrawable(Color.RED));
//            item.setIconDrawable(getDrawableById(R.drawable.ic_launcher));
//            item.setIconWidth(90);
//            item.setIconHeight(90);
            item.setText("delete");
            root.mSwipeMenuList.add(item);

            root.setDepth(1);
            List<BaseTreeData> childList1 = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                child1 = new SimpleExpandHolderData();
                child1.mItemSpanSize = GRID_SPAN_ITEM;
                child1.mDesc = "child " + i + " " + j;
                child1.setDepth(2);
                child1.mItemClickListener = buildItemClickListener();
                childList1.add(child1);
                List<BaseTreeData> childList2 = new ArrayList<>();
                for (int k = 0; k < 5; k++) {
                    child2 = new SimpleExpandHolderData();
                    child2.mDesc = "child " + i + " " + j + " " + k;
                    child2.setDepth(3);
                    child2.mItemClickListener = buildItemClickListener();
                    childList2.add(child2);
                }
                child1.setChildList(childList2);
            }
            root.setChildList(childList1);
            root.mItemClickListener = buildItemClickListener();
            mBaseHolderDataList.add(root);
        }
        mSwipeListAdapter.updateData(mBaseHolderDataList);
        mSwipeListAdapter.notifyDataSetChanged();
    }

    private IOnSwipeMenuItemClickListener<SimpleExpandHolderData> mSwipeMenuItemClickListener = null;
    private IOnSwipeMenuItemClickListener<SimpleExpandHolderData> buildSwipeMenuItemClickListener(){
        if(null == mSwipeMenuItemClickListener) {
            mSwipeMenuItemClickListener = new IOnSwipeMenuItemClickListener<SimpleExpandHolderData>() {
                @Override
                public void onItemClick(SimpleExpandHolderData data, int position, SwipeMenuItem item) {
                    mSwipeListAdapter.closeAllSwipeMenu();
                    showToast(data.mDesc + ", " + position + ", " + item.getText());
                }
            };
        }
        return mSwipeMenuItemClickListener;
    }

    private IOnClickListener<SimpleExpandHolderData> mItemClickListener = null;
    private IOnClickListener<SimpleExpandHolderData> buildItemClickListener() {
        if (null == mItemClickListener) {
            mItemClickListener = new IOnClickListener<SimpleExpandHolderData>() {
                @Override
                public void onClick(View v, SimpleExpandHolderData data, int position) {
                    if (data.isLeaf()) {
                        showToast(data.mDesc);
                    } else {
                        data.doExpand(!data.isExpand());
                    }
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

    private IOnPullStatusChangeListener mOnPullDownStatusChangeListener = new IOnPullStatusChangeListener() {

        private View mPullHeaderView;
        private ProgressBar mPullHeaderProgress;
        private TextView mPullHeaderText;

        @Override
        public View getView() {
            mPullHeaderView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_pull_down_refresh_header, null, false);
            mPullHeaderProgress = mPullHeaderView.findViewById(R.id.header_progress);
            mPullHeaderText = mPullHeaderView.findViewById(R.id.header_txt);
            return mPullHeaderView;
        }

        @Override
        public void onPullChanged(PullStatus status) {
            Log.d(TAG, "status = " + status);
            switch (status) {
                case CANCEL_REFRESH:
                    break;
                case PULL_REFRESH:
                    mPullHeaderProgress.setVisibility(View.GONE);
                    mPullHeaderText.setText("Pull down refresh");
                    break;
                case RELEASE_TO_REFRESH:
                    mPullHeaderProgress.setVisibility(View.GONE);
                    mPullHeaderText.setText("Release to refresh");
                    break;
                case START_REFRESHING:
                    requestData();
                    mPullHeaderProgress.setVisibility(View.VISIBLE);
                    mPullHeaderText.setText("Refreshing");
                    break;
                case STOP_REFRESH:
                    mPullHeaderProgress.setVisibility(View.GONE);
                    mPullHeaderText.setText("Stop refresh");
                    break;
                default:
                    break;
            }
        }
    };

    private IOnPullStatusChangeListener mOnPullUpStatusChangeListener = new IOnPullStatusChangeListener() {

        private View mPullFooterView;
        private ProgressBar mPullFooterProgress;
        private TextView mPullFooterText;

        @Override
        public View getView() {
            mPullFooterView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_pull_up_load_footer, null, false);
            mPullFooterProgress = mPullFooterView.findViewById(R.id.footer_progress);
            mPullFooterText = mPullFooterView.findViewById(R.id.footer_txt);
            return mPullFooterView;
        }

        @Override
        public void onPullChanged(PullStatus status) {
            Log.d(TAG, "status = " + status);
            switch (status) {
                case CANCEL_REFRESH:
                    break;
                case PULL_REFRESH:
                    mPullFooterProgress.setVisibility(View.GONE);
                    mPullFooterText.setText("Pull up refresh");
                    break;
                case RELEASE_TO_REFRESH:
                    mPullFooterProgress.setVisibility(View.GONE);
                    mPullFooterText.setText("Release to refresh");
                    break;
                case START_REFRESHING:
                    requestData();
                    mPullFooterProgress.setVisibility(View.VISIBLE);
                    mPullFooterText.setText("Refreshing");
                    break;
                case STOP_REFRESH:
                    mPullFooterProgress.setVisibility(View.GONE);
                    mPullFooterText.setText("Stop refresh");
                    break;
                default:
                    break;
            }
        }
    };

    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
