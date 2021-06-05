package com.rhino.rv;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.decoration.SimpleItemDecoration;
import com.rhino.rv.pull.DefaultOnPullDownStatusChangeListener;
import com.rhino.rv.pull.DefaultOnPullUpStatusChangeListener;
import com.rhino.rv.pull.PullRecyclerView;
import com.rhino.rv.pull.PullRefreshLayout;
import com.rhino.rv.pull.impl.IOnPullStatusChangeListener;
import com.rhino.rv.recvitem.RecvItemEmptyContent;
import com.rhino.rv.tree.TreeRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rhino
 * @since Create on 2020/12/11.
 **/
public class RecyclerViewHelper {

    private static final int DEFAULT_FIRST_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * pageSize
     */
    public int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * pageNum
     */
    public int pageNum = DEFAULT_FIRST_PAGE_NUM;
    /**
     * IOnPullStatusChangeListener
     */
    public IOnPullStatusChangeListener onPullDownStatusChangeListener;
    /**
     * IOnPullStatusChangeListener
     */
    public IOnPullStatusChangeListener onPullUpStatusChangeListener;
    /**
     * PullRefreshLayout
     */
    public PullRefreshLayout pullRefreshLayout;
    /**
     * PullRecyclerView
     */
    public PullRecyclerView recyclerView;
    /**
     * SimpleRecyclerAdapter
     */
    public TreeRecyclerAdapter adapter;
    /**
     * SimpleItemDecoration
     */
    public RecyclerView.ItemDecoration itemDecoration;
    /**
     * BaseRecvHolderData list
     */
    public List<BaseHolderData> holderDataList = new ArrayList<>();
    /**
     * 加载监听
     */
    public OnPullRefreshListener onPullRefreshListener;


