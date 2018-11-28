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
    private TkKeyboardPopup tkKeyboardPopup;

    TkKeyboardPanelImpl() {

    }

    @Override
    public boolean support(int themeId) {
        return FlexKeyboardManager.themeIdTkKeyboardTypeMap.get(themeId, (short) 0) != 0;
    }

    @Override
    public void show(final Activity activity, int themeId, final View visibleView) {
        tkKeyboardPopup = TkKeyboardPopup.create(activity,
                FlexKeyboardManager.themeIdTkKeyboardTypeMap.get(themeId), visibleView);
        if (tkKeyboardPopup != null) {
            tkKeyboardPopup.show();
            ActivityCallbackHelper.doOnActivityDestroyed(activity, this);
        }
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
        if (tkKeyboardPopup != null &&
                tkKeyboardPopup.isShowing()) {
            tkKeyboardPopup.dismiss();
        }
        tkKeyboardPopup = null;
    }
}
