package com.rhino.rv.demo.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

/**
 * <p>The utils of screen.</p>
 *
 * @author LuoLin
 * @since Create on 2016/9/1.
 **/
public class ScreenUtils {

    /**
     * get screen width
     *
     * @param ctx the context
     * @return the screen width
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context ctx) {
        WindowManager manager = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return display.getWidth();
    }

    /**
     * get screen height
     *
     * @param ctx the context
     * @return the screen height
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context ctx) {
        WindowManager manager = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return display.getHeight();
    }

    /**
     * get the status height
     *
     * @param ctx the context
     * @return the status bar height
     */
    public static int getStatusBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Get the screen brightness.
     *
     * @param activity the activity
     * @return the screen brightness
     */
    public static int getScreenBrightness(Activity activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Set the screen brightness.
     *
     * @param activity the activity
     * @param value    the value of screen brightness
     */
    public static void setScreenBrightness(Activity activity, int value) {
        value = 0 >= value ? 0 : value;
        value = 255 <= value ? 255 : value;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = value / 255f;
        activity.getWindow().setAttributes(params);
    }

    /**
     * change dp to px
     *
     * @param ctx     the context
     * @param dpValue the dp value
     * @return the px value
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * change px to dp
     *
     * @param ctx     the context
     * @param pxValue the px value
     * @return the dp value
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * change px to sp
     *
     * @param ctx     the context
     * @param pxValue the px value
     * @return the sp value
     */
    public static int px2sp(Context ctx, float pxValue) {
        final float fontScale = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * change sp to px
     *
     * @param ctx     the context
     * @param spValue the sp value
     * @return the px value
     */
    public static int sp2px(Context ctx, float spValue) {
        final float fontScale = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
