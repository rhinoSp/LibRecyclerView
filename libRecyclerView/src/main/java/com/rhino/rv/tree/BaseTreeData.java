package com.rhino.rv.tree;

import com.rhino.rv.base.BaseHolderData;

import java.util.List;


/**
 * Created by LuoLin on 2016/11/21.
 **/
public abstract class BaseTreeData extends BaseHolderData {

    /**
     * The node depth
     **/
    private int depth;
    /**
     * Whether expanded
     **/
    private boolean isExpand = false;
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
    public boolean isExpand() {
        return isExpand;
    }

    /**
     * Set expand status.
     *
     * @param isExpand true expanded.
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
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
     * Deal expand or fold and refresh ui.
     *
     * @param expand true expand， false fold.
     * @return true success
     */
    public boolean doExpand(boolean expand) {
        if (null != mHolder) {
            TreeRecyclerAdapter adapter = (TreeRecyclerAdapter) mHolder.getAdapter();
            if (null != adapter) {
                return adapter.doExpand(this, expand);
            }
        }
        return false;
    }

}
