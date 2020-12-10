package com.rhino.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rhino.rv.base.BaseHolder;
import com.rhino.rv.base.BaseHolderFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author LuoLin
 * @since Create on 2016/11/21.
 */
public class SimpleHolderFactory extends BaseHolderFactory {
    @SuppressWarnings("all")
    @Override
    public BaseHolder buildHolder(ViewGroup parent, int layoutResId, Class holderClass) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        BaseHolder holder = null;
        try {
            holder = (BaseHolder) holderClass.getConstructor(View.class).newInstance(view);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return holder;
    }
}
