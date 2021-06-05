package com.rhino.rv.tree;

import com.rhino.rv.SimpleRecyclerAdapter;
import com.rhino.rv.base.BaseHolderFactory;

import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public class TreeRecyclerAdapter extends SimpleRecyclerAdapter {

    public TreeRecyclerAdapter() {
        super();
    }

    public TreeRecyclerAdapter(BaseHolderFactory factory) {
        super(factory);
    }

    /**
     * Expand all item recursively.
     *
     * @param animate expand with animation or not.
     */
    public void expandAll(boolean animate) {
        for (int position = getDataList().size() - 1; position >= 0; position--) {
            expandRecursively(position, animate);
        }
    }

    /**
     * Expand an expandable item recursively.
     *
     * @param position position of the item.
     * @param animate  expand with animation or not.
     * @return the number of items that have been added.
     */
    public int expandRecursively(int position, boolean animate) {
        return expandRecursively(position, animate, true);
    }

    /**
     * Expand an expandable item recursively.
     *
     * @param position position of the item.
     * @param animate  expand with animation or not.
     * @param notify   notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                 yourself.
     * @return the number of items that have been added.
     */
    public int expandRecursively(int position, boolean animate, boolean notify) {
        return expand(position, animate, notify, true);
    }

    /**
     * Expand an expandable item.
     *
     * @param position position of the item.
     * @param animate  expand with animation or not.
     * @return the number of items that have been added.
     */
    public int expand(int position, boolean animate) {
        return expand(position, animate, true, false);
    }

    /**
     * Expand an expandable item.
     *
     * @param position  position of the item.
     * @param animate   expand items with animation.
     * @param notify    notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                  yourself.
     * @param recursive expand an expandable item recursively.
     * @return the number of items that have been added.
     */
    @SuppressWarnings("unchecked")
    public int expand(int position, boolean animate, boolean notify, boolean recursive) {
        BaseTreeData data = (BaseTreeData) getItem(position);
        if (null == data || data.isExpanded() || data.isLeaf()) {
            return 0;
        }

        int expandCount = recursiveExpand(position, recursive);
        if (notify) {
            if (animate) {
                notifyItemChanged(data.getBindPosition());
                notifyItemRangeInserted(data.getBindPosition() + 1, expandCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return expandCount;
    }

    /**
     * Sets all child nodes in the node as expanded state
     * and returns the total number of all collapsed nodes under that node.
     *
     * @param position position of the item.
     * @return the total number of all collapsed nodes under that node.
     */
    @SuppressWarnings("unchecked")
    public int recursiveExpand(int position, boolean recursive) {
        BaseTreeData data = (BaseTreeData) getItem(position);
        if (null == data || data.isExpanded() || data.isLeaf()) {
            return 0;
        }
        data.setExpanded(true);
        List childList = data.getChildList();
        getDataList().addAll(position + 1, childList);
        int expandCount = childList.size();

        if (recursive) {
            int childPosition = position + childList.size() - 1;
            for (int i = childList.size() - 1; i >= 0; i--, childPosition--) {
                BaseTreeData child = (BaseTreeData) childList.get(i);
                if (!child.isExpanded() && !child.isLeaf()) {
                    expandCount += recursiveExpand(childPosition + 1, true);
                }
            }
        }
        return expandCount;
    }

    /**
     * Collapse all item recursively.
     *
     * @param animate expand with animation or not.
     */
    public void collapseAll(boolean animate) {
        for (int position = getDataList().size() - 1; position >= 0; position--) {
            collapse(position, animate);
        }
    }

    /**
     * Collapse an expandable item that has been expanded.
     *
     * @param position position of the item.
     * @param animate  collapse with animation or not.
     * @return the number of subItems collapsed.
     */
    public int collapse(int position, boolean animate) {
        return collapse(position, animate, true);
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position position of the item.
     * @param animate  collapse with animation or not.
     * @param notify   notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                 yourself.
     * @return the number of subItems collapsed.
     */
    public int collapse(int position, boolean animate, boolean notify) {
        BaseTreeData data = (BaseTreeData) getItem(position);
        if (null == data || !data.isExpanded() || data.isLeaf()) {
            return 0;
        }
        int collapseCount = recursiveCollapse(position);
        if (notify) {
            if (animate) {
                notifyItemChanged(data.getBindPosition());
                notifyItemRangeRemoved(data.getBindPosition() + 1, collapseCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return collapseCount;
    }

    /**
     * Sets all child nodes in the node as collapsed state
     * and returns the total number of all expanded nodes under that node.
     *
     * @param position position of the item.
     * @return the total number of all expanded nodes under that node.
     */
    public int recursiveCollapse(int position) {
        BaseTreeData data = (BaseTreeData) getItem(position);
        if (null == data || !data.isExpanded() || data.isLeaf()) {
            return 0;
        }
        data.setExpanded(false);
        int collapseCount = 0;
        List<? extends BaseTreeData> childList = data.getChildList();
        for (int i = childList.size() - 1; i >= 0; i--) {
            BaseTreeData child = childList.get(i);
            if (child.isExpanded() && !child.isLeaf()) {
                collapseCount += recursiveCollapse(getItemPosition(child));
            }
            getDataList().remove(child);
            collapseCount++;
        }
        return collapseCount;
    }

}
