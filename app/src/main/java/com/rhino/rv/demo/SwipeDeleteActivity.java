package com.rhino.rv.demo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.rhino.rv.demo.utils.ColorUtils;
import com.rhino.rv.demo.utils.DrawableUtils;
import com.rhino.rv.SimpleGridSpan;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.impl.IOnClickListener;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.rv.swipe.BaseSwipeHolderData;
import com.rhino.rv.swipe.SwipeListAdapter;
import com.rhino.rv.swipe.SwipeMenuItem;
import com.rhino.rv.swipe.impl.IOnSwipeMenuItemClickListener;
import com.rhino.rv.demo.data.SimpleExpandHolderData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/4/20.
 */
public class SwipeDeleteActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private static final int GRID_SPAN_ITEM = 2;
    private List<BaseSwipeHolderData> mBaseHolderDataList = new ArrayList<>();
    private SwipeListAdapter mSwipeListAdapter;
    private PullRefreshLayout mPullRefreshLayout;

    private int mRootItemCount = 19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        for (int i = 0; i < mRootItemCount; i++) {
            root = new SimpleExpandHolderData();
            root.mItemSpanSize = GRID_SPAN_ITEM;
            root.mDesc = "item " + i;
            root.mShowArrow = true;
            root.mSwipeMenuItemClickListener = buildSwipeMenuItemClickListener();

            root.mSwipeMenuList = new ArrayList<>();
            SwipeMenuItem item = new SwipeMenuItem();
            item.setWidth(210);
            item.setBackgroundDrawable(DrawableUtils.buildColorStateListDrawable(0, 0,
                    ColorUtils.alphaColor(0.5f, Color.GRAY), Color.GRAY));
            item.setIconWidth(90);
            item.setIconHeight(90);
            item.setText("edit");
            root.mSwipeMenuList.add(item);

            item = new SwipeMenuItem();
            item.setWidth(210);
            item.setBackgroundDrawable(DrawableUtils.buildColorStateListDrawable(0, 0,
                    ColorUtils.alphaColor(0.5f, Color.RED), Color.RED));
            item.setIconDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            item.setIconColor(0);
            item.setText("delete");
            root.mSwipeMenuList.add(item);

            root.mItemClickListener = buildItemClickListener();
            mBaseHolderDataList.add(root);
        }
        mSwipeListAdapter.updateData(mBaseHolderDataList);
        mSwipeListAdapter.notifyDataSetChanged();
    }

    private IOnSwipeMenuItemClickListener<SimpleExpandHolderData> mSwipeMenuItemClickListener = null;

    private IOnSwipeMenuItemClickListener<SimpleExpandHolderData> buildSwipeMenuItemClickListener() {
        if (null == mSwipeMenuItemClickListener) {
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
                    showToast(data.mDesc);
                }
            };
        }
        return mItemClickListener;
    }

    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
