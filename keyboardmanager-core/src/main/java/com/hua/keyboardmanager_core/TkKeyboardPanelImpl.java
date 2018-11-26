package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.android.thinkive.framework.keyboard.KeyboardManager;
import com.android.thinkive.framework.keyboard.*;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 18:32
 */

class TkKeyboardPanelImpl implements IKeyboardPanel {
    private KeyboardManager tkKeyboardManager;

    TkKeyboardPanelImpl() {

    }

    @Override
    public void show(Activity activity, int themeId, View visibleView) {
        if (tkKeyboardManager != null) {
            tkKeyboardManager.dismiss();
        }

        View focusView = activity.getWindow().getCurrentFocus();
        if (focusView instanceof EditText) {
            tkKeyboardManager = new KeyboardManager(activity, (EditText) focusView,
                    translateKeyboardType(themeId));
        } else {
            tkKeyboardManager = new KeyboardManager(activity,translateKeyboardType(themeId));
        }

        tkKeyboardManager.show();
    }

    @Override
    public void dismiss() {
        if (tkKeyboardManager != null) {
            tkKeyboardManager.dismiss();
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
