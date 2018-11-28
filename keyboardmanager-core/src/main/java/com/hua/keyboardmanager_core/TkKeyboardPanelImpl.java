package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;

import com.android.thinkive.framework.keyboard.KeyboardManager;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 18:32
 */

class TkKeyboardPanelImpl implements IKeyboardPanel, ActivityCallbackHelper.LifecycleListener {
    private TkKeyboardPopup_new tkKeyboardPopup;

    TkKeyboardPanelImpl() {

    }

    @Override
    public boolean support(int themeId) {
        return FlexKeyboardManager.themeIdTkKeyboardTypeMap.get(themeId, (short) 0) != 0;
    }

    @Override
    public void show(final Activity activity, int themeId, final View visibleView) {
        if (tkKeyboardPopup == null) {
            tkKeyboardPopup = new TkKeyboardPopup_new(activity);
            ActivityCallbackHelper.doOnActivityDestroyed(activity, this);
        }
        tkKeyboardPopup.show(FlexKeyboardManager.themeIdTkKeyboardTypeMap.get(themeId), visibleView);
    }

    @Override
    public void dismiss() {
        if (tkKeyboardPopup != null) {
            tkKeyboardPopup.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return tkKeyboardPopup != null && tkKeyboardPopup.isShowing();
    }

    @Override
    public void onLifecycle(Activity activity) {
        if (tkKeyboardPopup != null) {
            tkKeyboardPopup.dismiss();
        }
        tkKeyboardPopup = null;
    }
}
