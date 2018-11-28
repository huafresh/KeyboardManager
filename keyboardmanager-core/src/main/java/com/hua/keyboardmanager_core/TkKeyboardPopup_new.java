package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.android.thinkive.framework.keyboard.KeyboardManager;
import com.android.thinkive.framework.util.ScreenUtil;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 18:31
 */

class TkKeyboardPopup_new {
    private Activity activity;
    private TkKeyboardManagerWrap curKeyboardManager;

    TkKeyboardPopup_new(Activity activity) {
        this.activity = activity;
    }

    void show(short keyboardType, final View visibleView) {
        if (curKeyboardManager != null && curKeyboardManager.isShowing()) {
            curKeyboardManager.dismiss();
        }
        View focusView = activity.getWindow().getCurrentFocus();
        if (focusView instanceof EditText) {
            curKeyboardManager = new TkKeyboardManagerWrap(activity,
                    (EditText) focusView, keyboardType, visibleView);
            curKeyboardManager.show();
        }
    }

    void dismiss() {
        if (curKeyboardManager != null) {
            curKeyboardManager.dismiss();
        }
    }

     boolean isShowing() {
        return curKeyboardManager != null && curKeyboardManager.isShowing();
    }

    private static class TkKeyboardManagerWrap extends KeyboardManager {
        private Context context;
        private View visibleView;
        private int keyboardHeight;

        TkKeyboardManagerWrap(Context context,
                              EditText editText,
                              short keyboardType,
                              View visibleView) {
            super(context, editText, keyboardType);
            this.context = context;
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

        @Override
        public void show() {
            super.show();
            int limitY = (int) (ScreenUtil.getScreenHeight(context) - keyboardHeight);
            ScrollAdjustHelper.adjust(visibleView, limitY);
        }

        @Override
        public void dismiss() {
            super.dismiss();
            ScrollAdjustHelper.reset(visibleView);
        }
    }

}
