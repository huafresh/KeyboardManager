package com.hua.softkeyboard_core;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;

/**
 * @author zhangsh
 * @version V1.0
 * @date 2019-11-28 11:37
 */

class Utils {

    static int getViewTopOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

    static int getViewBottomOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1] + view.getHeight();
    }

}
