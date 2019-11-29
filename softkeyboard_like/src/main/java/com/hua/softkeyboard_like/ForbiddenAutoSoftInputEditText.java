package com.hua.softkeyboard_like;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.lang.reflect.Method;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:40
 */

public class ForbiddenAutoSoftInputEditText extends AppCompatEditText
        implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener {
    public static final int KEYBOARD_TYPE_SYSTEM = 0;
    public static final int KEYBOARD_TYPE_CUSTOM = 1;
    private int keyboardType = KEYBOARD_TYPE_CUSTOM;
    private int keyboardThemeId;
    private int visibleViewId;
    private boolean systemSoftEnable = true;
    private OnKeyListener keyListener;
    private OnFocusChangeListener focusChangeListener;
    private OnClickListener clickListener;
    private OnShowSoftInputListener onShowSoftInputListener;

    public ForbiddenAutoSoftInputEditText(Context context) {
        this(context, null);
    }

    public ForbiddenAutoSoftInputEditText(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.editTextStyle);
    }

    public ForbiddenAutoSoftInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        setOnFocusChangeListener(this);
        setOnClickListener(this);
        setOnKeyListener(this);

        forbiddenShowSystemSoftInputOnFocus();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (!(l instanceof ForbiddenAutoSoftInputEditText)) {
            this.clickListener = l;
        } else {
            super.setOnClickListener(l);
        }
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        if (!(l instanceof ForbiddenAutoSoftInputEditText)) {
            this.focusChangeListener = l;
        } else {
            super.setOnFocusChangeListener(l);
        }
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        if (!(l instanceof ForbiddenAutoSoftInputEditText)) {
            this.keyListener = l;
        } else {
            super.setOnKeyListener(l);
        }
    }

    public void setOnShowSoftInputListener(OnShowSoftInputListener onShowSoftInputListener) {
        this.onShowSoftInputListener = onShowSoftInputListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // 焦点变化快于系统键盘的弹出
        if (hasFocus) {
            if (onShowSoftInputListener != null) {
                onShowSoftInputListener.onShowSoftInput(this);
            }
        }

        if (this.focusChangeListener != null) {
            focusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    private boolean isActivityContext() {
        return getContext() instanceof Activity;
    }

    private void forbiddenShowSystemSoftInputOnFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setShowSoftInputOnFocus(false);
        } else {
            try {
                Method setShowSoftInputOnFocus = EditText.class.getMethod(
                        "setShowSoftInputOnFocus", Boolean.TYPE);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        // 点击事件慢于系统键盘的弹出
        // 如果点击使View获取了焦点，则此回调不会走。
        if (onShowSoftInputListener != null) {
            onShowSoftInputListener.onShowSoftInput(this);
        }

        if (this.clickListener != null) {
            clickListener.onClick(v);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK &&
//                FlexKeyboardManager.get().isCustomShowing()) {
//            FlexKeyboardManager.get().dismissCustomSoftInput();
//            return true;
//        }
        return keyListener != null && keyListener.onKey(v, keyCode, event);
    }

    public interface OnShowSoftInputListener{
        void onShowSoftInput(ForbiddenAutoSoftInputEditText editText);
    }
}
