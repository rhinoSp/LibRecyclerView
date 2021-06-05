package com.rhino.rv.base;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public abstract class BaseHolder<T extends BaseHolderData> extends RecyclerView.ViewHolder {

    /**
     * The parent view.
     **/
    protected ViewGroup mParentView;
    /**
     * The Resources.
     */
    private Resources mResources;
    /**
     * The adapter.
     */
    private BaseRecyclerAdapter mAdapter;
    /**
     * The bind position.
     */
    private int mBindPosition = RecyclerView.NO_POSITION;
    /**
     * The bind data.
     */
    private T mData;

    /**
     * For refresh ui.
     **/
    public abstract void bindView(T data, int position);

    /**
     * Unbind view.
     **/
    public void unbindView(T data) {
    }

    public BaseHolder(View itemView) {
        super(itemView);
    }

    /**
     * Bind data and refresh ui.
     */
    public void onBindView(T data, int position) {
        this.mBindPosition = position;
        this.mData = data;
        this.mData.mHolder = this;
        itemView.setOnClickListener(getItemClickListener());
        itemView.setOnLongClickListener(getItemLongClickListener());
        bindView(data, position);
    }

    /**
     * Unbind view.
     */
    public void onUnbindView() {
        mBindPosition = RecyclerView.NO_POSITION;
        unbindView(mData);
    }

    /**
     * Set adapter.
     *
     * @param adapter BaseRecyclerAdapter
     */
    final public void setAdapter(BaseRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Set the parent view.
     *
     * @param parent The parent view
     */
    final public void setParentView(ViewGroup parent) {
        mParentView = parent;
    }

    /**
     * Get adapter.
     *
     * @return BaseRecyclerAdapter
     */
    final public BaseRecyclerAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Get the bind data.
     *
     * @return The bind data.
     */
    final public T getBindData() {
        return mData;
    }

    /**
     * Get the bind position.
     *
     * @return The bind position.
     */
    final public int getBindPosition() {
        return mBindPosition;
    }

    /**
     * Notify any registered observers that the data set has changed.
     */
    final public void notifyDataChanged() {
        if (isBindView() && mAdapter != null) {
            mAdapter.notifyItemChanged(mBindPosition);
        }
    }

    /**
     * Whether bind view.
     *
     * @return true has bind.
     */
    final public boolean isBindView() {
        return mBindPosition != RecyclerView.NO_POSITION;
    }

    /**
     * Get context.
     *
     * @return Context.
     */
    final protected Context getContext() {
        return itemView.getContext();
    }

    /**
     * Get Resources.
     *
     * @return mResources
     */
    @NonNull
    final protected Resources getResources() {
        if (null == mResources) {
            mResources = getContext().getResources();
        }
        return mResources;
    }

    /**
     * Get the item click listener.
     *
     * @return listener
     */
    @SuppressWarnings("all")
    final protected View.OnClickListener getItemClickListener() {
        if (null != getBindData() && getBindData().mItemClickListener != null) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getBindData().mItemClickListener != null) {
                        getBindData().mItemClickListener.onClick(v, getBindData(), getBindPosition());
                    }
                }
            };
        } else {
            return null;
        }
    }

    /**
     * Get the item long click listener.
     *
     * @return listener
     */
    @SuppressWarnings("all")
    final protected View.OnLongClickListener getItemLongClickListener() {
        if (null != getBindData() && getBindData().mItemLongClickListener != null) {
            return new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getBindData().mItemLongClickListener != null) {
                        return getBindData().mItemLongClickListener.onLongClick(v, getBindData(), getBindPosition());
                    }
                    return false;
                }
            };
        } else {
            return null;
        }
    }

    /**
     * Look for a child view with the given id.  If this view has the given
     * id, return this view.
     *
     * @param id The id to search for.
     * @return view
     * @see View#findViewById(int)
     */
    @SuppressWarnings("all")
    final protected <T extends View> T findSubViewById(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

}
