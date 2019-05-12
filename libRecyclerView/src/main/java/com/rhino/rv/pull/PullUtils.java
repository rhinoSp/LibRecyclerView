package com.rhino.rv.pull;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * @author rhino
 * @since Create on 2018/9/29.
 */
public class PullUtils {

    /**
     * Start rotate anim.
     */
    public static Animation buildAnimationRefreshing() {
        Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(-1);
        return anim;
    }

}
