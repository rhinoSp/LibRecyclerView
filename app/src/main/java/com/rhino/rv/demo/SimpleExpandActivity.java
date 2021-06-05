package com.rhino.rv.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhino.rv.SimpleGridSpan;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.demo.data.SimpleExpandHolderData;
import com.rhino.rv.demo.data.SimpleExpandHolderData1;
import com.rhino.rv.impl.IOnClickListener;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.rv.swipe.BaseSwipeHolderData;
import com.rhino.rv.swipe.SwipeListAdapter;
import com.rhino.rv.tree.BaseTreeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class SimpleExpandActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private static final int GRID_SPAN_ITEM = 2;
    private List<BaseSwipeHolderData> mBaseHolderDataList = new ArrayList<>();
    private SwipeListAdapter mSwipeListAdapter;
    private PullRefreshLayout mPullRefreshLayout;

    private int mRootItemCount = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_expand_activity);
        initView();
    }

    private void initView() {
        mPullRefreshLayout = findViewById(R.id.PullRefreshLayout);
        mPullRefreshLayout.setStyle(PullRefreshLayout.Style.FLEXIBLE);

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
        SimpleExpandHolderData1 child2;
        for (int i = 0; i < mRootItemCount; i++) {
            root = new SimpleExpandHolderData();
            root.mItemSpanSize = GRID_SPAN_ITEM;
            root.mDesc = "item " + i;

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
                    child2 = new SimpleExpandHolderData1();
                    child2.mDesc = "child " + i + " " + j + " " + k;
                    child2.setDepth(3);
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

    public void expandAll(View v) {
        mSwipeListAdapter.expandAll(false);
    }

    public void collapseAll(View v) {
        mSwipeListAdapter.collapseAll(false);
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
                        if (data.isExpanded()) {
                            data.collapse(false);
                        } else {
                            data.expand(false);
                        }
                    }
                }
            };
        }
        return mItemClickListener;
    }

    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}