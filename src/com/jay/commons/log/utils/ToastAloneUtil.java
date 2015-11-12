package com.jay.commons.log.utils;


import android.app.Activity;
import android.widget.Toast;


/**
 * @Description : 只会显示一个toast </br> 不会有延迟的toast
 * @Author: roy.ren
 * @Date: 2015/9/1
 * @Company :YeahMoBi
 */
public class ToastAloneUtil {
	
    /**
     * 唯一的toast
     */
    private static Toast mToast = null;

    private ToastAloneUtil() {}

    /**
     *
     * @param ctx
     * @param stringid
     * @param lastTime
     * @return
     */
    public static void showToast(final Activity ctx, final int stringid, final int lastTime) {
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast != null) {
                } else {
                    mToast = Toast.makeText(ctx, stringid, lastTime);
                }
                mToast.setText(stringid);
                mToast.show();
            }
        });
    }

    /**
     *
     * @param ctx
     * @param tips
     * @param lastTime
     * @return
     */
    public static void showToast(final Activity ctx, final String tips, final int lastTime) {
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast != null) {
                } else {
                    mToast = Toast.makeText(ctx, tips, lastTime);
                }
                mToast.setText(tips);
                mToast.show();
            }
        });
    }

    public static void showToast(final Activity ctx, final int gravity, final int stringid, final int lastTime) {
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast != null) {
                } else {
                    mToast = Toast.makeText(ctx, stringid, lastTime);
                }
                mToast.setText(stringid);
                mToast.setGravity(gravity, 0, 0);
                mToast.show();
            }
        });
    }

    public static void showToast(final Activity ctx, final int gravity, final String text, final int lastTime) {
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast != null) {
                } else {
                    mToast = Toast.makeText(ctx, text, lastTime);
                }
                mToast.setText(text);
                mToast.setGravity(gravity, 0, 0);
                mToast.show();
            }
        });
    }

}