    public RecyclerViewHelper(PullRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerViewHelper(PullRecyclerView recyclerView, PullRefreshLayout pullRefreshLayout) {
        this(recyclerView);
        this.pullRefreshLayout = pullRefreshLayout;
        onPullDownStatusChangeListener = new DefaultOnPullDownStatusChangeListener(recyclerView.getContext()) {
            @Override
            public void onPullChanged(PullStatus status) {
                super.onPullChanged(status);
                if (status == PullStatus.START_REFRESHING) {
                    this.mIvHeaderProgress.startAnimation(this.mAnimationRefreshing);
                    this.mIvHeaderProgress.setVisibility(View.VISIBLE);
                    this.mTvPullHeaderTips.setText("正在刷新");
                    loadFirstPageData(false);
                }
            }
        };
        onPullUpStatusChangeListener = new DefaultOnPullUpStatusChangeListener(recyclerView.getContext()) {
            @Override
            public void onPullChanged(PullStatus status) {
                super.onPullChanged(status);
                if (status == PullStatus.START_REFRESHING) {
                    this.mIvFooterProgress.startAnimation(this.mAnimationRefreshing);
                    this.mIvFooterProgress.setVisibility(View.VISIBLE);
                    this.mTvFooterTips.setText("正在加载");
                    loadNextPageData();
                }
            }
        };
    }

    /**
     * 初始化普通列表
     */
    public void initRecyclerView() {
        adapter = new TreeRecyclerAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (pullRefreshLayout != null) {
            pullRefreshLayout.setStyle(PullRefreshLayout.Style.FLEXIBLE);
        }
    }

    /**
     * 初始化网格列表
     */
    public void initGridRecyclerView(int gridCount) {
        adapter = new TreeRecyclerAdapter();
        GridLayoutManager manager = new GridLayoutManager(recyclerView.getContext(), gridCount, GridLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(new SimpleGridSpan(adapter, gridCount));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (pullRefreshLayout != null) {
            pullRefreshLayout.setStyle(PullRefreshLayout.Style.FLEXIBLE);
        }
    }

    /**
     * 初始化瀑布流列表
     */
    public void initStaggeredGridRecyclerView(int gridCount) {
        adapter = new TreeRecyclerAdapter();
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(gridCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        if (pullRefreshLayout != null) {
            pullRefreshLayout.setStyle(PullRefreshLayout.Style.FLEXIBLE);
        }
    }

    /**
     * 初始化下拉刷新列表
     */
    public void initPullRefreshRecyclerView(OnPullRefreshListener onPullRefreshListener) {
        this.onPullRefreshListener = onPullRefreshListener;
        adapter = new TreeRecyclerAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (pullRefreshLayout != null) {
            pullRefreshLayout.setStyle(PullRefreshLayout.Style.PULL_REFRESH);
            pullRefreshLayout.setOnPullDownStatusChangeListener(onPullDownStatusChangeListener);
            pullRefreshLayout.setOnPullUpStatusChangeListener(onPullUpStatusChangeListener);
        }
    }

    /**
     * 初始化下拉刷新列表
     */
    public void initGridPullRefreshRecyclerView(int gridCount, OnPullRefreshListener onPullRefreshListener) {
        this.onPullRefreshListener = onPullRefreshListener;
        adapter = new TreeRecyclerAdapter();
        GridLayoutManager manager = new GridLayoutManager(recyclerView.getContext(), gridCount, GridLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(new SimpleGridSpan(adapter, gridCount));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (pullRefreshLayout != null) {
            pullRefreshLayout.setStyle(PullRefreshLayout.Style.PULL_REFRESH);
            pullRefreshLayout.setOnPullDownStatusChangeListener(onPullDownStatusChangeListener);
            pullRefreshLayout.setOnPullUpStatusChangeListener(onPullUpStatusChangeListener);
        }
    }

    /**
     * 初始化瀑布流列表
     */
    public void initStaggeredGridRecyclerView(int gridCount, OnPullRefreshListener onPullRefreshListener) {
        this.onPullRefreshListener = onPullRefreshListener;
        adapter = new TreeRecyclerAdapter();
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(gridCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (pullRefreshLayout != null) {
            pullRefreshLayout.setStyle(PullRefreshLayout.Style.PULL_REFRESH);
            pullRefreshLayout.setOnPullDownStatusChangeListener(onPullDownStatusChangeListener);
            pullRefreshLayout.setOnPullUpStatusChangeListener(onPullUpStatusChangeListener);
        }
    }

    /**
     * 添加ItemDecoration
     */
    public void addSimpleItemDecoration() {
        itemDecoration = new SimpleItemDecoration(recyclerView.getContext(), 0XFFF6F6F6, 2);
        recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * 添加ItemDecoration
     */
    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decoration) {
        itemDecoration = decoration;
        recyclerView.addItemDecoration(decoration);
    }

    /**
     * 检查是否添加空白条目
     */
    public void checkAddEmptyContentItem() {
        if (holderDataList.isEmpty()) {
            if (recyclerView.getItemDecorationCount() > 0) {
                itemDecoration = recyclerView.getItemDecorationAt(0);
                recyclerView.removeItemDecoration(itemDecoration);
            }
            holderDataList.add(RecvItemEmptyContent.buildEmptyContent(recyclerView));
        } else {
            if (itemDecoration != null) {
                recyclerView.removeItemDecoration(itemDecoration);
                recyclerView.addItemDecoration(itemDecoration);
            }
        }
    }

    /**
     * 加载第一页
     */
    public void loadFirstPageData(boolean showLoadingDialog) {
        if (onPullRefreshListener != null) {
            onPullRefreshListener.onLoadData(showLoadingDialog, DEFAULT_FIRST_PAGE_NUM, pageNum);
        }
    }

    /**
     * 加载下一页
     */
    public void loadNextPageData() {
        if (onPullRefreshListener != null) {
            onPullRefreshListener.onLoadData(false, pageNum + 1, pageNum + 1);
        }
    }

    /**
     * 停止加载动画
     */
    public void stopLoadAnim() {
        if (pullRefreshLayout != null) {
            pullRefreshLayout.stopRefreshAndLoad();
        }
    }

    /**
     * 设置下拉刷新是否可用
     */
    public void setPullDownRefreshEnable(boolean enable) {
        pullRefreshLayout.setPullDownRefreshEnable(enable);
    }

    /**
     * 设置上拉加载是否可用
     */
    public void setPullUpLoadEnable(boolean enable) {
        pullRefreshLayout.setPullUpLoadEnable(enable);
    }

    /**
     * 设置一页数量
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取一页数量
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 加载成功，刷新pageNum
     */
    public void setPageNum(int requestDestPageNum) {
        pageNum = requestDestPageNum;
    }

    /**
     * 清空列表
     */
    public void clearHolderDataList() {
        holderDataList.clear();
    }

    /**
     * 获取列表
     */
    public List<BaseHolderData> getHolderDataList() {
        return holderDataList;
    }

    /**
     * 移除条目
     */
    public void removeHolderData(BaseHolderData data) {
        holderDataList.remove(data);
    }

    /**
     * 移除条目
     */
    public void removeHolderDataAndNotify(BaseHolderData data) {
        holderDataList.remove(data);
        adapter.updateDataAndNotify(holderDataList);
    }

    /**
     * 添加条目
     */
    public void addHolderData(BaseHolderData data) {
        holderDataList.add(data);
    }

    /**
     * 添加条目
     */
    public void addHolderData(@NonNull List<? extends BaseHolderData> dataList) {
        holderDataList.addAll(dataList);
    }

    /**
     * 追加条目，并刷新列表
     */
    public void addHolderData(int position, @NonNull BaseHolderData data) {
        holderDataList.add(position, data);
    }

    /**
     * 追加条目，并刷新列表
     */
    public void addHolderData(int position, @NonNull List<BaseHolderData> dataList) {
        holderDataList.addAll(position, dataList);
    }

    /**
     * 刷新列表
     */
    public void updateHolderDataAndNotify() {
        adapter.updateDataAndNotify(holderDataList);
    }

    /**
     * 追加条目，并刷新列表
     */
    public void addHolderDataAndNotify(@NonNull BaseHolderData data) {
        holderDataList.add(data);
        List<BaseHolderData> dataList = new ArrayList<>();
        dataList.add(data);
        adapter.addDataAndNotify(dataList);
    }

    /**
     * 追加条目，并刷新列表
     */
    public void addHolderDataAndNotify(@NonNull List<BaseHolderData> dataList) {
        holderDataList.addAll(dataList);
        adapter.addDataAndNotify(dataList);
    }

    /**
     * 追加条目，并刷新列表
     */
    public void addHolderDataAndNotify(int position, @NonNull BaseHolderData data) {
        holderDataList.add(position, data);
        adapter.updateDataAndNotify(holderDataList);
    }

    /**
     * 追加条目，并刷新列表
     */
    public void addHolderDataAndNotify(int position, @NonNull List<BaseHolderData> dataList) {
        holderDataList.addAll(position, dataList);
        adapter.updateDataAndNotify(holderDataList);
    }

    /**
     * 删除条目
     */
    public void removeHolderData(int position, int count) {
        for (int i = 0; i < count; i++) {
            holderDataList.remove(position);
        }
    }

    /**
     * 删除条目，并刷新列表
     */
    public void removeHolderDataAndNotify(int position, int count) {
        for (int i = 0; i < count; i++) {
            holderDataList.remove(position);
        }
        adapter.updateDataAndNotify(holderDataList);
    }

    /**
     * 查找条目在列表中的位置
     */
    public int findItemIndex(BaseHolderData data) {
        int index = 0;
        for (BaseHolderData d : holderDataList) {
            if (d == data) {
                return index;
            }
            index++;
        }
        return index;
    }

    /**
     * 检测是否流式布局需要设置setFullSpan
     */
    public static void checkStaggeredGridFullSpan(View itemView, int itemSpanSize) {
        if (itemSpanSize > 0) {
            return;
        }
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public interface OnPullRefreshListener {
        void onLoadData(boolean showLoadingDialog, int requestPageNum, int requestDestPageNum);
    }

}
