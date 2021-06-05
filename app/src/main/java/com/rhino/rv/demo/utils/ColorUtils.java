package com.rhino.rv.demo.utils;

import androidx.annotation.ColorInt;

/**
 * <p>The utils of color.<p>
 *
 * @author LuoLin
 * @since Created on 2016/11/1.
 **/
public final class ColorUtils {

    @ColorInt
    public static final int TRANSPARENT = 0;

    @ColorInt
    public static final int WHITE10 = 0x1AFFFFFF;
    @ColorInt
    public static final int WHITE20 = 0x33FFFFFF;
    @ColorInt
    public static final int WHITE30 = 0x4DFFFFFF;
    @ColorInt
    public static final int WHITE40 = 0x66FFFFFF;
    @ColorInt
    public static final int WHITE50 = 0x80FFFFFF;
    @ColorInt
    public static final int WHITE60 = 0x99FFFFFF;
    @ColorInt
    public static final int WHITE70 = 0xB3FFFFFF;
    @ColorInt
    public static final int WHITE80 = 0xCCFFFFFF;
    @ColorInt
    public static final int WHITE90 = 0x46FFFFFF;
    @ColorInt
    public static final int WHITE = 0xFFFFFFFF;

    @ColorInt
    public static final int BLACK10 = 0x1A000000;
    @ColorInt
    public static final int BLACK20 = 0x33000000;
    @ColorInt
    public static final int BLACK30 = 0x4D000000;
    @ColorInt
    public static final int BLACK40 = 0x66000000;
    @ColorInt
    public static final int BLACK50 = 0x80000000;
    @ColorInt
    public static final int BLACK60 = 0x99000000;
    @ColorInt
    public static final int BLACK70 = 0xB3000000;
    @ColorInt
    public static final int BLACK80 = 0xCC000000;
    @ColorInt
    public static final int BLACK90 = 0x46000000;
    @ColorInt
    public static final int BLACK = 0xFF000000;

    /**
     * change the color alpha
     *
     * @param alpha the alpha of color, 0.1f-1.0f
     * @param color the color
     * @return recolor
     */
    @ColorInt
    public static int alphaColor(float alpha, @ColorInt int color) {
        return (Math.round(alpha * (color >>> 24)) << 24) | (color & 0x00FFFFFF);
    }
}
