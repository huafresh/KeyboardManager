package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.view.View;

import androidx.annotation.IdRes;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/24
 */
public interface IKeyboardPanel {

    boolean support(@IdRes int themeId);

    /**
     * 弹出自绘键盘
     *
     * @param activity    自绘键盘使用PopupWindow弹出，因此需要Activity。
     * @param themeId     键盘UI的id，需要自定义实现{@link IKeyboardTheme}
     * @param visibleView 避免被自绘键盘遮住的视图
     */
    void show(Activity activity, @IdRes int themeId, View visibleView);

    void dismiss();

    boolean isShowing();
}
