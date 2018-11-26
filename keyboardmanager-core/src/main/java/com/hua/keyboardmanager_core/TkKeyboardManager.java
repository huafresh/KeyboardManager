package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.android.thinkive.framework.keyboard.KeyboardManager;
import com.android.thinkive.framework.util.ScreenUtil;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 18:31
 */

class TkKeyboardManager extends KeyboardManager {
    private Activity activity;
    private ComponentName attachWindow;

    TkKeyboardManager(Context context, EditText editText, short keyboardType) {
        super(context, editText, keyboardType);
        this.activity = (Activity) context;
    }

    void show(View visible){
//        if (tkKeyboardManager != null) {
//            tkKeyboardManager.dismiss();
//        }
//
//        View focusView = activity.getWindow().getCurrentFocus();
//        if (focusView instanceof EditText) {
//            tkKeyboardManager = new KeyboardManager(activity, (EditText) focusView,
//                    translateKeyboardType(themeId));
//        } else {
//            tkKeyboardManager = new KeyboardManager(activity, translateKeyboardType(themeId));
//        }
//
//        tkKeyboardManager.setKeyboardVisibleListener(new KeyboardManager.KeyboardVisibleListener() {
//            @Override
//            public void onShow(int keyboardHeight) {
//                if (scrollAdjustHelper == null) {
//                    scrollAdjustHelper = new ScrollAdjustHelper(visibleView,
//                            (int) (ScreenUtil.getScreenHeight(activity) - keyboardHeight));
//                } else {
//                    scrollAdjustHelper.update(visibleView, keyboardHeight);
//                }
//            }
//
//            @Override
//            public void onDismiss(int i) {
//
//            }
//        });
//
//        tkKeyboardManager.setKeyCodeListener(new KeyboardManager.KeyCodeListener() {
//            @Override
//            public void onKeyCode(int i) {
//
//            }
//        });
//
//        tkKeyboardManager.show();
    }

}
