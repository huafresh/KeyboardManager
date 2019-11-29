package com.hua.softkeyboard_like;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
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
