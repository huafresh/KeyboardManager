package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.android.thinkive.framework.keyboard.KeyboardManager;
import com.android.thinkive.framework.util.ScreenUtil;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 18:31
 */

class TkKeyboardPopup extends KeyboardManager {
    private Activity activity;
    private View visibleView;
    private int keyboardHeight;

    private TkKeyboardPopup(Context context,
                            EditText editText,
                            short keyboardType,
                            View visibleView) {
        super(context, editText, keyboardType);
        this.activity = (Activity) context;
        this.visibleView = visibleView;
        this.setKeyboardVisibleListener(new KeyboardVisibleListener() {
            @Override
            public void onShow(int i) {
                keyboardHeight = i;
            }

            @Override
            public void onDismiss(int i) {

            }
        });
    }

    static @Nullable
    TkKeyboardPopup create(final Activity activity,
                           short keyboardType,
                           final View visibleView) {
        TkKeyboardPopup manager = null;
        View focusView = activity.getWindow().getCurrentFocus();
        if (focusView instanceof EditText) {
            manager = new TkKeyboardPopup(activity,
                    (EditText) focusView, keyboardType, visibleView);
        }
        return manager;
    }

    @Override
    public void show() {
        super.show();
        int limitY = (int) (ScreenUtil.getScreenHeight(activity) - keyboardHeight);
        ScrollAdjustHelper.adjust(visibleView, limitY);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ScrollAdjustHelper.reset(visibleView);
    }
}
