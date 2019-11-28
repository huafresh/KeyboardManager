package com.hua.softkeyboard_core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author zhangsh
 * @version V1.0
 * @date 2019-11-28 11:01
 */

public interface ILikeSoftKeyboardTheme {

    int themeId();

    /**
     * 请注意不要保存返回的View为成员变量。
     */
    View createView(Context context, LayoutInflater inflater, ViewGroup parent);

}
