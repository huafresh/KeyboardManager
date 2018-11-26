package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
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
    private ScrollAdjustHelper scrollHelper;
    private ComponentName attachWindow;

    KeyboardPopup(Context context, int themeId) {
        super(context);
        this.activity = (Activity) context;
        this.attachWindow = activity.getComponentName();
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        setThemeId(themeId);
    }

    void setThemeId(int themeId) {
        if (this.themeId != themeId) {
            if (isShowing()) {
                dismiss();
            }
            this.themeId = themeId;
            keyboardView = getKeyboardViewByThemeId(activity, themeId);
            setContentView(keyboardView);
        }
    }

    private @Nullable
    View getKeyboardViewByThemeId(Activity activity, @IdRes int themeId) {
        View keyboardView = keyboardViews.get(themeId);
        if (keyboardView == null) {
            FrameLayout container = new FrameLayout(activity);
            IKeyboardTheme iKeyboardTheme = KeyboardManager.keyboardThemes.get(themeId);
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
        showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        IKeyboardTheme keyboardTheme = KeyboardManager.keyboardThemes.get(themeId);
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
        if (scrollHelper == null) {
            scrollHelper = new ScrollAdjustHelper(visibleView, popupView);
        } else {
            scrollHelper.updateView(visibleView, popupView);
        }
        scrollHelper.adjust();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        scrollHelper.reset();
    }

    boolean isSameWindow(Activity activity) {
        return attachWindow != null &&
                attachWindow.equals(activity.getComponentName());
    }

}
