package com.rhino.rv.tree;

import com.rhino.rv.base.BaseHolderData;

import java.util.List;


/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public abstract class BaseTreeData extends BaseHolderData {

    /**
     * The node depth
     **/
    private int depth;
    /**
     * Whether expanded
     **/
    private boolean isExpanded = false;
    /**
     * The child node list
     **/
    private List<? extends BaseTreeData> childList;

    /**
     * Get the node depth.
     *
     * @return depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Set the node depth.
     *
     * @param depth depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Whether expanded.
     *
     * @return true expanded.
     */
    public boolean isExpanded() {
        return isExpanded;
    }

    /**
     * Set expand status.
     *
     * @param isExpanded true expanded.
     */
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    /**
     * Get the child node list.
     *
     * @return list
     */
    public List<? extends BaseTreeData> getChildList() {
        return childList;
    }

    /**
     * Set the child node list.
     *
     * @param childList list
     */
    public void setChildList(List<? extends BaseTreeData> childList) {
        this.childList = childList;
    }

    /**
     * Whether leaf node.
     *
     * @return true yes
     */
    public boolean isLeaf() {
        return null == childList || childList.isEmpty();
    }

    /**
     * Expand an expandable item recursively.
     *
     * @param animate expand items with animation.
     * @return the number of items that have been added.
     */
    @SuppressWarnings("unchecked")
    public int expandRecursively(boolean animate) {
        return expandRecursively(animate, true);
    }

    /**
     * Expand an expandable item recursively.
     *
     * @param animate expand items with animation.
     * @param notify  notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                yourself.
     * @return the number of items that have been added.
     */
    @SuppressWarnings("unchecked")
    public int expandRecursively(boolean animate, boolean notify) {
        return expand(animate, notify, true);
    }

    /**
     * Expand an expandable item.
     *
     * @param animate expand items with animation.
     * @return the number of items that have been added.
     */
    @SuppressWarnings("unchecked")
    public int expand(boolean animate) {
        return expand(animate, true);
    }

    /**
     * Expand an expandable item.
     *
     * @param animate expand items with animation.
     * @param notify  notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                yourself.
     * @return the number of items that have been added.
     */
    @SuppressWarnings("unchecked")
    public int expand(boolean animate, boolean notify) {
        return expand(animate, notify, false);
    }

    /**
     * Expand an expandable item.
     *
     * @param animate   expand items with animation.
     * @param notify    notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                  yourself.
     * @param recursive expand an expandable item recursively.
     * @return the number of items that have been added.
     */
    @SuppressWarnings("unchecked")
    public int expand(boolean animate, boolean notify, boolean recursive) {
        if (null != mHolder) {
            TreeRecyclerAdapter adapter = (TreeRecyclerAdapter) mHolder.getAdapter();
            if (null != adapter) {
                return adapter.expand(getBindPosition(), animate, notify, recursive);
            }
        }
        return 0;
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param animate collapse with animation or not.
     * @return the number of subItems collapsed.
     */
    public int collapse(boolean animate) {
        return collapse(animate, true);
    }

    /**
     * Collapse an expandable item that has been expanded.
     *
     * @param animate collapse with animation or not.
     * @param notify  notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                yourself.
     * @return the number of subItems collapsed.
     */
    public int collapse(boolean animate, boolean notify) {
        if (null != mHolder) {
            TreeRecyclerAdapter adapter = (TreeRecyclerAdapter) mHolder.getAdapter();
            if (null != adapter) {
                return adapter.collapse(getBindPosition(), animate, notify);
            }
        }
        return 0;
    }

}
