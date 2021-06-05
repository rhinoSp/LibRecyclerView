package com.rhino.rv.demo.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;


/**
 * <p>The utils of drawable.</p>
 *
 * @author LuoLin
 * @since Create on 2016/10/31.
 **/
public class DrawableUtils {

    /**
     * Tint drawable.
     *
     * @param drawable drawable
     * @param colors   ColorStateList
     * @return drawable
     */
    public static Drawable tintDrawable(@NonNull Drawable drawable, @NonNull ColorStateList colors) {
        try {
            int[][] states = ReflectUtils.getObjectByFieldName(colors, "mStateSpecs",
                    int[][].class);
            if (states != null) {
                StateListDrawable stateListDrawable = new StateListDrawable();
                for (int i = 0; i < states.length; i++) {
                    stateListDrawable.addState(states[i], drawable);
                }
                Drawable.ConstantState state = stateListDrawable.getConstantState();
                drawable = DrawableCompat.wrap(state == null ? stateListDrawable
                        : state.newDrawable()).mutate();
                DrawableCompat.setTintList(drawable, colors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * Build a circle GradientDrawable.
     *
     * @param strokeWidth The width in pixels of the stroke
     * @param strokeColor The color of the stroke
     * @param solidColor  The color of the solid
     * @return GradientDrawable
     */
    public static GradientDrawable buildShapeStrokeOvalDrawable(int strokeWidth,
                                                                @ColorInt int strokeColor, @ColorInt int solidColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    /**
     * Build a rect GradientDrawable.
     *
     * @param strokeWidth The width in pixels of the stroke
     * @param strokeColor The color of the stroke
     * @param solidColor  The color of the solid
     * @param corner      The radius in pixels of the corners of the rectangle shape
     * @return GradientDrawable
     */
    public static GradientDrawable buildShapeStrokeRectDrawable(int strokeWidth,
                                                                @ColorInt int strokeColor, @ColorInt int solidColor, int corner) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(corner);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(solidColor);
        return gradientDrawable;
    }

    /**
     * Build a rect GradientDrawable.
     *
     * @param strokeWidth        The width in pixels of the stroke
     * @param strokeColor        The color of the stroke
     * @param solidColor         The color of the solid
     * @param xTopLeftCorner     px
     * @param yTopLeftCorner     px
     * @param xTopRightCorner    px
     * @param yTopRightCorner    px
     * @param xBottomRightCorner px
     * @param yBottomRightCorner px
     * @param xBottomLeftCorner  px
     * @param yBottomLeftCorner  px
     * @return Drawable
     */
    public static Drawable buildShapeStrokeRectDrawable(int strokeWidth, @ColorInt int strokeColor,
                                                        @ColorInt int solidColor, int xTopLeftCorner, int yTopLeftCorner, int xTopRightCorner,
                                                        int yTopRightCorner, int xBottomRightCorner, int yBottomRightCorner,
                                                        int xBottomLeftCorner, int yBottomLeftCorner) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(new float[]{xTopLeftCorner, yTopLeftCorner, yTopRightCorner,
                xTopRightCorner, xBottomRightCorner, yBottomRightCorner, xBottomLeftCorner, yBottomLeftCorner});
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(solidColor);
        return gradientDrawable;
    }

    /**
     * Build a ColorStateList.
     *
     * @param disableColor  the color of disable
     * @param selectedColor the color of select
     * @param normalColor   the color of normal
     * @return ColorStateList
     */
    public static ColorStateList buildColorStateList(@ColorInt int disableColor,
                                                     @ColorInt int selectedColor, @ColorInt int normalColor) {
        int[] colors = new int[]{disableColor, selectedColor, normalColor};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    /**
     * Build a ColorStateList.
     *
     * @param disableColor  the color of disable
     * @param selectedColor the color of select
     * @param pressedColor  the color of pressed
     * @param normalColor   the color of normal
     * @return ColorStateList
     */
    public static ColorStateList buildColorStateList(@ColorInt int disableColor,
                                                     @ColorInt int selectedColor, @ColorInt int pressedColor, @ColorInt int normalColor) {
        int[] colors = new int[]{disableColor, selectedColor, pressedColor, normalColor};
        int[][] states = new int[4][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{android.R.attr.state_pressed};
        states[3] = new int[]{};
        return new ColorStateList(states, colors);
    }

    /**
     * Build a StateListDrawable.
     *
     * @param disableDrawable  the drawable of disable
     * @param selectedDrawable the drawable of select
     * @param pressedDrawable  the drawable of pressed
     * @param normalDrawable   the drawable of normal
     * @return StateListDrawable
     */
    public static StateListDrawable buildStateListDrawable(Drawable disableDrawable, Drawable selectedDrawable,
                                                           Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (null != disableDrawable) {
            stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        }
        if (null != selectedDrawable) {
            stateListDrawable.addState(new int[]{android.R.attr.state_selected,
                    -android.R.attr.state_pressed}, selectedDrawable);
        }
        if (null != pressedDrawable) {
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        }
        if (null != normalDrawable) {
            stateListDrawable.addState(new int[]{}, normalDrawable);
        }
        return stateListDrawable;
    }

    /**
     * Build a StateListDrawable.
     *
     * @param disableDrawable the drawable of disable
     * @param focusedDrawable the drawable of focused
     * @param normalDrawable  the drawable of normal
     * @return StateListDrawable
     */
    public static StateListDrawable buildFocuseStateListDrawable(Drawable disableDrawable,
                                                                 Drawable focusedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (null != disableDrawable) {
            stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        }
        if (null != focusedDrawable) {
            stateListDrawable.addState(new int[]{android.R.attr.state_focused}, focusedDrawable);
        }
        if (null != normalDrawable) {
            stateListDrawable.addState(new int[]{}, normalDrawable);
        }
        return stateListDrawable;
    }

    /**
     * Build a StateListDrawable.
     *
     * @param disableColor  the color of disable
     * @param selectedColor the color of select
     * @param pressedColor  the color of pressed
     * @param normalColor   the color of normal
     * @return StateListDrawable
     */
    public static StateListDrawable buildColorStateListDrawable(@ColorInt int disableColor,
                                                                @ColorInt int selectedColor, @ColorInt int pressedColor, @ColorInt int normalColor) {
        return buildStateListDrawable(
                disableColor != 0 ? new ColorDrawable(disableColor) : null,
                selectedColor != 0 ? new ColorDrawable(selectedColor) : null,
                pressedColor != 0 ? new ColorDrawable(pressedColor) : null,
                normalColor != 0 ? new ColorDrawable(normalColor) : null);
    }

}
