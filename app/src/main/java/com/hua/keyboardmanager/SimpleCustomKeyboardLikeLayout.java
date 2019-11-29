package com.hua.keyboardmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hua.softkeyboard_like.ISoftKeyboardLikeLayoutTheme;
import com.hua.softkeyboard_like.SoftKeyboardLike;

/**
 * @author zhangsh
 * @version V1.0
 * @date 2019-11-28 14:30
 */

public class SimpleCustomKeyboardLikeLayout implements ISoftKeyboardLikeLayoutTheme {
    @Override
    public int layoutThemeId() {
        return 0;
    }

    @Override
    public View createView(Context context, LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.simple_keyboard, parent);
    }

    public static void register(){
        SoftKeyboardLike.registerTheme(new SimpleCustomKeyboardLikeLayout());
    }
}
