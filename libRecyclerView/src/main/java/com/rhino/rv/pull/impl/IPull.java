package com.rhino.rv.pull.impl;

/**
 * Created by LuoLin on 2017/4/13.
 **/
public interface IPull {

    /**
     * Whether it scrolled to the bottom.
     */
    boolean isScrolledToBottom();

    /**
     * Whether it scrolled to the top.
     */
    boolean isScrolledToTop();

}
