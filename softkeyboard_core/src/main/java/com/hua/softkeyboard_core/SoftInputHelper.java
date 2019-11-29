package com.hua.softkeyboard_core;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author zhangsh
 * @version V1.0
 * @date 2019-11-28 20:19
 */

public class SoftInputHelper {

    /**
     * 弹出键盘
     *
     * @param context
     * @param view
     */
    public static void showSoftInput(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view, OnSoftInputDismissListener listener) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public interface OnSoftInputDismissListener {
        void onHiden();
    }

}
