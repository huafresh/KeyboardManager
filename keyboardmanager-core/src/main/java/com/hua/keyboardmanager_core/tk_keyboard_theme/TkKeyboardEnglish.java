package com.hua.keyboardmanager_core.tk_keyboard_theme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.thinkive.framework.keyboard.KeyboardManager;
import com.hua.keyboardmanager_core.IKeyboardTheme;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 18:10
 */

public class TkKeyboardEnglish implements IKeyboardTheme {

    @Override
    public int themeId() {
        return 0;
    }

    @Override
    public View onCreateKeyboardView(Context context, LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    public void onBindInputTarget(@NonNull View target) {

    }
}
