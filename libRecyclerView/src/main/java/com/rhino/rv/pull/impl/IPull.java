package com.rhino.rv.pull.impl;

/**
 * @author LuoLin
 * @since Create on 2017/4/13.
 */
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
