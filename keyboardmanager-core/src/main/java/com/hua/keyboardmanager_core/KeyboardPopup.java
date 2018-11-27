package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 11:09
 */

class KeyboardPopup extends PopupWindow {
    private Activity activity;
    private SparseArray<View> keyboardViews = new SparseArray<>();
    private int themeId = -1;
    private View keyboardView;
    private ComponentName attachWindow;
    private View visibleView;

    KeyboardPopup(Context context, int themeId) {
        super(context);
        this.activity = (Activity) context;
        this.attachWindow = activity.getComponentName();
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        updateThemeId(themeId);
    }

    void updateThemeId(int themeId) {
        if (this.themeId != themeId) {
            if (isShowing()) {
                dismiss();
            }
            this.themeId = themeId;
            keyboardView = getKeyboardViewByThemeId(activity, themeId);
            setContentView(keyboardView);
        }
    }

    private View getKeyboardViewByThemeId(Activity activity, @IdRes int themeId) {
        View keyboardView = keyboardViews.get(themeId);
        if (keyboardView == null) {
            FrameLayout container = new FrameLayout(activity);
            IKeyboardTheme iKeyboardTheme = FlexKeyboardManager.keyboardThemes.get(themeId);
            if (iKeyboardTheme != null) {
                View view = iKeyboardTheme.onCreateKeyboardView(activity,
                        LayoutInflater.from(activity), container);
                container.addView(view);
                keyboardView = container;
            }
        }
        return keyboardView;
    }

    void show(final View visibleView) {
        this.visibleView = visibleView;
        showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        IKeyboardTheme keyboardTheme = FlexKeyboardManager.keyboardThemes.get(themeId);
        View focus = activity.getWindow().getCurrentFocus();
        if (keyboardTheme != null && focus != null) {
            keyboardTheme.onBindInputTarget(focus);
        }
        keyboardView.post(new Runnable() {
            @Override
            public void run() {
                scrollEnsureViewVisible(visibleView, keyboardView);
            }
        });
    }

    private void scrollEnsureViewVisible(View visibleView, View popupView) {
        ScrollAdjustHelper.adjust(visibleView, ScrollAdjustHelper.getViewTopOnScreen(popupView));
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ScrollAdjustHelper.reset(visibleView);
    }

    boolean isSameWindow(Activity activity) {
        return attachWindow != null &&
                attachWindow.equals(activity.getComponentName());
    }

}
