package com.hua.keyboardmanager_core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:50
 */

public interface IKeyboardTheme {

    @IdRes
    int themeId();

    View onCreateKeyboardView(Context context,
                              LayoutInflater inflater,
                              ViewGroup container);

    void onBindInputTarget(@NonNull View target);

}
