package com.rhino.rv.swipe;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.widget.LinearLayout;

import com.rhino.rv.swipe.impl.IOnSwipeMenuItemClickListener;

/**
 * Created by LuoLin on 2017/6/16.
 **/
public class SwipeMenuItem {

    private int mId;
    private Object mExtraData;
    private int mWidth;
    private int mHeight;

    private Drawable mBackgroundDrawable;

    private Drawable mIconDrawable;
    private int mIconWidth;
    private int mIconHeight;
    @ColorInt
    private int mIconColor;
    private PorterDuff.Mode mIconColorMode;

    private String mText;
    private int mTextSize;
    @ColorInt
    private int mTextColor;
    private Typeface mTextTypeface;

    private IOnSwipeMenuItemClickListener mClickListener;

    public SwipeMenuItem() {
        mWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
        mHeight = LinearLayout.LayoutParams.MATCH_PARENT;
        mBackgroundDrawable = new ColorDrawable(Color.RED);

        mIconDrawable = new ColorDrawable(Color.BLACK);
        mIconWidth = 50;
        mIconHeight = 50;
        mIconColor = Color.WHITE;

        mText = "delete";
        mTextSize = 14;
        mTextColor = Color.WHITE;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Object getExtraData() {
        return mExtraData;
    }

    public void setExtraData(Object obj) {
        this.mExtraData = obj;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        this.mBackgroundDrawable = drawable;
    }

    public Drawable getIconDrawable() {
        return mIconDrawable;
    }

    public void setIconDrawable(Drawable drawable) {
        this.mIconDrawable = drawable;
    }

    public int getIconWidth() {
        return mIconWidth;
    }

    public void setIconWidth(int width) {
        this.mIconWidth = width;
    }

    public int getIconHeight() {
        return mIconHeight;
    }

    public void setIconHeight(int height) {
        this.mIconHeight = height;
    }

    public int getIconColor() {
        return mIconColor;
    }

    public void setIconColor(@ColorInt int color) {
        this.mIconColor = color;
    }

    public PorterDuff.Mode getIconColorMode() {
        return mIconColorMode;
    }

    public void setIconColorMode(PorterDuff.Mode mode) {
        this.mIconColorMode = mode;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(@ColorInt int color) {
        this.mTextColor = color;
    }

    public Typeface getTextTypeface() {
        return mTextTypeface;
    }

    public void setTextTypeface(Typeface typeface) {
        this.mTextTypeface = typeface;
    }

    public IOnSwipeMenuItemClickListener getClickListener() {
        return mClickListener;
    }

    public void setClickListener(IOnSwipeMenuItemClickListener listener) {
        this.mClickListener = listener;
    }
}
