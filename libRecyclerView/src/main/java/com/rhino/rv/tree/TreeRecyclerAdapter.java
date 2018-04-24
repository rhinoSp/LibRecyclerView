package com.rhino.rv.tree;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.rhino.rv.SimpleRecyclerAdapter;
import com.rhino.rv.base.BaseHolderData;
import com.rhino.rv.base.BaseHolderFactory;

/**
 * Created by LuoLin on 2016/11/21.
 **/
public class TreeRecyclerAdapter extends SimpleRecyclerAdapter {

    public TreeRecyclerAdapter() {
        super();
    }

    public TreeRecyclerAdapter(BaseHolderFactory factory) {
        super(factory);
    }

    /**
     * Expand or fold all.
     *
     * @param expand true expand， false fold.
     */
    public void doExpandAll(boolean expand) {
        for (int i = 0; i < getDataList().size(); i++) {
            BaseHolderData data = getDataList().get(i);
            if (data instanceof BaseTreeData) {
                BaseTreeData d = (BaseTreeData) data;
                if (!d.isLeaf() && d.isExpand() != expand) {
                    d.setExpand(expand);
                    updateData(getRealDataWhenExpandChange(getDataList(), i));
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Deal the expand or fold.
     *
     * @param data   the data.
     * @param expand true expand， false fold.
     */
    public boolean doExpand(BaseTreeData data, boolean expand) {
        if (!data.isLeaf() && data.isExpand() != expand) {
            data.setExpand(expand);
            updateData(getRealDataWhenExpandChange(getDataList(), data.mHolder.getBindPosition()));
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    /**
     * When you change the folding and unfolding state of a node, call this interface to obtain the real data.
     *
     * @param mDataList the old data.
     * @param position  the change position.
     * @return new data list.
     */
    @NonNull
    public List<BaseHolderData> getRealDataWhenExpandChange(List<? extends BaseHolderData> mDataList, int position) {
        List<BaseHolderData> realDataList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            BaseHolderData data = mDataList.get(i);
            realDataList.add(data);
            if (data instanceof BaseTreeData) {
                BaseTreeData d = (BaseTreeData) data;
                if (i == position && !d.isLeaf()) {
                    if (d.isExpand()) { // If it is expanded, add its first layer of son nodes
                        realDataList.addAll(getRealDataWhenExpandChange(d.getChildList(), -1));
                    } else { // Skip all the son nodes under this node
                        i += setChildUnExpand(d);
                    }
                }
            }
        }
        return realDataList;
    }

    /**
     * Sets all child nodes in the node as collapsed state
     * and returns the total number of all expanded nodes under that node.
     *
     * @param data the node.
     * @return the total number of all expanded nodes under that node.
     */
    public int setChildUnExpand(BaseTreeData data) {
        if (data.isLeaf()) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < data.getChildList().size(); i++) {
            count++;
            BaseTreeData child = data.getChildList().get(i);
            if (null != child && !child.isLeaf() && child.isExpand()) {
                child.setExpand(false);
                count += setChildUnExpand(child);
            }
        }
        return count;
    }
}
