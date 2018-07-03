package com.rhino.rv.drag.impl;


/**
 * @author LuoLin
 * @since Create on 2018/1/10.
 */
public interface IOnItemDragCallbackListener {

    boolean onSwiped(int position, int direction);

    boolean onMove(int srcPosition, int targetPosition);

}