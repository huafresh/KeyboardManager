package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.android.thinkive.framework.keyboard.KeyboardManager;
import com.android.thinkive.framework.keyboard.*;
import com.android.thinkive.framework.util.ScreenUtil;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 18:32
 */

class TkKeyboardPanelImpl implements IKeyboardPanel {
    private KeyboardManager tkKeyboardManager;
    private ScrollAdjustHelper scrollAdjustHelper;

    TkKeyboardPanelImpl() {

    }

    @Override
    public void show(final Activity activity, int themeId, final View visibleView) {

    }

    @Override
    public void dismiss() {
        if (tkKeyboardManager != null) {
            tkKeyboardManager.dismiss();
        }
        if (scrollAdjustHelper != null) {
            scrollAdjustHelper.reset();
        }
    }

    @Override
    public boolean isShowing() {
        return tkKeyboardManager.isShowing();
    }

    private static short translateKeyboardType(int themeId) {
        if (R.id.tk_keyboard_theme_english == themeId) {
            return com.android.thinkive.framework.keyboard.KeyboardManager.KEYBOARD_TYPE_ENGLISH;
        }

        return com.android.thinkive.framework.keyboard.KeyboardManager.KEYBOARD_TYPE_ENGLISH;
    }
}