package com.hua.keyboardmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hua.softkeyboard_core.ILikeSoftKeyboardTheme;
import com.hua.softkeyboard_core.LikeSoftKeyboard;

/**
 * @author zhangsh
 * @version V1.0
 * @date 2019-11-28 14:30
 */

public class SimpleCustomKeyboard implements ILikeSoftKeyboardTheme {
    @Override
    public int themeId() {
        return 0;
    }

    @Override
    public View createView(Context context, LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.simple_keyboard, parent);
    }

    public static void register(){
        LikeSoftKeyboard.registerTheme(new SimpleCustomKeyboard());
    }
}
