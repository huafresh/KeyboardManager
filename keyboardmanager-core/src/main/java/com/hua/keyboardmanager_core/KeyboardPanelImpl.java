package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.view.View;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/24
 */
class KeyboardPanelImpl implements IKeyboardPanel, ActivityCallbackHelper.LifecycleListener {
    private KeyboardPopup keyboardPopup;

    KeyboardPanelImpl() {

    }

    @Override
    public boolean support(int themeId) {
        return FlexKeyboardManager.customKeyboardThemes.get(themeId) != null;
    }

    @Override
    public void show(Activity activity, int themeId, final View visibleView) {
        if (keyboardPopup == null || !keyboardPopup.isSameWindow(activity)) {
            keyboardPopup = new KeyboardPopup(activity, themeId);
        } else {
            keyboardPopup.updateThemeId(themeId);
        }

        keyboardPopup.show(visibleView);

        ActivityCallbackHelper.doOnActivityDestroyed(activity, this);
    }

    @Override
    public void dismiss() {
        if (keyboardPopup != null) {
            keyboardPopup.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return keyboardPopup != null && keyboardPopup.isShowing();
    }

    @Override
    public void onLifecycle(Activity activity) {
        if (keyboardPopup != null && keyboardPopup.isSameWindow(activity)) {
            if (keyboardPopup.isShowing()) {
                keyboardPopup.dismiss();
            }
            keyboardPopup = null;
        }
    }
}
